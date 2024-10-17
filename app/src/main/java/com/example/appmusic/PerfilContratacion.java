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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.appmusic.entidades.Artista;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PerfilContratacion extends Fragment {
    private SharedPreferences preferences;

    TextView logogrpoP;
TextView Nombregrupo,numeroIntegrantes, descripcionp,direccionp,correop,telefonop,generoMusicalP;
   int IDRecibido=0,IDGrupo,idParaEnviarAconsulta, idRecibidDeContra=0;

   String idUsuarioRecibido;
   ImageView LogoP;
   ProgressDialog progressDialog;
    String HttpURI = "https://precatory-levels.000webhostapp.com/DetalleArtista.php";
    String HttpURILog = "https://precatory-levels.000webhostapp.com/UsuarioLogeado.php";

    Bundle bundle = new Bundle();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilContratacion() {
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
    public static PerfilContratacion newInstance(String param1, String param2) {
        PerfilContratacion fragment = new PerfilContratacion();

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
      View view = inflater.inflate(R.layout.fragment_perfilcontratacion, container, false);




        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Button btnContratar = view.findViewById(R.id.idContratar);

        ImageButton regresarAGrupos = view.findViewById(R.id.Perfilgrupomusical);

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreUsuarioLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idUsuarioLogeado);

        //Consulta del usuario logeado

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, HttpURILog,
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
                Toast.makeText(getContext().getApplicationContext(), error.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }){
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idUsuario",String.valueOf(idUsuarioRecibido));
                return parametros;
            }
        };
        requestQueue1.add(stringRequest1);
        //Fin de la consulta del usuario logeado


        //Consulta Datos del artista
        Nombregrupo       = (TextView) view.findViewById(R.id.idNombregrupoP);
        numeroIntegrantes = (TextView) view.findViewById(R.id.idTipoServicioPer);
        descripcionp      = (TextView) view.findViewById(R.id.idDescripcionP);
        generoMusicalP    = (TextView) view.findViewById(R.id.idGeneroMusicalP);
        telefonop         =(TextView) view.findViewById(R.id.idNumerop);
        correop           =(TextView) view.findViewById(R.id.idMailp);
        direccionp        =(TextView) view.findViewById(R.id.idDireccionP);
        LogoP             = (ImageView) view.findViewById(R.id.idLogoPerfil);

        Bundle objetoPersona = getArguments();
        Artista artista = null;
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){

            artista = (Artista) objetoPersona.getSerializable("IdArtista");
            IDRecibido=artista.getIdArtista();

            idUsuarioRecibido = String.valueOf(getArguments().getSerializable("idUsuario"));
        }


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

                            Boolean error     = obj.getBoolean("error");
                            IDGrupo           = obj.getInt("idArtista");
                            String NomG       = obj.getString("nombreGrupo");
                            String NumIG      = obj.getString("tipoServicio");
                            String DescriP    = obj.getString("descripcion");
                            String TelefonoP  = obj.getString("telefonoGrupo");
                            String CorreoP    = obj.getString("correo");
                            String DireccionP = obj.getString("direccion");
                            String MunicipioP = obj.getString("municipio");
                            String GeneroP    = obj.getString("generoMusical");
                            String rutaLogo   = obj.getString("rutaLogo");


                            if (error == true)
                                Toast.makeText(getContext().getApplicationContext(), "No se establecio conexion",
                                        Toast.LENGTH_LONG).show();
                            else {

                                String urlImagen="https://precatory-levels.000webhostapp.com/"+rutaLogo;
                                urlImagen=urlImagen.replace(" ","%20");

                                ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        LogoP.setImageBitmap(response);
                                    }
                                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext().getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                requestQueue.add(imageRequest);
                                Nombregrupo.setText(NomG);
                                numeroIntegrantes.setText(NumIG);
                                descripcionp.setText(DescriP);
                                telefonop.setText(TelefonoP);
                                correop.setText(CorreoP);
                                direccionp.setText(DireccionP+", "+MunicipioP);
                                generoMusicalP.setText(GeneroP);

                                Toast.makeText(getContext().getApplicationContext(), NomG,
                                        Toast.LENGTH_LONG).show();

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getContext().getApplicationContext(), error.toString(),
                        Toast.LENGTH_LONG).show();
            }

        }){
            protected Map<String, String> getParams(){
                Map<String,String> parametros = new HashMap<>();
                parametros.put("idArtista",String.valueOf( IDRecibido));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);


        //fin consulta datos artista


        regresarAGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putSerializable("idUsuario",idUsuarioRecibido);
                Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_gruposMusicales,bundle);
            }
        });


        btnContratar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putString("idUsuario", idUsuarioRecibido);
                bundle.putInt("idGrupo",IDRecibido);
                Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_contratacion,bundle);

            }
        });

        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarPerfilContra);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_perfilContra);




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
                        Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_bienvenida2,bundle);

                        break;
                    case R.id.nav_search:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_datosArtistas,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_gruposMusicales,bundle);
                        break;
                    case R.id.nav_cerrarsesion:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_perfilContratacion_to_mainFragment);
                        break;
                }
                return true;
            }
        });


//Termino le menu lateral

    }

}
