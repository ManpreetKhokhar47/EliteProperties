package com.example.eliteproperties;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.eliteproperties.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.Property;
import model.PropertyAdapter;

public class DashboardActivity extends DrawerActivity {

    ActivityDashboardBinding activityDashboardBinding;

    ListView lvProList;
    FirebaseUser currentUser;
    DatabaseReference dbProperties;
    PropertyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());

        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");
        initialize();

    }
    private void initialize()
    {

        setLoginMenu();
        lvProList = findViewById(R.id.lvProList);
        dbProperties = FirebaseDatabase.getInstance().getReference("Properties");
        ArrayList<Property> propList = new ArrayList<Property>();

        getPropertiesForSale(propList);
        lvProList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Property prp = propList.get(position);
                Intent intent = new Intent(getApplicationContext(),propertyDetails.class);
                intent.putExtra("PropDetail",prp);
                startActivity(intent);
            }
        });


    }

    private void getPropertiesForSale(ArrayList<Property> propList) {
        dbProperties.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Property prop;
                    for(DataSnapshot aProp : dataSnapshot.getChildren() ){
                        prop = aProp.getValue(Property.class);
                        propList.add(prop);
                    }
                    adapter = new PropertyAdapter(getApplicationContext(), propList);
                    lvProList.setAdapter(adapter);

                }
            }
        });
    }

}