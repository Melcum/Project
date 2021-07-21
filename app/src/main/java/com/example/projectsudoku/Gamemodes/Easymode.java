package com.example.projectsudoku.Gamemodes;

import java.io.Serializable;

public class Easymode extends Gamemode implements Serializable {

    private int help_left;

    public Easymode(String gm_name, int num_cells_remains, double coin_multiplier, int help_left){
        super(gm_name, num_cells_remains, coin_multiplier);
        this.help_left = help_left;
    }

    public int getHelp_left() {
        return this.help_left;
    }


    public void setHelp_left(int help_left) {
        this.help_left = help_left;
    }


    @Override
    public String toString() {
        return "Easymode{" +
                "help_left=" + help_left +
                ", gm_name='" + gm_name + '\'' +
                ", num_cells_remains=" + num_cells_remains +
                ", coin_multiplier=" + coin_multiplier +
                '}';
    }
}
