package com.example.projectsudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellHighlightColor;

    private final int letterColor;
    private final int letterColorSolved;
    private final int letterColorWrong;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();
    private final Paint numberSolvedTextPaint = new Paint();
    private final Paint numberWrongTextPaint = new Paint();

    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private int cellSize;

    private final Solver solver = new Solver();



    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.SudokuBoard,0,0);

        try{
            boardColor = a.getInteger(R.styleable.SudokuBoard_boardColor,0);
            cellFillColor = a.getInteger(R.styleable.SudokuBoard_cellFillColor,0);
            cellHighlightColor = a.getInteger(R.styleable.SudokuBoard_cellHighlightColor,0);
            letterColor = a.getInteger(R.styleable.SudokuBoard_letterColor,0);
            letterColorSolved = a.getInteger(R.styleable.SudokuBoard_letterColorSolved,0);
            letterColorWrong = a.getInteger(R.styleable.SudokuBoard_letterColorWrong,0);
        }finally {
            a.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dimension = Math.min(this.getMeasuredWidth(),this.getMeasuredHeight());
        cellSize = dimension /9;

        setMeasuredDimension(dimension,dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setAntiAlias(true);
        cellFillColorPaint.setColor(cellFillColor);

        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setAntiAlias(true);
        cellHighlightColorPaint.setColor(cellHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        numberSolvedTextPaint.setStyle(Paint.Style.FILL);
        numberSolvedTextPaint.setAntiAlias(true);
        numberSolvedTextPaint.setColor(letterColorSolved);

        numberWrongTextPaint.setStyle(Paint.Style.FILL);
        numberWrongTextPaint.setAntiAlias(true);
        numberWrongTextPaint.setColor(letterColorWrong);

        colorCell(canvas,solver.getSelected_row(), solver.getSelected_column());
        canvas.drawRect(0,0, getWidth() , getHeight() , boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean isValid;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){

            solver.setSelected_row((int) Math.ceil(y/cellSize));
            solver.setSelected_column((int) Math.ceil(x/cellSize));


            isValid = true;
        }
        else{
            isValid = false;
        }




        return isValid;
    }

    private void drawNumbers(Canvas canvas){

        letterPaint.setTextSize(cellSize);
        numberSolvedTextPaint.setTextSize(cellSize);
        numberWrongTextPaint.setTextSize(cellSize);
        for(int r = 0; r< 9; r++){
            for(int c = 0; c< 9; c++){


                if(solver.getBoard()[r][c] != 0){
                    String text = Integer.toString(solver.getBoard()[r][c]);
                    float width, height;

                    if(solver.checkIfPlaceable(r+1,c+1)){


                            numberSolvedTextPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                            width = numberSolvedTextPaint.measureText(text);
                            height = letterPaintBounds.height();

                            canvas.drawText(text, (c * cellSize) + ((cellSize - width)) / 2,
                                    (r * cellSize + cellSize) - ((cellSize - height) / 2),
                                    numberSolvedTextPaint);


                    }
                    else {
                        letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                        width = letterPaint.measureText(text);
                        height = letterPaintBounds.height();

                        canvas.drawText(text, (c * cellSize) + ((cellSize - width)) / 2,
                                (r * cellSize + cellSize) - ((cellSize - height) / 2),
                                letterPaint);
                    }
                }
            }
        }

        for (ArrayList<Object> letter : solver.getEmptyBoxIndex()){

            int r = (int) letter.get(0);
            int c = (int) letter.get(1);

            String text = Integer.toString(solver.getBoard()[r][c]);
            float width, height;

            letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
            width = letterPaint.measureText(text);
            height = letterPaintBounds.height();

            canvas.drawText(text, (c*cellSize) + ((cellSize-width))/2,
                    (r * cellSize + cellSize) - ((cellSize - height)/2),
                    letterPaint);
        }
    }


    private void colorCell(Canvas canvas, int r, int c){
        if(solver.getSelected_column() != -1 && solver.getSelected_row() != -1){
            canvas.drawRect((c-1) * cellSize,0,c * cellSize,cellSize * 9 ,cellHighlightColorPaint);

            canvas.drawRect(0,(r-1) * cellSize,cellSize * 9,r * cellSize ,cellHighlightColorPaint);

            canvas.drawRect((c-1) * cellSize,(r-1) * cellSize,c * cellSize,r * cellSize ,cellHighlightColorPaint);
        }

        invalidate();
    }


    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);

    }

    private void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);

    }

    private void drawBoard(Canvas canvas){

        for (int c = 0; c<10; c++){
            if(c % 3 == 0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }
            canvas.drawLine(cellSize * c, 0, cellSize * c, getWidth(), boardColorPaint);
        }

        for(int r = 0; r< 10; r++){
            if(r % 3 == 0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }

            canvas.drawLine(0, cellSize * r, getWidth(), cellSize * r, boardColorPaint);

        }

    }

    public Solver getSolver(){
        return this.solver;
    }
}
