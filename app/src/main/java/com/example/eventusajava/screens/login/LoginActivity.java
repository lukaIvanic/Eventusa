package com.example.eventusajava.screens.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eventusajava.R;
import com.example.eventusajava.screens.eventsList.EventsListActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout usernameLayout;
    TextInputEditText usernameEditText;

    TextInputLayout passwordLayout;
    TextInputEditText passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        usernameLayout = findViewById(R.id.usernameInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);

        passwordLayout = findViewById(R.id.passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextScreen();
            }
        });
    }

    private void gotoNextScreen() {
        Intent intent = new Intent(LoginActivity.this, EventsListActivity.class);
        startActivity(intent);
    }


}