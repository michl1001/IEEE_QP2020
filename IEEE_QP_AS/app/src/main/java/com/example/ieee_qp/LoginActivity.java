package com.example.ieee_qp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button = (Button) findViewById(R.id.loginBtn);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), DetailsActivity.class);
                startActivity(login);
                finish();
            }
        });
    }
}
