package com.google.firebase.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.quickstart.database.models.Usuarios;

public class NovoCadastroLoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "NovoCadastroLoginActivity";

    private EditText email, senha;
    private Button botaoCadastrar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_novo_cadastro_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        email = (EditText) findViewById(R.id.edtEmail);
        senha = (EditText) findViewById(R.id.edtSenha);
        botaoCadastrar = (Button) findViewById(R.id.btnAcessar);

        // Listeners
        botaoCadastrar.setOnClickListener(this);

    }

     private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String eemail = email.getText().toString();
        String ssenha = senha.getText().toString();

        mAuth.createUserWithEmailAndPassword(eemail, ssenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(NovoCadastroLoginActivity.this, "Ops! Algo deu errado, verifique sua conexão com a internet.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Novo usuario, cad
        writeNewUser(user.getUid(), username, user.getEmail());

        // Abre o Main
        startActivity(new Intent(NovoCadastroLoginActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("O e-mail é necessário!");
            result = false;
        } else {
            senha.setError(null);
        }

        if (TextUtils.isEmpty(senha.getText().toString())) {
            senha.setError("A senha é necessária!");
            result = false;
        } else {
            senha.setError(null);
        }

        return result;
    }

    private void writeNewUser(String userId, String nome, String email) {
        Usuarios usuarios = new Usuarios(nome, email);

        mDatabase.child("usuarios").child(userId).setValue(usuarios);
    }

    @Override
    public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btnAcessar) {
                signUp();
            }
        }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBackPressed()  {

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

