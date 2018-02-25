package com.example.syyam.lilgems;

import android.content.Intent;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Medic extends AppCompatActivity {
    private RecyclerView mToiletList;
    private ActionBarDrawerToggle mToggle;
    private String Intentt;
    private String mPostKey;
    private LinearLayoutManager mLayoutManager;
    private ImageButton mSendBtn;
    private DatabaseReference DatabaseComment;
    private DatabaseReference hDatabase;
    private DatabaseReference DatabaseCommenttttt;
    RecyclerView mProfileList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toiletring);

        mToiletList=(RecyclerView) findViewById(R.id.toilet_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mToiletList.setHasFixedSize(true);
        mToiletList.setLayoutManager(new LinearLayoutManager(this));

        DatabaseComment= FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseCommenttttt= FirebaseDatabase.getInstance().getReference().child("Users");


        DatabaseComment.keepSynced(true);
        DatabaseCommenttttt.keepSynced(true);
        mLayoutManager = new LinearLayoutManager(Medic.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<PhotosData, MedicDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PhotosData, MedicDataViewHolder>(

                PhotosData.class,
                R.layout.child_row,
                MedicDataViewHolder.class,
                DatabaseCommenttttt
        ) {
            @Override
            protected void populateViewHolder(MedicDataViewHolder viewHolder, PhotosData model, int position) {

                final String post_key= getRef(position).getKey();

                viewHolder.setNames(model.getName());
                viewHolder.mmLinearName.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                        Intent userProfile=new Intent(Medic.this,MedicInfo.class);
                        userProfile.putExtra("profile_id",post_key);
                        startActivity(userProfile);

                    }
                });

            }
        };
        mToiletList.setLayoutManager(mLayoutManager);
        mToiletList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class MedicDataViewHolder extends RecyclerView.ViewHolder
    {
        View mmView;

        LinearLayout mmLinearName;

        public MedicDataViewHolder(View itemView) {
            super(itemView);

            mmView=itemView;
            mmLinearName=(LinearLayout) mmView.findViewById(R.id.layout_name);
        }


        public void setNames(String name)
        {
            TextView post_phone=(TextView) mmView.findViewById(R.id.others_name);
            post_phone.setText(name);
        }


    }
}
