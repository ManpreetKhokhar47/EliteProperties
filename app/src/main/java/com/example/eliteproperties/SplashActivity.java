package com.example.eliteproperties;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    // Constant Time Delay
    private final int SPLASH_DELAY = 2000;

    // Fields (Widgets)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Methods to call
        getWindow().setBackgroundDrawable(null);
        initializeView();
        animateLogo();
        goToMainActivity();

    }
    private void goToMainActivity() {
        // This method will redirect the user to the main activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            finish();
        }, SPLASH_DELAY);
    }

    private void animateLogo() {
        // This will animate the logo
        Animation fadingAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadingAnimation.setDuration(SPLASH_DELAY);
        imageView.startAnimation(fadingAnimation);
    }
    private void initializeView() {
        imageView = findViewById(R.id.imageView);
    }

}