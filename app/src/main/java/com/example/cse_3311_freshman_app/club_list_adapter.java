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

import org.w3c.dom.Text;

import java.util.ArrayList;
// adapters bind a recyclerview to the data that will be displayed in the view
public class club_list_adapter extends RecyclerView.Adapter<club_list_adapter.MyViewHolder> {

    Context context;    // context variable to hold the context; used for layout inflation
    ArrayList<Organizations> orgs;    // arraylist to hold the orgs

    // Adapter constuctor
    public club_list_adapter(Context context, ArrayList<Organizations> orgs){
        this.orgs = orgs;
        this.context =  context;
    }

    @NonNull
    @Override
    public club_list_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.resource_club_name_viewer,parent,false);
        // inflates the layout of the recyclerview to take up the entire screen context
        return new MyViewHolder(v); // returns the internal class constructor with the layout inflation passed
    }

    @Override
    public void onBindViewHolder(@NonNull club_list_adapter.MyViewHolder holder, int position) {
        Organizations org = orgs.get(position); // attach the event to the position on the page it is i
        // Check clubs formatting was correct to transfer from firebase
        if(org != null) {
            holder.itemView.setOnClickListener(v -> { // attach the listener on the individual items
                Intent intent = new Intent(context, club_page.class);
                intent.putExtra("org", org); // stores the item's org to send to next acitivity
                context.startActivity(intent); // starts up OpenEventActivity
            });

            holder.club_name_view.setText(org.name);
        }
    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView club_name_view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            club_name_view = itemView.findViewById(R.id.club_name);
        }
    }
}
