package com.example.projectsudoku;

import java.util.ArrayList;

class Solver {


    private int [][] board;
    private int[][] solvedBoard;
    private boolean [][] preCells;

    private int mistakesLeft = 3;
    private int hints_left = 0; // change back to 3 afterwards

    ArrayList<ArrayList<Object>> emptyBoxIndex;

    private int selected_row;
    private int selected_column;


    Solver(){
        selected_row = -1;
        selected_row = -1;

        emptyBoxIndex = new ArrayList<>();
    }

    public void setBoard(int[][] grid){
        this.board = grid;
    }

    public void setSolvedBoard(int[][] solvedGrid){
        this.solvedBoard = solvedGrid;
    }

    public void setPreCells(boolean[][] preCells){
        this.preCells = preCells;
    }




    public void setNumberPos(int num){
        if(this.selected_row != -1 && this.selected_column != -1){
            if(this.board[this.selected_row - 1][this.selected_column - 1] == num){
                this.board[this.selected_row - 1][this.selected_column - 1] = 0;
                this.solvedBoard[this.selected_row -1][this.selected_column -1] = num;
                printBoard(this.board);
                printSolved(this.solvedBoard);
            }
            else{
                if(this.solvedBoard[this.selected_row -1 ][this.selected_column -1] == num){

                    this.board[this.selected_row - 1][this.selected_column - 1] = num;
                    this.solvedBoard[this.selected_row -1][this.selected_column -1] = 0;
                    printBoard(this.board);
                    printSolved(this.solvedBoard);
                }
                else{
                    this.mistakesLeft--;
                    if(this.mistakesLeft == 0) System.out.println("Game Over!!!!!!!!!!!!!!");
                    printBoard(this.board);
                    printSolved(this.solvedBoard);

                }
            }
        }
    }

    public int[][] getBoard(){
        return this.board;
    }

    public int[][] getSolvedBoard() {
        return this.solvedBoard;
    }

    public int getMistakesLeft() {
        return mistakesLeft;
    }

    public int getHints_left() {
        return this.hints_left;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex(){
        return this.emptyBoxIndex;
    }

    public int getSelected_row() {
        return selected_row;
    }

    public int getSelected_column() {
        return selected_column;
    }

    public void setSelected_row(int r) {
        this.selected_row = r;
    }

    public void setSelected_column(int c) {
        this.selected_column = c;
    }

    public void setHints_left(int hints_left) {
        this.hints_left = hints_left;
    }

    public boolean checkIfPlaceable(int selected_row, int selected_column){
        return (this.preCells[selected_row-1][selected_column-1]);
    }


    public boolean checkIfSolved(){

        for(int r = 0; r < 9; r++){
            for(int c = 0; c < 9; c++){
                if(this.solvedBoard[r][c] != 0) return false;
            }
        }
        return true;
    }

    public void printBoard(int[][] board){
        System.out.println();
        System.out.println("---------------------");
        for(int r = 0; r< 9; r++){
            for(int c = 0; c< 9;c++){
                System.out.print(board[r][c]);
            }
            System.out.println();
        }
        System.out.println("---------------------");
    }

    public void printSolved(int[][] solvedBoard){
        System.out.println();
        System.out.println("---------------------");
        for(int r = 0; r< 9; r++){
            for(int c = 0; c< 9;c++){
                System.out.print(solvedBoard[r][c]);
            }
            System.out.println();
        }
        System.out.println("---------------------");
    }
}


