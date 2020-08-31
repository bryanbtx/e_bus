package com.e_bus.e_bus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class HistUtilizacao extends Fragment{
    JSONArray valorJson;
    JSONArray busJson;
    JSONArray dataJson;
    double[] valor;
    String[] bus;
    String[] data;
    Button buttonUSincronizar;
    ProgressBar progressBarUSincronizar;
    ListView listViewUtilizacao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_histutilizacao, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.tittle_historico_de_utilizacao));

        progressBarUSincronizar = getView().findViewById(R.id.progressBarUSincronizar);
        buttonUSincronizar = getView().findViewById(R.id.buttonUSincronizar);
        listViewUtilizacao=getView().findViewById(R.id.listViewUtilizacao);

        buttonUSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUtilizacao(String.valueOf(SharedPreferencesManager.getInstance(getContext()).getCartaoNumber()));
            }
        });
        listViewUtilizacao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new MessageBox().show(getActivity().getFragmentManager(),getString(R.string.tittle_mensagen),getString(R.string.msg_valor)+valor[position]+getString(R.string.msg_onibus)+bus[position]+getString(R.string.msg_data)+data[position]);
            }
        });
        selectUtilizacao(String.valueOf(SharedPreferencesManager.getInstance(getContext()).getCartaoNumber()));
    }

    private void selectUtilizacao(final String numero_cartao) {
        progressBarUSincronizar.setVisibility(View.VISIBLE);
        buttonUSincronizar.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_selectUtilizacao, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        valorJson = jsonObject.getJSONArray("valor");
                        busJson = jsonObject.getJSONArray("bus");
                        dataJson = jsonObject.getJSONArray("data");
                        valor=new double[valorJson.length()];
                        bus=new String[valorJson.length()];
                        data=new String[valorJson.length()];
                        for(int i=0;i<valorJson.length();i++){
                            valor[i]=(valorJson.getDouble(i));
                            bus[i]=(busJson.getString(i));
                            data[i]=(Validate.dataHoraFromDB(dataJson.getString(i)).formatedString);
                        }
                        ListView listView = getView().findViewById(R.id.listViewUtilizacao);
                        CustomAdapter customAdapter = new CustomAdapter();
                        listView.setAdapter(customAdapter);
                    }
                } catch (JSONException e) {
                    Log.wtf("!!!", response);
                    new MessageBox().show(getActivity().getFragmentManager(),e,getString(R.string.tittle_error),e.getLocalizedMessage());
                }
                progressBarUSincronizar.setVisibility(View.INVISIBLE);
                buttonUSincronizar.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarUSincronizar.setVisibility(View.INVISIBLE);
                buttonUSincronizar.setVisibility(View.VISIBLE);
                new MessageBox().show(getActivity().getFragmentManager(), error, getString(R.string.tittle_error), getString(R.string.volley_error));
                Log.wtf("!!!",error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("numero_cartao", numero_cartao);
                return params;
            }
        };
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return valor.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.list_utilizacao, null);

            TextView text_valor = view.findViewById(R.id.textViewUListValor);
            TextView text_bus = view.findViewById(R.id.textViewUListBus);
            TextView text_data = view.findViewById(R.id.textViewUListData);
            text_valor.setText(getString(R.string.string_moeda)+String.valueOf(valor[i]));
            text_bus.setText(bus[i]);
            text_data.setText(data[i]);
            return view;
        }
    }
}
