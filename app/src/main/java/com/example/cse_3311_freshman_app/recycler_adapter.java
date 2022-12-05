package com.example.cse_3311_freshman_app;

import android.content.Context;
import android.content.Intent;
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
// adapters bind a recyclerview to the data that will be displayed in the view
public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.MyViewHolder> {

    Context context;    // context variable to hold the context; used for layout inflation
    ArrayList<Event> events;    // arraylist to hold the events
    // constructor for the class to get the context and events in the program
    public recycler_adapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull

    @Override   // Method for inflating the layout of the recyclerview to the screen context
    public recycler_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.resource_list_viewer,parent,false);
        // inflates the layout of the recyclerview to take up the entire screen context
        return new MyViewHolder(v); // returns the internal class constructor with the layout inflation passed
    }

    @Override   // sets the data in the event to the value of the event name in the event class
    public void onBindViewHolder(@NonNull recycler_adapter.MyViewHolder holder, int position) {
        Event event = events.get(position); // attach the event to the position on the page it is in
        holder.itemView.setOnClickListener(v -> { // attach the listener on the individual items
            Intent intent = new Intent(context, OpenEventActivity.class); 
            intent.putExtra("EVENT", event); // stores the item's event to send to next acitivity
            context.startActivity(intent); // starts up OpenEventActivity
        });
        holder.e_name.setText(event.e_name);
        holder.e_org.setText(event.e_org);
        holder.e_description.setText(event.e_desc);
        holder.e_location.setText(event.e_location);
        holder.e_time.setText(event.getStringTime());
        Glide.with(context).load(event.getE_image()).apply(new RequestOptions().override(1000,1000)).into(holder.e_image);
    }

    @Override   // getter for getting the amount of events in the database
    public int getItemCount() {
        return events.size();
    }

    // This internal class is similar to an onCreate method, as it just assigns the data in the recyclerview to vars
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView e_org, e_name, e_description, e_location, e_time;  // vars for the data in the recyclerview
        ImageView e_image;  // var for holding the image for the event

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            e_name = itemView.findViewById(R.id.event_name);    // connecting the vars to the items in the recyclerview
            e_org = itemView.findViewById(R.id.event_organizer);
            e_description = itemView.findViewById(R.id.event_description);
            e_location = itemView.findViewById(R.id.event_location);
            e_time = itemView.findViewById(R.id.event_time);
            e_image = itemView.findViewById(R.id.event_banner);
        }
    }
}
