package com.example.ivwing.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ivwing.R;
import com.example.ivwing.Data.IntravenousData;

public class IntravenousAdapter extends RecyclerView.Adapter<IntravenousAdapter.IntravenousViewHolder> {
    // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
    private IntravenousData[] dataSet;
    // 생성자
    public IntravenousAdapter(IntravenousData[] dataSet){
        this.dataSet = dataSet;
    }

    // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
    static class IntravenousViewHolder extends  RecyclerView.ViewHolder{
        // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것
        TextView intravenous_name;
        TextView intravenous_left;
        TextView intravenous_max;
        TextView intravenous_time;
        TextView intravenous_gtt;
        RelativeLayout intravenous_outer;
        RelativeLayout intravenous_length;
//        RelativeLayout.LayoutParams length_lp;

        IntravenousViewHolder(View view){
            super(view);
            this.intravenous_name = view.findViewById(R.id.intravenous_name);
            this.intravenous_left = view.findViewById(R.id.intravenous_left);
            this.intravenous_max = view.findViewById(R.id.intravenous_max);
            this.intravenous_time = view.findViewById(R.id.intravenous_time);
            this.intravenous_gtt = view.findViewById(R.id.intravenous_gtt);
            this.intravenous_outer = view.findViewById(R.id.intravenous_outer);
            this.intravenous_length = view.findViewById(R.id.intravenous_length);
//            this.length_lp = (RelativeLayout.LayoutParams) intravenous_length.getLayoutParams();
        }
    }

    // 어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
    // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
    @NonNull
    @Override
    public IntravenousViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item_intravenous, viewGroup, false);
        return new IntravenousViewHolder(holderView);
    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull IntravenousViewHolder myViewHolder, int i) {
        float iv_sample = 822 * this.dataSet[i].getLeft() / this.dataSet[i].getMax();
        myViewHolder.intravenous_name.setText(this.dataSet[i].getName());
        myViewHolder.intravenous_left.setText(String.valueOf(this.dataSet[i].getLeft()));
        myViewHolder.intravenous_max.setText(String.valueOf(this.dataSet[i].getMax()));
        myViewHolder.intravenous_time.setText(String.valueOf(this.dataSet[i].getTime()));
        myViewHolder.intravenous_gtt.setText(String.valueOf(this.dataSet[i].getGtt()));

        RelativeLayout.LayoutParams length_lp = (RelativeLayout.LayoutParams) myViewHolder.intravenous_length.getLayoutParams();
        length_lp.width = Math.round(iv_sample);
        myViewHolder.intravenous_length.setLayoutParams(length_lp);
        myViewHolder.intravenous_length.requestLayout();

    }

    // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
