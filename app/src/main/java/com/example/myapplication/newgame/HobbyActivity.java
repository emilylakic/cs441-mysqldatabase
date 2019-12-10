package com.example.myapplication.newgame;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HobbyActivity extends AppCompatActivity {

    private TextView tvName, tvPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);

        tvName = findViewById(R.id.tvusername);
        tvPass = findViewById(R.id.tvpassword);

        tvName.setText(MainActivity.firstName);
        tvPass.setText(MainActivity.hobby);

    }
}