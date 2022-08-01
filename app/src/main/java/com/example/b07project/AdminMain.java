package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class AdminMain extends AppCompatActivity {
    RecyclerView recyclerView;
    Button createV;
    Button viewV;
    Myadapter myadapter;
    ArrayList<Venue> venlist;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Venues");
    ArrayList<Venue> venues = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        SharedPreferences sharedPref = getSharedPreferences("save",MODE_PRIVATE);
        boolean isadmin = sharedPref.getBoolean("value",false);
        if (!isadmin){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        createV = findViewById(R.id.CreateVenue);
        createV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateVenue.class));
                //Link to Create Venues page but set to an arbitary venue for now

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myadapter);
        myadapter = new Myadapter(this,venues);
        //The following method will be triggered when any venue is clicked
        viewV = findViewById(R.id.ViewVenue);
        viewV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminMain.this, ViewVenue.class);
                intent.putExtra("hashCode", venues.get(0).getVenueHashCode());
                startActivity(intent);
            }
        });

        //The following code loops through the database and creates objects from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    String hashCode = snapshot.getKey();
                    System.out.println(hashCode);
                    String date = snapshot.child("date").getValue().toString();
                    int endTime = Integer.parseInt(snapshot.child("endTime").getValue().toString());
                    int startTime = Integer.parseInt(snapshot.child("startTime").getValue().toString());
                    String venueName = snapshot.child("venueName").getValue().toString();
//                    String location = snapshot.child("location").getValue().toString();

                    //Eventually a sorting alorithm will go here so that the location is priority
                    Venue obj = new Venue(venueName, startTime, endTime, date, null, hashCode, null);
                    venues.add(obj);




                }
                myadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}