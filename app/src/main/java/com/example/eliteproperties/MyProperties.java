package com.example.eliteproperties;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eliteproperties.databinding.ActivityMyPropertiesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Property;
import model.PropertyAdapter;

public class MyProperties extends DrawerActivity {
    ActivityMyPropertiesBinding activityMyPropertiesBinding;
    ListView lvMyProList;
    PropertyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyPropertiesBinding = ActivityMyPropertiesBinding.inflate(getLayoutInflater());
        setContentView(activityMyPropertiesBinding.getRoot());
        allocateActivityTitle("My Properties");

        //setContentView(R.layout.activity_my_properties);
        initi();
    }

    private void initi() {
        setLoginMenu();
        lvMyProList = findViewById(R.id.lvProList);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        ArrayList<Property> propertiesList = new ArrayList<Property>();

        getMyProperties(propertiesList);

        adapter = new PropertyAdapter(getApplicationContext(),propertiesList);
        lvMyProList.setAdapter(adapter);

        lvMyProList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Property prp = propertiesList.get(position);
                Intent intent = new Intent(getApplicationContext(),MyPropertyDetail.class);
                intent.putExtra("PropDetail",prp);
                startActivity(intent);
            }
        });


    }

    private void getMyProperties(ArrayList<Property> propertiesList) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = FirebaseDatabase.getInstance().getReference("Properties").orderByChild("userId").equalTo(uid);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    propertiesList.clear();
                    Property property;
                    for(DataSnapshot aProp : snapshot.getChildren())
                    {
                        property = aProp.getValue(Property.class);
                        propertiesList.add(property);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}