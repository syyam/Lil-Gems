package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostPhotos extends AppCompatActivity {

    private Uri mImageUri=null;
    private Bitmap mImageBitmap=null;
    private ImageButton selectImage;

    private EditText phone;

    private Button submit;

    Long tsLong;

    private StorageReference mstorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_photos);

        final String[] items=new String[] {"Camera","Gallery"};
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 1) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 0);
                }
                else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
                            String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(cameraIntent, 1);

                    //startActivityForResult(cameraIntent, 1);
                }
            }
        });



        final AlertDialog dialog=builder.create();

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();
        mProgress=new ProgressDialog(this);
        mstorage= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Photos");
        selectImage =(ImageButton) findViewById(R.id.MimageSelect);
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        phone =(EditText) findViewById(R.id.Mphone);
        submit =(Button) findViewById(R.id.submit);



        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }
    private void startPosting(){
        //startActivity(new Intent(PostPhotos.this,RealAlarm.class));
        mProgress.setMessage("Uploading Data");

        tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        mProgress.show();
        final String PHONE=phone.getText().toString().trim();
        if(!TextUtils.isEmpty(PHONE) && mImageUri!=null) //
        {
            StorageReference filepath=mstorage.child("Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadUrl= taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost=mDatabase.push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // newPost.child("Name").setValue(NAME);
                            newPost.child("Phone").setValue(PHONE);
                            newPost.child("Image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("Name").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                startActivity(new Intent(PostPhotos.this,Photos.class));
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(PostPhotos.this,"An Error Has Occurred.",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            Toast.makeText(PostPhotos.this,"Error Uploading data",Toast.LENGTH_LONG).show();
                        }
                    });

                    mProgress.dismiss();
                    //startActivity(new Intent(PostPhotos.this,setupActivity.class));
                }
            });
        }
        else
        {
            mProgress.dismiss();
            Toast.makeText(PostPhotos.this, "Invalid data", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==0 && resultCode==RESULT_OK)
        {
            //gallery
            try {
                mImageUri =data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                selectImage.setImageBitmap(bitmap);
            }

            catch (Exception e)
            {
                Toast.makeText(this,"Error: Image Size Too Large.", Toast.LENGTH_SHORT).show();
            }

        }
        else if (requestCode==1 && resultCode==RESULT_OK)
        {
            //camera

            try {
                mImageUri.toString();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                selectImage.setImageBitmap(bitmap);

            }
            catch (Exception e)
            {
                Toast.makeText(this,"Error: Image Size Too Large.", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
