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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Paquetes extends Fragment {
    private SharedPreferences preferences;
    ArrayList<String> listaPaquetes;
    ArrayList<String> paquetes;

    Bundle bundle = new Bundle();
    String idArtistaRecibido;

    int opcionPaquete;
    Spinner tipoPaquete;
    EditText descripcion,precio;
    ProgressDialog progressDialog;
    String HttpURI = "https://precatory-levels.000webhostapp.com/ArtistaLogeado.php";
    StringRequest stringRequest;
    ProgressDialog progreso;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Paquetes() {
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
    public static Paquetes newInstance(String param1, String param2) {
        Paquetes fragment = new Paquetes();
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
        return inflater.inflate(R.layout.fragment_paquetes, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ImageButton regresarAPerfil = (ImageButton) view.findViewById(R.id.idPaquetesAPerfil);
        Button registrarPaquete = view.findViewById(R.id.idService);
        tipoPaquete       = (Spinner)  view.findViewById(R.id.idTipoPaquete);
        descripcion       = (EditText)  view.findViewById(R.id.idDescripcion);
        precio            = (EditText) view.findViewById(R.id.idPrecio);

        //recibo el id
        Bundle objetoPersona = getArguments();
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){
            idArtistaRecibido = String.valueOf(objetoPersona.getInt("idArtista"));
        }


        //iin recepcion

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationViewArtista);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreArtistaLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idNombreArtistaLogeado);
        ImageView fotoDelArtistaLogeado = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.idLogoArtistaLogeado);
        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarPaquetes);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_paquetes);


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
                        Navigation.findNavController(view).navigate(R.id.action_paquetes_to_perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_paquetes_to_historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.paquetes,bundle);
                        break;

                    case R.id.nav_perfil:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_paquetes_to_editarPerfilArtista,bundle);
                        break;
                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_paquetes_to_mainFragment);

                        break;
                }
                return true;
            }
        });
        //fin menu lateral




        regresarAPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_paquetes_to_perfilArtista,bundle);
            }
        });


        String[] valoresPaquete = {"","1.Basico","2.Medio","3.Estandar","4.Premium"};
        tipoPaquete.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresPaquete));
        tipoPaquete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                opcionPaquete=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });

        RequestQueue queue1 = Volley.newRequestQueue(getContext());

        registrarPaquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progreso = new ProgressDialog(getContext());
                progreso.setMessage("Cargando...");
                progreso.show();


                String url = "https://precatory-levels.000webhostapp.com/paquetes.php";

                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progreso.hide();
                        if(response.trim().equalsIgnoreCase("Registra")){
                            String[] valoresPaquete = {"->","1.Basico","2.Medio","3.Estandar","4.Premium"};
                            tipoPaquete.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresPaquete));
                            tipoPaquete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
                                {
                                    Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                    opcionPaquete=position;
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                    // vacio

                                }
                            });
                            descripcion.setText("");
                            precio.setText("");

                            Toast.makeText(getContext(),"Se ha registrado con exito",Toast.LENGTH_LONG).show();


                            bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                            Navigation.findNavController(view).navigate(R.id.action_paquetes_to_perfilArtista,bundle);
                        }else
                        {

                            Toast.makeText(getContext(),"No se ha podido registrar "+response,Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"No se ha podido conecatar",Toast.LENGTH_LONG).show();
                        progreso.hide();


                    }
                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String sDescripcion   = descripcion.getText().toString();
                        String sPrecio     = precio.getText().toString();
                        Map<String,String> parametros = new HashMap<>();
                        parametros.put("idArtista",idArtistaRecibido);
                        parametros.put("tipoPaquete",String.valueOf(opcionPaquete));
                        parametros.put("descripcion",sDescripcion);
                        parametros.put("precio",sPrecio);

                        return parametros;
                    }
                };

                queue1.add(stringRequest);



            }
        });


    }
    public class Constants {

        public static final int MY_DEFAULT_TIMEOUT = 2500;

        //...
    }

}