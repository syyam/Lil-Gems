package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMedic extends AppCompatActivity {

    private EditText med;
    private EditText note;
    private EditText temp;
    private EditText weight;

    private Button Update;

    private FirebaseUser mCurrentUser;

    private DatabaseReference mDayDatabase;
    private String nPostKey=null;
    private DatabaseReference eDatabase;
    private DatabaseReference lDatabase;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_medic);

        med=(EditText) findViewById(R.id.medicines);
        note=(EditText) findViewById(R.id.notes);
        temp=(EditText) findViewById(R.id.temp);
        weight=(EditText) findViewById(R.id.weight);

        med.setFocusable(false);
        med.setClickable(false);
        note.setFocusable(false);
        note.setClickable(false);
        temp.setFocusable(false);
        temp.setClickable(false);
        weight.setFocusable(false);
        weight.setClickable(false);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_id = mCurrentUser.getUid();

        mDayDatabase= FirebaseDatabase.getInstance().getReference().child("Medicine");
        eDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        lDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);

        lDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot ldataSnapshot) {

                mDayDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot2) {
                        if (dataSnapshot2.hasChild(ldataSnapshot.child("name").getValue().toString()))
                        {
                            mDayDatabase.child(ldataSnapshot.child("name").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

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
    }
}
