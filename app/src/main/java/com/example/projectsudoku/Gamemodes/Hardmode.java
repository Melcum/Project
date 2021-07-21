package com.example.projectsudoku.Gamemodes;

import com.example.projectsudoku.Gamemodes.Gamemode;

import java.io.Serializable;

public class Hardmode extends Gamemode implements Serializable {

    private int time_left_limit;

    public Hardmode(String gm_name, int num_cells_remains, double coin_multiplier,int time_left_limit){
        super(gm_name, num_cells_remains, coin_multiplier);
        this.time_left_limit = time_left_limit;
    }


    public int getTime_left_limit() {
        return this.time_left_limit;
    }

    public void setTime_left_limit(int time_left_limit) {
        this.time_left_limit = time_left_limit;
    }


    @Override
    public String toString() {
        return "Hardmode{" +
                "time_left_limit=" + time_left_limit +
                ", gm_name='" + gm_name + '\'' +
                ", num_cells_remains=" + num_cells_remains +
                ", coin_multiplier=" + coin_multiplier +
                '}';
    }
}
