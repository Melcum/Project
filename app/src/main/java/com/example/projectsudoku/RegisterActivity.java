package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText et_email, et_password,et_name;
    private Button bt_sign_up;
    private ProgressBar bar;
    private TextView back;
    



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
                startActivity(new Intent(RegisterActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(RegisterActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(RegisterActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = RegisterActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(RegisterActivity.this, MyReceiver.class);
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
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        back = (TextView) findViewById(R.id.back);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_name = (EditText) findViewById(R.id.et_name);
        bt_sign_up = (Button) findViewById(R.id.bt_sign_up);
        bar = (ProgressBar) findViewById(R.id.bar);

        bt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

    }

    private void signUp() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String name = et_name.getText().toString().trim();

        bar.setVisibility(View.VISIBLE);

        if(email.isEmpty()){
            et_email.setError("Email is required!");
            et_email.requestFocus();
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please provide valid email!");
            et_email.requestFocus();
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if(password.length()<6){
            et_password.setError("Min password length should be 6 characters!");
            et_password.requestFocus();
            bar.setVisibility(View.INVISIBLE);
            return;
        }
        if(name.isEmpty()){
            et_name.setError("Name is required!");
            et_name.requestFocus();
            bar.setVisibility(View.INVISIBLE);
        }
        if(name.length() < 2){
            et_name.setError("Name should be at least 2 chars!");
            et_name.requestFocus();
            bar.setVisibility(View.INVISIBLE);
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Failed to register! try again!", Toast.LENGTH_SHORT).show();
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException error){
                        et_email.setError("Email is already used");
                        et_email.requestFocus();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    String email = et_email.getText().toString();
                    String password = et_password.getText().toString();
                    String name = et_name.getText().toString();
                    UserHelper userHelper = new UserHelper(email,password, name);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userHelper);
                    FirebaseDatabase.getInstance().getReference("Shop")
                            .child("Item0").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

    }
}