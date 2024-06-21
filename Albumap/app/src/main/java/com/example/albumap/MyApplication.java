package com.example.albumap;

import android.app.Application;

import com.example.albumap.entities.Usuario;

public class MyApplication extends Application {
    private Usuario usuarioEmSessao;

    @Override
    public void onCreate(){
        super.onCreate();
        usuarioEmSessao = null;
    }

    public Usuario getUsuarioEmSessao() {
        return usuarioEmSessao;
    }

    public void setUsuarioEmSessao(Usuario usuarioEmSessao) {
        this.usuarioEmSessao = usuarioEmSessao;
    }
}
