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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class sendNotification extends AppCompatActivity {

    private RecyclerView mProfileList;

    String  lmPostKey;
    private LinearLayoutManager mLayoutManager;
    private ImageButton mSendBtn;
    private EditText myComment;
    private DatabaseReference DatabaseComment;
    private DatabaseReference DatabaseCommenttttt;
    private DatabaseReference hDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        mProfileList=(RecyclerView) findViewById(R.id.comment_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mProfileList.setHasFixedSize(true);
        mProfileList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_id = mCurrentUser.getUid();;

        hDatabase=FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseComment= FirebaseDatabase.getInstance().getReference().child("Notification").child(current_id);
        DatabaseCommenttttt= FirebaseDatabase.getInstance().getReference().child("Notification").child(current_id).child("awain");

        mSendBtn=(ImageButton) findViewById(R.id.sendbtn);
        myComment=(EditText) findViewById(R.id.my_comment);


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
                Utils.hideKeyboard(sendNotification.this);
                myComment.setText("");
            }

            private void sendComment() {
                final String message=myComment.getText().toString();
                if (!TextUtils.isEmpty(message))
                {
                    DatabaseComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            hDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {


                                    HashMap<String,String> userMap=new HashMap<>();
                                    userMap.put("comments",message);
                                    userMap.put("othersName",dataSnapshot1.child(mAuth.getCurrentUser().getUid()).child("name").getValue().toString());

                                    DatabaseComment.child("awain").push().setValue(userMap);
                                    /*DatabaseComment.child("comments").setValue(message);
                                    DatabaseComment.child("othersName")
                                            .setValue(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("name").getValue());
                                    DatabaseComment.child("othersDP")
                                            .setValue(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("DP").getValue());*/
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
        DatabaseCommenttttt.keepSynced(true);
        DatabaseComment.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(sendNotification.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<PhotosData,NotificationViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PhotosData, NotificationViewHolder>(

                PhotosData.class,
                R.layout.notification_row,
                NotificationViewHolder.class,
                DatabaseCommenttttt
        ) {

            @Override
            protected void populateViewHolder(NotificationViewHolder viewHolder, PhotosData model, int position) {
                viewHolder.setComments(model.getComments());
                viewHolder.setOthersName(model.getOthersName());
            }

        };

        mProfileList.setLayoutManager(mLayoutManager);
        mProfileList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        LinearLayout mLinearName;
        TextView userName;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

            userName=(TextView) mView.findViewById(R.id.post_name);
            mLinearName=(LinearLayout) mView.findViewById(R.id.layout_name);
        }


        public void setComments(String name)
        {
            TextView post_phone=(TextView) mView.findViewById(R.id.others_comments);
            post_phone.setText(name);
        }
        public void setOthersName(String othersName) {
            TextView post_name=(TextView) mView.findViewById(R.id.others_name);
            post_name.setText(othersName);
        }

    }
}
