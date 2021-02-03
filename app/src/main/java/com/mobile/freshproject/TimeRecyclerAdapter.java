package com.mobile.freshproject;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeRecyclerAdapter extends RecyclerView.Adapter<TimeRecyclerAdapter.MyViewHolder>{
    public ArrayList<TimeViewItem> listOfTimes;

    public TimeRecyclerAdapter(ArrayList<TimeViewItem> listOfTimes) {
        this.listOfTimes = listOfTimes;
   }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.time_flag, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.order.setText(listOfTimes.get(i).getOrder());
        myViewHolder.currentTime.setText(listOfTimes.get(i).getCurrentTime());
        myViewHolder.difference.setText(listOfTimes.get(i).getDifference());
    }

    @Override
    public int getItemCount() {
        return listOfTimes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView order;
        public TextView difference;
        public TextView currentTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.order);
            difference = itemView.findViewById(R.id.difference);
            currentTime = itemView.findViewById(R.id.currentTime);
        }
    }
}
