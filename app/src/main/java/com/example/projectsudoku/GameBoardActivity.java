package com.example.projectsudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectsudoku.Gamemodes.Easymode;
import com.example.projectsudoku.Gamemodes.Gamemode;
import com.example.projectsudoku.Gamemodes.Hardmode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GameBoardActivity extends AppCompatActivity{

    private Button back;

    private TextView strikes;

    private Button music;

    private SudokuBoard gameBoard;
    private Solver gameBoardSolver;

    private ConstraintLayout game_layout;

    private Button digit1;
    private Button digit2;
    private Button digit3;
    private Button digit4;
    private Button digit5;
    private Button digit6;
    private Button digit7;
    private Button digit8;
    private Button digit9;

    private TextView hardmode_timeLeft;

    private Button hint;

    private Chronometer timer;
    private CountDownTimer countDownTimer;

    private String finishedTime;

    private DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("currentBackground");


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null)countDownTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService(new Intent(GameBoardActivity.this, MyService.class));
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.about_first:
                startActivity(new Intent(GameBoardActivity.this, AboutSudoku.class));
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(GameBoardActivity.this, AboutInfo.class));
                finish();
                return true;
            case R.id.settings2:
                startActivity(new Intent(GameBoardActivity.this, Settings.class));
                finish();
                return true;
            case R.id.exit_menu2:
                PackageManager pm  = GameBoardActivity.this.getPackageManager();
                ComponentName componentName = new ComponentName(GameBoardActivity.this, MyReceiver.class);
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        hardmode_timeLeft = (TextView) findViewById(R.id.hardmode_timeLeft);
        hint = (Button) findViewById(R.id.bt_hint);
        music = (Button) findViewById(R.id.music);



        game_layout = (ConstraintLayout) findViewById(R.id.game_layout);

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String background = snapshot.getValue().toString();
                switch (background){
                    case "Default Background":
                        game_layout.setBackgroundResource(R.drawable.background0);
                        break;
                    case "Purple & Blue":
                        game_layout.setBackgroundResource(R.drawable.background1);
                        break;
                    case "Dusty Grass":
                        game_layout.setBackgroundResource(R.drawable.background2);
                        break;
                    case "Sunny Morning":
                        game_layout.setBackgroundResource(R.drawable.background3);
                        break;
                    case "Winter Cold":
                        game_layout.setBackgroundResource(R.drawable.background4);
                        break;
                    case "Night Fade":
                        game_layout.setBackgroundResource(R.drawable.background5);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(isMyServiceRunning(MyService.class)){
            music.setText("Stop Music");
        }
        else{
            music.setText("Play Music");
        }


        final Intent intent = getIntent();

        Gamemode gm = (Gamemode) intent.getSerializableExtra("gamemode");
        int difficulty;
        double mul;
        if(gm instanceof Hardmode){
            Hardmode hardmode = (Hardmode) gm;
            int time_limit = hardmode.getTime_left_limit();
            time_limit = time_limit * 60000;
            difficulty = hardmode.getNum_cells_remains();
            mul = hardmode.getCoin_multiplier();
            hardmode_timeLeft.setVisibility(View.VISIBLE);
            countDownTimer = new CountDownTimer(time_limit, 1000) {

                public void onTick(long millisUntilFinished) {
                    hardmode_timeLeft.setText("Minutes remaining: " + millisUntilFinished / 60000);
                }



                public void onFinish() {
                    startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                    finish();
                }
            }.start();
        }
        else{
            final Easymode easymode = (Easymode) gm;
            difficulty = easymode.getNum_cells_remains();
            mul = easymode.getCoin_multiplier();
            reff.getParent().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String hints_num_string = snapshot.child("hintsNumber").getValue().toString();
                    int hints_num = Integer.parseInt(hints_num_string);
                    int base_hints_nums = easymode.getHelp_left();
                    hint.setText("Hints: "+(hints_num+base_hints_nums));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            hint.setVisibility(View.VISIBLE);
        }

        final double end_mul = mul;




        Generator generator = new Generator(9,81 - difficulty);
        generator.fillValues();


        int[][] mat = new int[9][9];
        int[][] solvedMat = new int[9][9];


        for (int r = 0; r<9; r++) {
            for (int c = 0; c < 9; c++) {
                mat[r][c] = generator.getMat()[r][c];
                solvedMat[r][c] = generator.getSolvedMat()[r][c];
            }
        }

        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                if(mat[r][c] != 0){
                    solvedMat[r][c] = 0;
                }
            }
        }

        boolean[][] preCells = new boolean[9][9];


        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                if(mat[r][c] == 0){
                    preCells[r][c] = true;
                }
            }
        }


        gameBoard = findViewById(R.id.SudokuBoard);
        gameBoardSolver = gameBoard.getSolver();

        gameBoardSolver.setBoard(mat);
        gameBoardSolver.setPreCells(preCells);
        gameBoardSolver.setSolvedBoard(solvedMat);






        digit1 = (Button) findViewById(R.id.digit1);
        digit2 = (Button) findViewById(R.id.digit2);
        digit3 = (Button) findViewById(R.id.digit3);
        digit4 = (Button) findViewById(R.id.digit4);
        digit5 = (Button) findViewById(R.id.digit5);
        digit6 = (Button) findViewById(R.id.digit6);
        digit7 = (Button) findViewById(R.id.digit7);
        digit8 = (Button) findViewById(R.id.digit8);
        digit9 = (Button) findViewById(R.id.digit9);

        timer = (Chronometer) findViewById(R.id.timer);
        timer.setBase(SystemClock.elapsedRealtime());


        strikes = (TextView) findViewById(R.id.strikes);

        back = (Button) findViewById(R.id.back_to_welcome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index_first = hint.getText().toString().indexOf(" ") +1;
                int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                gameBoardSolver.setHints_left(hints_number_check-3);
                if(gameBoardSolver.getHints_left() < 0) gameBoardSolver.setHints_left(0);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("hintsNumber")
                        .setValue(gameBoardSolver.getHints_left());
                startActivity(new Intent(GameBoardActivity.this, WelcomeActivity.class));
                finish();
            }
        });

        timer.start();

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index_first = hint.getText().toString().indexOf(" ") +1;
                int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                if(hint.getVisibility() == View.VISIBLE && hints_number_check != 0){
                    if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                        if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                            if (gameBoardSolver.getBoard()[gameBoardSolver.getSelected_row()-1][gameBoardSolver.getSelected_column()-1] == 0){
                                int correct_answer = gameBoardSolver.getSolvedBoard()[gameBoardSolver.getSelected_row()-1][gameBoardSolver.getSelected_column()-1];
                                gameBoardSolver.setNumberPos(correct_answer);
                                gameBoard.invalidate();
                                gameBoardSolver.setHints_left(hints_number_check - 1);
                                hint.setText("Hints: "+gameBoardSolver.getHints_left());
                                if (gameBoardSolver.checkIfSolved()) {
                                    Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                                    timer.stop();
                                    finishedTime = timer.getText().toString();

                                    gameBoardSolver.setHints_left(gameBoardSolver.getHints_left()-3);
                                    if(gameBoardSolver.getHints_left() < 0) gameBoardSolver.setHints_left(0);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("hintsNumber")
                                            .setValue(gameBoardSolver.getHints_left());

                                    Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                                    resultIntent.putExtra("time", finishedTime);
                                    resultIntent.putExtra("mul", end_mul);
                                    startActivity(resultIntent);
                                    finish();
                                }
                            }
                        }
                    }
                }
            }
        });



        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMyServiceRunning(MyService.class)){
                    music.setText("Play Music");
                    stopService(new Intent(GameBoardActivity.this, MyService.class));
                }
                else{
                    music.setText("Stop Music");
                    startService(new Intent(GameBoardActivity.this, MyService.class));
                }
            }
        });




        digit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(1);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(2);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(3);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(4);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(5);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(6);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(7);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(8);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
        digit9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameBoardSolver.getSelected_row() != -1 && gameBoardSolver.getSelected_column() != -1) {
                    if (gameBoardSolver.checkIfPlaceable(gameBoardSolver.getSelected_row(), gameBoardSolver.getSelected_column())) {
                        gameBoardSolver.setNumberPos(9);
                        gameBoard.invalidate();
                        strikes.setText("Strikes Left: " + gameBoardSolver.getMistakesLeft());
                        if (gameBoardSolver.getMistakesLeft() == 0) {
                            Toast.makeText(GameBoardActivity.this, "You Lost!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("hintsNumber")
                                    .setValue(gameBoardSolver.getHints_left());
                            startActivity(new Intent(GameBoardActivity.this, LostActivity.class));
                            finish();
                        }
                    }
                    if (gameBoardSolver.checkIfSolved()) {
                        Toast.makeText(GameBoardActivity.this, "You finished the sudoku successfully!", Toast.LENGTH_SHORT).show();
                        timer.stop();
                        finishedTime = timer.getText().toString();
                        int index_first = hint.getText().toString().indexOf(" ") +1;
                        int hints_number_check = Integer.parseInt(hint.getText().toString().substring(index_first));
                        gameBoardSolver.setHints_left(hints_number_check-3);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hintsNumber")
                                .setValue(gameBoardSolver.getHints_left());
                        Intent resultIntent = new Intent(GameBoardActivity.this, ResultActivity.class);
                        resultIntent.putExtra("time", finishedTime);
                        resultIntent.putExtra("mul", end_mul);
                        startActivity(resultIntent);
                        finish();
                    }
                }
            }
        });
    }

    public boolean isMyServiceRunning(Class<MyService> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}