package com.example.syyam.lilgems;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ImageButton mGoals;
    private ImageButton mSendNotification;
    private ImageButton mMeals;
    private ImageButton mUploadPhotos;
    private ImageButton mSupplies;
    private ImageButton mToileting;
    private ImageButton mMedication;
    private ImageButton mRecieveMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoals=(ImageButton) findViewById(R.id.goals);
        mMeals=(ImageButton) findViewById(R.id.meals);
        mSupplies=(ImageButton) findViewById(R.id.supplies);
        mToileting=(ImageButton) findViewById(R.id.toileting);
        mMedication=(ImageButton) findViewById(R.id.medication);
        mUploadPhotos=(ImageButton) findViewById(R.id.addPhotos);
        mRecieveMessage=(ImageButton) findViewById(R.id.recieveMessage);
        mSendNotification=(ImageButton) findViewById(R.id.sendNotification);

        mRecieveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent no=new Intent(MainActivity.this,recieveList.class);
                startActivity(no);
            }
        });

        mMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to=new Intent(MainActivity.this,Medic.class);
                startActivity(to);
            }
        });

        mSupplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to=new Intent(MainActivity.this,Supplies.class);
                startActivity(to);
            }
        });
        mToileting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toilet=new Intent(MainActivity.this,Toiletring.class);
                startActivity(toilet);
            }
        });

        mMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal=new Intent(MainActivity.this,Meals.class);
                startActivity(meal);
            }
        });

        mGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendnoti=new Intent (MainActivity.this,Goals.class);
                startActivity(sendnoti);
            }
        });


        mSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"syyamnoor@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mUploadPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goalIntent=new Intent(MainActivity.this,Photos.class);
                startActivity(goalIntent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out
                    Intent login=new Intent(MainActivity.this,startActivity.class);
                    startActivity(login);
                }
                // ...
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.action_addUser)
        {
            Intent signup=new Intent(MainActivity.this,registerActivity.class);
            signup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signup);
        }
        if (item.getItemId()==R.id.logout)
        {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Are you sure?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                Process.killProcess(Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
