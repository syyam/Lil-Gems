package com.example.syyam.lilgems;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class startActivity extends AppCompatActivity {

    private ImageButton mAdminPanel;
    private ImageButton mFamilyPanel;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
                    final String current_id= mCurrentUser.getUid();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(current_id)) {
                                // run some code
                                Intent i = new Intent(startActivity.this, UserMainActivity.class);
                                startActivity(i);

                            }
                            else
                            {
                                Intent i = new Intent(startActivity.this, MainActivity.class);
                                startActivity(i);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    // User is signed out
                }
                // ...
            }
        };

        mAdminPanel=(ImageButton) findViewById(R.id.imageButton2);
        mFamilyPanel=(ImageButton) findViewById(R.id.imageButton);

        mAdminPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ad=new Intent(startActivity.this,adminLoginActivity.class);
                ad.putExtra("admin","admin");
                startActivity(ad);
            }
        });
        mFamilyPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fa=new Intent(startActivity.this,userLoginActivity.class);
                fa.putExtra("family","family");
                startActivity(fa);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}