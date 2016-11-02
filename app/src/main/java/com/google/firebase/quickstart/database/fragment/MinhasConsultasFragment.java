package com.google.firebase.quickstart.database.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MinhasConsultasFragment extends ConsultasListFragment {

    public MinhasConsultasFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Todas as consultas
        return databaseReference.child("usuarios-consultas")
                .child(getUid());
    }

}
