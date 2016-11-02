package com.google.firebase.quickstart.database.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MinhasTopConsultasUsuarioFragment extends ConsultasListUsuarioFragment {

    public MinhasTopConsultasUsuarioFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // Início query + database.
        // Consultas que receberarm o favoritar
        String myUserId = getUid();
        Query myTopPostsQuery = databaseReference.child("usuarios-consultas").child(myUserId)
                .orderByChild("starCount");
        // Fim do query + database.

        return myTopPostsQuery;
    }
}
