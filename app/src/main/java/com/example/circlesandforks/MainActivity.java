package com.example.circlesandforks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private char currentPlayer = 'X';

    //初始化遊戲板：board 是一個二維字符數組，
    // 用於表示遊戲棋盤。每個單元格可以是 'X'、'O
    private char[][] board = new char[3][3];

    private GridLayout gridLayout;
    Button ExitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExitBtn = findViewById(R.id.Exit);
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Intent 遷移到另一個頁面
                Intent intent = new Intent(MainActivity.this,boardingActivity.class);
                startActivity(intent);
            }
        });

        gridLayout = findViewById(R.id.gridLayout);

        initializeBoard();
    }

    //initializeBoard 方法創建一個 3x3 的 GridLayout，每個單元格都是一個 Button。
    private void initializeBoard() {

        // 創建外框 Drawable
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);  // 設置形狀為矩形
        drawable.setCornerRadius(8);  // 設置圓角半徑
        drawable.setStroke(2, Color.BLACK);  // 設置外框寬度和顏色

        //設置Button的寬度和高度，這裡設置為 169dp 寬，98dp 高
        int widthInDp = 98;
        int heightInDp = 98;

        // 遍歷 3x3 的矩陣
        // 在這裡創建按鈕，設置標記，
        // 添加點擊監聽器，然後將其添加到 GridLayout 中。
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
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

                //使用了數學計算，將行和列的索引轉換為一維位置。
                button.setTag(row * 3 + col);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 點擊每個按鈕會調用 onCellClicked 方法。
                        onCellClicked((int) view.getTag());
                    }
                });
                gridLayout.addView(button);
            }
        }
    }

    //處理按鈕點擊事件
    //它檢查所點擊的單元格是否是空的，如果是，
    // 則將當前玩家的符號（'X' 或 'O'）放入單元格中
    private void onCellClicked(int position) {
        //用於將一維位置（position）
        //轉換為二維網格的行（row）和列（col）索引。
        int row = position / 3;
        int col = position % 3;
        Handler handler = new Handler(Looper.getMainLooper());
        //0 表示該格子是空的。
        // 如果格子是空的，則填入currentPlayer(O，X)
        if (board[row][col] == 0) {
            board[row][col] = currentPlayer;
            updateButton(row, col);

            //檢查勝利條件
            if (checkForWin(row, col)) {
                showToast(currentPlayer + " wins!");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 在這裡執行需要延遲的代碼
                        resetGame();
                    }
                }, 3000); // 3000 毫秒即 3 秒

            //如果棋盤滿了，宣布平局
            } else if (isBoardFull()) {
                showToast("We are tied!");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 在這裡執行需要延遲的代碼
                        resetGame();
                    }
                }, 3000); // 3000 毫秒即 3 秒
            } else {
                //切換玩家
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
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

    //檢查是否贏得遊戲
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

    //檢查是否平局
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
    //顯示結果
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //重置遊戲
    private void resetGame(){
        // 移除 GridLayout 中的所有子視圖（按鈕）
        gridLayout.removeAllViews();
        board = new char[3][3];
        // 重新初始化遊戲板
        initializeBoard();

        currentPlayer = 'X';
    }


}