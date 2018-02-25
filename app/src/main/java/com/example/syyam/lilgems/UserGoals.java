package com.example.syyam.lilgems;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserGoals extends AppCompatActivity {

    private RecyclerView mProfileList;

    private DatabaseReference awain;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    String  UPostKey;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference DatabaseComment;
    private DatabaseReference hDatabase;
    private DatabaseReference DatabaseCommenttttt;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_goals);
        mProfileList=(RecyclerView) findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mProfileList.setHasFixedSize(true);
        mProfileList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseComment= FirebaseDatabase.getInstance().getReference().child("Goals");
        DatabaseCommenttttt= FirebaseDatabase.getInstance().getReference().child("Goals");

        hDatabase=FirebaseDatabase.getInstance().getReference().child("Users");





        hDatabase.keepSynced(true);
        DatabaseComment.keepSynced(true);
        DatabaseCommenttttt.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(UserGoals.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PhotosData, Goals.GPhotosDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PhotosData, Goals.GPhotosDataViewHolder>(

                PhotosData.class,
                R.layout.commentttt_row,
                Goals.GPhotosDataViewHolder.class,
                DatabaseCommenttttt
        ) {
            @Override
            protected void populateViewHolder(Goals.GPhotosDataViewHolder viewHolder, PhotosData model, int position) {

                viewHolder.setGoals(model.getGoals());

            }
        };
        mProfileList.setLayoutManager(mLayoutManager);
        mProfileList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class GPhotosDataViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        LinearLayout mLinearName;
        TextView userName;
        TextView mComment;

        public GPhotosDataViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

            userName=(TextView) mView.findViewById(R.id.post_name);

        }


        public void setGoals(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.others_name);
            post_phone.setText(name);
        }


    }
}
