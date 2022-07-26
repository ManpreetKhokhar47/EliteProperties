package com.example.eliteproperties;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eliteproperties.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import model.User;

public class Login extends DrawerActivity implements View.OnClickListener {

    ActivityLoginBinding activityLoginBinding;
    MenuItem menuProfile,menuLogin,menuLogout;

    Button btnLogin, btnSignUp;
    EditText edEmail, edPassword;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        allocateActivityTitle("Login");

        //setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        setLoginMenu();
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        edEmail = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edUserPassword);

        mAuth = FirebaseAuth.getInstance();

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case  R.id.btnLogin:
                loginUser();
                break;
            case R.id.btnSignUp:
                gotoSignUpActivity();
        }

    }

    private void loginUser() {
        String emailId = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if(!(emailId.isEmpty() && password.isEmpty())){
            mAuth.signInWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        edPassword.setText(null);
                        openUserActivity();
                    }
                    else {
                        Toast.makeText(Login.this, "Invalid username or password. Please check your credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Enter Username and Password", Toast.LENGTH_SHORT).show();
            edEmail.requestFocus();
        }
    }


    private void openUserActivity() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currentUser.getUid();

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                intent.putExtra("userDetail", user);
                intent.putExtra("isLogin",true);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuLogin).setVisible(false);
        return true;
    }*/

    private void gotoSignUpActivity() {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}