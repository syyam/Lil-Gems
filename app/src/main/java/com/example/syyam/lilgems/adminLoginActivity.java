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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminLoginActivity extends AppCompatActivity {

    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private TextView button;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseAdminUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        mAuth=FirebaseAuth.getInstance();

        mLoginEmailField=(EditText) findViewById(R.id.loginEmailField);
        mLoginPasswordField=(EditText) findViewById(R.id.loginPasswordField);
        mLoginBtn=(Button) findViewById(R.id.loginBtn);
        mProgress=new ProgressDialog(this);

        mDatabaseAdminUsers= FirebaseDatabase.getInstance().getReference().child("Users");

        /*Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {

            String j =(String) b.get("family");
            if (j.equals("family"))
            {
                return;
            }

        }*/


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }
    private void checkLogin() {

        String email=mLoginEmailField.getText().toString().trim();
        String password=mLoginPasswordField.getText().toString().trim();

        Intent in= getIntent();
        Bundle c = in.getExtras();
        if (c!=null)
        {

            String j =(String) c.get("admin");
            if (j.equals("admin") && !email.equals("adminlittle@lg.com"))
            {
                Toast.makeText(adminLoginActivity.this,"Incorrect Email or Password",Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
        {
            mProgress.setMessage("Logging in");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        mProgress.dismiss();
                        checkUserExist();

                    }
                    else
                    {
                        mProgress.dismiss();
                        Toast.makeText(adminLoginActivity.this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        else {
            Toast.makeText(adminLoginActivity.this,"Incorrect Email or Password",Toast.LENGTH_LONG).show();
        }

    }
    private void checkUserExist() {
        if (mAuth.getCurrentUser()!=null) {
            final String user_id = mAuth.getCurrentUser().getUid();
            mDatabaseAdminUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user_id)) {
                        Intent login = new Intent(adminLoginActivity.this, MainActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(login);
                    } else {
                        Intent setup = new Intent(adminLoginActivity.this, MainActivity.class);
                        setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setup);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
    }
    @Override
    public void onBackPressed() {
        Intent start=new Intent(this,startActivity.class);
        startActivity(start);
    }
}
