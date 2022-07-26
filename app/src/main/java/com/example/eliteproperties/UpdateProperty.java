package com.example.eliteproperties;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.app.ProgressDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityUpdatePropertyBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import model.Property;

public class UpdateProperty extends DrawerActivity implements View.OnClickListener, OnSuccessListener, OnFailureListener {
    ActivityUpdatePropertyBinding activityUpdatePropertyBinding;

    Spinner spinnerParkingSpaces;
    EditText edLotSize,edCoveredArea,edNoOfBed,edNoOfBath,edPrice,edAddress,edDesc;
    ImageView imgProp;
    Button btnUpdate,btnCancel;
    Uri photoPath;
    String parkingSpaces,propertyType,postingFor;
    float propPrice;
    String[] parkingSpacesArray;
    ArrayAdapter<String> propTypeAdap;

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
        //setContentView(R.layout.activity_update_property);
        activityUpdatePropertyBinding = ActivityUpdatePropertyBinding.inflate(getLayoutInflater());
        setContentView(activityUpdatePropertyBinding.getRoot());
        allocateActivityTitle("Update Information");
        initilize();
    }

    private void initilize() {
        setLoginMenu();
        spinnerParkingSpaces = findViewById(R.id.edParkingSpacesU);
        parkingSpacesArray  = getResources().getStringArray(R.array.parkingSpacesArray);
        propTypeAdap = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,parkingSpacesArray);
        spinnerParkingSpaces.setAdapter(propTypeAdap);
        spinnerParkingSpaces.setSelection(0);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                getPhoto(result);
            }
        });


        edLotSize = findViewById(R.id.edLotSizeU);
        edCoveredArea = findViewById(R.id.edCoveredAreaU);
        edNoOfBed = findViewById(R.id.edBedroomsU);
        edNoOfBath = findViewById(R.id.edBathroomsU);
        edPrice = findViewById(R.id.edPriceU);
        edAddress = findViewById(R.id.edPropAddressU);
        edDesc = findViewById(R.id.edDescU);

        btnUpdate = findViewById(R.id.btnUpdateU);
        btnUpdate.setOnClickListener(this);
        imgProp = findViewById(R.id.imgPropertyU);
        imgProp.setOnClickListener(this);

        btnCancel = findViewById(R.id.btnCancelU);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        property = (Property) getIntent().getExtras().getSerializable("property");

        setValues(property);

    }

    private void setValues(Property property) {
        edLotSize.setText(property.getLotSize());
        edCoveredArea.setText(property.getCoveredArea());
        edNoOfBed.setText(property.getBedroomsCount());
        edNoOfBath.setText(property.getBathroomCount());
        edPrice.setText(String.valueOf(property.getPrice()));
        edAddress.setText(property.getAddress());
        edDesc.setText(property.getDesc());
        spinnerParkingSpaces.setSelection(((ArrayAdapter<String>)spinnerParkingSpaces.getAdapter()).getPosition(property.getParkingSpaces()));

        Picasso.get().load(property.getSrcImg()).into(imgProp);
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
            case R.id.btnUpdateU:
                updateRec();
                break;
            case R.id.imgPropertyU:
                browsePicture();
                break;
        }

    }

    private void updateRec() {
        lotSize = edLotSize.getText().toString().trim();
        coveredArea = edCoveredArea.getText().toString().trim();
        noOfBed = edNoOfBed.getText().toString().trim();
        noOfBath = edNoOfBath.getText().toString().trim();
        parkingSpaces = spinnerParkingSpaces.getSelectedItem().toString();
        price = edPrice.getText().toString().trim();
        address = edAddress.getText().toString().trim();
        desc = edDesc.getText().toString().trim();


        if(validateData(lotSize,coveredArea,noOfBed,noOfBath,spinnerParkingSpaces,price,address,desc,photoPath)){
            try {
                String propertyId = property.getPropertyId();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String imageUrl = "Assigned on inserting time";
                propPrice = Float.valueOf(price);

                property.setLotSize(lotSize);
                property.setCoveredArea(coveredArea);
                property.setBedroomsCount(noOfBed);
                property.setBathroomCount(noOfBath);
                property.setParkingSpaces(parkingSpaces);
                property.setPrice(propPrice);
                property.setAddress(address);
                property.setDesc(desc);

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


    private void uploadPhoto(Property property) {
        try {
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
                                    property.setSrcImg(imageUrl);

                                    FirebaseDatabase.getInstance().getReference("Properties").child(property.getPropertyId()).setValue(property).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(), "Property Updated successfully", Toast.LENGTH_SHORT).show();
                                                finish();

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed to add property ! Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        catch (Exception e){
            Toast.makeText(this, "Error 1" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void browsePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Photo"));
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