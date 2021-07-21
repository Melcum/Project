package com.example.projectsudoku.Gamemodes;


import java.io.Serializable;

public class Gamemode implements Serializable {

    protected String gm_name;
    protected int num_cells_remains;
    protected double coin_multiplier;

    public Gamemode(String gm_name, int num_cells_remains, double coin_multiplier){
        this.gm_name = gm_name;
        this.num_cells_remains = num_cells_remains;
        this.coin_multiplier = coin_multiplier;
    }

    public String getGm_name() {
        return this.gm_name;
    }

    public int getNum_cells_remains() {
        return this.num_cells_remains;
    }

    public double getCoin_multiplier() {
        return this.coin_multiplier;
    }

    public void setGm_name(String gm_name) {
        this.gm_name = gm_name;
    }

    public void setNum_cells_remains(int num_cells_remains) {
        this.num_cells_remains = num_cells_remains;
    }

    public void setCoin_multiplier(double coin_multiplier) {
        this.coin_multiplier = coin_multiplier;
    }

    @Override
    public String toString() {
        return "Gamemode{" +
                "gm_name='" + gm_name + '\'' +
                ", num_cells_remains=" + num_cells_remains +
                ", coin_multiplier=" + coin_multiplier +
                '}';
    }
}
