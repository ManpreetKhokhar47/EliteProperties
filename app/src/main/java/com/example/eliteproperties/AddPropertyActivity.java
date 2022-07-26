package com.example.eliteproperties;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityAddPropertyBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import model.Property;
import model.User;

public class AddPropertyActivity extends DrawerActivity implements View.OnClickListener , OnSuccessListener, OnFailureListener {

    ActivityAddPropertyBinding activityAddPropertyBinding;
    Spinner spinnerParkingSpaces;
    EditText edLotSize,edCoveredArea,edNoOfBed,edNoOfBath,edPrice,edAddress,edDesc;
    RadioGroup rgPropType,rgPostingFor;
    ImageView imgProp;
    Button btnRegister;
    Uri photoPath;
    String parkingSpaces,propertyType,postingFor;
    float propPrice;


    ProgressDialog progressDialog;

    ActivityResultLauncher activityResultLauncher;

    FirebaseStorage storage;
    StorageReference storageReference,sRef;
    DatabaseReference databaseReference;
    Property property;


    String lotSize,coveredArea,noOfBed,noOfBath,price,address,desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddPropertyBinding = ActivityAddPropertyBinding.inflate(getLayoutInflater());
        setContentView(activityAddPropertyBinding.getRoot());
        allocateActivityTitle("Add New Property");
        initialize();
    }

    private void initialize() {
        setLoginMenu();
        spinnerParkingSpaces = findViewById(R.id.edParkingSpaces);
        String[] parkingSpacesArray  = getResources().getStringArray(R.array.parkingSpacesArray);
        ArrayAdapter<String> propTypeAdap = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,parkingSpacesArray);
        spinnerParkingSpaces.setAdapter(propTypeAdap);
        spinnerParkingSpaces.setSelection(0);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                getPhoto(result);
            }
        });

        edLotSize = findViewById(R.id.edLotSize);
        edCoveredArea = findViewById(R.id.edCoveredArea);
        edNoOfBed = findViewById(R.id.edBedrooms);
        edNoOfBath = findViewById(R.id.edBathrooms);
        edPrice = findViewById(R.id.edPrice);
        edAddress = findViewById(R.id.edPropAddress);
        edDesc = findViewById(R.id.edDesc);

        rgPropType = findViewById(R.id.rgPropType);
        rgPostingFor = findViewById(R.id.rgPropFor);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        imgProp = findViewById(R.id.imgProperty);
        imgProp.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();



    }

    private void getPhoto(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK && result.getData() != null)
        {
            photoPath = result.getData().getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoPath);
                imgProp.setImageBitmap(bitmap);

            }catch (IOException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                addRecord();
                break;
            case R.id.imgProperty:
                browsePicture();
                break;
        }
    }

    private void browsePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Photo"));
    }

    private void addRecord(){


        lotSize = edLotSize.getText().toString().trim();
        coveredArea = edCoveredArea.getText().toString().trim();
        noOfBed = edNoOfBed.getText().toString().trim();
        noOfBath = edNoOfBath.getText().toString().trim();
        parkingSpaces = spinnerParkingSpaces.getSelectedItem().toString();
        price = edPrice.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        desc = edDesc.getText().toString().trim();


        switch (rgPropType.getCheckedRadioButtonId()){
            case R.id.rbHouse:
                propertyType = "House";
                break;
            case R.id.rbBasement:
                propertyType = "Basement";
                break;
            case R.id.rbApartment:
                propertyType = "Apartment";
                break;
        }

        switch (rgPostingFor.getCheckedRadioButtonId()){
            case R.id.rbForSell:
                postingFor = "Sell";
                break;
            case R.id.rbForRent:
                postingFor = "Rent";
                break;
        }



        if(validateData(lotSize,coveredArea,noOfBed,noOfBath,spinnerParkingSpaces,price,address,desc,photoPath)){
            try {
                String propertyId = UUID.randomUUID().toString();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String imageUrl = "Assigned on inserting time";
                propPrice = Float.valueOf(price);
                postingFor = postingFor.concat(propertyType);
                property = new Property(propertyId, userId, lotSize, coveredArea, noOfBed, noOfBath, parkingSpaces, propPrice, address, desc, propertyType, imageUrl, postingFor);
                uploadPhoto(property);
            }
            catch (Exception e){
                Toast.makeText(this, "Enter valid value for rent", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }



    }

    private void uploadPhoto(Property prop)
    {

            try{
                if(photoPath != null){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading the photo in progress...");
                    progressDialog.show();
                    sRef = storageReference.child("images/"+ UUID.randomUUID());
                    sRef.putFile(photoPath).addOnSuccessListener(this);
                    sRef.putFile(photoPath).addOnFailureListener(this);
                    UploadTask uploadTask = sRef.putFile(photoPath);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return  sRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    String imageUrl = downloadUri.toString();

                                    // Adding property detail
                                    prop.setSrcImg(imageUrl);

                                    FirebaseDatabase.getInstance().getReference("Properties").child(prop.getPropertyId()).setValue(prop).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                FirebaseDatabase.getInstance().getReference(prop.getPostingFor()+"_"+prop.getPropType()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                        ArrayList<String> propertiesNumber;
                                                        if (dataSnapshot.exists())
                                                        {
                                                            propertiesNumber = (ArrayList<String>)dataSnapshot.child("List").getValue();
                                                            propertiesNumber.add(prop.getPropertyId());
                                                            FirebaseDatabase.getInstance().getReference(prop.getPostingFor()+"_"+prop.getPropType()).child("List").setValue(propertiesNumber);
                                                        }
                                                        else {
                                                            propertiesNumber = new ArrayList<String>();
                                                            propertiesNumber.add(prop.getPropertyId());
                                                            FirebaseDatabase.getInstance().getReference(prop.getPostingFor()+"_"+prop.getPropType()).child("List").setValue(propertiesNumber);

                                                        }
                                                    }
                                                });

                                                String uid = prop.getUserId();

                                                FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.exists()){
                                                            User user = snapshot.getValue(User.class);
                                                            //Log.d("Details","Data"+snapshot);

                                                            ArrayList<String> myProperties = user.getPropertiesList();
                                                            myProperties.add(prop.getPropertyId());
                                                            user.setPropertiesList(myProperties);
                                                            FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(AddPropertyActivity.this, "User List Updated", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                                                AlertDialog.Builder alert = new AlertDialog.Builder(AddPropertyActivity.this);
                                                alert.setMessage("Do you want to add another property ? ")
                                                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                clearFields();
                                                            }
                                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        });




                                                AlertDialog alertDialog = alert.create();
                                                alertDialog.setTitle("Property Added Successfully");
                                                alertDialog.show();


                                            } else {
                                                Toast.makeText(AddPropertyActivity.this, "Failed to add property ! Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

            }
            catch (Exception e) {
                Toast.makeText(this, "Error 1" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    }


    private void clearFields() {
        edLotSize.setText(null);
        edCoveredArea.setText(null);
        edNoOfBed.setText(null);
        edNoOfBath.setText(null);
        edAddress.setText(null);
        edDesc.setText(null);
        edPrice.setText(null);
        spinnerParkingSpaces.setSelection(0);
        imgProp.setImageResource(R.drawable.ic_baseline_image_24);

    }

    private boolean validateData(String lotSize, String coveredArea , String noOfBed, String noOfBath , Spinner spinnerParkingSpaces,String price, String address, String desc, Uri path )
    {
        if(lotSize.isEmpty()){
            edLotSize.setError("lot size is required");
            edLotSize.requestFocus();
            return false;
        }
        if(coveredArea.isEmpty()){
            edCoveredArea.setError("covered area is required");
            edCoveredArea.requestFocus();
            return false;
        }
        if(noOfBed.isEmpty()){
            edNoOfBed.setError("Number of bed is required");
            edNoOfBed.requestFocus();
            return false;
        }
        if(noOfBath.isEmpty()){
            edNoOfBath.setError("Number of bathroom is required");
            edNoOfBath.requestFocus();
            return false;
        }
        if(spinnerParkingSpaces.getSelectedItemPosition() == 0){
            TextView errorText = (TextView)spinnerParkingSpaces.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Parking Spaces");//changes the selected item text to this
            spinnerParkingSpaces.requestFocus();
            return false;
        }

        if(price.isEmpty()){
            edPrice.setError("Price is required");
            edPrice.requestFocus();
            return false;
        }

        if(address.isEmpty()){
            edAddress.setError("Address is required");
            edAddress.requestFocus();
            return false;
        }

        if(desc.isEmpty()){
            edDesc.setError("Please provide some description");
            edDesc.requestFocus();
            return false;
        }
        if(path == null){
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
            return false;
        }
    return true;
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(Object o) {
        progressDialog.dismiss();

    }
}