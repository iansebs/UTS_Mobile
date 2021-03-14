package com.example.uts_31862_sebastian2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {
    EditText username, pass;
    Button masuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) this.findViewById(R.id.username);
        pass = (EditText) this.findViewById(R.id.pass);
        masuk = findViewById(R.id.masuk);

        masuk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (username.getText().toString().matches("uasmobile") && pass.getText().toString().matches("uasmobilegenap")){
                    logmasuk();
                }
            }
        });
    }
    private void logmasuk(){
        Intent intent = new Intent(login.this, list.class);
        startActivity(intent);
    }
}