package com.example.journey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



import java.util.HashSet;

import java.util.Set;


import com.google.gson.Gson;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OrderedRoutesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RouteAdapter routeAdapter;
    private List<Route> orderedRouteList;
    private DatabaseReference databaseReference;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_routes);

        recyclerView = findViewById(R.id.orderedRoutesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderedRouteList = new ArrayList<>();
        routeAdapter = new RouteAdapter(this, orderedRouteList);
        recyclerView.setAdapter(routeAdapter);

        // Ініціалізація Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("routes");

        // Завантаження замовлених маршрутів
        loadOrderedRoutes();
    }

    private void loadOrderedRoutes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderedRouteList.clear();
                int count = 0;  // Лічильник для отримання лише двох маршрутів
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count >= 1) break;  // Завантажити лише два маршрути
                    Route route = snapshot.getValue(Route.class);
                    if (route != null) {
                        orderedRouteList.add(route);
                        count++;
                    }
                }
                routeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("OrderedRoutesActivity", "Error loading routes: ", databaseError.toException());
            }
        });

    }
}
