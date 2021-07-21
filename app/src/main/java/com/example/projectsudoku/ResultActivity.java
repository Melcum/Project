package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity {

    private TextView timeShow;
    private TextView coins;

    private TextView first_name_view,second_name_view,third_name_view,first_time_view,second_time_view,third_time_view;

    private Button home;


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
                startActivity(new Intent(ResultActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(ResultActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(ResultActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = ResultActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(ResultActivity.this, MyReceiver.class);
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
        setContentView(R.layout.activity_result);

        timeShow = (TextView) findViewById(R.id.timeShow);
        coins = (TextView) findViewById(R.id.coins);

        home = (Button) findViewById(R.id.home);

        Intent intent = getIntent();
        String time = intent.getStringExtra("time");

        int timeSecs = time_Calc(time);

        final double coin_mul = intent.getDoubleExtra("mul", -1);
        final int coins_earned = (int) (10 * coin_mul);

        final String gamemode;

        if(coin_mul == 1) gamemode = "easy";
        else if (coin_mul == 2.5) gamemode = "medium";
        else gamemode = "hard";

        updateLeaderboard(timeSecs,gamemode);

        first_name_view = (TextView) findViewById(R.id.first_username);
        second_name_view = (TextView) findViewById(R.id.second_username);
        third_name_view = (TextView) findViewById(R.id.third_username);
        first_time_view = (TextView) findViewById(R.id.first_score);
        second_time_view = (TextView) findViewById(R.id.second_score);
        third_time_view = (TextView) findViewById(R.id.third_score);


        timeShow.setText(time);
        coins.setText(String.valueOf(coins_earned));

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserHelper helper = snapshot.getValue(UserHelper.class);
                        int total_coins = helper.getCoins();
                        String time = timeShow.getText().toString();
                        int time_integer = time_Calc(time);
                        int difficulty_time;
                        switch((coin_mul == 1)?0: (coin_mul== 2.5)?1: (coin_mul == 5)?2:3){
                            case 0:
                                difficulty_time = helper.getEasyBestTime();
                                if(difficulty_time == -1){
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("easyBestTime").setValue(time_integer);
                                }
                                else{
                                    if(difficulty_time > time_integer){
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("easyBestTime").setValue(time_integer);
                                    }
                                }
                                break;
                            case 1:
                                difficulty_time = helper.getMediumBestTime();
                                if(difficulty_time == -1){
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("mediumBestTime").setValue(time_integer);
                                }
                                else{
                                    if(difficulty_time > time_integer){
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("mediumBestTime").setValue(time_integer);
                                    }
                                }
                                break;
                            case 2:
                                difficulty_time = helper.getHardBestTime();
                                if(difficulty_time == -1){
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("hardBestTime").setValue(time_integer);
                                }
                                else{
                                    if(difficulty_time > time_integer){
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("hardBestTime").setValue(time_integer);
                                    }
                                }
                                break;
                        }

                                FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("coins").setValue(total_coins + coins_earned);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failed to perform", Toast.LENGTH_SHORT).show();
                    }
                });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, WelcomeActivity.class));
                finish();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 2 seconds
                FirebaseDatabase.getInstance().getReference("Leaderboard")
                        .child(gamemode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int first, second, third;
                        String first_s_name, second_s_name, third_s_name;

                        if(snapshot.child("FirstPlace").child("Username").exists()){
                            first_s_name = snapshot.child("FirstPlace").child("Username").getValue().toString();
                            first = Integer.parseInt(snapshot.child("FirstPlace").child("Score").getValue().toString());
                            first_name_view.setText(first_s_name);
                            first_time_view.setText(timeIntToString(first));
                        }

                        if(snapshot.child("SecondPlace").child("Username").exists()){
                            second_s_name = snapshot.child("SecondPlace").child("Username").getValue().toString();
                            second = Integer.parseInt(snapshot.child("SecondPlace").child("Score").getValue().toString());
                            second_name_view.setText(second_s_name);
                            second_time_view.setText(timeIntToString(second));
                        }

                        if(snapshot.child("ThirdPlace").child("Username").exists()){
                            third_s_name = snapshot.child("ThirdPlace").child("Username").getValue().toString();
                            third = Integer.parseInt(snapshot.child("ThirdPlace").child("Score").getValue().toString());
                            third_name_view.setText(third_s_name);
                            third_time_view.setText(timeIntToString(third));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 2000);

    }
    public int time_Calc(String time){

        int colon_index1 = time.indexOf(":");
        int colon_index2 = time.indexOf(colon_index1);
        String h,m,s;
        if(colon_index2 != -1){
            h = time.substring(0,colon_index1);
            m = time.substring(colon_index1+1,colon_index2);
            s = time.substring(colon_index2+1);
            int hour = Integer.parseInt(h);
            int min = Integer.parseInt(m);
            int sec = Integer.parseInt(s);
            return (hour*360 + min*60 + sec);
        }
        else {
            m = time.substring(0,colon_index1);
            s = time.substring(colon_index1+1);
            int min = Integer.parseInt(m);
            int sec = Integer.parseInt(s);
            return (min*60 + sec);
        }
    }

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

    public void updateLeaderboard(final int currentTime, final String game_mode){

        final DatabaseReference generalReff = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference reffToLeaderboard = FirebaseDatabase.getInstance().getReference("Leaderboard")
                .child(game_mode);
        generalReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean first_exist = true, second_exist = true, third_exist = true;
                if(!snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Username").exists()) first_exist = false;
                if(!snapshot.child("Leaderboard").child(game_mode).child("SecondPlace").child("Username").exists()) second_exist = false;
                if(!snapshot.child("Leaderboard").child(game_mode).child("ThirdPlace").child("Username").exists()) third_exist = false;



                int first_int_time,second_int_time,third_int_time;


                if(!first_exist && !second_exist && !third_exist){
                    reffToLeaderboard.child("FirstPlace").child("Username").setValue(snapshot.child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue().toString());
                    reffToLeaderboard.child("FirstPlace").child("Score").setValue(currentTime);
                }

                if(first_exist && !second_exist && !third_exist){

                    String first_name = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Username").getValue().toString();
                    String first_time = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Score").getValue().toString();

                    first_int_time = Integer.parseInt(first_time);
                    if(first_int_time <= currentTime) {
                        reffToLeaderboard.child("SecondPlace").child("Username").setValue(snapshot.child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                        reffToLeaderboard.child("SecondPlace").child("Score").setValue(currentTime);
                    }
                    else {
                        reffToLeaderboard.child("SecondPlace").child("Username").setValue(first_name);
                        reffToLeaderboard.child("SecondPlace").child("Score").setValue(first_time);

                        reffToLeaderboard.child("FirstPlace").child("Username").setValue(snapshot.child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                        reffToLeaderboard.child("FirstPlace").child("Score").setValue(currentTime);
                    }
                }

                if(first_exist && second_exist && !third_exist){

                    String first_name = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Username").getValue().toString();
                    String first_time = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Score").getValue().toString();
                    String second_name = snapshot.child("Leaderboard").child(game_mode).child("SecondPlace").child("Username").getValue().toString();
                    String second_time = snapshot.child("Leaderboard").child(game_mode).child("SecondPlace").child("Score").getValue().toString();



                    first_int_time = Integer.parseInt(first_time);
                    second_int_time = Integer.parseInt(second_time);
                    switch ((currentTime < first_int_time)?0 : (currentTime < second_int_time)?1 :2){

                        case 0:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(second_name);
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(second_time);

                            reffToLeaderboard.child("SecondPlace").child("Username").setValue(first_name);
                            reffToLeaderboard.child("SecondPlace").child("Score").setValue(first_time);

                            reffToLeaderboard.child("FirstPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("FirstPlace").child("Score").setValue(currentTime);
                            break;

                        case 1:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(second_name);
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(second_time);

                            reffToLeaderboard.child("SecondPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("SecondPlace").child("Score").setValue(currentTime);
                            break;

                        case 2:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(currentTime);
                            break;
                    }
                }
                if(first_exist && second_exist && third_exist){

                    String first_name = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Username").getValue().toString();
                    String first_time = snapshot.child("Leaderboard").child(game_mode).child("FirstPlace").child("Score").getValue().toString();
                    String second_name = snapshot.child("Leaderboard").child(game_mode).child("SecondPlace").child("Username").getValue().toString();
                    String second_time = snapshot.child("Leaderboard").child(game_mode).child("SecondPlace").child("Score").getValue().toString();
                    String third_name = snapshot.child("Leaderboard").child(game_mode).child("ThirdPlace").child("Username").getValue().toString();
                    String third_time = snapshot.child("Leaderboard").child(game_mode).child("ThirdPlace").child("Score").getValue().toString();



                    first_int_time = Integer.parseInt(first_time);
                    second_int_time = Integer.parseInt(second_time);
                    third_int_time = Integer.parseInt(third_time);

                    switch ((currentTime < first_int_time)?0 : (currentTime < second_int_time)?1 :(currentTime < third_int_time)?2 :3){

                        case 0:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(second_name);
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(second_time);

                            reffToLeaderboard.child("SecondPlace").child("Username").setValue(first_name);
                            reffToLeaderboard.child("SecondPlace").child("Score").setValue(first_time);

                            reffToLeaderboard.child("FirstPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("FirstPlace").child("Score").setValue(currentTime);
                            break;

                        case 1:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(second_name);
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(second_time);

                            reffToLeaderboard.child("SecondPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("SecondPlace").child("Score").setValue(currentTime);
                            break;

                        case 2:
                            reffToLeaderboard.child("ThirdPlace").child("Username").setValue(snapshot.child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").getValue());
                            reffToLeaderboard.child("ThirdPlace").child("Score").setValue(currentTime);
                            break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}