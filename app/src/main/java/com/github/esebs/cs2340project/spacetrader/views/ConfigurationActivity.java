package com.github.esebs.cs2340project.spacetrader.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.esebs.cs2340project.spacetrader.R;
import com.github.esebs.cs2340project.spacetrader.entities.Difficulty;
import com.github.esebs.cs2340project.spacetrader.viewmodels.PlayerViewModel;
import com.google.gson.Gson;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * @version 1.0
 * @author Sebastian Escobar
 */
public class ConfigurationActivity extends AppCompatActivity {

    private final PlayerViewModel viewModel = new PlayerViewModel();
    private Gson gson;

    // Player Name
    private EditText editPlayerName;

    //Point Labels
    private TextView pilotPointsLabel;
    private TextView engineerPointsLabel;
    private TextView fighterPointsLabel;
    private TextView traderPointsLabel;
    private TextView totalSkillPoints;

    private int pilotPoints;
    private int fighterPoints;
    private int traderPoints;
    private int engineerPoints;

    // Difficulty Spinner
    private Spinner difficultySpinner;

    /**
     * Set up when activity starts
     * @param savedInstanceState current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        if (viewModel.isLoaded()) {
            Intent intent = new Intent(ConfigurationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Finds name text edit
        editPlayerName = findViewById(R.id.playerName);

        // Finds all point Labels
        pilotPointsLabel = findViewById(R.id.pilotLabel);
        engineerPointsLabel = findViewById(R.id.engineerLabel);
        fighterPointsLabel = findViewById(R.id.fighterLabel);
        traderPointsLabel = findViewById(R.id.traderLabel);
        totalSkillPoints = findViewById(R.id.skillPoints);

        // Finds all point seek bars
        SeekBar editPilotPoints = findViewById(R.id.pilotBar);
        SeekBar editEngineerPoints = findViewById(R.id.engineerBar);
        SeekBar editFighterPoints = findViewById(R.id.fighterBar);
        SeekBar editTraderPoints = findViewById(R.id.traderBar);

        // Finds Spinners
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Finds Buttons
        Button startGame = findViewById(R.id.exitGame);
        Button cancel = findViewById(R.id.goBack);

        ArrayAdapter<Difficulty> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList(Difficulty.values()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // Individual category points
        pilotPoints = editPilotPoints.getProgress();
        fighterPoints = editFighterPoints.getProgress();
        traderPoints = editTraderPoints.getProgress();
        engineerPoints = editEngineerPoints.getProgress();


        // Changes values when SeekBar is moved
        editPilotPoints.setOnSeekBarChangeListener(pilotListener);
        editEngineerPoints.setOnSeekBarChangeListener(engineerListener);
        editFighterPoints.setOnSeekBarChangeListener(fighterListener);
        editTraderPoints.setOnSeekBarChangeListener(traderListener);

        // Creates player when button is selected
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // added Points
                int sumPoints = pilotPoints + fighterPoints + traderPoints
                        + engineerPoints;

                // Creates player when total points equals MAX_POINTS
                final int MAX_POINTS = 20;
                if (sumPoints != MAX_POINTS) {
                    Toast toast = Toast.makeText(ConfigurationActivity.this,
                            "Points must sum to 20", Toast.LENGTH_LONG);
                    toast.show();

                } else {
                    Editable playerName = editPlayerName.getText();
                    viewModel.setPlayer(playerName.toString(),
                            (Difficulty) difficultySpinner.getSelectedItem(),
                            pilotPoints,
                            fighterPoints,
                            traderPoints,
                            engineerPoints);

                    gson = new Gson();
                    File path = getApplicationContext().getFilesDir();
                    String json = gson.toJson(viewModel.getPlayer());
                    File file = new File(path, "Player.json");

                    try {
                        FileOutputStream stream = new FileOutputStream(file);
                        stream.write(json.getBytes());
                        stream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("Save:", "Player");
                    Intent intent = new Intent(ConfigurationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ConfigurationActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

    private final SeekBar.OnSeekBarChangeListener pilotListener =
            new SeekBar.OnSeekBarChangeListener() {
        /**
         * Changes pilot points to new value and updates Points Remaining
         * @param seekBar editPilotPoints
         * @param progress current progress of pilot bar
         * @param fromUser if progress is from user
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            pilotPoints = progress;
            pilotPointsLabel.setText(getString(R.string.pilot_points, pilotPoints));
            totalSkillPoints.setText(getString(R.string.points_remaining, (20 - (pilotPoints
                    + fighterPoints + traderPoints + engineerPoints))));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    private final SeekBar.OnSeekBarChangeListener engineerListener =
            new SeekBar.OnSeekBarChangeListener() {
        /**
         * Changes engineer points to new value and updates Points Remaining
         * @param seekBar editEngineerPoints
         * @param progress current progress of engineer bar
         * @param fromUser if progress is from user
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            engineerPoints = progress;
            engineerPointsLabel.setText(getString(R.string.engineer_points, engineerPoints));
            totalSkillPoints.setText(getString(R.string.points_remaining, (20 - (pilotPoints
                    + fighterPoints + traderPoints + engineerPoints))));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    private final SeekBar.OnSeekBarChangeListener fighterListener =
            new SeekBar.OnSeekBarChangeListener() {
        /**
         * Changes fighter points to new value and updates Points Remaining
         * @param seekBar editFighterPoints
         * @param progress current progress of fighter bar
         * @param fromUser if progress is from user
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            fighterPoints = progress;
            fighterPointsLabel.setText(getString(R.string.fighter_points, fighterPoints));
            totalSkillPoints.setText(getString(R.string.points_remaining, (20 - (pilotPoints
                    + fighterPoints + traderPoints + engineerPoints))));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    private final SeekBar.OnSeekBarChangeListener traderListener =
            new SeekBar.OnSeekBarChangeListener() {
        /**
         * Changes Trader points to new value and updates Points Remaining
         * @param seekBar editTraderPoints
         * @param progress current progress of trader bar
         * @param fromUser if progress is from user
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            traderPoints = progress;
            traderPointsLabel.setText(getString(R.string.trader_points, traderPoints));
            totalSkillPoints.setText(getString(R.string.points_remaining, (20 - (pilotPoints
                    + fighterPoints + traderPoints + engineerPoints))));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };


}
