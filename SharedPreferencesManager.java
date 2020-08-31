package com.e_bus.e_bus;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.DecimalFormat;

public class SharedPreferencesManager {
    private static SharedPreferencesManager myInstance;
    private static Context myContext;

    private static final String SHARED_PREF_NAME = "mysharedpreferences";
    private static final String SAVE_CPF = "savecpf";
    private static final String SAVE_SENHA = "savesenha";
    private static final String LAST_LOGIN = "lastlogin";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_CPF = "usercpf";
    private static final String KEY_USER_RG = "userrg";
    private static final String KEY_USER_NOME = "usernome";
    private static final String KEY_USER_DATA = "userdata";
    private static final String KEY_USER_TELEFONE = "usertelefone";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_SEXO = "usersexo";
    private static final String KEY_USER_MAE = "usermae";
    private static final String KEY_USER_SENHA = "usersenha";
    private static final String KEY_CARD_ID = "cardid";
    private static final String KEY_CARD_NUMBER = "cardnumber";
    private static final String KEY_CARD_BALANCE = "cardbalance";
    private static final String KEY_CARD_STATUS = "cardstatus";

    private SharedPreferencesManager(Context context) {
        myContext = context;

    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new SharedPreferencesManager(context);
        }
        return myInstance;
    }

    public boolean userLogin(int id, String cpf, String rg, String nome, String data, String telefone, String email, String sexo, String mae, String senha) {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_CPF, cpf);
        editor.putString(KEY_USER_RG, rg);
        editor.putString(KEY_USER_NOME, nome);
        editor.putString(KEY_USER_DATA, data);
        editor.putString(KEY_USER_TELEFONE, telefone);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_SEXO, sexo);
        editor.putString(KEY_USER_MAE, mae);
        editor.putString(KEY_USER_SENHA, senha);

        editor.apply();
        return true;
    }

    public boolean setSenha(String senha) {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_SENHA, senha);

        editor.apply();
        return true;
    }

    public boolean setCartao(int id, String numero, double saldo, String status) {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_CARD_ID, id);
        editor.putString(KEY_CARD_NUMBER, numero);
        editor.putFloat(KEY_CARD_BALANCE, (float) saldo);
        editor.putString(KEY_CARD_STATUS, status);

        editor.apply();
        return true;
    }

    public int getCartaoId() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_CARD_ID, 0);
    }

    public String getCartaoNumber() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CARD_NUMBER, null);
    }

    public double getCartaoSaldo() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDecimalForm.format(sharedPreferences.getFloat(KEY_CARD_BALANCE, 0)));
    }

    public int getId() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0);
    }

    public String getCPF() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_CPF, null);
    }

    public String getRG() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_RG, null);
    }

    public String getNome() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NOME, null);
    }

    public String getData() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_DATA, null);
    }

    public String getTelefone() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TELEFONE, null);
    }

    public String getEmail() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getSexo() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_SEXO, null);
    }

    public String getMae() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_MAE, null);
    }

    public String getSenha() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_SENHA, null);
    }

    public boolean setLastLogin(boolean lastLogin) {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(LAST_LOGIN, lastLogin);

        editor.apply();
        return true;
    }

    public boolean getLastLogin() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LAST_LOGIN, false);
    }

    public boolean saveLogin(boolean cpf, boolean senha) {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SAVE_CPF, cpf);
        editor.putBoolean(SAVE_SENHA, senha);

        editor.apply();
        return true;
    }

    public boolean isCPFChecked() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE_CPF, false);
    }

    public boolean isSenhaChecked() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE_SENHA, false);
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = myContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0) > 0;
    }
}
