package com.google.firebase.quickstart.database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DefinicoesUsuarioActivity extends AppCompatActivity {


    private AlertDialog alerta;//atributo da classe.
    private Button btnSair, btnVoltarMenu, btnUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes_usuario);

        btnUsuarios = (Button) findViewById(R.id.btnUsuario);
        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarEscolha = new Intent(DefinicoesUsuarioActivity.this, EscolhaLoginActivity.class);
                startActivity(voltarEscolha);
                finish();

            }
        });

        btnSair = (Button) findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DefinicoesUsuarioActivity.this);//Cria o gerador do AlertDialog
                builder.setTitle("Atenção!");//define o titulo
                builder.setMessage("Deseja mesmo sair da aplicação?");//define a mensagem
                //define um botão como positivo
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(DefinicoesUsuarioActivity.this, "Até logo...", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent voltarMenu = new Intent(DefinicoesUsuarioActivity.this, TelaPrincipalUsuarioActivity.class);
                        startActivity(voltarMenu);
                        Toast.makeText(DefinicoesUsuarioActivity.this, "Obrigado por continuar conosco.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                alerta = builder.create();//cria o AlertDialog
                alerta.show();//Exibe
            }

        });

        btnVoltarMenu = (Button) findViewById(R.id.btnVoltar);
        btnVoltarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarMenu = new Intent(DefinicoesUsuarioActivity.this, TelaPrincipalUsuarioActivity.class);
                startActivity(voltarMenu);
                finish();
            }
        });
    }
}
