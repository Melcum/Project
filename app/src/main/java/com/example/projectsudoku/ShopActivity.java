package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsudoku.ItemDecoration.SpacingItemDecoration;
import com.example.projectsudoku.ShopAdapters.ShopGameBackgroundAdapter;
import com.example.projectsudoku.ShopAdapters.ShopHintsPackAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";
    private ArrayList<ShopProduct> arrayList1 = new ArrayList<>();
    private TextView coins_amount;

    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private Button back;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_first:
                startActivity(new Intent(ShopActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(ShopActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(ShopActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = ShopActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(ShopActivity.this, MyReceiver.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        back = (Button) findViewById(R.id.shop_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShopActivity.this, WelcomeActivity.class));
                finish();
            }
        });
        coins_amount = (TextView) findViewById(R.id.coins_amount);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String coins = snapshot.child("coins").getValue().toString();
                coins_amount.setText(coins);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShopActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });


        ShopGameBackgroundItem item1 = new ShopGameBackgroundItem(R.drawable.background1, "Purple & Blue", "Feels like a purple sunset", 2000);
        ShopGameBackgroundItem item2 = new ShopGameBackgroundItem(R.drawable.background2, "Dusty Grass", "Let's roll on this grass", 2500);
        ShopGameBackgroundItem item3 = new ShopGameBackgroundItem(R.drawable.background3, "Sunny Morning", "What a lovely morning we woke up to", 2500);
        ShopGameBackgroundItem item4 = new ShopGameBackgroundItem(R.drawable.background4, "Winter Cold", "It's freezing outside", 2500);
        ShopGameBackgroundItem item5 = new ShopGameBackgroundItem(R.drawable.background5, "Night Fade", "Darkness incoming", 3000);

        ShopHintsPackItem item6 = new ShopHintsPackItem( "Hints", "This pack contains 1 hint.", 100, 1);
        ShopHintsPackItem item7 = new ShopHintsPackItem("Hints", "This pack contains 5 hint.", 400, 5);
        ShopHintsPackItem item8 = new ShopHintsPackItem("Hints", "This pack contains 10 hint.", 750, 10);
        ShopHintsPackItem item9 = new ShopHintsPackItem("Hints", "This pack contains 20 hint.", 1200, 20);

        arrayList1.add(item1);
        arrayList1.add(item2);
        arrayList1.add(item3);
        arrayList1.add(item4);
        arrayList1.add(item5);


        arrayList1.add(item6);
        arrayList1.add(item7);
        arrayList1.add(item8);
        arrayList1.add(item9);

        initRecyclerView();
        initRecyclerView2();


    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        SpacingItemDecoration itemDecoration = new SpacingItemDecoration(100);
        recyclerView.addItemDecoration(itemDecoration);
        ShopGameBackgroundAdapter adapter = new ShopGameBackgroundAdapter(this, arrayList1);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView2(){
        Log.d(TAG, "initRecyclerView2: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(layoutManager);
        SpacingItemDecoration itemDecoration = new SpacingItemDecoration(100);
        recyclerView2.addItemDecoration(itemDecoration);
        ShopHintsPackAdapter adapter2 = new ShopHintsPackAdapter(this, arrayList1);
        recyclerView2.setAdapter(adapter2);
    }
}