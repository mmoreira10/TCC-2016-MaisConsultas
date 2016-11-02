package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class EscolhaLoginActivity extends AppCompatActivity {

    private Button btnUsuario, btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_escolha_login);

        btnUsuario = (Button) findViewById(R.id.btnUsuario);
        btnAdmin = (Button) findViewById(R.id.btnAdministrador);

        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginUsuario = new Intent(EscolhaLoginActivity.this, LoginUsuarioActivity.class);
                startActivity(loginUsuario);
                finish();

            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginAdministrador = new Intent(EscolhaLoginActivity.this, LoginActivity.class);
                startActivity(loginAdministrador);
                finish();

            }
        });
    }

    public void onBackPressed()  {

    }
}