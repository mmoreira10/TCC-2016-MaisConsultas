package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaPrincipalUsuarioActivity extends AppCompatActivity {

    private Button btnDefinicoes, btnCadastrar, btnListar, btnEditar, btnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_usuario);

        btnExcluir = (Button) findViewById(R.id.btnExcluirConsultas);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent deletar = new Intent(TelaPrincipalUsuarioActivity.this, DeletarConsultasActivity.class);
                startActivity(deletar);
                finish();
            }
        });

        btnEditar = (Button) findViewById(R.id.btnEditarConsultas);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editar = new Intent(TelaPrincipalUsuarioActivity.this, EditarConsultasActivity.class);
                startActivity(editar);
                finish();
            }
        });

        btnListar = (Button) findViewById(R.id.btnListarConsultas);
        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listar = new Intent(TelaPrincipalUsuarioActivity.this, MainActivityUsuario.class);
                startActivity(listar);
                finish();
            }
        });

        btnDefinicoes = (Button) findViewById(R.id.btnDefinicoes);
        btnDefinicoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent definicoes = new Intent(TelaPrincipalUsuarioActivity.this, DefinicoesUsuarioActivity.class);
                startActivity(definicoes);
                finish();

            }
        });

        btnCadastrar = (Button) findViewById(R.id.btnCadastrarConsulta);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cadastrar = new Intent(TelaPrincipalUsuarioActivity.this, NovaConsultaUsuario.class);
                startActivity(cadastrar);
                finish();
            }
        });
    }

    public void onBackPressed()  {

    }
}
