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

public class LoginUsuarioActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginUsuarioActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText email;
    private EditText senha;
    private Button botaoAcessar;
    private Button botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Views
        email = (EditText) findViewById(R.id.edtEmail);
        senha = (EditText) findViewById(R.id.edtSenha);
        botaoAcessar = (Button) findViewById(R.id.btnAcessar);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastrar);

        // Listeners
        botaoAcessar.setOnClickListener(this);
        botaoCadastrar.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String eemail = email.getText().toString();
        String ssenha = senha.getText().toString();

        mAuth.signInWithEmailAndPassword(eemail, ssenha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginUsuarioActivity.this, "Ops! Algo deu errado, verifique seu usuário e senha ou sua conexão com a internet.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
                            Toast.makeText(LoginUsuarioActivity.this, "Ops! Algo deu errado, verifique sua conexão com a internet.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        writeNewUser(user.getUid(), username, user.getEmail());

        startActivity(new Intent(LoginUsuarioActivity.this, TelaPrincipalUsuarioActivity.class));
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
            email.setError("É necessário informar o seu e-mail.");
            result = false;
        } else {
            senha.setError(null);
        }

        if (TextUtils.isEmpty(senha.getText().toString())) {
            senha.setError("É necessário informar a sua senha.");
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
            signIn();
        } else if (i == R.id.btnCadastrar) {
            signUp();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(LoginUsuarioActivity.this, EscolhaLoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void onBackPressed()  {

    }
}
