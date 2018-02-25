package com.example.syyam.lilgems;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;
    private Button mRegisterButton;
    private DatabaseReference mAdminDatabase;
    private ProgressDialog mProgress;
    private TextView button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mRegisterButton = (Button) findViewById(R.id.registerBtn);

        mProgress = new ProgressDialog(this);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        final String email = mEmailField.getText().toString().trim();
        final String password = mPasswordField.getText().toString().trim();
        final String name = mNameField.getText().toString().trim();
        mAdminDatabase.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mProgress.setMessage("Registering User");
                        mProgress.show();
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mAdminDatabase.child(user_id);

                                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = current_user.getUid();
                                    mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    //mDatabase.child(user_id).child("name").setValue(name);
                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("DP", "default");

                                    mAdminDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgress.dismiss();
                                                mAuth.signOut();
                                                Intent ma = new Intent(registerActivity.this, startActivity.class);
                                                ma.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ma);
                                                Toast.makeText(registerActivity.this,"User Created Successfuly",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    //current_user_db.child("name").setValue(name);
                                    // current_user_db.child("image").setValue("default");

                                } else {
                                    mProgress.dismiss();
                                    Toast.makeText(registerActivity.this, "Please Provide Valid Data or Check Your Internet Connection.", Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }
                } else {
                    Toast.makeText(registerActivity.this, "User Name Must be Unique.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
