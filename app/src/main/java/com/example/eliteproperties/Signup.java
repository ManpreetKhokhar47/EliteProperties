package com.example.eliteproperties;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.User;

public class Signup extends DrawerActivity implements View.OnClickListener {

    ActivitySignupBinding activitySignupBinding;

    private FirebaseAuth mAuth;

    EditText edFirstName, edLastName, edContactNo, edEmail, edPassword, edConfPass;
    RadioGroup rgLoginType;
    CheckBox termCondition;
    Button btnRegister;

    TextView tvAccountExist;

    String userType="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySignupBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(activitySignupBinding.getRoot());
        allocateActivityTitle("Registration");

        //setContentView(R.layout.activity_signup);

        initialize();

    }

    private void initialize() {

        setLoginMenu();
        mAuth = FirebaseAuth.getInstance();

        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edContactNo = findViewById(R.id.edContactNo);
        edEmail = findViewById(R.id.edEmailId);
        edPassword = findViewById(R.id.edPassword);
        edConfPass = findViewById(R.id.edConfPass);
        rgLoginType = findViewById(R.id.rgLoginType);
        termCondition = findViewById(R.id.term_condition);
        tvAccountExist = findViewById(R.id.tvAccountExist);

        btnRegister = findViewById(R.id.btnRegister);

        tvAccountExist.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRegister.setEnabled(false);

        termCondition.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tvAccountExist:
                finish();
                break;
            case R.id.btnRegister:
                registerUser();
                break;
            case R.id.term_condition:
                enableButton();
                break;

        }
    }
    private void enableButton() {
        if(termCondition.isChecked()){
            btnRegister.setEnabled(true);
        }
        else {
            btnRegister.setEnabled(false);
        }
    }

    private void registerUser() {

        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();
        String contactNo = edContactNo.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String confPassword = edConfPass.getText().toString().trim();


        int rbid = rgLoginType.getCheckedRadioButtonId();
        switch (rbid){
            case R.id.rbBuyer:
                userType = "Buyer";
                break;
            case R.id.rbOwner:
                userType = "Owner";
                break;
        }
        if(ValidateDetails(firstName, lastName, contactNo, email, password, confPassword)){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        ArrayList<String> myProperties = new ArrayList<String>();
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        User user = new User(firstName,lastName,contactNo,email,password,userType,myProperties,uid);
                        FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Signup.this, "User has been registered successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(Signup.this, "failed to register ! Try again inner " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(Signup.this, "failed to register ! Try again outer" + task.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }

    private boolean ValidateDetails(String firstName, String lastName, String contactNo, String email, String password, String confPassword) {
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
        if(email.isEmpty()){
            edEmail.setError("Email id is required");
            edEmail.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Enter a valid emial id");
            edEmail.requestFocus();
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