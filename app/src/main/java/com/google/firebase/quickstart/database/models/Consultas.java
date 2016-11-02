package com.google.firebase.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

// Início da classe.
@IgnoreExtraProperties
public class Consultas {

    public String uid;

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade() {
        this.especialidade = especialidade;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String usuario;
    public String especialidade;
    public String medico;
    public String local;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Consultas() {
    }

    public Consultas(String uid, String usuario, String especialidade, String medico, String local) {
        this.uid = uid;
        this.usuario = usuario;
        this.especialidade = especialidade;
        this.medico = medico;
        this.local = local;
    }

    // Inicio consultas e o mapa.
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("usuario", usuario);
        result.put("especialidade", especialidade);
        result.put("medico", medico);
        result.put("local", local);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
    // Fim consultas e o mapa.

}
// Final da classe.
