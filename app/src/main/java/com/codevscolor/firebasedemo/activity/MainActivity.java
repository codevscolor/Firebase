package com.codevscolor.firebasedemo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codevscolor.firebasedemo.R;
import com.codevscolor.firebasedemo.account.LogoutActivity;
import com.codevscolor.firebasedemo.account.SignupActivity;
import com.codevscolor.firebasedemo.realtimedatabase.Person;
import com.codevscolor.firebasedemo.realtimedatabase.RealTimeActivity;
import com.codevscolor.firebasedemo.storage.StorageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button loginButton;

    private String emailText;
    private String pwdText;
    private FirebaseAuth auth;
    private TextInputLayout emailLayout;
    private TextInputLayout pwdLayout;
    private TextView signUp;
    private ProgressDialog progressDialog;

    private TextView realTimeDataText;
    private TextView storageDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.editTextEmail);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.button);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        emailLayout = (TextInputLayout) findViewById(R.id.layout_email);
        pwdLayout = (TextInputLayout) findViewById(R.id.layout_password);

        signUp = (TextView) findViewById(R.id.signUp);
        realTimeDataText = (TextView)findViewById(R.id.realTimeDatabase);

        realTimeDataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RealTimeActivity.class);
                startActivity(i);
            }
        });

        storageDatabase = (TextView)findViewById(R.id.storageDatabase);
        storageDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StorageActivity.class);
                startActivity(i);
            }
        });

        auth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                progressDialog.show();
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        if(auth.getCurrentUser() != null){
            //user is logged in
            Intent i = new Intent(this,LogoutActivity.class);
            startActivity(i);
        }


    }



    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void login() {
        emailText = etEmail.getText().toString().trim();
        pwdText = etPassword.getText().toString().trim();

        if (emailText.isEmpty()) {
            emailLayout.setError(getString(R.string.hint_empty));
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailLayout.setError(getString(R.string.error_email));
        } else {
            emailLayout.setErrorEnabled(false);
        }
        if (pwdText.isEmpty()) {
            pwdLayout.setError(getString(R.string.hint_empty));
        } else if (pwdText.length() < 6) {
            pwdLayout.setError(getString(R.string.hint_pwd_min));
        } else {
            pwdLayout.setErrorEnabled(false);
        }

        if (!emailLayout.isErrorEnabled() && !pwdLayout.isErrorEnabled()) {
            auth.signInWithEmailAndPassword(emailText, pwdText).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                        builder.setMessage("Login Failed !!");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    } else {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogStyle);
                        builder.setMessage("Login Successfull !!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                    }
                }
            });


        }else {
            progressDialog.dismiss();
        }

    }
}
