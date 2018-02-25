package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ToiletInfo extends AppCompatActivity {

    private String mPostKey=null;
    private EditText Info;
    private Button save;
    private TimePicker time;
    private DatabaseReference mDatabase;
    private DatabaseReference eDatabase;
    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_info);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Toiletries");
        mPostKey=getIntent().getExtras().getString("profile_id");
        eDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mPostKey);

        Info=(EditText) findViewById(R.id.Info);
        save=(Button) findViewById(R.id.save);
        time=(TimePicker) findViewById(R.id.timeToilet);


        final int hour = time.getHour();
        final int minute = time.getMinute();
        final String date = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String data=Info.getText().toString();

                mProgressDialog=new ProgressDialog(ToiletInfo.this);
                mProgressDialog.setTitle("Uploading Data...");
                mProgressDialog.setMessage("Please wait while we Upload Data.");
                mProgressDialog.show();

                final DatabaseReference newPost=mDatabase.push();
                eDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // newPost.child("Name").setValue(NAME);
                        newPost.child("Notes").setValue(data);
                        newPost.child("Hour").setValue(String.valueOf(hour));
                        newPost.child("Minute").setValue(String.valueOf(minute));
                        newPost.child("Date").setValue(date);
                        newPost.child("name").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    startActivity(new Intent(ToiletInfo.this,Toiletring.class));
                                    finish();
                                }
                                else
                                    {
                                        Toast.makeText(ToiletInfo.this,"An Error Has Occurred.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(ToiletInfo.this,"Error Uploading data",Toast.LENGTH_LONG).show();
                    }
                });

            }

        });

        eDatabase.keepSynced(true);
        mDatabase.keepSynced(true);
    }
}
