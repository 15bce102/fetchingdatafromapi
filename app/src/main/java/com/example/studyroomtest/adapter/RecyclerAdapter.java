package com.example.studyroomtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studyroomtest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;

    ArrayList<ArrayList<String>> list;

    public RecyclerAdapter(Context applicationContext, ArrayList<ArrayList<String>> list) {

        this.layoutInflater = LayoutInflater.from(applicationContext);
        this.list = list;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Picasso.with(holder.imageView.getContext()).load(list.get(position).get(2)).into(holder.imageView);
        holder.textView.setText(list.get(position).get(0));
        holder.text.setText(list.get(position).get(1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView text;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.person_photo);
            this.textView = (TextView) itemView.findViewById(R.id.title);
            this.text = (TextView) itemView.findViewById(R.id.tags);
        }
    }
}
