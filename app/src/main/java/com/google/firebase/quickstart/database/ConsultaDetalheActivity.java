package com.google.firebase.quickstart.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.Consultas;
import com.google.firebase.quickstart.database.models.Observacoes;
import com.google.firebase.quickstart.database.models.Usuarios;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDetalheActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ConsultaDetalheActivity";

    public static final String EXTRA_POST_KEY = "consulta_chave";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private StorageReference storageReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private CommentAdapter mAdapter;
    private TextView tvUsuario;
    private TextView tvEspecialidade;

    private TextView tvMedico;
    private TextView tvLocal;
    private EditText mCommentField;
    private Button mCommentButton;
    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_consulta_detalhe);

        // Pega post das consultas
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Inicializa Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("consultas").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("consultas-obs").child(mPostKey);

        // Inicializa Views
        tvUsuario = (TextView) findViewById(R.id.consulta_usuario);
        tvEspecialidade = (TextView) findViewById(R.id.tvEspecialidade);
        tvMedico = (TextView) findViewById(R.id.tvMedico);
        tvLocal = (TextView) findViewById(R.id.tvLocal);
        mCommentField = (EditText) findViewById(R.id.edtObs);
        mCommentButton = (Button) findViewById(R.id.btnObs);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_obs);

        mCommentButton.setOnClickListener(this);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

        storageReference = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onStart() {
        super.onStart();

        // Adiciona itens de Obs a consulta.
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Pega o valor do objeto.
                Consultas consultas = dataSnapshot.getValue(Consultas.class);

                tvUsuario.setText(consultas.usuario);
                tvEspecialidade.setText(consultas.especialidade);
                tvMedico.setText(consultas.medico);
                tvLocal.setText(consultas.local);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Pega alguma falha ao postar obs.
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                Toast.makeText(ConsultaDetalheActivity.this, "Ops! Falha ao carregar suas consultas.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        mPostReference.addValueEventListener(postListener);
        mPostListener = postListener;
        mAdapter = new CommentAdapter(this, mCommentsReference);
        mCommentsRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        mAdapter.cleanupListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnObs) {
            postComment();
        }
    }

    private void postComment() {

        if (!validateForm()) {
            return;
        } else {

            final String uid = getUid();
            FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuarios user = dataSnapshot.getValue(Usuarios.class);
                            String authorName = user.nome;

                            String commentText = mCommentField.getText().toString();
                            Observacoes comment = new Observacoes(uid, authorName, commentText);

                            mCommentsReference.push().setValue(comment);

                            mCommentField.setText(null);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.obs_usuario);
            bodyView = (TextView) itemView.findViewById(R.id.obs_conteudo);
        }
    }

    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Observacoes> mComments = new ArrayList<>();

        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    Observacoes comment = dataSnapshot.getValue(Observacoes.class);


                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    Observacoes newComment = dataSnapshot.getValue(Observacoes.class);
                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {

                        mComments.set(commentIndex, newComment);

                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    String commentKey = dataSnapshot.getKey();

                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {

                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    Observacoes movedComment = dataSnapshot.getValue(Observacoes.class);
                    String commentKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Ops! Falha ao carregar suas observações.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);

            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_obs, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Observacoes comment = mComments.get(position);
            holder.authorView.setText(comment.usuario);
            holder.bodyView.setText(comment.texto);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

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

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mCommentField.getText().toString())) {
            mCommentField.setError("A observação é necessária!");
            result = false;
        } else {
            mCommentField.setError(null);
        }

        return result;
    }

}
