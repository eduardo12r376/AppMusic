package com.example.appmusic;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.adapter.HistoarialAdapter;
import com.example.appmusic.entidades.Historial;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistorialContrataciones#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistorialContrataciones extends Fragment {
    private SharedPreferences preferences;
    Bundle bundle = new Bundle();
    String idArtistaRecibido;
    ProgressDialog progressDialog;
    String HttpURI = "https://precatory-levels.000webhostapp.com/ArtistaLogeado.php";
    StringRequest stringRequest;
    ProgressDialog dialogo;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    RecyclerView recyclerHistorial;
    ArrayList<Historial> listaHistorial;
int idArtistaRecibido2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistorialContrataciones.
     */
    // TODO: Rename and change types and number of parameters
    public static HistorialContrataciones newInstance(String param1, String param2) {
        HistorialContrataciones fragment = new HistorialContrataciones();
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
        return inflater.inflate(R.layout.fragment_historial_contrataciones, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageButton DeHistorialAPefil = (ImageButton) view.findViewById(R.id.idHitosialAPerfilArtista);

        Bundle objetoPersona = getArguments();

        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona != null){

            idArtistaRecibido = String.valueOf(objetoPersona.getInt("idArtista"));

            idArtistaRecibido2=objetoPersona.getInt("idArtista");
        }


        //fin recepcion

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationViewArtista);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreArtistaLogeado   = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idNombreArtistaLogeado);
        ImageView fotoDelArtistaLogeado = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.idLogoArtistaLogeado);
        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarHistorial);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_historial);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Verificando...");
        progressDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, HttpURI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String serverResponse) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(serverResponse);

                            String ArtistaLogeado = obj.getString("NombreGrupo");
                            String RutaLogoLogeado = obj.getString("RutaLogo");

                            String urlImagen="https://precatory-levels.000webhostapp.com/"+RutaLogoLogeado;
                            urlImagen=urlImagen.replace(" ","%20");

                            ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    fotoDelArtistaLogeado.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext().getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                                }
                            });
                            requestQueue.add(imageRequest);
                            NombreArtistaLogeado.setText(ArtistaLogeado);

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
                parametros.put("idArtista",idArtistaRecibido);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
        //Fin de la consulta del usuario logeado


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
                        bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_historialContrataciones_to_perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_historialContrataciones_to_paquetes,bundle);
                        break;
                    case R.id.nav_perfil:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_historialContrataciones_to_editarPerfilArtista,bundle);
                        break;
                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_historialContrataciones_to_mainFragment);

                        break;
                }
                return true;
            }
        });
        //fin menu lateral



        DeHistorialAPefil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_historialContrataciones_to_perfilArtista,bundle);
            }
        });

        listaHistorial=new ArrayList<>();
        recyclerHistorial = (RecyclerView) view.findViewById(R.id.idRecyclerHistorial);
        recyclerHistorial.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerHistorial.setHasFixedSize(true);
        request= Volley.newRequestQueue(getContext());
        cargarWebService();
    }


    private void cargarWebService() {

        dialogo=new ProgressDialog(getContext());
        dialogo.setMessage("Consultando Registros ");
        dialogo.show();



        String url="https://precatory-levels.000webhostapp.com/ConsultarContrataciones.php?idArtista="+idArtistaRecibido2;

       jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Historial historial=null;
                JSONArray json=response.optJSONArray("contratacion");

                try {

                    for (int i=json.length()-1;i>=0;i--){
                        historial = new Historial();
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        historial.setIdContrato(jsonObject.optInt("idContrato"));
                        historial.setNombre(jsonObject.optString("nombreUsuario"));
                        historial.setTipoPaquete(jsonObject.optString("TipoPaquete"));
                        historial.setApellidopaternoP(jsonObject.optString("apellidoPaterno"));
                        historial.setApellidomaternoP(jsonObject.optString("apellidoMaterno"));
                        historial.setHoraEvento(jsonObject.optString("HoraEvento"));
                        historial.setFechaEvento(jsonObject.optString("FechaEvento"));
                        historial.setFechareservada(jsonObject.optString("Fecharecervada"));
                        listaHistorial.add(historial);
                    }
                    dialogo.hide();
                    recyclerHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
                    HistoarialAdapter adapterHistorial = new HistoarialAdapter(listaHistorial,getContext());
                    recyclerHistorial.setAdapter(adapterHistorial);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor", Toast.LENGTH_LONG).show();
                    dialogo.hide();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Aun no tiene contrataciones", Toast.LENGTH_LONG).show();
                dialogo.hide();
            }
        });


        request.add(jsonObjectRequest);

    }


    }
