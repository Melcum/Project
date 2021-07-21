package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class Settings extends AppCompatActivity {

    private Switch music_switch;

    private Button back;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_first:
                startActivity(new Intent(Settings.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(Settings.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                Toast.makeText(Settings.this, "You are already on settings page",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = Settings.this.getPackageManager();
                ComponentName componentName = new ComponentName(Settings.this, MyReceiver.class);
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
        setContentView(R.layout.activity_settings);


        music_switch = (Switch) findViewById(R.id.first_switch);
        back = (Button) findViewById(R.id.st_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    startActivity(new Intent(Settings.this, MainActivity.class));
                }
                else {
                    startActivity(new Intent(Settings.this, WelcomeActivity.class));
                }
                finish();
            }
        });

        if(!isMyServiceRunning(MyService.class)){
            music_switch.setChecked(false);
        }


        music_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!music_switch.isChecked()){
                    stopService(new Intent(Settings.this, MyService.class));
                }
                else{
                    startService(new Intent(Settings.this, MyService.class));
                }
            }
        });

    }

    private boolean isMyServiceRunning(Class<MyService> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}