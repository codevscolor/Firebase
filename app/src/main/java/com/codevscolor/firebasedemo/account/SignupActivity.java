package com.codevscolor.firebasedemo.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button signUpButton;

    private String emailText;
    private String pwdText;

    private TextInputLayout emailLayout;
    private TextInputLayout pwdLayout;
    private FirebaseAuth auth;
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = this;

        etEmail = (EditText) findViewById(R.id.signup_editTextEmail);
        etPassword = (EditText) findViewById(R.id.signup_editTextPassword);
        signUpButton = (Button) findViewById(R.id.signup_button);

        emailLayout = (TextInputLayout) findViewById(R.id.signup_layout_email);
        pwdLayout = (TextInputLayout) findViewById(R.id.signup_layout_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getSupportActionBar().setTitle("Sign Up");

        auth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                progressDialog.show();
                signUp();
            }
        });

    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }


    private void signUp() {
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
            auth.createUserWithEmailAndPassword(emailText, pwdText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
                        builder.setMessage("Oops..please try again !!");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    } else {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogStyle);
                        builder.setMessage("Yeah..one new user is created !!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
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
