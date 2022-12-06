package com.example.cse_3311_freshman_app;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
public class recycler_adapter_search extends RecyclerView.Adapter<recycler_adapter_search.MyViewHolder> {

    Context context;    // context variable to hold the context; used for layout inflation
    ArrayList<Organizations> clubs;    // arraylist to hold the events
    // constructor for the class to get the context and events in the program
    public recycler_adapter_search(Context context, ArrayList<Organizations> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull

    @Override   // Method for inflating the layout of the recyclerview to the screen context
    public recycler_adapter_search.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.resource_search_events,parent,false);
        // inflates the layout of the recyclerview to take up the entire screen context
        return new MyViewHolder(v); // returns the internal class constructor with the layout inflation passed
    }

    @Override   // sets the data in the event to the value of the event name in the event class
    public void onBindViewHolder(@NonNull recycler_adapter_search.MyViewHolder holder, int position) {
        Organizations club = clubs.get(position); // attach the event to the position on the page it is in
        holder.itemView.setOnClickListener(v -> { // attach the listener on the individual items
            Intent intent = new Intent(context, ClubProfileActivity.class);
            intent.putExtra("CLUB", club);
            context.startActivity(intent);      // Starts up the club profile
        });
        holder.clubName.setText(club.name);
        //holder.desc.setText(club.desc);
        //holder.location.setText(club.location);
    }

    @Override   // getter for getting the amount of events in the database
    public int getItemCount() {
        return clubs.size();
    }

    // This internal class is similar to an onCreate method, as it just assigns the data in the recyclerview to vars
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.name_of_club);    // connecting the vars to the items in the recyclerview
        }
    }
}
