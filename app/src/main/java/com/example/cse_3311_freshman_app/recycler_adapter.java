package com.example.cse_3311_freshman_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.MyViewHolder> {


    Context context;
    ArrayList<Event> events;

    public recycler_adapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull

    @Override
    public recycler_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.resource_list_viewer,parent,false);



        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull recycler_adapter.MyViewHolder holder, int position) {
        Event event = events.get(position);

        holder.e_name.setText(event.e_name);
        holder.e_description.setText(event.e_desc);
        holder.e_location.setText(event.e_location);
        //holder.e_time.setText((CharSequence) event.time);
        holder.e_time.setText(event.time);
        //holder.e_image.layout(0,0,0,0);
        Glide.with(context).load(event.getE_image()).apply(new RequestOptions().override(1000,1000)).into(holder.e_image);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView e_org, e_name, e_description, e_location, e_time;
        ImageView e_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //e_org = itemView.findViewById(R.id.event_organizer);
            e_name = itemView.findViewById(R.id.event_name);
            e_description = itemView.findViewById(R.id.event_description);
            e_location = itemView.findViewById(R.id.event_location);
            e_time = itemView.findViewById(R.id.event_time);
            e_image = itemView.findViewById(R.id.event_banner);
        }
    }
}
