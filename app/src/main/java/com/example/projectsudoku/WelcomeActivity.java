package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsudoku.Gamemodes.Easymode;
import com.example.projectsudoku.Gamemodes.Hardmode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.Serializable;
import java.util.Random;

public class WelcomeActivity extends AppCompatActivity {

    private Button easy;
    private Button medium;
    private Button hard;

    private Button moveToShop; //remove later
    private Button moveToInventory;

    private TextView tv_coins_amount, logged_as;

    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


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
                        PackageManager pm  = WelcomeActivity.this.getPackageManager();
                        ComponentName componentName = new ComponentName(WelcomeActivity.this, MyReceiver.class);
                        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_second:
                startActivity(new Intent(WelcomeActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.disconnect:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.menu_leaderboards:
                startActivity(new Intent(WelcomeActivity.this, Leaderboards.class));
                finish();
                return true;
            case R.id.settings:
                startActivity(new Intent(WelcomeActivity.this, Settings.class));
                finish();
                return true;
            case R.id.about_project:
                startActivity(new Intent(WelcomeActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.exit_menu:
                PackageManager pm  = WelcomeActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(WelcomeActivity.this, MyReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.second_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        easy = (Button) findViewById(R.id.bt_easy);
        medium = (Button) findViewById(R.id.bt_medium);
        hard = (Button) findViewById(R.id.bt_hard);

        tv_coins_amount = (TextView) findViewById(R.id.tv_coins_amount);
        logged_as = (TextView) findViewById(R.id.logged_as);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String coins = snapshot.child("coins").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                tv_coins_amount.setText(coins);
                logged_as.setText("Logged in as: "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WelcomeActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });

        moveToShop = (Button) findViewById(R.id.moveToShop);
        moveToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, ShopActivity.class));
                finish();
            }
        });

        moveToInventory = (Button) findViewById(R.id.moveToInventory);
        moveToInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, InventoryActivity.class));
                finish();
            }
        });

        final Random rnd = new Random();

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,GameBoardActivity.class);
                int cells = rnd.nextInt(6) + 40;
                Easymode gamemode = new Easymode("easy",cells,1, 3);
                intent.putExtra("gamemode", (Serializable) gamemode);
                intent.putExtra("cells",43);
                startActivity(intent);
                finish();
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,GameBoardActivity.class);
                int cells = rnd.nextInt(6) + 30;
                Hardmode gamemode = new Hardmode("medium",cells,2.5, 10);
                intent.putExtra("gamemode", (Serializable) gamemode);
                intent.putExtra("cells",33);
                startActivity(intent);
                finish();
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,GameBoardActivity.class);
                int cells = rnd.nextInt(6) + 20;
                Hardmode hardmode = new Hardmode("hard",cells,5, 20);
                intent.putExtra("gamemode", (Serializable) hardmode);
                intent.putExtra("cells",23);
                startActivity(intent);
                finish();
            }
        });

    }
}