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
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPaquete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPaquete extends Fragment {
    String HttpURI = "https://precatory-levels.000webhostapp.com/ArtistaLogeado.php";

    StringRequest stringRequest;
    ProgressDialog progressDialog;
    String HttpURIPaquete = "https://precatory-levels.000webhostapp.com/ConsultarPaquete.php";
    Bundle bundle = new Bundle();
    String idArtistaRecibido,idPaqueteAEditar;
    private SharedPreferences preferences;
    ProgressDialog progreso;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarPaquete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarPaquete.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarPaquete newInstance(String param1, String param2) {
        EditarPaquete fragment = new EditarPaquete();
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
        return inflater.inflate(R.layout.fragment_editar_paquete, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton regresarAmisPaquetes = (ImageButton) view.findViewById(R.id.idEditarPaquetesAmispaqutes);

        EditText descripcionEditar =(EditText) view.findViewById(R.id.idDescripcionEditar);
        EditText precioEdtar =(EditText) view.findViewById(R.id.idPrecioEditar);
        Button   confirmarEditar = (Button) view.findViewById(R.id.idEditarPaquete);
        Button   canselarEditar = (Button) view.findViewById(R.id.idCanselarEdision);

        //recibo el id
        Bundle objetoPersona = getArguments();
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){
            idArtistaRecibido = String.valueOf(objetoPersona.getInt("idArtista"));
            idPaqueteAEditar = String.valueOf(objetoPersona.getInt("idPaqueteAEditar"));
        }

        regresarAmisPaquetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_misPaquetes,bundle);
            }
        });
        //iin recepcion
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationViewArtista);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreArtistaLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idNombreArtistaLogeado);
        ImageView fotoDelArtistaLogeado = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.idLogoArtistaLogeado);
        //Se agrego el menu lateral
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarEditarPaquetes);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_editarpaquete);

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
                        Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_paquetes,bundle);
                        break;

                    case R.id.nav_perfil:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_editarPerfilArtista,bundle);
                        break;
                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_mainFragment);

                        break;
                }
                return true;
            }
        });
        //fin menu lateral


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
                                    Toast.makeText(getContext().getApplicationContext(),"Error al cargar datos del usuario",Toast.LENGTH_SHORT).show();
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



        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());

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
                                descripcionEditar.setText(DescripcionPaquete);
                                precioEdtar.setText(PrecioPaquete);

                            }

                            if (Error == true){

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
                parametros.put("idTipoPaquete", String.valueOf(idPaqueteAEditar));
                parametros.put("idArtista",idArtistaRecibido);
                return parametros;
            }
        };
        requestQueue1.add(stringRequest);
        //paquetes registrados

        canselarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(v).navigate(R.id.action_editarPaquete_to_misPaquetes,bundle);
            }
        });

        RequestQueue queue1 = Volley.newRequestQueue(getContext());
        confirmarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progreso = new ProgressDialog(getContext());
                progreso.setMessage("Cargando...");
                progreso.show();

/////////////////////////////////////////editar
                String url = "https://precatory-levels.000webhostapp.com/Editarpaquetes.php";

                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progreso.hide();
                        if(response.trim().equalsIgnoreCase("Registra")){

                            descripcionEditar.setText("");
                            precioEdtar.setText("");

                            Toast.makeText(getContext(),"Se ha Actualizado el paquete con exito",Toast.LENGTH_LONG).show();


                            bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                            Navigation.findNavController(view).navigate(R.id.action_editarPaquete_to_misPaquetes,bundle);
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
                        String sDescripcion   = descripcionEditar.getText().toString();
                        String sPrecio     = precioEdtar.getText().toString();
                        Map<String,String> parametros = new HashMap<>();
                        parametros.put("tipoPaquete",String.valueOf(idPaqueteAEditar));
                        parametros.put("descripcion",sDescripcion);
                        parametros.put("precio",sPrecio);
                        parametros.put("idArtista",idArtistaRecibido);
                        return parametros;
                    }
                };

                queue1.add(stringRequest);

            }
        });

    }
}