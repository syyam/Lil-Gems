package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Meals extends AppCompatActivity {

    private EditText monday;
    private EditText tuesday;
    private EditText thursday;
    private EditText wednesday;
    private EditText friday;
    private EditText saturday;
    private EditText sunday;

    private Button Update;

    private DatabaseReference mDayDatabase;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);


        monday=(EditText) findViewById(R.id.monday);
        tuesday=(EditText) findViewById(R.id.tuesday);
        wednesday=(EditText) findViewById(R.id.wednesday);
        thursday=(EditText) findViewById(R.id.thursday);
        friday=(EditText) findViewById(R.id.friday);
        saturday=(EditText) findViewById(R.id.saturday);
        sunday=(EditText) findViewById(R.id.sunday);

        Update=(Button) findViewById(R.id.update);


        mDayDatabase= FirebaseDatabase.getInstance().getReference().child("Menu");


        mDayDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Toast.makeText(Meals.this,"Loading data...",Toast.LENGTH_SHORT);
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

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Monday=monday.getText().toString();
                String Tuesday=tuesday.getText().toString();
                String Wednesday=wednesday.getText().toString();
                String Thursday=thursday.getText().toString();
                String Friday=friday.getText().toString();
                String Saturday=saturday.getText().toString();
                String Sunday=sunday.getText().toString();

                if (!Monday.isEmpty()&&!Tuesday.isEmpty()&&!Wednesday.isEmpty()&&!Thursday.isEmpty()&&!Friday.isEmpty()&&!Saturday.isEmpty()&&!Sunday.isEmpty())
                {
                    mProgressDialog=new ProgressDialog(Meals.this);
                    mProgressDialog.setTitle("Saving Changes");
                    mProgressDialog.setMessage("Please wait while we save the changes.");
                    mProgressDialog.show();

                    mDayDatabase.child("Monday").setValue(Monday);
                    mDayDatabase.child("Tuesday").setValue(Tuesday);
                    mDayDatabase.child("Wednesday").setValue(Wednesday);
                    mDayDatabase.child("Thursday").setValue(Thursday).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mProgressDialog.dismiss();

                            }
                        }
                    });
                    mDayDatabase.child("Friday").setValue(Friday).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mProgressDialog.dismiss();

                            }
                        }
                    });
                    mDayDatabase.child("Saturday").setValue(Saturday).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mProgressDialog.dismiss();

                            }
                        }
                    });
                    mDayDatabase.child("Sunday").setValue(Sunday).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                mProgressDialog.dismiss();

                            }
                        }
                    });
                }

            }
        });
    }
}
