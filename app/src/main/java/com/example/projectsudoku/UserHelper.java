package com.example.projectsudoku;

public class UserHelper {

    private String email;
    private String password;
    private String name;

    private int easyBestTime;
    private int mediumBestTime;
    private int hardBestTime;

    private int coins;

    private int hintsNumber;

    private String currentBackground;



    public UserHelper(String email, String password,String name){
        this.email = email;
        this.password = password;
        this.name = name;
        this.easyBestTime = -1;
        this.mediumBestTime = -1;
        this.hardBestTime = -1;
        this.coins = 0;
        this.hintsNumber = 0;
        this.currentBackground = "Default Background";
    }

    public UserHelper(){
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public int getEasyBestTime() {
        return this.easyBestTime;
    }

    public int getMediumBestTime() {
        return this.mediumBestTime;
    }

    public int getHardBestTime() {
        return this.hardBestTime;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getHintsNumber() {
        return this.hintsNumber;
    }

    public String getCurrentBackground() {
        return this.currentBackground;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEasyBestTime(int easyBestTime) {
        this.easyBestTime = easyBestTime;
    }

    public void setMediumBestTime(int mediumBestTime) {
        this.mediumBestTime = mediumBestTime;
    }

    public void setHardBestTime(int hardBestTime) {
        this.hardBestTime = hardBestTime;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHintsNumber(int hintsNumber) {
        this.hintsNumber = hintsNumber;
    }

    public void setCurrentBackground(String currentBackground) {
        this.currentBackground = currentBackground;
    }

    @Override
    public String toString() {
        return "UserHelper{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", easyBestTime=" + easyBestTime +
                ", mediumBestTime=" + mediumBestTime +
                ", hardBestTime=" + hardBestTime +
                ", coins=" + coins +
                ", hintsNumber=" + hintsNumber +
                ", currentBackground='" + currentBackground + '\'' +
                '}';
    }
}
