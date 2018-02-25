package com.example.syyam.lilgems;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Goals extends AppCompatActivity {

    private RecyclerView mProfileList;
    private LinearLayoutManager mLayoutManager;
    private ImageButton mSendBtn;
    private EditText myComment;
    private DatabaseReference DatabaseComment;
    private DatabaseReference hDatabase;
    private DatabaseReference DatabaseCommenttttt;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);


        mProfileList=(RecyclerView) findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mProfileList.setHasFixedSize(true);
        mProfileList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseComment= FirebaseDatabase.getInstance().getReference().child("Goals");
        DatabaseCommenttttt= FirebaseDatabase.getInstance().getReference().child("Goals");

        hDatabase=FirebaseDatabase.getInstance().getReference().child("Users");

        mSendBtn=(ImageButton) findViewById(R.id.sendbtn);
        myComment=(EditText) findViewById(R.id.my_comment);


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
                Utils.hideKeyboard(Goals.this);
                myComment.setText("");
            }

            private void sendComment() {
                final String message=myComment.getText().toString();
                if (!TextUtils.isEmpty(message))
                {
                    DatabaseComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            hDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    HashMap<String,String> userMap=new HashMap<>();
                                    userMap.put("Goals",message);

                                    DatabaseComment.push().setValue(userMap);
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
        });

        hDatabase.keepSynced(true);
        DatabaseComment.keepSynced(true);
        DatabaseCommenttttt.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(Goals.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PhotosData, GPhotosDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PhotosData, GPhotosDataViewHolder>(

                PhotosData.class,
                R.layout.commentttt_row,
                GPhotosDataViewHolder.class,
                DatabaseCommenttttt
        ) {
            @Override
            protected void populateViewHolder(GPhotosDataViewHolder viewHolder, PhotosData model, int position) {

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

