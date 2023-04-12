package com.example.rescuesupport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Switch_shelter_page extends AppCompatActivity {
    private Button btn_switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_shelter);

        btn_switch1 = findViewById(R.id.btn_switch1);
        btn_switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Switch_shelter_page.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}