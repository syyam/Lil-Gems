package com.example.syyam.lilgems;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageButton mGoals;
    private ImageButton mSendNotification;
    private ImageButton mMeals;
    private ImageButton mUploadPhotos;
    private ImageButton mSupplies;
    private ImageButton mToileting;
    private ImageButton mMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        mGoals=(ImageButton) findViewById(R.id.goals);
        mMeals=(ImageButton) findViewById(R.id.meals);
        mSupplies=(ImageButton) findViewById(R.id.supplies);
        mToileting=(ImageButton) findViewById(R.id.toileting);
        mMedication=(ImageButton) findViewById(R.id.medication);
        mUploadPhotos=(ImageButton) findViewById(R.id.addPhotos);
        mSendNotification=(ImageButton) findViewById(R.id.sendMessage);

        mMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//hogya
                Intent to=new Intent(UserMainActivity.this,UserMedic.class);
                startActivity(to);
            }
        });

        mSupplies.setOnClickListener(new View.OnClickListener() {//hogya
            @Override
            public void onClick(View view) {//hogya
                Intent to=new Intent(UserMainActivity.this,UserSupplies.class);
                startActivity(to);
            }
        });
        mToileting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//hogya
                Intent toilet=new Intent(UserMainActivity.this,UserToileting.class);//hogya
                startActivity(toilet);
            }
        });

        mMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//hogya
                Intent meal=new Intent(UserMainActivity.this,UserMeals.class);
                startActivity(meal);
            }
        });

        mGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//hogya
                Intent sendnoti=new Intent (UserMainActivity.this,UserGoals.class);
                startActivity(sendnoti);
            }
        });


        mSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendnoti=new Intent (UserMainActivity.this,sendNotification.class);
                startActivity(sendnoti);
            }
        });
        mUploadPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//hogya
                Intent goalIntent=new Intent(UserMainActivity.this,Photos.class);
                startActivity(goalIntent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out
                    Intent login=new Intent(UserMainActivity.this,startActivity.class);
                    startActivity(login);
                }
                // ...
            }
        };
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.logout)
        {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                Process.killProcess(Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
