package com.cs349.a4;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button about, reset;
    Drawing d;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        d = findViewById(R.id.draw);

        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ragdoll Ryuk");
        dialog.setMessage("Chenyi Yang 20660449");
        dialog.setCancelable(true);


        about = findViewById(R.id.about);
        reset = findViewById(R.id.reset);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create().show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d = findViewById(R.id.draw);
                d.reset();
            }
        });
    }
}
