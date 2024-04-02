package com.example.rescuesupport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final ArrayList<DisasterMessage> data;

    public MyAdapter(ArrayList<DisasterMessage> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.itemTextView); // 아이템 레이아웃의 TextView ID
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false); // 아이템 레이아웃 파일명
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DisasterMessage item = data.get(position);
        holder.textView.setText(item.getMsg()); // 또는 item.getMsg()를 사용
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}





