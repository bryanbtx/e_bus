package com.e_bus.e_bus;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackGroundWorker {
    private static BackGroundWorker myInstance;
    private static Context myCtx;

    private BackGroundWorker(Context context) {
        myCtx = context;
    }
    public static synchronized BackGroundWorker getInstance(Context context) {
        if (myInstance == null) {
            myInstance = new BackGroundWorker(context);
        }
        return myInstance;
    }

    public void executeSynchronization() {
        List<Pendente> pendentes = new LocalDataBase(myCtx).select();
        if (pendentes.size() > 0) {
            Pendente p = pendentes.get(0);
            new LocalDataBase(myCtx).delete(String.valueOf(p.ID));
            insertSaldo(String.valueOf(p.ID), String.valueOf(p.CARD_NUMBER), String.valueOf(p.VALUE), p.DATE);
        }
    }
    private void insertSaldo(final String id,final String numero_cartao,final String valor, final String data) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_insertSaldo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                executeSynchronization();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LocalDataBase(myCtx).insertDate(numero_cartao,valor,data);
                executeSynchronization();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("valor", valor);
                params.put("data",data);
                params.put("numero_cartao", numero_cartao);
                return params;
            }
        };
        VolleySingleton.getInstance(myCtx).addToRequestQueue(stringRequest);
    }
}
