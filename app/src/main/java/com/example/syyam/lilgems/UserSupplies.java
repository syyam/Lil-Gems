package com.example.syyam.lilgems;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserSupplies extends AppCompatActivity {

    private TextView toilet;
    private TextView clothing;
    private DatabaseReference mDatabase;
    private DatabaseReference nDatabase;
    private DatabaseReference lDatabase;
    private FirebaseUser mCurrentUser;

    private String key;
    private DatabaseReference mDatabaseCuurentUser;
    private Query mQueryCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_supplies);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_id = mCurrentUser.getUid();

        toilet=(TextView) findViewById(R.id.toilet);
        clothing=(TextView) findViewById(R.id.clothing);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Supplies");
        lDatabase= FirebaseDatabase.getInstance().getReference().child("Supplies");
        nDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);


        nDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String naam = dataSnapshot.child("name").getValue().toString();

                mDatabase.orderByChild(naam).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot1) {
                        if(dataSnapshot1.exists())
                        {
                            String myparent=dataSnapshot1.getKey();
                            for (DataSnapshot child: dataSnapshot1.getChildren())
                            {
                                key = child.getKey().toString();
                                lDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot2) {
                                        clothing.setText(dataSnapshot2.child(key).child("clothingWarning").getValue().toString());
                                        toilet.setText(dataSnapshot2.child(key).child("toiletriesWarning").getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
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


        mDatabase.keepSynced(true);
        nDatabase.keepSynced(true);

    }
}
