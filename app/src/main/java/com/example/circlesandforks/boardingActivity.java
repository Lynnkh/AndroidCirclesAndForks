package com.example.circlesandforks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class boardingActivity extends AppCompatActivity {

    Button friendButton,SingleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);


        SingleButton = findViewById(R.id.Singlebutton);
        friendButton = findViewById(R.id.friendbutton2);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Intent 遷移到另一個頁面
                Intent intent = new Intent(boardingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        SingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Intent 遷移到另一個頁面
                Intent intent = new Intent(boardingActivity.this,AIFightActivity.class);
                startActivity(intent);
            }
        });
    }


}