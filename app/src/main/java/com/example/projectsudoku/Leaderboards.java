package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Leaderboards extends AppCompatActivity {

    private ProgressBar lb_bar;

    private Button lb_back;

    private RadioButton rb_easy, rb_medium, rb_hard;

    private TextView lb_first_name_view,lb_second_name_view,lb_third_name_view,lb_first_time_view,lb_second_time_view,lb_third_time_view;


    public String timeIntToString (int time){

        int secs = time;
        int hours = secs / 3600;
        int mins = (secs % 3600) / 60;
        secs = secs % 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (mins < 10)
            formattedTime += "0";
        formattedTime += mins + ":";

        if (secs < 10)
            formattedTime += "0";
        formattedTime += secs ;

        return formattedTime;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_first:
                startActivity(new Intent(Leaderboards.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(Leaderboards.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(Leaderboards.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = Leaderboards.this.getPackageManager();
                ComponentName componentName = new ComponentName(Leaderboards.this, MyReceiver.class);
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
        setContentView(R.layout.activity_leaderboards);

        lb_bar = (ProgressBar) findViewById(R.id.lb_progressBar);

        lb_back = (Button) findViewById(R.id.back_leaderboards);

        rb_easy = (RadioButton) findViewById(R.id.rb_easy);
        rb_medium = (RadioButton) findViewById(R.id.rb_medium);
        rb_hard = (RadioButton) findViewById(R.id.rb_hard);

        lb_first_name_view = (TextView) findViewById(R.id.lb_first_username);
        lb_second_name_view = (TextView) findViewById(R.id.lb_second_username);
        lb_third_name_view = (TextView) findViewById(R.id.lb_third_username);
        lb_first_time_view = (TextView) findViewById(R.id.lb_first_score);
        lb_second_time_view = (TextView) findViewById(R.id.lb_second_score);
        lb_third_time_view = (TextView) findViewById(R.id.lb_third_score);

        lb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Leaderboards.this, WelcomeActivity.class));
                finish();
            }
        });

        rb_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb_bar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 2 seconds
                        FirebaseDatabase.getInstance().getReference("Leaderboard")
                                .child("easy").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int first, second, third;
                                String first_s_name, second_s_name, third_s_name;

                                if(snapshot.child("FirstPlace").child("Username").exists()){
                                    first_s_name = snapshot.child("FirstPlace").child("Username").getValue().toString();
                                    first = Integer.parseInt(snapshot.child("FirstPlace").child("Score").getValue().toString());
                                    lb_first_name_view.setText(first_s_name);
                                    lb_first_time_view.setText(timeIntToString(first));
                                }
                                else{
                                    lb_first_name_view.setText("Empty");
                                    lb_first_time_view.setText("Empty");
                                }

                                if(snapshot.child("SecondPlace").child("Username").exists()){
                                    second_s_name = snapshot.child("SecondPlace").child("Username").getValue().toString();
                                    second = Integer.parseInt(snapshot.child("SecondPlace").child("Score").getValue().toString());
                                    lb_second_name_view.setText(second_s_name);
                                    lb_second_time_view.setText(timeIntToString(second));
                                }
                                else{
                                    lb_second_name_view.setText("Empty");
                                    lb_second_time_view.setText("Empty");
                                }

                                if(snapshot.child("ThirdPlace").child("Username").exists()){
                                    third_s_name = snapshot.child("ThirdPlace").child("Username").getValue().toString();
                                    third = Integer.parseInt(snapshot.child("ThirdPlace").child("Score").getValue().toString());
                                    lb_third_name_view.setText(third_s_name);
                                    lb_third_time_view.setText(timeIntToString(third));
                                }
                                else{
                                    lb_third_name_view.setText("Empty");
                                    lb_third_time_view.setText("Empty");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }, 2000);
                lb_bar.setVisibility(View.INVISIBLE);
            }
        });

        rb_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb_bar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 2 seconds
                        FirebaseDatabase.getInstance().getReference("Leaderboard")
                                .child("medium").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int first, second, third;
                                String first_s_name, second_s_name, third_s_name;

                                if(snapshot.child("FirstPlace").child("Username").exists()){
                                    first_s_name = snapshot.child("FirstPlace").child("Username").getValue().toString();
                                    first = Integer.parseInt(snapshot.child("FirstPlace").child("Score").getValue().toString());
                                    lb_first_name_view.setText(first_s_name);
                                    lb_first_time_view.setText(timeIntToString(first));
                                }
                                else{
                                    lb_first_name_view.setText("Empty");
                                    lb_first_time_view.setText("Empty");
                                }

                                if(snapshot.child("SecondPlace").child("Username").exists()){
                                    second_s_name = snapshot.child("SecondPlace").child("Username").getValue().toString();
                                    second = Integer.parseInt(snapshot.child("SecondPlace").child("Score").getValue().toString());
                                    lb_second_name_view.setText(second_s_name);
                                    lb_second_time_view.setText(timeIntToString(second));
                                }
                                else{
                                    lb_second_name_view.setText("Empty");
                                    lb_second_time_view.setText("Empty");
                                }

                                if(snapshot.child("ThirdPlace").child("Username").exists()){
                                    third_s_name = snapshot.child("ThirdPlace").child("Username").getValue().toString();
                                    third = Integer.parseInt(snapshot.child("ThirdPlace").child("Score").getValue().toString());
                                    lb_third_name_view.setText(third_s_name);
                                    lb_third_time_view.setText(timeIntToString(third));
                                }
                                else{
                                    lb_third_name_view.setText("Empty");
                                    lb_third_time_view.setText("Empty");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }, 2000);
                lb_bar.setVisibility(View.INVISIBLE);
            }
        });

        rb_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lb_bar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 2 seconds
                        FirebaseDatabase.getInstance().getReference("Leaderboard")
                                .child("hard").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int first, second, third;
                                String first_s_name, second_s_name, third_s_name;

                                if(snapshot.child("FirstPlace").child("Username").exists()){
                                    first_s_name = snapshot.child("FirstPlace").child("Username").getValue().toString();
                                    first = Integer.parseInt(snapshot.child("FirstPlace").child("Score").getValue().toString());
                                    lb_first_name_view.setText(first_s_name);
                                    lb_first_time_view.setText(timeIntToString(first));
                                }
                                else{
                                    lb_first_name_view.setText("Empty");
                                    lb_first_time_view.setText("Empty");
                                }

                                if(snapshot.child("SecondPlace").child("Username").exists()){
                                    second_s_name = snapshot.child("SecondPlace").child("Username").getValue().toString();
                                    second = Integer.parseInt(snapshot.child("SecondPlace").child("Score").getValue().toString());
                                    lb_second_name_view.setText(second_s_name);
                                    lb_second_time_view.setText(timeIntToString(second));
                                }
                                else{
                                    lb_second_name_view.setText("Empty");
                                    lb_second_time_view.setText("Empty");
                                }

                                if(snapshot.child("ThirdPlace").child("Username").exists()){
                                    third_s_name = snapshot.child("ThirdPlace").child("Username").getValue().toString();
                                    third = Integer.parseInt(snapshot.child("ThirdPlace").child("Score").getValue().toString());
                                    lb_third_name_view.setText(third_s_name);
                                    lb_third_time_view.setText(timeIntToString(third));
                                }
                                else{
                                    lb_third_name_view.setText("Empty");
                                    lb_third_time_view.setText("Empty");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }, 2000);
                lb_bar.setVisibility(View.INVISIBLE);
            }
        });
    }
}