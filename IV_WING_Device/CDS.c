#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <wiringPi.h>
#include <wiringPiSPI.h>
#include <string.h>
#include <pthread.h>

/* 필수 */
#define CS_MCP3208  6			// BCM_GPIO 25
#define CS_LED 27				// BCM_GPIO 8
#define SPI_CHANNEL 0
#define SPI_SPEED 1000000		// 1MHz

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


/* 필수 */
int read_mcp3208_adc(unsigned char adcChannel);

/* 계산 */
int var_update(int mVar, int var, int weight) { return (weight * mVar + var) / (weight + 1); }

/* 배열 */
int partition(int list[], int left, int right);
void quick_sort(int list[], int left, int right);
double arr_average(int list[], int arrSize);
double sec_to_gtt(double cycle);

/* 쓰레드 */




/* Global var */
int onLed;


int main(int argc, char **argv) {
	/* 필수 */
	char errorMsg[50];
	int adcChannel = 0;			// 측정값
	int adcValue = 0;			// 변환된 값

	/* 기준 */
	int mDelta = INIT_DELTA;
	int mMax = 0;
	int mMin = 0;
	int mDetect = 0;			// 측정 기준
	int mDrop = 0;
	double mGtt = 0;

	/* boolean */
	int isFirst = 1;
	int isDetected = 0;

	/* 임시 */
	int dropArr[ARR_VOL];
	int cycleArr[CYCLE_VOL];
	int cycleCnt = 0;
	int cycleIndex = 0;

	/* 쓰레드 */

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
				printf("%d\n", ++mDrop);

				if (cycleIndex != CYCLE_VOL - 1) cycleIndex++;
				else cycleIndex = 0;
				cycleArr[cycleIndex] = cycleCnt;
				cycleCnt = 0;
				mGtt = sec_to_gtt(arr_average(cycleArr, CYCLE_VOL));
				printf("%lf\n", mGtt);
				isDetected = 1;
				onLed = 1;
			}
		}
		else {
			isDetected = 0;
			mMax = var_update(mMax, dropArr[ARR_VOL - 1], 2);
		}
	}

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
		printf("%d ", list[i]);
	}
	printf("\n");
	return avr / arrSize;
}

double sec_to_gtt(double cycle) {
	cycle *= CYCLE_TO_TIME;		// cycle to ms
	cycle /= MS_TO_MIN;			// ms to min
	cycle *= CONSTANT;			// 보정값
	return 1 / cycle;
}