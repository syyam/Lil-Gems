package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMeals extends AppCompatActivity {

    private EditText monday;
    private EditText tuesday;
    private EditText thursday;
    private EditText wednesday;
    private EditText friday;
    private EditText saturday;
    private EditText sunday;
    private DatabaseReference mDayDatabase;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_meals);

        monday=(EditText) findViewById(R.id.monday);
        tuesday=(EditText) findViewById(R.id.tuesday);
        wednesday=(EditText) findViewById(R.id.wednesday);
        thursday=(EditText) findViewById(R.id.thursday);
        friday=(EditText) findViewById(R.id.friday);
        saturday=(EditText) findViewById(R.id.saturday);
        sunday=(EditText) findViewById(R.id.sunday);

        monday.setFocusable(false);
        monday.setClickable(false);
        tuesday.setFocusable(false);
        tuesday.setClickable(false);
        wednesday.setFocusable(false);
        wednesday.setClickable(false);
        thursday.setFocusable(false);
        thursday.setClickable(false);
        friday.setFocusable(false);
        friday.setClickable(false);
        saturday.setFocusable(false);
        saturday.setClickable(false);
        sunday.setFocusable(false);
        sunday.setClickable(false);


        mDayDatabase= FirebaseDatabase.getInstance().getReference().child("Menu");


        mDayDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Toast.makeText(UserMeals.this,"Loading data...",Toast.LENGTH_SHORT);
                String mo=dataSnapshot.child("Monday").getValue().toString();
                String tu=dataSnapshot.child("Tuesday").getValue().toString();
                String we=dataSnapshot.child("Wednesday").getValue().toString();
                String th=dataSnapshot.child("Thursday").getValue().toString();
                String fr=dataSnapshot.child("Friday").getValue().toString();
                String sa=dataSnapshot.child("Saturday").getValue().toString();
                String su=dataSnapshot.child("Sunday").getValue().toString();

                monday.setText(mo);
                tuesday.setText(tu);
                wednesday.setText(we);
                thursday.setText(th);
                friday.setText(fr);
                saturday.setText(sa);
                sunday.setText(su);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
