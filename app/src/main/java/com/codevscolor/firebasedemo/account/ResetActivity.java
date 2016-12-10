package com.codevscolor.firebasedemo.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.codevscolor.firebasedemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button button;
    private String emailText;
    private EditText etEmail;
    private ProgressDialog progressDialog;
    private TextInputLayout emailLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        auth = FirebaseAuth.getInstance();
        button = (Button)findViewById(R.id.button_email);
        etEmail = (EditText) findViewById(R.id.editText_pwd_reset);
        emailLayout = (TextInputLayout) findViewById(R.id.layout_pwd_reset);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                progressDialog.show();
                sendPwdResetMail();
            }
        });

    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void sendPwdResetMail() {
        emailText = etEmail.getText().toString().trim();

        if (emailText.isEmpty()) {
            emailLayout.setError(getString(R.string.hint_empty));
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailLayout.setError(getString(R.string.error_email));
        } else {
            emailLayout.setErrorEnabled(false);
        }

        if (!emailLayout.isErrorEnabled() ) {
            auth.sendPasswordResetEmail(emailText).addOnCompleteListener(ResetActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetActivity.this, R.style.DialogStyle);
                    builder.setMessage("Failed..Please Try again !!");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }else{
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetActivity.this, R.style.DialogStyle);
                    builder.setMessage("One password reset mail is sent to your email address. Please check your inbox");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                }
            });

        }else {
            progressDialog.dismiss();
        }

    }
}
