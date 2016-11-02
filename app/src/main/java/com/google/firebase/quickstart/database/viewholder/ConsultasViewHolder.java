package com.google.firebase.quickstart.database.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.models.Consultas;

public class ConsultasViewHolder extends RecyclerView.ViewHolder {

    public TextView usuario;

    public TextView getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(TextView especialidade) {
        this.especialidade = especialidade;
    }

    public TextView getMedico() {
        return medico;
    }

    public void setMedico(TextView medico) {
        this.medico = medico;
    }

    public TextView getLocal() {
        return local;
    }

    public void setLocal(TextView local) {
        this.local = local;
    }

    public TextView especialidade;
    public TextView medico;
    public TextView local;
    public ImageView starView;
    public TextView numStarsView;

    public ConsultasViewHolder(View itemView) {

        super(itemView);
        usuario = (TextView) itemView.findViewById(R.id.consulta_usuario);
        especialidade = (TextView) itemView.findViewById(R.id.tvEspecialidade);
        medico = (TextView) itemView.findViewById(R.id.tvMedico);
        local = (TextView) itemView.findViewById(R.id.tvLocal);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
    }

    public void bindToPost(Consultas consultas, View.OnClickListener starClickListener) {
        usuario.setText(consultas.usuario);
        especialidade.setText(consultas.especialidade);
        medico.setText(consultas.medico);
        local.setText(consultas.local);
        numStarsView.setText(String.valueOf(consultas.starCount));
        starView.setOnClickListener(starClickListener);
    }
}
