package com.google.firebase.quickstart.database;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class BuscasActivity extends AppCompatActivity {

    private ImageView ivAtivarBuscas;
    private ImageView ivBuscas;
    private EditText edtBuscas;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscas);

        edtBuscas = (EditText) findViewById(R.id.edtBuscas);

        ivAtivarBuscas = (ImageView) findViewById(R.id.ivAtivaBuscas);
        ivAtivarBuscas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtBuscas.setVisibility(View.VISIBLE);
                ivBuscas.setVisibility(View.VISIBLE);
                ivAtivarBuscas.setVisibility(View.INVISIBLE);
                edtBuscas.requestFocus();

            }
        });

        ivBuscas = (ImageView) findViewById(R.id.ivBuscas);
        ivBuscas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuscasActivity.this, "Função de buscas inativas até o momento.", Toast.LENGTH_LONG).show();
            }

        });


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void onBackPressed()  {

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}