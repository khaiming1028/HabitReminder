package com.example.asgmandrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.MyViewHolder> {

    private Context context;

    int position;
    private ArrayList habit_id, title, description, startingTime, endingTime;
    HabitsAdapter(Context context, ArrayList habit_id, ArrayList title, ArrayList description, ArrayList startingTime, ArrayList endingTime){
        this.context = context;
        this.habit_id = habit_id;
        this.title = title;
        this.description = description;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.habit_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.activity_habit_id.setText(String.valueOf(habit_id.get(position)));
        holder.habit_title.setText(String.valueOf(title.get(position)));
        holder.habit_description.setText(String.valueOf(description.get(position)));
        holder.start_time.setText(String.valueOf(startingTime.get(position)));
        holder.end_time.setText(String.valueOf(endingTime.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Update.class);
                intent.putExtra("id", String.valueOf(habit_id.get(position)));
                intent.putExtra("title", String.valueOf(title.get(position)));
                intent.putExtra("description", String.valueOf(description.get(position)));
                intent.putExtra("startingTime", String.valueOf(startingTime.get(position)));
                intent.putExtra("endingTime", String.valueOf(endingTime.get(position)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habit_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView activity_habit_id,habit_title, habit_description, start_time, end_time;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            activity_habit_id = itemView.findViewById(R.id.activity_habit_id);
            habit_title = itemView.findViewById(R.id.habit_title);
            habit_description = itemView.findViewById(R.id.habit_description);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
