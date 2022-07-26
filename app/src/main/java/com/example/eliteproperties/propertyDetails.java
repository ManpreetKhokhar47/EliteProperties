package com.example.eliteproperties;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.eliteproperties.databinding.ActivityPropertyDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import model.Broker;
import model.Property;
import model.User;

public class propertyDetails extends DrawerActivity implements View.OnClickListener {
    ActivityPropertyDetailsBinding activityPropertyDetailsBinding;
    ImageView propImg;
    Button btnViewContact,btnBackPD;
    Property property;

    TextView propSize,propAddress,propDesc,propAmen, propType,propRent,propManagedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPropertyDetailsBinding = ActivityPropertyDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityPropertyDetailsBinding.getRoot());
        allocateActivityTitle("Property Detail");

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_property_details);

        init();

    }

    private void init() {
        setLoginMenu();
        propSize = findViewById(R.id.tvSizeProp);
        propAddress = findViewById(R.id.tvAddressProp);
        propDesc = findViewById(R.id.tvDescProp);
        propAmen = findViewById(R.id.tvAmenitiesProp);
        propType = findViewById(R.id.tvTypeProp);
        propRent = findViewById(R.id.tvRentProp);
        propImg = findViewById(R.id.imgProp);
        propManagedBy = findViewById(R.id.tvManagedBy);

        btnViewContact = findViewById(R.id.btnViewContact);
        btnBackPD = findViewById(R.id.btnBackPD);

        btnBackPD.setOnClickListener(this);
        btnViewContact.setOnClickListener(this);


        property = (Property) getIntent().getExtras().getSerializable("PropDetail");

        setValues(property);
    }
    private void setValues(Property property) {
        propSize.append(property.getLotSize());
        propAddress.append(property.getAddress());
        propDesc.append(property.getDesc());
        propAmen.setText( "Parking Spaces : "+property.getParkingSpaces());
        propType.append(property.getPropType());
        propRent.append("$ "+String.valueOf(property.getPrice()));
        propManagedBy.append(property.getManagedBy());
        Picasso.get().load(property.getSrcImg()).into(propImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnViewContact:
                gotoView();
                break;
            case R.id.btnBackPD:
                finish();
                break;
        }
    }

    private void gotoView() {
        AlertDialog.Builder alert = new AlertDialog.Builder(propertyDetails.this);
        if(isLogin()) {

            String manager = property.getManagedBy();
            String managerId = property.getManagerId();
            String tableName;
            if (manager.equals("Broker")) {
                tableName = "Brokers";
            } else {
                tableName = "Users";
            }
            FirebaseDatabase.getInstance().getReference(tableName).child(managerId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Broker aBroker;

                        if (tableName.equals("Brokers")) {
                            aBroker = snapshot.getValue(Broker.class);
                            alert.setMessage("Name : " + aBroker.getFirstName() + " " + aBroker.getLastName() + "\nPhone No : " + aBroker.getContactNo() + "\nEmail Id : " + aBroker.getEmailId())
                                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                        } else {
                            User aUser = snapshot.getValue(User.class);
                            alert.setMessage("Name : " + aUser.getFirstName() + " " + aUser.getLastName() + "\nPhone No : " + aUser.getContactNo() + "\nEmail Id : " + aUser.getEmailId())
                                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });

                        }

                        AlertDialog alertDialog = alert.create();
                        alertDialog.setTitle("Contact Information");
                        alertDialog.show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else {
            alert.setMessage("Please Login to view contact information")
                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

            AlertDialog alertDialog = alert.create();
            alertDialog.setTitle("Login Required");
            alertDialog.show();

        }

    }


}