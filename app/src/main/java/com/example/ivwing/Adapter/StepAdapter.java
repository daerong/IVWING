package com.example.ivwing.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ivwing.R;
import com.example.ivwing.Data.StepData;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
    private StepData[] dataSet;
    // 생성자
    public StepAdapter(StepData[] dataSet){
        this.dataSet = dataSet;
    }

    // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
    static class StepViewHolder extends RecyclerView.ViewHolder{
        // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
        TextView recycler_month;
        TextView recycler_day;
        RelativeLayout recycler_graph;

        StepViewHolder(View view){
            super(view);
            this.recycler_month = view.findViewById(R.id.recycler_month);
            this.recycler_day = view.findViewById(R.id.recycler_day);
            this.recycler_graph = view.findViewById(R.id.recycler_graph);
        }
    }

    // 어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
    // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_step, viewGroup, false);
        return new StepViewHolder(holderView);
    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull StepViewHolder myViewHolder, int i) {
        int step_vol = this.dataSet[i].getStep();
        float step_height;
        if(step_vol < 12000){
            step_height = 1116 * step_vol / 12000;
        }
        else{
            step_height = 1116;
        }

        myViewHolder.recycler_month.setText(this.dataSet[i].getMonth());
        myViewHolder.recycler_day.setText(this.dataSet[i].getDay());

        LinearLayout.LayoutParams length_lp = (LinearLayout.LayoutParams) myViewHolder.recycler_graph.getLayoutParams();
        length_lp.height = Math.round(step_height);
        myViewHolder.recycler_graph.setLayoutParams(length_lp);
        myViewHolder.recycler_graph.requestLayout();
    }

    // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
