package com.example.journey;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;

import android.widget.Button;

import androidx.core.content.ContextCompat;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<Route> routeList;
    private Context context;

    public RouteAdapter(Context context, List<Route> routeList) {
        this.context = context;
        this.routeList = routeList;
    }




    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        Route route = routeList.get(position);


        holder.fromTextView.setText(route.getFrom());
        holder.toTextView.setText(route.getTo());
        holder.timeTextView.setText(route.getTime());
        holder.priceTextView.setText(String.valueOf(route.getPrice()));
        holder.transportTypeTextView.setText(route.getTransportType());


        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            private boolean isOrdered = false;

            @Override
            public void onClick(View v) {
                if (!isOrdered) {
                    holder.orderButton.setText("Замовлено");
                    holder.orderButton.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                    isOrdered = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView fromTextView, toTextView, timeTextView, priceTextView, transportTypeTextView;
        Button orderButton;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.fromTextView);
            toTextView = itemView.findViewById(R.id.toTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            transportTypeTextView = itemView.findViewById(R.id.transportTypeTextView);
            orderButton = itemView.findViewById(R.id.orderButton); // Ініціалізація кнопки
        }
    }
}
