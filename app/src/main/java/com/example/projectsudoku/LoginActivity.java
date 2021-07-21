package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private Button bt_sign_in;
    private TextView register, back;
    private FirebaseAuth mFirebaseAuth;
    private ProgressBar bar;





    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close the app?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_first:
                startActivity(new Intent(LoginActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(LoginActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(LoginActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = LoginActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(LoginActivity.this, MyReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        bar = (ProgressBar) findViewById(R.id.login_bar);
        bar.setVisibility(View.INVISIBLE);
        mFirebaseAuth = FirebaseAuth.getInstance();
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        bt_sign_in = (Button) findViewById(R.id.bt_sign_in);
        back = (TextView) findViewById(R.id.back);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void userLogin() {

        bar.setVisibility(View.VISIBLE);
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(email.isEmpty()){
            et_email.setError("Email is required!");
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please provide valid email!");
            et_email.requestFocus();
            return;
        }
        if(password.length()<6){
            et_password.setError("Min password length should be 6 characters!");
            et_password.requestFocus();
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    bar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                    finish();
                }
                else{
                    bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}