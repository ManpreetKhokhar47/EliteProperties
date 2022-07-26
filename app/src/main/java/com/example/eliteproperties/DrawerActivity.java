package com.example.eliteproperties;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Menu menu;
    MenuItem menuLogin,menuSearch;
    Boolean loginStatus;


    Animation slideIn,slideOut;


    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        initialize();



    }


    private void initialize() {
        menu = navigationView.getMenu();
        menuLogin = menu.findItem(R.id.menuLogin);
        menuSearch = menu.findItem(R.id.menuSearch);
        //setLoginMenu(false);
        slideIn= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in);
        slideOut= AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_out);



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuHome:
                gotoHome();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menuLogin:
                gotoLoginActivity();
                break;
            case R.id.menuAddProperty:
                gotoAddPropertyActivity();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menuLogOut:
                gotoLogOut();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menuProfile:
                gotoProfile();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menuSearch:
                gotoSearchProp();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menuMyProperties:
                gotoMyProperty();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            default:
                return true;
        }


        return false;
    }

    private void gotoHome() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoMyProperty() {
        Intent intent = new Intent(this,MyProperties.class);
        startActivity(intent);
        finish();
    }

    private void gotoSearchProp() {
        Intent intent = new Intent(this,PropertyList.class);
        startActivity(intent);
        finish();
    }

    private void gotoProfile() {
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }

    private void gotoLogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,DashboardActivity.class);
        overridePendingTransition(0,0);
        intent.putExtra("isLogin",false);
        startActivity(intent);
        finish();

    }

    protected void allocateActivityTitle(String title){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(title);
        }
    }

    protected void setLoginMenu()
    {
        loginStatus = isLogin();
        if(loginStatus){
            menu.findItem(R.id.menuLogin).setVisible(false);
            menu.findItem(R.id.menuProfile).setVisible(true);
            menu.findItem(R.id.menuLogOut).setVisible(true);
            menu.findItem(R.id.menuAddProperty).setVisible(true);
            menu.findItem(R.id.menuMyProperties).setVisible(true);

        }
        else {
            menu.findItem(R.id.menuLogin).setVisible(true);
            menu.findItem(R.id.menuProfile).setVisible(false);
            menu.findItem(R.id.menuLogOut).setVisible(false);
            menu.findItem(R.id.menuAddProperty).setVisible(false);
            menu.findItem(R.id.menuMyProperties).setVisible(false);
        }
    }
    protected boolean isLogin(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            return true;
        }
        else {
            return false;
        }
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this,Login.class);
        overridePendingTransition(0,0);
        startActivity(intent);
        finish();
    }
    private void gotoAddPropertyActivity() {
        Intent intent = new Intent(this,AddPropertyActivity.class);
        overridePendingTransition(0,0);
        startActivity(intent);
        finish();
    }

}