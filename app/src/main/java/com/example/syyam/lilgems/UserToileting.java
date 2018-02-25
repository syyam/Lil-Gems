package com.example.syyam.lilgems;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserToileting extends AppCompatActivity {

    private RecyclerView mToiletList;
    private ActionBarDrawerToggle mToggle;
    private String Intentt;
    private String mPostKey;
    private LinearLayoutManager mLayoutManager;
    private ImageButton mSendBtn;
    private DatabaseReference DatabaseToilet;
    private DatabaseReference hDatabase;
    private DatabaseReference DatabaseToiletttttt;
    private FirebaseUser mCurrentUser;
    RecyclerView UserProfileList;
    private String key;

    private DatabaseReference mDatabaseCuurentUser;
    private Query mQueryCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_toileting);

        mToiletList=(RecyclerView) findViewById(R.id.toilet_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mToiletList.setHasFixedSize(true);
        mToiletList.setLayoutManager(new LinearLayoutManager(this));


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_id = mCurrentUser.getUid();
        DatabaseToilet = FirebaseDatabase.getInstance().getReference().child("Toiletries");
        DatabaseToiletttttt = FirebaseDatabase.getInstance().getReference().child("Users").child(current_id);


        DatabaseToilet.keepSynced(true);
        DatabaseToiletttttt.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(UserToileting.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseToiletttttt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String naam = dataSnapshot.child("name").getValue().toString();

                mDatabaseCuurentUser=FirebaseDatabase.getInstance().getReference().child("Toiletries");
                mQueryCurrentUser=mDatabaseCuurentUser.orderByChild("name").equalTo(naam);

                FirebaseRecyclerAdapter<PhotosData, UserToiletDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PhotosData, UserToiletDataViewHolder>(

                        PhotosData.class,
                        R.layout.user_row,
                        UserToiletDataViewHolder.class,
                        mQueryCurrentUser
                ) {
                    @Override
                    protected void populateViewHolder(UserToiletDataViewHolder viewHolder, PhotosData model, int position) {

                        final String post_key = getRef(position).getKey();

                        viewHolder.setName(model.getName());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setHour(model.getHour());
                        viewHolder.setMinute(model.getMinute());
                        viewHolder.setNotes(model.getNotes());


                    }
                };
                mToiletList.setLayoutManager(mLayoutManager);
                mToiletList.setAdapter(firebaseRecyclerAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static class UserToiletDataViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public UserToiletDataViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }


        public void setName(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.name);
            post_phone.setText(name);
        }

        public void setHour(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.hour);
            post_phone.setText(name+"   :");
        }
        public void setMinute(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.minute);
            post_phone.setText(name);
        }
        public void setNotes(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.notes);
            post_phone.setText(name);
        }
        public void setDate(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.date);
            post_phone.setText(name);
        }

    }
}

