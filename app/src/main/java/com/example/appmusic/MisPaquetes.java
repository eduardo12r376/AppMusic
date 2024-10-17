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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisPaquetes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisPaquetes extends Fragment {
    private SharedPreferences preferences;
String idArtistaRecibido;
Bundle bundle = new Bundle();
    String HttpURI = "https://precatory-levels.000webhostapp.com/ArtistaLogeado.php";

    StringRequest stringRequest;
    ProgressDialog progressDialog;
    int PaqueteAConsultar;
    String HttpURIPaquete = "https://precatory-levels.000webhostapp.com/ConsultarPaquete.php";
    Spinner tipoPaquete;
    EditText descripciondeMiPaquete;
    EditText precioMiPaquete;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MisPaquetes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisPaquetes.
     */
    // TODO: Rename and change types and number of parameters
    public static MisPaquetes newInstance(String param1, String param2) {
        MisPaquetes fragment = new MisPaquetes();
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
        return inflater.inflate(R.layout.fragment_mis_paquetes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton regresardeMisPaquetesAPerfil = (ImageButton) view.findViewById(R.id.idMisPaquetesAPerfilA);
        Button nuevoPaquete             =(Button) view.findViewById(R.id.idRegistarMiPaquete);
        Button editarMipaquete = (Button) view.findViewById(R.id.idEditarMiPaquete);
        tipoPaquete             = (Spinner)  view.findViewById(R.id.idTipoMisPaquetes);
        descripciondeMiPaquete = (EditText)  view.findViewById(R.id.idDescripcionMiPaquete);
        precioMiPaquete                 = (EditText) view.findViewById(R.id.idPrecioMiPaquete);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                                    Toast.makeText(getContext().getApplicationContext(),"Error al cargar datos del usuario logeado",Toast.LENGTH_SHORT).show();
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


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarMisPaquetes);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_mispaquetes);

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
                        Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_paquetes,bundle);
                        break;

                    case R.id.nav_perfil:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_editarPerfilArtista,bundle);
                        break;
                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_mainFragment);

                        break;
                }
                return true;
            }
        });
        //fin menu lateral

        String[] valoresPaquete = {"1.Basico","2.Medio","3.Estandar","4.Premium"};
        tipoPaquete.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresPaquete));
        tipoPaquete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {

                PaqueteAConsultar=position+1;

                descripciondeMiPaquete.setText("");
                precioMiPaquete.setText("");

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                progressDialog = new ProgressDialog(getContext());

                progressDialog.setMessage("Verificando...");
                progressDialog.show();

                stringRequest = new StringRequest(Request.Method.POST, HttpURIPaquete,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String serverResponse) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(serverResponse);

                                    boolean Error = obj.getBoolean("Error");
                                    String DescripcionPaquete = obj.getString("Descripcion");
                                    String PrecioPaquete = obj.getString("Precio");

                                    if(Error == false){
                                        descripciondeMiPaquete.setText(DescripcionPaquete);
                                        precioMiPaquete.setText(PrecioPaquete);
                                        editarMipaquete.setEnabled(true);
                                    }

                                    if (Error == true){

                                        editarMipaquete.setEnabled(false);
                                        Toast.makeText(getContext(),"No se ha registrado este paquete", Toast.LENGTH_SHORT).show();
                                    }



                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        Toast.makeText(getContext().getApplicationContext(),"No se encontraron Paquetes registrados",
                                Toast.LENGTH_LONG).show();
                    }

                }){
                    protected Map<String, String> getParams(){
                        Map<String,String> parametros = new HashMap<>();
                        parametros.put("idTipoPaquete", String.valueOf(PaqueteAConsultar));
                        parametros.put("idArtista",idArtistaRecibido);
                        return parametros;
                    }
                };
                requestQueue.add(stringRequest);
                //paquetes registrados


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });


        editarMipaquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
                bundle.putInt("idPaqueteAEditar",PaqueteAConsultar);
                Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_editarPaquete,bundle);
            }
        });

        regresardeMisPaquetesAPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_perfilArtista,bundle);
            }
        });

        nuevoPaquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_misPaquetes_to_paquetes,bundle);
            }
        });
    }
}