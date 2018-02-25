package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SuppliesInfo extends AppCompatActivity {

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private Button warning;
    private DatabaseReference mDatabase;
    private DatabaseReference eDatabase;
    private DatabaseReference lDatabase;

    private ProgressDialog mProgressDialog;
    private String nPostKey=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplies_info);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Supplies");
        nPostKey=getIntent().getExtras().getString("profile_id");
        eDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        lDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(nPostKey);

        checkBox1=(CheckBox) findViewById(R.id.checkBox);
        checkBox2=(CheckBox) findViewById(R.id.checkBox2);
        warning=(Button) findViewById(R.id.warninig);

        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            mProgressDialog=new ProgressDialog(SuppliesInfo.this);
            mProgressDialog.setTitle("Uploading Data...");
            mProgressDialog.setMessage("Please wait while we Upload Data.");
            mProgressDialog.show();



            lDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot1) {
                    final String temp=dataSnapshot1.child("name").getValue().toString();
                    final DatabaseReference newPost=mDatabase.child(temp).push();

                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String,String> userMap=new HashMap<>();

                            if(checkBox1.isChecked() && checkBox2.isChecked() ) {
                                userMap.put("toiletriesWarning","Toiletries Replacement Required");
                                userMap.put("clothingWarning","Clothing Replacement Required");

                                //dataSnapshot.child("toiletriesWarning").setValue("Toiletries Replacement Required");
                                //dataSnapshot.child("clothingWarning").setValue("Clothing Replacement Required");
                            }
                            if(!checkBox1.isChecked() && !checkBox2.isChecked()) {
                                userMap.put("toiletriesWarning","No Toiletries Replacement Required");
                                userMap.put("clothingWarning","No Clothing Replacement Required");
                                //newPost.child("toiletriesWarning").setValue("No Toiletries Replacement Required");
                                //newPost.child("clothingWarning").setValue("No Clothing Replacement Required");
                            }
                            if(checkBox2.isChecked()&&!checkBox1.isChecked())
                            {
                                userMap.put("toiletriesWarning","No Toiletring Replacement Required");
                                userMap.put("clothingWarning","Clothing Replacement Required");
                                //newPost.child("toiletriesWarning").setValue("Toiletring Replacement Required");
                                //newPost.child("clothingWarning").setValue("Clothing Replacement Required");
                            }
                            if(!checkBox2.isChecked()&&checkBox1.isChecked())
                            {

                                userMap.put("toiletriesWarning","Toiletring Replacement Required");
                                userMap.put("clothingWarning","No Clothing Replacement Required");
                                //newPost.child("clothingWarning").setValue("No Clohing Replacement Required");
                                //newPost.child("toiletriesWarning").setValue("Toiletring Replacement Required");
                            }
                            mDatabase.child(temp).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        mProgressDialog.dismiss();
                                        Intent ma=new Intent(SuppliesInfo.this,Supplies.class);
                                        ma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(ma);
                                        finish();
                                    }
                                }
                            });


                            /*newPost.child("name").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        startActivity(new Intent(SuppliesInfo.this,Supplies.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SuppliesInfo.this,"An Error Has Occurred.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(SuppliesInfo.this,"Error Uploading data",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            }
        });
        eDatabase.keepSynced(true);
        mDatabase.keepSynced(true);
    }
}
