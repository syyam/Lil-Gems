package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;

public class MedicInfo extends AppCompatActivity {

    private EditText med;
    private EditText note;
    private EditText temp;
    private EditText weight;

    private Button Update;

    private DatabaseReference mDayDatabase;
    private String nPostKey=null;
    private DatabaseReference eDatabase;
    private DatabaseReference lDatabase;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_info);

        med=(EditText) findViewById(R.id.medicines);
        note=(EditText) findViewById(R.id.notes);
        temp=(EditText) findViewById(R.id.temp);
        weight=(EditText) findViewById(R.id.weight);

        Update=(Button) findViewById(R.id.update);

        nPostKey=getIntent().getExtras().getString("profile_id");
        mDayDatabase= FirebaseDatabase.getInstance().getReference().child("Medicine");
        eDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        lDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(nPostKey);

        lDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot ldataSnapshot) {
               // String se=ldataSnapshot.child("name").getValue().toString();

                mDayDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        if (dataSnapshot2.hasChild(ldataSnapshot.child("name").getValue().toString()))
                        {
                            mDayDatabase.child(ldataSnapshot.child("name").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {




                                    Toast.makeText(MedicInfo.this,"Loading data...",Toast.LENGTH_SHORT);

                                    String th=dataSnapshot.child("Medicine").getValue().toString();
                                    String fr=dataSnapshot.child("Notes").getValue().toString();
                                    String sa=dataSnapshot.child("Temp").getValue().toString();
                                    String su=dataSnapshot.child("Weight").getValue().toString();

                                    med.setText(th);
                                    note.setText(fr);
                                    temp.setText(sa);
                                    weight.setText(su);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    Toast.makeText(MedicInfo.this,"Error",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog=new ProgressDialog(MedicInfo.this);
                mProgressDialog.setTitle("Uploading Data...");
                mProgressDialog.setMessage("Please wait while we Upload Data.");
                mProgressDialog.show();


                final String medi=med.getText().toString();
                final String noti=note.getText().toString();
                final String temi=temp.getText().toString();
                final String wegi=weight.getText().toString();

                lDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        final String temp=dataSnapshot1.child("name").getValue().toString();
                        final DatabaseReference newPost=mDayDatabase.child(temp).push();

                        mDayDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String,String> userMap=new HashMap<>();
                                userMap.put("Medicine",medi);
                                userMap.put("Notes",noti);
                                userMap.put("Temp",temi);
                                userMap.put("Weight",wegi);

                                mDayDatabase.child(temp).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        mProgressDialog.dismiss();
                                        Intent ma=new Intent(MedicInfo.this,Medic.class);
                                        ma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(ma);
                                        finish();
                                    }
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                Toast.makeText(MedicInfo.this,"Error Uploading data",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


















        /*mDayDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                String th=dataSnapshot.child("Medicine").getValue().toString();
                String fr=dataSnapshot.child("Notes").getValue().toString();
                String sa=dataSnapshot.child("Temprature").getValue().toString();
                String su=dataSnapshot.child("Weight").getValue().toString();


                med.setText(th);
                note.setText(fr);
                temp.setText(sa);
                weight.setText(su);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }
}
