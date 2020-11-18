#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>		// sleep()

#include <wiringPi.h>
#include <wiringPiSPI.h>

#include <pthread.h>
#include <mysql/mysql.h>
//#include "mysql.h"
#include "dbInfo.h"
#pragma comment(lib, "libmysql.lib")

/* 필수 */
#define CS_MCP3208  6			// BCM_GPIO 25
#define CS_LED 27				// BCM_GPIO 8
#define SPI_CHANNEL 0
#define SPI_SPEED 1000000		// 1MHz
#define LINKER_ID 13

/* 배열 */
#define ARR_VOL 20
#define CYCLE_VOL 30				// 방울간 CYCLE 수
#define SWAP(x, y, temp) ( (temp)=(x), (x)=(y), (y)=(temp) )

/* 계산 */
#define CYCLE_TIME 5			// 5ms
#define CYCLE_TO_TIME CYCLE_TIME*ARR_VOL	// 1cycle에 해당하는 ms
#define MS_TO_MIN 60000
#define INIT_DELTA 150
#define CONSTANT 1.041667			// 보정값

/* 구조체 */
typedef struct IV {
	int userID;
	int recordID;
	int ivID;
	int ivNow;
	int ivMax;
} IV;


/* 필수 */
int read_mcp3208_adc(unsigned char adcChannel);

/* 계산 */
int var_update(int mVar, int var, int weight) { return (weight * mVar + var) / (weight + 1); }

/* 배열 */
int partition(int list[], int left, int right);
void quick_sort(int list[], int left, int right);
double arr_average(int list[], int arrSize);
double sec_to_gtt(double cycle);

/* SQL */
int userSetting(MYSQL* connection, MYSQL *conn, char query[], IV* iv);
int updateDB(MYSQL* connection, MYSQL *conn, char query[]);

/* 쓰레드 */
void* SQL_ev_func(void *data);

/* Global var */
int onLed;
char SQL_func_msg[] = "SQL thread";
int gDrop = 0;
double gGtt = 0;


int main(int argc, char **argv) {
	/* 필수 */
	char errorMsg[50];
	int adcChannel = 0;			// 측정값
	int adcValue = 0;			// 변환된 값

	/* 기준 */
	int mMax = 0;
	int mMin = 0;
	int mDetect = 0;			// 측정 기준

	/* boolean */
	int isFirst = 1;
	int isDetected = 0;

	/* 임시 */
	int dropArr[ARR_VOL];
	int cycleArr[CYCLE_VOL];
	int cycleCnt = 0;
	int cycleIndex = 0;

	/* 쓰레드 */
	pthread_t SQL_ev_thread;
	int SQL_thread_id;
	void *thread_result;

	/* Connection */
	if (wiringPiSetup() == -1) {
		fprintf(stdout, "Unable to start wiringPi: %s\n", strerror(errno));
		return 0;
	}

	if (wiringPiSPISetup(SPI_CHANNEL, SPI_SPEED) == -1) {
		fprintf(stdout, "wiringPiSPISetup Failed: %s\n", strerror(errno));
		return 0;
	}


	/* Initialize */
	pinMode(CS_MCP3208, OUTPUT);
	pinMode(CS_LED, OUTPUT);
	digitalWrite(CS_LED, 0);
	onLed = 0;
	for (int i = 0; i < CYCLE_VOL; i++) {
		cycleArr[i] = 0;
	}


	/* 1cycle */
	for (int i = 0; i < ARR_VOL; i++) {
		adcValue = read_mcp3208_adc(adcChannel);
		dropArr[i] = adcValue;
		delay(CYCLE_TIME);
	}

	quick_sort(dropArr, 0, ARR_VOL - 1);
	mMax = dropArr[ARR_VOL - 1];
	mMin = dropArr[0] - INIT_DELTA;
	mDetect = var_update(mMin, mMax, 2);

	printf("mMax = %d, mMin = %d, mDetect = %d\n", mMax, mMin, mDetect);

	SQL_thread_id = pthread_create(&SQL_ev_thread, NULL, SQL_ev_func, (void *)&SQL_func_msg);

	while (1) {
		for (int i = 0; i < ARR_VOL; i++) {
			adcValue = read_mcp3208_adc(adcChannel);
			dropArr[i] = adcValue;
			delay(CYCLE_TIME);
			//printf("%4.d ", dropArr[i]);
		}

		//printf("\n");

		quick_sort(dropArr, 0, ARR_VOL - 1);
		cycleCnt++;

		if (dropArr[0] < mDetect) {
			if (isFirst) {
				mMin = dropArr[0];
				mDetect = var_update(mMin, mMax, 2);
				isFirst = 0;
			}
			else if (isDetected) {
				mMin = var_update(dropArr[0], mMin, 2);
				mDetect = var_update(mMin, mMax, 2);
			}
			else {
				mMin = var_update(dropArr[0], mMin, 2);
				mDetect = var_update(mMin, mMax, 2);
				printf("mDrop : %d\t", ++gDrop);

				if (cycleIndex != CYCLE_VOL - 1) cycleIndex++;
				else cycleIndex = 0;
				cycleArr[cycleIndex] = cycleCnt;
				cycleCnt = 0;
				gGtt = sec_to_gtt(arr_average(cycleArr, CYCLE_VOL));
				printf("mGtt : %lf\n", gGtt);
				isDetected = 1;
				onLed = 1;
			}
		}
		else {
			isDetected = 0;
			mMax = var_update(mMax, dropArr[ARR_VOL - 1], 2);
		}
	}

	pthread_join(SQL_ev_thread, (void *)&thread_result);

	return 0;
}

int read_mcp3208_adc(unsigned char adcChannel) {
	unsigned char buff[3];
	int adcValue = 0;
	buff[0] = 0x06 | ((adcChannel & 0x07) >> 2);
	buff[1] = ((adcChannel & 0x07) << 6);
	buff[2] = 0x00;
	digitalWrite(CS_MCP3208, 0);  // Low : CS Active
	wiringPiSPIDataRW(SPI_CHANNEL, buff, 3);
	buff[1] = 0x0F & buff[1];
	adcValue = (buff[1] << 8) | buff[2];
	digitalWrite(CS_MCP3208, 1);  // High : CS Inactive
	return adcValue;
}

int partition(int list[], int left, int right) {
	int pivot, temp;
	int low, high;
	low = left;
	high = right + 1;
	pivot = list[left];
	do {
		do {
			low++;
		} while (low <= right && list[low] < pivot);
		do {
			high--;
		} while (high >= left && list[high] > pivot);
		if (low < high) {
			SWAP(list[low], list[high], temp);
		}
	} while (low < high);
	SWAP(list[left], list[high], temp);
	return high;
}

void quick_sort(int list[], int left, int right) {
	if (left < right) {
		int q = partition(list, left, right);
		quick_sort(list, left, q - 1);
		quick_sort(list, q + 1, right);
	}
}

double arr_average(int list[], int arrSize) {
	double avr = 0.0;
	for (int i = 0; i < arrSize; i++) {
		avr += list[i];
		//printf("%d ", list[i]);
	}
	//printf("\n");
	return avr / arrSize;
}

double sec_to_gtt(double cycle) {
	cycle *= CYCLE_TO_TIME;		// cycle to ms
	cycle /= MS_TO_MIN;			// ms to min
	cycle *= CONSTANT;			// 보정값
	return 1 / cycle;
}

int userSetting(MYSQL* connection, MYSQL *conn, char query[], IV* iv) {
	MYSQL_RES*	sql_result;
	MYSQL_ROW   sql_row;
	int			query_stat;

	int user_id = 0;

	query_stat = mysql_query(connection, query);
	if (query_stat != 0) {
		fprintf(stderr, "Mysql query error : %s", mysql_error(conn));
		return 0;
	}

	sql_result = mysql_store_result(connection);

	while ((sql_row = mysql_fetch_row(sql_result)) != NULL) {
		printf("%s %s %s %s %s\n", sql_row[0], sql_row[1], sql_row[2], sql_row[3], sql_row[4]);
		iv->userID = atoi(sql_row[0]);
		iv->recordID = atoi(sql_row[1]);
		iv->ivID = atoi(sql_row[2]);
		iv->ivNow = atoi(sql_row[3]);
		iv->ivMax = atoi(sql_row[4]);
	}

	mysql_free_result(sql_result);

	return 1;
}

int updateDB(MYSQL* connection, MYSQL *conn, char query[]) {
	int query_stat;

	query_stat = mysql_query(connection, query);
	if (query_stat != 0) {
		fprintf(stderr, "Mysql query error : %s", mysql_error(conn));
		return 1;
	}

	return 0;
}

void* SQL_ev_func(void *data) {
	/* SQL */
	IV iv;
	MYSQL		conn;
	MYSQL*		connection = NULL;
	char		query[300];
	int mL = 0;

	mysql_init(&conn);

	connection = mysql_real_connect(&conn, DB_HOST, DB_USER, DB_PASS, DB_NAME, 3306, (char *)NULL, 0);

	if (connection == NULL) {
		fprintf(stderr, "Mysql connection error : %s", mysql_error(&conn));
		return NULL;
	}

	sprintf(query, "SELECT user_id, record_id, iv_id, iv_now, iv_max FROM (SELECT user_id, record_id, record_iv FROM (SELECT user_id FROM user WHERE user_linker = %d) non_record LEFT JOIN record ON non_record.user_id = record.record_user) non_iv LEFT JOIN iv ON non_iv.record_iv = iv.iv_id", LINKER_ID);
	userSetting(connection, &conn, query, &iv);

	while (1) {
		if (gDrop > 20) {
			mL = gDrop / 20;
			gDrop -= mL * 20;

			iv.ivNow -= mL;

			sprintf(query, "UPDATE iv SET iv_now = %d WHERE iv_id = %d", iv.ivNow, iv.ivID);
			updateDB(connection, &conn, query);

			printf("Update : %d (mL)\t", iv.ivNow);

			sprintf(query, "UPDATE record SET record_gtt = %lf WHERE record_id = %d", gGtt, iv.recordID);
			updateDB(connection, &conn, query);

			printf("Update : %lf (gtt)\n", gGtt);
		}
		sleep(3);
	}

	mysql_close(connection);

	return NULL;
}