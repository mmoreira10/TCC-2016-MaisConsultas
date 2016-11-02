package com.google.firebase.quickstart.database.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class RecentesConsultasEditarFragment extends ConsultasListEditarFragment {

    public RecentesConsultasEditarFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {

        // Início recentes consultas
        // Lista as 100 consyltas
        Query recentPostsQuery = databaseReference.child("consultas")
                .limitToFirst(100);
        // Fim da query das recentes consultas.

        return recentPostsQuery;
    }
}
