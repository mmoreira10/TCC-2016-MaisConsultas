package com.google.firebase.quickstart.database.models;

import com.google.firebase.database.IgnoreExtraProperties;

// Inicio da classe dos Usuarios

@IgnoreExtraProperties
public class Usuarios {

    public String nome;
    public String email;

    public Usuarios() {
    }

    public Usuarios(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

}
// Inicio da classe dos Usuarios
