package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectsudoku.ItemDecoration.SpacingItemDecoration;
import com.example.projectsudoku.ShopAdapters.InventoryBackgroundAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    private static final String TAG = "InventoryActivity";

    private ArrayList<InventoryBackgroundItem> inventory_list = new ArrayList<>();

    private DatabaseReference reffToShop = FirebaseDatabase.getInstance().getReference("Shop");

    private ProgressBar p_bar;

    private Button back;




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
                startActivity(new Intent(InventoryActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(InventoryActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(InventoryActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = InventoryActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(InventoryActivity.this, MyReceiver.class);
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
        setContentView(R.layout.activity_inventory);
        back = (Button) findViewById(R.id.inventory_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this, WelcomeActivity.class));
                finish();
            }
        });


        p_bar = (ProgressBar) findViewById(R.id.p_bar);
        p_bar.setVisibility(View.VISIBLE);
        reffToShop.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                InventoryBackgroundItem defaultItem = new InventoryBackgroundItem(R.drawable.background0, "Default Background", "Normal boring white");
                inventory_list.add(defaultItem);

                if(snapshot.child("Item1").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    InventoryBackgroundItem firstItem = new InventoryBackgroundItem(R.drawable.background1,"Purple & Blue","Feels like a purple sunset");
                    inventory_list.add(firstItem);
                }
                if(snapshot.child("Item2").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    InventoryBackgroundItem secondItem = new InventoryBackgroundItem(R.drawable.background2,"Dusty Grass","Let's roll on this grass");
                    inventory_list.add(secondItem);
                }
                if(snapshot.child("Item3").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    InventoryBackgroundItem thirdItem = new InventoryBackgroundItem(R.drawable.background3,"Sunny Morning","What a lovely morning we woke up to");
                    inventory_list.add(thirdItem);
                }
                if(snapshot.child("Item4").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    InventoryBackgroundItem fourthItem = new InventoryBackgroundItem(R.drawable.background4,"Winter Cold","It's freezing outside");
                    inventory_list.add(fourthItem);
                }
                if(snapshot.child("Item5").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                    InventoryBackgroundItem fifthItem = new InventoryBackgroundItem(R.drawable.background5,"Night Fade","Darkness incoming");
                    inventory_list.add(fifthItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InventoryActivity.this, "There was an issue while loading the inventory", Toast.LENGTH_SHORT).show();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 2 seconds
                p_bar.setVisibility(View.INVISIBLE);
                initInventoryRecyclerView();
            }
        }, 2000);
    }

    private void initInventoryRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        SpacingItemDecoration itemDecoration = new SpacingItemDecoration(100);
        recyclerView.addItemDecoration(itemDecoration);
        InventoryBackgroundAdapter adapter = new InventoryBackgroundAdapter(this, inventory_list);
        recyclerView.setAdapter(adapter);
    }
}