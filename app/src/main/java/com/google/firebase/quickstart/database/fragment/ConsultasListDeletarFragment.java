package com.google.firebase.quickstart.database.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.TelaPrincipalUsuarioActivity;
import com.google.firebase.quickstart.database.models.Consultas;
import com.google.firebase.quickstart.database.viewholder.ConsultasViewHolder;

public abstract class ConsultasListDeletarFragment extends Fragment {

    private static final String TAG = "ConsultasListDeletarFragment";

    // Atribuição da classe Alert.
    private AlertDialog alerta;

    // Inicia o BD.
    private DatabaseReference mDatabase;
    // Finaliza a declaração do BD.

    private FirebaseRecyclerAdapter<Consultas, ConsultasViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public ConsultasListDeletarFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_todas_consultas_usuario, container, false);

        // Inicia o BD.
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Finaliza a declaração do BD.

        mRecycler = (RecyclerView) rootView.findViewById(R.id.consultas_list_usuario);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Seta o uso do layout Manager.
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Seta o Firebase para usar a Query que referencia o BD.
        final Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Consultas, ConsultasViewHolder>(Consultas.class, R.layout.item_consulta_usuario,
                ConsultasViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ConsultasViewHolder viewHolder, final Consultas model, final int position) {

                final DatabaseReference postRef = getRef(position);

                // Ação quando clicar no RecyclerView.
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//Cria o gerador do AlertDialog
                            builder.setTitle("Atenção!");// Define o título
                            builder.setMessage("Deseja mesmo excluir esta consulta?");// Define a mensagem
                            // Define um botão como positivo
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    postRef.removeValue();
                                    mDatabase.child("usuarios-consultas").child(model.uid).child(postRef.getKey()).removeValue();
                                    Intent voltarMenu = new Intent(getActivity(), TelaPrincipalUsuarioActivity.class);
                                    startActivity(voltarMenu);
                                    Toast.makeText(getActivity(), "Consulta excluída com sucesso.", Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                }
                            });
                            // Define um botão como negativo.
                            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent voltarMenu = new Intent(getActivity(), TelaPrincipalUsuarioActivity.class);
                                    startActivity(voltarMenu);
                                    Toast.makeText(getActivity(), "Operação cancelada.", Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                }
                            });
                            alerta = builder.create(); // Cria o AlertDialog
                            alerta.show();// Exibe o alert
                        }

                    });

                // Ação quando o usuário clicar na estrela para favoritar.
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#FFBB33"));
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Ação ao salvar a Consulta.
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Salca no banco as consultas.
                        DatabaseReference globalPostRef = mDatabase.child("consultas").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("usuarios-consultas").child(model.uid).child(postRef.getKey());

                        // Inicia as transações.
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    // Início do save.
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Consultas p = mutableData.getValue(Consultas.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Tira o favoritar em -1, ou seja, estrela comum.
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());

                } else {
                    // Coloca o favoritar em +1, ou seja, estrela preenchida.
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Seta o valor da transação no BD.
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Se até aqui tudo ocorrer sem erros, ok.
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // Final de postagem da Consulta.

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

}
