package com.example.sawaiz.smartworker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreen extends AppCompatActivity {


    private Switch availabilitySwitch;

    private FirebaseAuth mAuth;

    private Button logoutBtn;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private TextView ProfileEmail;



    private android.support.v7.widget.Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        mToolbar = (android.support.v7.widget.Toolbar) (findViewById(R.id.nav_action));
        setSupportActionBar(mToolbar);
        mdrawerLayout = (DrawerLayout) (findViewById(R.id.drawerLayout));
        mToggle = new ActionBarDrawerToggle(this, mdrawerLayout, R.string.open, R.string.close);
        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logoutBtn = (Button) (findViewById(R.id.logOutBtn));

        ProfileEmail = (TextView)(findViewById(R.id.nav_profileEmail));



        // Firebase Instance

        mAuth = FirebaseAuth.getInstance();
//get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        availabilitySwitch = (Switch) (findViewById(R.id.availabiltySwitch));
        availabilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    Toast.makeText(HomeScreen.this, "You're online now", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(HomeScreen.this, "You're offline now", Toast.LENGTH_SHORT).show();
                }

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null){
                    Toast.makeText(HomeScreen.this, user.getEmail() + " Your season has ended",
                            Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            Intent logoutIntent = new Intent(HomeScreen.this,LoginActivity.class);
                            startActivity(logoutIntent);
                            finish();
                }else{
                    Toast.makeText(HomeScreen.this,  " Not Signed in",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
