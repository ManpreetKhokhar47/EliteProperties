package com.example.eliteproperties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eliteproperties.databinding.ActivityMyPropertyDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import model.Broker;
import model.Property;
import model.User;

public class MyPropertyDetail extends DrawerActivity implements View.OnClickListener {
    ActivityMyPropertyDetailBinding activityMyPropertyDetailBinding;

    ImageView propImg;
    Button btnUpdate,btnAssignBrk,btnBack;
    Property property;

    TextView propSize,propAddress,propDesc,propAmen, propType,propRent,propManagedBy,viewbroker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyPropertyDetailBinding = ActivityMyPropertyDetailBinding.inflate(getLayoutInflater());
        setContentView(activityMyPropertyDetailBinding.getRoot());
        allocateActivityTitle("My Property Details");
        //setContentView(R.layout.activity_my_property_detail);
        initial();
    }

    private void initial() {
        setLoginMenu();
        propSize = findViewById(R.id.tvSizePropM);
        propAddress = findViewById(R.id.tvAddressPropM);
        propDesc = findViewById(R.id.tvDescPropM);
        propAmen = findViewById(R.id.tvAmenitiesPropM);
        propType = findViewById(R.id.tvTypePropM);
        propRent = findViewById(R.id.tvRentPropM);
        propImg = findViewById(R.id.imgPropM);
        btnUpdate = findViewById(R.id.btnUpdateDetailsM);
        btnAssignBrk = findViewById(R.id.btnAssignBrkM);
        btnBack = findViewById(R.id.btnBackM);
        propManagedBy = findViewById(R.id.tvManagedByM);
        viewbroker = findViewById(R.id.tvViewBroker);

        btnUpdate.setOnClickListener(this);
        btnAssignBrk.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        property = (Property) getIntent().getExtras().getSerializable("PropDetail");
        setValues(property);

        viewbroker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Brokers").child(property.getManagerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Broker aBroker;
                            aBroker = snapshot.getValue(Broker.class);
                            AlertDialog.Builder alert = new AlertDialog.Builder(MyPropertyDetail.this);
                            alert.setMessage("Name : " + aBroker.getFirstName() + " " + aBroker.getLastName() + "\nPhone No : " + aBroker.getContactNo() + "\nEmail Id : " + aBroker.getEmailId())
                                    .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });


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
        });

    }
    private void setValues(Property property) {
        propSize.append(property.getLotSize());
        propAddress.append(property.getAddress());
        propDesc.append(property.getDesc());
        propAmen.setText( "Parking Spaces : "+property.getParkingSpaces());
        propType.append(property.getPropType());
        propRent.append("$ "+String.valueOf(property.getPrice()));
        propManagedBy.append((property.getManagedBy().equals("Broker"))?"Broker":"My Self");
        if(!property.getManagedBy().equals("Broker")){
            viewbroker.setVisibility(View.INVISIBLE);
        }
        Picasso.get().load(property.getSrcImg()).into(propImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateDetailsM:
                gotoUpdateProp();
                break;
            case R.id.btnAssignBrkM:
                assignBroker();
                break;
            case R.id.btnBackM:
                finish();
                break;
        }

    }

    private void assignBroker() {
        Intent intent = new Intent(this,BrokerList.class);
        intent.putExtra("property",property);
        startActivity(intent);
        finish();
    }

    private void gotoUpdateProp() {
        Intent intent = new Intent(this,UpdateProperty.class);
        intent.putExtra("property",property);
        startActivity(intent);
        finish();

    }
}