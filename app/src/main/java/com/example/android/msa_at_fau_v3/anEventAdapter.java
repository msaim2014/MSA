package com.example.android.msa_at_fau_v3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class anEventAdapter extends RecyclerView.Adapter<anEventAdapter.anEventViewHolder>{

    private Context context;
    private List<anEvent> eventList;

    public anEventAdapter(Context context, List<anEvent> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public anEventAdapter.anEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_view, parent,false);
        anEventViewHolder holder = new anEventViewHolder(view);
        return holder;
    }

    //binds data to view holder
    @Override
    public void onBindViewHolder(anEventViewHolder holder, int position) {
        holder.title.setText(eventList.get(position).title);
        holder.desc.setText(eventList.get(position).description);
        holder.location.setText(eventList.get(position).location);
        holder.startTime.setText(eventList.get(position).start);
        holder.endTime.setText(eventList.get(position).end);
    }

    //returns the size of the list
    @Override
    public int getItemCount() {
        return eventList.size();
    }


    class anEventViewHolder extends RecyclerView.ViewHolder{
        TextView title, desc, location, startTime, endTime;


        public anEventViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
        }
    }

}
