package com.example.appmusic;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.adapter.registrosImagenUrlAdapter;
import com.example.appmusic.entidades.Artista;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GruposMusicales extends Fragment implements SearchView.OnQueryTextListener, Response.Listener<JSONObject>, Response.ErrorListener{
    private SharedPreferences preferences;

    RecyclerView recyclerTodosArtista;
    ArrayList<Artista> listaArtista;
    ViewFlipper v_flipper;
    ProgressDialog dialogo;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SearchView Buscar;
    registrosImagenUrlAdapter adapter;
    String idUsuarioRecibido;
    ProgressDialog progressDialog;

    String HttpURI = "https://precatory-levels.000webhostapp.com/UsuarioLogeado.php";
    Bundle bundle = new Bundle();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GruposMusicales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TercerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GruposMusicales newInstance(String param1, String param2) {
        GruposMusicales fragment = new GruposMusicales();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gruposmusicales, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Buscar = (SearchView) view.findViewById(R.id.idBuscador);
        ImageButton regresar = view.findViewById(R.id.grupomusical);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreUsuarioLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idUsuarioLogeado);
        //Consulta del usuario logeado

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Verificando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String serverResponse) {
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(serverResponse);

                            String UsuarioLogeado = obj.getString("Usuario");


                            NombreUsuarioLogeado.setText(UsuarioLogeado);



                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext().getApplicationContext(),"No se encuntra conectado a internet",
                        Toast.LENGTH_LONG).show();
            }

        }){
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idUsuario",String.valueOf(idUsuarioRecibido));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
        //Fin de la consulta del usuario logeado



        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt("idUsuario",Integer.parseInt(idUsuarioRecibido));
                Navigation.findNavController(view).navigate(R.id.action_gruposMusicales_to_bienvenida2,bundle);
            }
        });

        Bundle objetoPersona = getArguments();

        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){

            idUsuarioRecibido = String.valueOf(getArguments().getSerializable("idUsuario"));
        }

        listaArtista=new ArrayList<>();

        recyclerTodosArtista = (RecyclerView) view.findViewById(R.id.idRecyclerTodosArtista);
        recyclerTodosArtista.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerTodosArtista.setHasFixedSize(true);

        request= Volley.newRequestQueue(getContext());

        cargarWebService();

        Buscar.setOnQueryTextListener(this);

        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarGrupos);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_grupos);




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) { drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id){

                    case R.id.nav_home:
                        bundle.putInt("idUsuario",Integer.parseInt(idUsuarioRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_gruposMusicales_to_bienvenida2,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.action_gruposMusicales_to_datosArtistas,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.gruposMusicales,bundle);
                        break;
                    case R.id.nav_cerrarsesion:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_gruposMusicales_to_mainFragment);
                        break;

                }
                return true;
            }
        });


//Termino le menu lateral

    }
    private void cargarWebService() {
        dialogo=new ProgressDialog(getContext());
        dialogo.setMessage("Consultando Imagenes");
        dialogo.show();

        String url="https://precatory-levels.000webhostapp.com/ConsultarArtistas.php";
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
        //VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(), "No se puede conectar al servidor", Toast.LENGTH_LONG).show();
        System.out.println();
        dialogo.hide();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Artista artista=null;

        JSONArray json=response.optJSONArray("nuevoartista");

        try {

            for (int i=0;i<json.length();i++){
                artista= new Artista();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                artista.setIdArtista(jsonObject.optInt("idArtista"));
                artista.setNombreGrupo((jsonObject.optString("nombreGrupo")));
                artista.setDescripcion(jsonObject.optString("descripcion"));
                artista.setGeneroMusical(jsonObject.optString("generoMusical"));
                artista.setCorreoGrupo(jsonObject.optString("correo"));
                artista.setRutaLogo(jsonObject.optString("rutaLogo"));
                listaArtista.add(artista);
            }
            dialogo.hide();
             adapter = new registrosImagenUrlAdapter(listaArtista, getContext());
            recyclerTodosArtista.setAdapter(adapter);

            adapter.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bundle.putSerializable("IdArtista", listaArtista.get(recyclerTodosArtista.getChildAdapterPosition(v)));
                    bundle.putSerializable("idUsuario",idUsuarioRecibido);
                    Navigation.findNavController(v).navigate(R.id.action_gruposMusicales_to_perfilContratacion,bundle);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor", Toast.LENGTH_LONG).show();
            dialogo.hide();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        adapter.filtrado(newText);
        return false;
    }
}

