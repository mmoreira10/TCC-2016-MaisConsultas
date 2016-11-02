package com.google.firebase.quickstart.database;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.Consultas;
import com.google.firebase.quickstart.database.models.Usuarios;

import java.util.HashMap;
import java.util.Map;

public class NovaConsultaUsuario extends BaseActivity {

    private static final String TAG = "NovaConsultaUsuario";

    private static final String REQUIRED = "É preciso informar este campo.";

    private DatabaseReference mDatabase;

    private AutoCompleteTextView especialidade;
    private String[] Especialidades;
    private EditText medico;
    private EditText local;
    private Button botaoAgendar;
    private Button botaoVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_nova_consulta_usuario);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        especialidade = (AutoCompleteTextView) findViewById(R.id.edtEspecialidade);
        Especialidades = getResources().getStringArray(R.array.especialidades);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Especialidades);
        especialidade.setAdapter(adapter);
        medico = (EditText) findViewById(R.id.edtMedico);
        local = (EditText) findViewById(R.id.edtLocal);

        botaoAgendar = (Button) findViewById(R.id.btnAgendar);

        botaoVoltar = (Button) findViewById(R.id.btnUsuario);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent voltarMenu = new Intent(NovaConsultaUsuario.this, TelaPrincipalUsuarioActivity.class);
                startActivity(voltarMenu);
                finish();
            }
        });

        botaoAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String edtEspecialidade = especialidade.getText().toString();
        final String edtMedico = medico.getText().toString();
        final String edtLocal = local.getText().toString();

        if (TextUtils.isEmpty(edtEspecialidade)) {
            especialidade.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(edtMedico)) {
            medico.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(edtLocal)) {
            local.setError(REQUIRED);
            return;
        }

        setEditingEnabled(false);
        Intent voltarMenu = new Intent(NovaConsultaUsuario.this, TelaPrincipalUsuarioActivity.class);
        startActivity(voltarMenu);
        finish();
        gerarNotificacao(null);

        final String userId = getUid();
        mDatabase.child("usuarios").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Usuarios user = dataSnapshot.getValue(Usuarios.class);

                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "Usuarios " + userId + " is unexpectedly null");
                            Toast.makeText(NovaConsultaUsuario.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            writeNewPost(userId, user.nome, edtEspecialidade, edtMedico, edtLocal);
                        }

                        setEditingEnabled(true);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                        setEditingEnabled(true);

                    }
                });
    }

    private void setEditingEnabled(boolean enabled) {
        especialidade.setEnabled(enabled);
        medico.setEnabled(enabled);
        local.setEnabled(enabled);
        if (enabled) {
            botaoAgendar.setVisibility(View.VISIBLE);
        } else {
            botaoAgendar.setVisibility(View.GONE);
        }
    }

    private void writeNewPost(String userId, String nome, String especialidade, String medico, String local) {

        String key = mDatabase.child("consultas").push().getKey();
        Consultas consultas = new Consultas(userId, nome, especialidade, medico, local);
        Map<String, Object> postValues = consultas.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/consultas/" + key, postValues);
        childUpdates.put("/usuarios-consultas/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    public void gerarNotificacao(View view){

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, MainActivityUsuario.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle("Consulta agendada com sucesso!");
        builder.setSmallIcon(R.drawable.ok);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.doctor));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String [] descs = new String[]{ especialidade.getText().toString(), medico.getText().toString(), local.getText().toString()};
        for(int i = 0; i < descs.length; i++){
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.ok, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, TelaPrincipalUsuarioActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onBackPressed() {

        startActivity(new Intent(this, TelaPrincipalUsuarioActivity.class));
        finish();
    }

}

