package com.example.syyam.lilgems;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Photos extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private DatabaseReference qDatabase;
    private DatabaseReference mDatabaseCuurentUser;
    private RecyclerView mPhotosDataList;
    private Query mQueryCurrentUser;
    //private FirebaseAuthException firebase
    // ;
    private FirebaseUser mCurrentUser;

    private FirebaseAuth firebase;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        mPhotosDataList=(RecyclerView) findViewById(R.id.photos_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mPhotosDataList.setHasFixedSize(true);
        mPhotosDataList.setLayoutManager(new LinearLayoutManager(this));
        mAuth= FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent login=new Intent(Photos.this,startActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                }
                // ...
            }
        };
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Photos");
        String currentUserId=mAuth.getCurrentUser().getUid();
        mDatabaseCuurentUser=FirebaseDatabase.getInstance().getReference().child("Photos");



        mDatabase.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(Photos.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //checkUserExist();
        // mAuth.addAuthStateListener(mAuthListener);
        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<PhotosData,PhotosDataViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PhotosData, PhotosDataViewHolder>(

                PhotosData.class,
                R.layout.photo_row,
                PhotosDataViewHolder.class,
                mDatabaseCuurentUser
        ) {
            @Override
            protected void populateViewHolder(PhotosDataViewHolder viewHolder, PhotosData model, int position) {

                final String post_key= getRef(position).getKey();
                viewHolder.setPhone(model.getPhone());
                viewHolder.setImage(getApplicationContext(),model.getImage());

            }
        };
        mPhotosDataList.setLayoutManager(mLayoutManager);
        mPhotosDataList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PhotosDataViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView post_name;

        public PhotosDataViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            post_name=(TextView) mView.findViewById(R.id.post_name);
        }
        public void setPhone(String phone)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.post_name);
            post_phone.setText(phone);
        }

        public void setImage(final Context ctx, final String image)
        {
            final ImageView post_image=(ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Picasso.with(ctx).load(image).into(post_image);
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.goals_menu, menu);
        qDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        final String current_id= mCurrentUser.getUid();
        qDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(current_id)) {
                    // run some code
                    menu.getItem(0).setVisible(false);
                    menu.getItem(0).setEnabled(false);


                }
                else
                {
                    menu.getItem(0).setVisible(true);
                    menu.getItem(0).setEnabled(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_add)
        {

            startActivity(new Intent(this,PostPhotos.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
