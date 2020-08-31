package com.e_bus.e_bus;

import android.content.Context;

public class Validate{
    public static Response cpfToDB(String cpf,Context ctx) {//fazer o teste para ver se o cpf eh valido
        cpf = cpf.trim();
        if (cpf.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_cpf_vazio));
        } else if (cpf.length() != 11) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_cpf_invalido_1));
        }
        int[] cpfInt = new int[9];
        int dv1, dv2, soma1 = 0, soma2 = 0;

        for (int i = 0; i < 9; i++) {
            cpfInt[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));
        }
        for (int i = 0; i < 9; i++) {
            soma1 += cpfInt[i] * (10 - i);
        }
        if (soma1 % 11 < 2) {
            dv1 = 0;
        } else {
            dv1 = 11 - (soma1 % 11);
        }
        for (int i = 0; i < 10; i++) {
            if (i < 9) {
                soma2 += cpfInt[i] * (11 - i);
            } else {
                soma2 += dv1 * (11 - i);
            }
        }
        if (soma2 % 11 < 2) {
            dv2 = 0;
        } else {
            dv2 = 11 - (soma2 % 11);
        }
        boolean res = dv1 == Integer.parseInt(String.valueOf(cpf.charAt(9))) && dv2 == Integer.parseInt(String.valueOf(cpf.charAt(10)));
        return new Response(res ? Response.OK : Response.INVALIDO, res ? cpf : ctx.getString(R.string.validate_cpf_invalido_2));
    }

    public static Response rgToDB(String rg,Context ctx) {
        rg = rg.trim();
        if (rg.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_rg_vazio));
        } else if (rg.length() != 9) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_rg_invalido));
        }
        return new Response(Response.OK, rg);
    }

    public static Response nomeToDB(String nome,Context ctx) {
        nome = nome.trim();
        if (nome.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_nome_vazio));
        }
        if (nome.length() > 80) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_nome_invalido));
        }
        return new Response(Response.OK, nome);
    }

    public static Response nomeMaeToDB(String nome,Context ctx) {
        nome = nome.trim();
        if (nome.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_mae_vazio));
        }
        if (nome.length() > 80) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_mae_invalido));
        }
        return new Response(Response.OK, nome);
    }

    public static Response dataToDB(String data,Context ctx) {
        if (data.trim().isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_data_vazio));
        }
        String[] date = data.split("/");
        return new Response(Response.OK, date[2] + "-" + date[1] + "-" + date[0]);
    }

    public static Response telefoneToDB(String telefone,Context ctx) {
        telefone = telefone.trim();
        if (telefone.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_telefone_vazio));
        } else if (telefone.length() != 11) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_telefone_invalido));
        }
        return new Response(Response.OK, telefone);
    }

    public static Response emailToDB(String email,Context ctx) {
        email = email.trim();
        if (email.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_email_vazio));
        }
        if (email.length() > 50) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_email_invalido));
        }
        return new Response(Response.OK, email);
    }

    public static Response senhaToDB(String senha, String rSenha,Context ctx) {
        senha = senha.trim();
        rSenha = rSenha.trim();
        if (senha.isEmpty()) {
            return new Response(Response.VAZIO, ctx.getString(R.string.validate_senha_vazio));
        } else if (senha.length() < 6 || senha.length() > 32) {
            return new Response(Response.INVALIDO, ctx.getString(R.string.validate_senha_invalido_1));
        } else {
            if (senha.equals(rSenha)) {
                return new Response(Response.OK, senha);
            } else {
                return new Response(Response.INVALIDO, ctx.getString(R.string.validate_senha_invalido_2));
            }
        }
    }

    public static Response dataHoraFromDB(String dataHora) {
        String[] dateTime = dataHora.split(" ");
        String[] date = dateTime[0].split("-");

        return new Response(Response.OK, date[2] + "/" + date[1] + "/" + date[0] + " " + dateTime[1]);
    }

    public static class Response {
        static final int OK = 0, VAZIO = 1, INVALIDO = 2;
        int erro;
        String formatedString;

        public Response(int erro, String formatedString) {
            this.erro = erro;
            this.formatedString = formatedString;
        }
    }
}