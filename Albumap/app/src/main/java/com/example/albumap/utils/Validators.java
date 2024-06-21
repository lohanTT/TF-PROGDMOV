package com.example.albumap.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

    public static boolean isEmail(String email){
        if (email.equals("")){
            return  false;
        }

        String padraoEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(padraoEmail);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()){
            return false;
        }

        return true;
    }

    public static boolean isNome(String nome){
        return (!nome.isEmpty());
    }

    public static boolean isSenha(String senha){
        String padraoSenha = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!.%*?&]{8,}$";
        Pattern pattern = Pattern.compile(padraoSenha);
        Matcher matcher = pattern.matcher(senha);

        if (!matcher.matches()){
            System.out.println(senha);
            return false;
        }

        return true;
    }

    public static boolean isConfirmacaoSenha(String senha, String confirmacao){
        if(confirmacao.isEmpty())
            return false;

        return confirmacao.equals(senha);
    }
}
