package com.google.firebase.quickstart.database.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MinhasConsultasUsuarioFragment extends ConsultasListUsuarioFragment {

    public MinhasConsultasUsuarioFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Todas as consultas usuários.
        return databaseReference.child("usuarios-consultas")
                .child(getUid());
    }
}