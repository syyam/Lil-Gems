package com.example.syyam.lilgems;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class recieveNotification extends AppCompatActivity {

    private RecyclerView mProfileList;

    private LinearLayoutManager mLayoutManager;
    private ImageButton mSendBtn;
    private EditText myComment;
    private DatabaseReference DatabaseComment;
    private DatabaseReference DatabaseCommenttttt;
    private DatabaseReference hDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mPostKey=null;

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
        mAuth = FirebaseAuth.getInstance();

        mPostKey=getIntent().getExtras().getString("profile_id");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_id = mCurrentUser.getUid();;

        hDatabase= FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseCommenttttt= FirebaseDatabase.getInstance().getReference().child("Notification").child(mPostKey).child("awain");

        hDatabase.keepSynced(true);
        DatabaseCommenttttt.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(recieveNotification.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<PhotosData,recieveNotificationViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PhotosData, recieveNotificationViewHolder>(

                PhotosData.class,
                R.layout.notification_row,
                recieveNotificationViewHolder.class,
                DatabaseCommenttttt
        ) {

            @Override
            protected void populateViewHolder(recieveNotificationViewHolder viewHolder, PhotosData model, int position) {
                viewHolder.setComments(model.getComments());
                viewHolder.setOthersName(model.getOthersName());
            }

        };

        mProfileList.setLayoutManager(mLayoutManager);
        mProfileList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class recieveNotificationViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView userName;

        public recieveNotificationViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

            userName=(TextView) mView.findViewById(R.id.post_name);
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
