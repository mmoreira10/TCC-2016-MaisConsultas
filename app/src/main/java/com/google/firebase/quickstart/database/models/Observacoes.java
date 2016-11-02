package com.google.firebase.quickstart.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

// Inicio comentarios

@IgnoreExtraProperties
public class Observacoes {

    public String uid;
    public String usuario;
    public String texto;

    public Observacoes() {
    }

    public Observacoes(String uid, String usuario, String texto) {
        this.uid = uid;
        this.usuario = usuario;
        this.texto = texto;
    }

}
// // Fim comentarios

