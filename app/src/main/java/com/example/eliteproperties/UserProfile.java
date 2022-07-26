package com.example.eliteproperties;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityUserProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import model.User;

public class UserProfile extends DrawerActivity {
    ActivityUserProfileBinding activityUserProfileBinding;


    EditText edFirstName, edLastName, edContactNo, edPassword, edConfPass;

    Button btnUpdate, btnCancel;

    TextView edEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("Add New Property");

        //setContentView(R.layout.activity_user_profile);

        initialize();
    }

    private void initialize() {
        setLoginMenu();
        edFirstName = findViewById(R.id.edFName);
        edLastName = findViewById(R.id.edLName);
        edContactNo = findViewById(R.id.edCn);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPass);
        edConfPass = findViewById(R.id.edCPass);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        setUserDetails();
        
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = edFirstName.getText().toString().trim();
                String lastName = edLastName.getText().toString().trim();
                String contactNo = edContactNo.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                String confPassword = edConfPass.getText().toString().trim();

                if(ValidateDetails(firstName, lastName, contactNo, password, confPassword)){

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                User user = snapshot.getValue(User.class);
                                //Log.d("Details","Data"+snapshot);

                                user.setFirstName(firstName);
                                user.setLastName(lastName);
                                user.setContactNo(contactNo);
                                user.setPassword(password);

                                FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UserProfile.this, "Details Updated", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setUserDetails() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                edFirstName.setText(user.getFirstName());
                edLastName.setText(user.getLastName());
                edContactNo.setText(user.getContactNo());
                edEmail.setText(user.getEmailId());
                edPassword.setText(user.getPassword());
                edConfPass.setText(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean ValidateDetails(String firstName, String lastName, String contactNo, String password, String confPassword) {
        if(firstName.isEmpty()){
            edFirstName.setError("First name is required");
            edFirstName.requestFocus();
            return false;
        }
        if(lastName.isEmpty()){
            edLastName.setError("Last name is required");
            edLastName.requestFocus();
            return false;
        }
        if(contactNo.isEmpty()){
            edContactNo.setError("Contact number is required");
            edContactNo.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            edPassword.setError("Password is required");
            edPassword.requestFocus();
            return false;
        }
        if(password.length() < 6){
            edPassword.setError("Min password length should be 6 characters !");
            edPassword.requestFocus();
            return false;
        }

        if(confPassword.isEmpty()){
            edConfPass.setError("Enter password to verify");
            edConfPass.requestFocus();
            return false;
        }
        if (!password.equals(confPassword)){
            edConfPass.setError("Password does't match");
            edConfPass.requestFocus();
            return false;
        }
        return true;
    }

}