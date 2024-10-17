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
import android.widget.CalendarView;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PerfilArtista extends Fragment {
    private SharedPreferences preferences;
    CalendarView calendarView;
    Bundle bundle = new Bundle();
    String idArtistaRecibido;
    int idArtistaRecibido2;
    ProgressDialog progressDialog;
    String HttpURI = "https://precatory-levels.000webhostapp.com/DetalleArtista.php";
    JsonObjectRequest jsonObjectRequest;
    ProgressDialog dialogo;

    TextView  DescripcionPerfil;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilArtista() {
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
    public static PerfilArtista newInstance(String param1, String param2) {
        PerfilArtista fragment = new PerfilArtista();
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
        return inflater.inflate(R.layout.fragment_perfilartista, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button btnMisPaquetes = view.findViewById(R.id.idMisPaquetes);

        calendarView = (CalendarView) view.findViewById(R.id.idCalendarioFechas);
        ImageView LogoPerfilartista = (ImageView) view.findViewById(R.id.idLogoMiPerfil);
        EditText  NombreArtista     = (EditText)  view.findViewById(R.id.idNombreGrupoPerfil);
                  DescripcionPerfil = (TextView)  view.findViewById(R.id.idDescripcionPerfil);
        TextView  GeneroPerfil      = (TextView)  view.findViewById(R.id.idGeneroPerfil);
        TextView  TipoAgrupacion    = (TextView)  view.findViewById(R.id.idTipoAgrupacionPerfil);
        TextView  DireccionPerfil   = (TextView)  view.findViewById(R.id.idDireccionPerfil);
        TextView  TelefonoPerfil    = (TextView)  view.findViewById(R.id.idTeleefonoPerfil);
        TextView  CorreoPerfil      = (TextView)  view.findViewById(R.id.idCorreoPerfil);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today,nextYear.getTime()).withSelectedDate(today);



        //recibo el id
        Bundle objetoPersona = getArguments();
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){

            idArtistaRecibido = String.valueOf(objetoPersona.getInt("idArtista"));
            idArtistaRecibido2 = objetoPersona.getInt("idArtista");
        }
        //iin recepcion

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationViewArtista);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreArtistaLogeado   = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idNombreArtistaLogeado);
        ImageView fotoDelArtistaLogeado = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.idLogoArtistaLogeado);
        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarPerfilArtista);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_perfilArtista);
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
                        Navigation.findNavController(view).navigate(R.id.perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_perfilArtista_to_historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_perfilArtista_to_paquetes,bundle);
                        break;
                    case R.id.nav_perfil:
                        bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_perfilArtista_to_editarPerfilArtista,bundle);
                        break;
                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_perfilArtista_to_mainFragment);
                        break;
                }
                return true;
            }
        });

        btnMisPaquetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                Navigation.findNavController(view).navigate(R.id.action_perfilArtista_to_misPaquetes,bundle);

            }
        });

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

                            Boolean error      = obj.getBoolean("error");
                            int     IDGrupo    = obj.getInt("idArtista");
                            String  NomG       = obj.getString("nombreGrupo");
                            String  NumIG      = obj.getString("tipoServicio");
                            String  DescriP    = obj.getString("descripcion");
                            String  TelefonoP  = obj.getString("telefonoGrupo");
                            String  CorreoP    = obj.getString("correo");
                            String  DireccionP = obj.getString("direccion");
                            String  MunicipioP = obj.getString("municipio");
                            String  GeneroP    = obj.getString("generoMusical");
                            String  rutaLogo   = obj.getString("rutaLogo");

                            if (error == true)
                                Toast.makeText(getContext().getApplicationContext(), "No se establecio conexion",
                                        Toast.LENGTH_LONG).show();
                            else {
                                String urlImagen="https://precatory-levels.000webhostapp.com/"+rutaLogo;
                                urlImagen=urlImagen.replace(" ","%20");

                                ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        LogoPerfilartista.setImageBitmap(response);
                                        fotoDelArtistaLogeado.setImageBitmap(response);
                                    }
                                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext().getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                requestQueue.add(imageRequest);

                                NombreArtista.setText(NomG);
                                NombreArtistaLogeado.setText(NomG);
                                TipoAgrupacion.setText(NumIG);
                                DescripcionPerfil.setText(DescriP);
                                TelefonoPerfil.setText(TelefonoP);
                                CorreoPerfil.setText(CorreoP);
                                DireccionPerfil.setText(DireccionP+", "+MunicipioP);
                                GeneroPerfil.setText(GeneroP);

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
                parametros.put("idArtista",String.valueOf(idArtistaRecibido));
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
     fechaReservadas();
    }
    private void fechaReservadas() {
        RequestQueue request = Volley.newRequestQueue(getContext());
        String url="https://precatory-levels.000webhostapp.com/ConsultarFechasReservadas.php?idArtista="+idArtistaRecibido2;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray json=response.optJSONArray("fechas");
                long [] arrayLong = new long[json.length()];
                try {
                    for (int i=0;i<json.length();i++){
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);
                        String fechaReservada = jsonObject.optString("FechaEvento");
                        SimpleDateFormat convertirFecha = new SimpleDateFormat("dd/MM/yyyy");
                        Date fecha = convertirFecha.parse(fechaReservada);
                        arrayLong[i] = fecha.getTime();
                        calendarView.setDate(arrayLong[i]);
                    }
                } catch (JSONException | ParseException e) {
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
        request.add(jsonObjectRequest1);
    }

    
}