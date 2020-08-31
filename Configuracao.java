package com.e_bus.e_bus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Configuracao extends Fragment{

    Button buttonConfAltSenha,buttonConfAltUsuario,buttonConfSair;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracao, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tittle_configuracoes));

        buttonConfAltSenha=getView().findViewById(R.id.buttonConfAltSenha);
        buttonConfAltUsuario=getView().findViewById(R.id.buttonConfAltUsuario);
        buttonConfSair=getView().findViewById(R.id.buttonConfSair);

        buttonConfAltSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AlterarSenha.class);
                startActivity(intent);
            }
        });
        buttonConfAltUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AlterarUsuario.class);
                startActivity(intent);
            }
        });
        buttonConfSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance(getContext()).setLastLogin(false);
                Intent intent=new Intent(getContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
