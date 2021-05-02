package com.example.busse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busse.MapsActivity;
import com.example.busse.R;
import com.example.busse.common.Utility;
import com.example.busse.model.arrivalsinfo.ArrivalsInfo;

import java.util.ArrayList;
import java.util.List;

public class ArrivalsAdapter extends RecyclerView.Adapter<ArrivalsAdapter.ArrivalsHolder> {
    private List<ArrivalsInfo> arrivals = new ArrayList<>();
    private Context context;

    // Attaches arrivals_item_card.xml layout to holder
    @NonNull
    @Override
    public ArrivalsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.arrivals_item_card, parent, false);
        return new ArrivalsHolder(itemView); // Create new holder that contains the itemView (arrivals_item_card.xml)
    }

    // Adds data from arrivals into the layout that the holder contains
    @Override
    public void onBindViewHolder(@NonNull ArrivalsHolder holder, int position) {
        ArrivalsInfo arrival = arrivals.get(position);

        holder.textViewStopPointNameValue.setText(context.getString(R.string.arrivals_item_card_stop_point_value, arrival.getStopPointName()));
        holder.textViewLineValue.setText(context.getString(R.string.arrivals_item_card_line_value, arrival.getLine()));

        if (arrival.getArrivalTime() != null) {
            String parsedTime = Utility.parseDateTimeForDisplay(arrival.getArrivalTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
            holder.textViewArrivalTimeValue.setText(
                    context.getString(R.string.arrivals_item_card_arrival_time_value, String.valueOf(arrival.getTimeUntilArrival()), parsedTime));
            holder.textViewArrivalTime.setText(context.getString(R.string.arrivals_item_card_arrival_time, "Saapumisaika"));
        } else if (arrival.getDepartureTime() != null) {
            String parsedTime = Utility.parseDateTimeForDisplay(arrival.getDepartureTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
            holder.textViewArrivalTimeValue.setText(
                    context.getString(R.string.arrivals_item_card_arrival_time_value, String.valueOf(arrival.getTimeUntilArrival()), parsedTime));
            holder.textViewArrivalTime.setText(context.getString(R.string.arrivals_item_card_arrival_time, "Tauolla - lähtöaika"));
        }
        holder.textViewDestinationValue.setText(context.getString(R.string.arrivals_item_card_destination_value, arrival.getDestination()));
        holder.textViewDistanceValue.setText(context.getString(R.string.activity_main_search_distance_value, String.valueOf(arrival.getDistanceToStopPoint())));

        // onClick handler for every item card that opens the map activity and sends some relevant data within the intent
        holder.relativeLayoutItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                intent.putExtra("vehicleRef", arrival.getVehicleRef());
                intent.putExtra("stopPointLatitude", arrival.getStopPointLatitude());
                intent.putExtra("stopPointLongitude", arrival.getStopPointLongitude());
                intent.putExtra("line", arrival.getLine());
                intent.putExtra("stopPointName", arrival.getStopPointName());
                intent.putExtra("destination", arrival.getDestination());
                v.getContext().startActivity(intent);
            }
        });
    }

    // Determines how many items to show on the item view
    @Override
    public int getItemCount() {
        return arrivals.size();
    }

    // Set the data and notify that the data has changed
    public void setArrivals(List<ArrivalsInfo> arrivals) {
        this.arrivals = arrivals;
        notifyDataSetChanged();
    }

    // Holder class "holds" the layout
    static class ArrivalsHolder extends RecyclerView.ViewHolder {
        private final TextView textViewStopPointNameValue;
        private final TextView textViewLineValue;
        private final TextView textViewArrivalTimeValue;
        private final TextView textViewArrivalTime;
        private final TextView textViewDestinationValue;
        private final TextView textViewDistanceValue;
        private RelativeLayout relativeLayoutItemCard;

        public ArrivalsHolder(@NonNull View itemView) {
            super(itemView);
            // Values
            textViewStopPointNameValue = itemView.findViewById(R.id.text_view_stop_point_name_value);
            textViewLineValue = itemView.findViewById(R.id.text_view_line_value);
            textViewArrivalTimeValue = itemView.findViewById(R.id.text_view_arrival_time_value);
            textViewArrivalTime = itemView.findViewById(R.id.text_view_arrival_time);
            textViewDestinationValue = itemView.findViewById(R.id.text_view_destination_value);
            textViewDistanceValue = itemView.findViewById(R.id.text_view_distance_value);

            relativeLayoutItemCard = itemView.findViewById(R.id.relative_layout_item_card);
        }
    }
}
