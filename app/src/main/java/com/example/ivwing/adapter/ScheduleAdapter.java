package com.example.ivwing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ivwing.R;
import com.example.ivwing.data.ScheduleData;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {
    // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
    private String[] textSet1;
    private String[] textSet2;
    private String[] textSet3;
    private String[] textSet4;
    private String[] textSet5;

    // 생성자
    public ScheduleAdapter(String[] textSet1, String[] textSet2, String[] textSet3, String[] textSet4, String[] textSet5){
        this.textSet1 = textSet1;
        this.textSet2 = textSet2;
        this.textSet3 = textSet3;
        this.textSet4 = textSet4;
        this.textSet5 = textSet5;
    }

    // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;

        public MyViewHolder(View view){
            super(view);
            this.textView1 = view.findViewById(R.id.textView1);
            this.textView2 = view.findViewById(R.id.textView2);
            this.textView3 = view.findViewById(R.id.textView3);
            this.textView4 = view.findViewById(R.id.textView4);
            this.textView5 = view.findViewById(R.id.textView5);
        }
    }

    // 어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
    // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(holderView);
        return myViewHolder;
    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView1.setText(this.textSet1[i]);
        myViewHolder.textView2.setText(this.textSet2[i]);
        myViewHolder.textView3.setText(this.textSet3[i]);
        myViewHolder.textView4.setText(this.textSet4[i]);
        myViewHolder.textView5.setText(this.textSet5[i]);
    }

    // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return textSet1.length;
    }
}
