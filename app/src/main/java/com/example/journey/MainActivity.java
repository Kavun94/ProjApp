package com.example.journey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFrom, editTextTo;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private RouteAdapter routeAdapter;
    private List<Route> routeList;
    private List<Route> filteredRouteList;
    private Spinner transportTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTo = findViewById(R.id.editTextTo);
        buttonSearch = findViewById(R.id.buttonSearch);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        routeList = new ArrayList<>();
        filteredRouteList = new ArrayList<>();

        transportTypeSpinner = findViewById(R.id.transportTypeSpinner);

        routeAdapter = new RouteAdapter(this, filteredRouteList);  // Використовуємо відфільтрований список
        recyclerView.setAdapter(routeAdapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = editTextFrom.getText().toString().trim();
                String to = editTextTo.getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference routesRef = database.getReference("routes");
                routesRef.orderByChild("from").equalTo(from).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        routeList.clear();
                        for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                            Route route = routeSnapshot.getValue(Route.class);
                            if (route != null && route.getTo().equals(to)) {
                                routeList.add(route);
                            }
                        }
                        routeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("Firebase", "er", databaseError.toException());
                    }
                });
            }
            });


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton viewOrdersButton = findViewById(R.id.viewOrdersButton);
        viewOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderedRoutesActivity.class);
                startActivity(intent);
            }
        });
        // Налаштування Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.transport_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportTypeSpinner.setAdapter(adapter);

        // Обробка вибору в Spinner
        transportTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getItemAtPosition(position).toString();
                filterRoutesByType(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Завантаження даних з Firebase
        loadRoutesFromFirebase();
    }

    private void loadRoutesFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("routes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                routeList.clear();
                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                    Route route = routeSnapshot.getValue(Route.class);
                    routeList.add(route);
                }
                filterRoutesByType(transportTypeSpinner.getSelectedItem().toString());  // Фільтруємо при завантаженні
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Метод для фільтрації за типом транспорту
    private void filterRoutesByType(String transportType) {
        filteredRouteList.clear();
        if (transportType.equals("Усі")) {
            filteredRouteList.addAll(routeList);
        } else {
            for (Route route : routeList) {
                if (route.getTransportType().equals(transportType)) {
                    filteredRouteList.add(route);
                }
            }
        }
        routeAdapter.notifyDataSetChanged();
    }
}