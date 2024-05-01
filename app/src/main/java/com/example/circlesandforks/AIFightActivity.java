package com.example.circlesandforks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class AIFightActivity extends AppCompatActivity {
    Button mEXitSingle;
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private GridLayout gridLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aifight);

        mEXitSingle = findViewById(R.id.ExitButtonAI);
        mEXitSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Intent 遷移到另一個頁面
                Intent intent = new Intent(AIFightActivity.this,boardingActivity.class);
                startActivity(intent);
            }
        });

        gridLayout = findViewById(R.id.gridLayout);

        initializeBoard();

    }

    private void initializeBoard() {

        // 創建外框 Drawable
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);  // 設置形狀為矩形
        drawable.setCornerRadius(8);  // 設置圓角半徑
        drawable.setStroke(2, Color.BLACK);  // 設置外框寬度和顏色

        //設置Button的寬度和高度，這裡設置為 169dp 寬，98dp 高
        int widthInDp = 98;
        int heightInDp = 98;

        for (int row = 0; row < 3; row++){
            for (int col = 0; col < 3; col++){
                Button button = new Button(this);


                //將dp轉換為像素，使不同的設備，有相同的尺寸
                int widthInPixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, widthInDp, getResources().getDisplayMetrics());

                int heightInPixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, heightInDp, getResources().getDisplayMetrics());

                //設置Button的寬度和高度
                button.setLayoutParams(new ViewGroup.LayoutParams(widthInPixels, heightInPixels));

                // 設置背景
                button.setBackground(drawable);

                button.setTag(row * 3 + col);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCellClicked((int) view.getTag());
                    }
                });
                gridLayout.addView(button);
            }
        }
    }

    private void onCellClicked(int position){
        int row = position / 3;
        int col = position % 3;
        Handler handler = new Handler(Looper.getMainLooper());
        if (board[row][col] == 0){
            board[row][col] = currentPlayer;
            updateButton(row, col);

            if (checkForWin(row, col)){
                showToast(currentPlayer + " wins!");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 在這裡執行需要延遲的代碼
                        resetGame();
                    }
                }, 3000); // 3000 毫秒即 3 秒
            }else if (isBoardFull()) {
                showToast("We are tied!");
                resetGame();
            }else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                // 執行AI的回合
                playAI();
            }
        }
    }
    //更新按鈕狀態
    private void updateButton(int row, int col) {

        //獲取按鈕的參考
        Button button = (Button) gridLayout.getChildAt(row * 3 + col);
        //這裡將其設置為當前玩家的記號，即 'X' 或 'O'。
        button.setText(String.valueOf(currentPlayer));


        //如果當前玩家是 'X'，設置文字顏色為紅色
        if (currentPlayer == 'X') {
            button.setTextColor(Color.RED);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        } else {
            //如果是 'O'，你可以設置其他顏色，或者使用預設顏色
            //這裡使用 Color.BLUE 作為 'O' 的預設顏色
            button.setTextColor(Color.BLUE);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        }

        //這是為了防止玩家在一步棋完成後繼續點擊相同的按鈕。
        button.setEnabled(false);
    }

    private boolean checkForWin(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer) {
            return true;
        }
        // Check column
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer) {
            return true;
        }
        // Check diagonals
        if (row == col && board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        if (row + col == 2 && board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer) {
            return true;
        }
        return false;
    }
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetGame() {
        gridLayout.removeAllViews();
        board = new char[3][3];
        initializeBoard();
        currentPlayer = 'X';
    }

    private void playAI() {
        // 簡單的AI邏輯：找到第一個空位置，填上'O'
        for (int row = 0; row < 3; row++){
            for (int col = 0; col < 3; col++){
                if (board[row][col] == 0){
                    board[row][col] = 'O';
                    updateButton(row, col);

                    if (checkForWin(row, col)){
                        showToast("AI wins!");
                        resetGame();
                    } else if (isBoardFull()) {
                        showToast("We are tied!");
                        resetGame();
                    }else{
                        currentPlayer = 'X';
                    }
                    return;
                }
            }
        }
    }
}
