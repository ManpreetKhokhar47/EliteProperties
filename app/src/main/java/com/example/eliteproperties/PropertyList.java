package com.example.eliteproperties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityMainBinding;
import com.example.eliteproperties.databinding.ActivityPropertyListBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import model.Property;
import model.PropertyAdapter;

public class PropertyList extends DrawerActivity  {

    ActivityPropertyListBinding activityPropertyListBinding;

    ListView lvProperList;
    FirebaseUser currentUser;
    DatabaseReference dbProperties;
    PropertyAdapter adapter;
    Button btnFilter,btnSearch;
    boolean isPanelVisible = false;
    View searchPanel;

    RadioGroup rgPropTypeS,rgLookingFor;
    EditText edMinPrice,edMaxprice,edBedCount,edParking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        activityPropertyListBinding = ActivityPropertyListBinding.inflate(getLayoutInflater());
        setContentView(activityPropertyListBinding.getRoot());
        allocateActivityTitle("Properties List");
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_property_list);

        initialize();

    }

    private void initialize() {


        setLoginMenu();
        lvProperList = findViewById(R.id.lvPropertyList);
        dbProperties = FirebaseDatabase.getInstance().getReference("Properties");
        btnFilter = findViewById(R.id.btnFilter);
        searchPanel = findViewById(R.id.searchPanel);
        btnSearch = findViewById(R.id.btnSearch);

        rgPropTypeS = findViewById(R.id.rgPropTypeS);
        rgLookingFor = findViewById(R.id.rgLookingForS);
        edMinPrice = findViewById(R.id.edMinPrice);
        edMaxprice =findViewById(R.id.edMaxPrice);
        edBedCount = findViewById(R.id.edbedCount);
        edParking = findViewById(R.id.edParkCountS);
        ArrayList<Property> propList = new ArrayList<Property>();

        adapter = new PropertyAdapter(getApplicationContext(), propList);
        lvProperList.setAdapter(adapter);





        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPanelVisible){
                    isPanelVisible = false;
                    searchPanel.startAnimation(slideOut);
                    searchPanel.setVisibility(View.GONE);
                } else{
                    isPanelVisible = true;
                    searchPanel.startAnimation(slideIn);
                    searchPanel.setVisibility(View.VISIBLE);
                }
            }
        });



        SearchProperties(propList);

        lvProperList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Property prp = propList.get(position);
                Intent intent = new Intent(getApplicationContext(),propertyDetails.class);
                intent.putExtra("PropDetail",prp);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchPanel.startAnimation(slideOut);
                searchPanel.setVisibility(View.GONE);
                isPanelVisible = false;
                String lookingFor = null,propType=null;
                float minPrice = -1,maxPrice = -1;
                int bedCount = -1,parking = -1;

                switch (rgLookingFor.getCheckedRadioButtonId()){
                    case R.id.rbBuyS:
                        lookingFor = "Sell";
                        break;
                    case R.id.rbRentS:
                        lookingFor = "Rent";
                        break;
                }
                switch (rgPropTypeS.getCheckedRadioButtonId()){
                    case R.id.rbHouseS:
                        propType = "House";
                        break;
                    case R.id.rbBasementS:
                        propType = "Basement";
                        break;
                    case R.id.rbApartmentS:
                        propType = "Apartment";
                        break;
                }
                if(!(edMinPrice.getText().toString().isEmpty())){
                    try {
                        minPrice = Float.parseFloat(edMinPrice.getText().toString());
                    }
                    catch (Exception e){

                    }
                }

                if(!(edMaxprice.getText().toString().isEmpty())){
                    try {
                        maxPrice = Float.parseFloat(edMaxprice.getText().toString());
                    }
                    catch (Exception e){

                    }
                }

                if(!(edBedCount.getText().toString().isEmpty())){
                    try {
                        bedCount = Integer.parseInt(edBedCount.getText().toString());
                    }
                    catch (Exception e){

                    }
                }
                if(!(edParking.getText().toString().isEmpty())){
                    try {
                        parking = Integer.parseInt(edParking.getText().toString());
                    }
                    catch (Exception e){

                    }
                }

                if(minPrice > 0 && maxPrice >0 ){
                    if(minPrice>maxPrice)
                    {
                        edMaxprice.setError("Min price should be less than Max Price");
                    }
                    else {
                        customSearch(propList,lookingFor,propType,minPrice,maxPrice,bedCount,parking);
                    }

                }
                else {
                    customSearch(propList,lookingFor,propType,minPrice,maxPrice,bedCount,parking);

                }

            }
        });


    }

    private void customSearch(ArrayList<Property> propList, String lookingFor, String propType, float minPrice, float maxPrice, int bedCount, int parking) {
        Query query = dbProperties.orderByChild("postingFor").equalTo(lookingFor.concat(propType));
        
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                propList.clear();
                if (snapshot.exists()) {
                    Property prop;
                    for (DataSnapshot aProp : snapshot.getChildren()) {
                        prop = aProp.getValue(Property.class);
                        propList.add(prop);
                    }

                    for (Iterator<Property> prp = propList.iterator(); prp.hasNext(); ) {
                        Property oneProp = prp.next();
                        if (minPrice > 0 && oneProp.getPrice() < minPrice) {
                            prp.remove();
                            continue;
                        }
                        if (maxPrice > 0 && oneProp.getPrice() > maxPrice) {
                            prp.remove();
                            continue;
                        }
                        if (bedCount > 0 && Integer.parseInt(oneProp.getBedroomsCount()) != bedCount) {
                            prp.remove();
                            continue;
                        }
                        if (parking > 0 && Integer.parseInt(oneProp.getParkingSpaces()) != parking) {
                            prp.remove();
                            continue;

                        }
                    }
                    /*for (Property aProp : propList) {
                        if (minPrice > 0 && aProp.getPrice() < minPrice) {
                            propList.remove(aProp);
                        }
                    }
                    /*
                        if (maxPrice > 0 && aProp.getPrice() > maxPrice) {
                            propList.remove(aProp);
                            continue;
                        }
                        if (bedCount > 0 && Integer.parseInt(aProp.getBedroomsCount()) != bedCount) {
                            propList.remove(aProp);
                            continue;
                        }
                        if (parking > 0 && Integer.parseInt(aProp.getParkingSpaces()) != parking) {
                            propList.remove(aProp);
                            Toast.makeText(PropertyList.this, "Parking Filter Works", Toast.LENGTH_SHORT).show();
                            continue;
                        }

                    }*/
                    adapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(PropertyList.this, "No result found", Toast.LENGTH_SHORT).show();
                    adapter = new PropertyAdapter(getApplicationContext(), propList);
                    lvProperList.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
            
        });

    }

    private void SearchProperties(ArrayList<Property> propList ) {
        dbProperties.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Property prop;
                    propList.clear();
                    for(DataSnapshot aProp : dataSnapshot.getChildren() ){
                        prop = aProp.getValue(Property.class);
                        propList.add(prop);
                    }
                    //adapter = new PropertyAdapter(getApplicationContext(), propList);
                    //lvProperList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }
        });
    }


}