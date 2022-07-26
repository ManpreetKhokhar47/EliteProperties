package com.example.eliteproperties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityBrokerListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.Broker;
import model.BrokerAdapter;
import model.Property;

public class BrokerList extends DrawerActivity {

    ActivityBrokerListBinding activityBrokerListBinding;

    ListView lvBrokerList;
    ArrayList<Broker> brokersList;
    BrokerAdapter brokerAdapter;
    Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_broker_list);

        activityBrokerListBinding = ActivityBrokerListBinding.inflate(getLayoutInflater());
        setContentView(activityBrokerListBinding.getRoot());
        allocateActivityTitle("Broker List");
        property = (Property) getIntent().getExtras().getSerializable("property");

        initialize();
    }

    private void initialize() {
        lvBrokerList = findViewById(R.id.lvBrokerListM);
        brokersList = new ArrayList<Broker>();


        lvBrokerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Broker aBroker = brokersList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(BrokerList.this);
                alert.setMessage("Are you sure to assign this property to " + aBroker.getFirstName() + " "+ aBroker.getLastName())
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                property.setManagedBy("Broker");
                                property.setManagerId(aBroker.getUserId());
                                
                                FirebaseDatabase.getInstance().getReference("Properties").child(property.getPropertyId()).setValue(property).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(BrokerList.this, "Broker assigned successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(BrokerList.this, "Oops ! Something went wrong. Please try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


                AlertDialog alertDialog = alert.create();
                alertDialog.setTitle("Assign Broker ?");
                alertDialog.show();
            }
        });

        getbrokerlist();

    }
    private void getbrokerlist() {
        FirebaseDatabase.getInstance().getReference("Brokers").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Broker broker;
                    for(DataSnapshot aBro : dataSnapshot.getChildren() ){
                        broker = aBro.getValue(Broker.class);
                        brokersList.add(broker);
                    }
                    brokerAdapter = new BrokerAdapter(getApplicationContext(), brokersList);
                    lvBrokerList.setAdapter(brokerAdapter);

                }

            }
        });
    }
}