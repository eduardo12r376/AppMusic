package com.example.appmusic;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.RequiresApi;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Contratacion extends Fragment {
    private SharedPreferences preferences;

private int dia;
    private int mes;
    private int ano;
    private int hora;
    private int minutos;
    ViewFlipper v_flipper;
    StringRequest stringRequest;
    int idGrupoRecibido;
    String idUsuarioRecibido;
    Bundle bundle = new Bundle();
EditText descripcionsevicioofrecido,precioservicioofrecido,insertarFecha,insertarHora;
    ImageButton Bfecha,Bhora;
    ProgressDialog progressDialog;
    ProgressDialog progreso;
    String HttpURILog = "https://precatory-levels.000webhostapp.com/UsuarioLogeado.php";

    int PaqueteAConsultar;
    String HttpURIPaquete = "https://precatory-levels.000webhostapp.com/ConsultarPaquete.php";
    Spinner tipoPaquetesOfrecidos;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Contratacion() {
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
    public static Contratacion newInstance(String param1, String param2) {
        Contratacion fragment = new Contratacion();
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
        return inflater.inflate(R.layout.fragment_contratacion, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ImageButton regresarAPerfilContracion = view.findViewById(R.id.Contratacion);
        Button btnContratacion = view.findViewById(R.id.idReservar);
         Bfecha  = (ImageButton) view.findViewById(R.id.idSelecFecha);
         Bhora   = (ImageButton) view.findViewById(R.id.idSelecHora);
         insertarFecha = (EditText) view.findViewById(R.id.idInsertarFecha);
         insertarHora  = (EditText) view.findViewById(R.id.idInsertarHora);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreUsuarioLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idUsuarioLogeado);

        tipoPaquetesOfrecidos= (Spinner) view.findViewById(R.id.tiposervicioofrecido);
        descripcionsevicioofrecido = (EditText) view.findViewById(R.id.idDescripcionServicioOfrecido);
        precioservicioofrecido = (EditText) view.findViewById(R.id.idCostoServicioOfrecido);



        Bundle objetoPersona = getArguments();

        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){

            idGrupoRecibido=getArguments().getInt("idGrupo");

            idUsuarioRecibido = String.valueOf(getArguments().getSerializable("idUsuario"));
        }

        regresarAPerfilContracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putSerializable("idUsuario", idUsuarioRecibido);
                Navigation.findNavController(view).navigate(R.id.action_contratacion_to_gruposMusicales,bundle);
            }
        });

        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarContratacion);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_contratacion);




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
                        Navigation.findNavController(view).navigate(R.id.action_contratacion_to_bienvenida2,bundle);

                        break;
                    case R.id.nav_search:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.action_contratacion_to_datosArtistas,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putSerializable("idUsuario",idUsuarioRecibido);
                        Navigation.findNavController(view).navigate(R.id.action_contratacion_to_gruposMusicales,bundle);
                        break;

                    case R.id.nav_cerrarsesion:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_contratacion_to_mainFragment);
                        break;
                }
                return true;
            }
        });

//Termino le menu lateral

        //Consulta del usuario logeado

        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, HttpURILog,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String serverResponse) {
                       // progressDialog.dismiss();
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

        //Consult paquetes
        String[] valoresPaquete = {"1.Basico","2.Medio","3.Estandar","4.Premium"};
        tipoPaquetesOfrecidos.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresPaquete));
        tipoPaquetesOfrecidos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {

                PaqueteAConsultar=position+1;

                descripcionsevicioofrecido.setText("");
                precioservicioofrecido.setText("");
                insertarFecha.setText("");
                insertarHora.setText("");


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
                                        descripcionsevicioofrecido.setText(DescripcionPaquete);
                                        precioservicioofrecido.setText(PrecioPaquete);
                                        Bfecha.setEnabled(true);
                                        Bhora.setEnabled(true);
                                        btnContratacion.setEnabled(true);

                                    }

                                    if (Error == true){
                                        Bfecha.setEnabled(false);
                                        Bhora.setEnabled(false);
                                        btnContratacion.setEnabled(false);

                                        Toast.makeText(getContext(),"No se encuntra disponible este servicio", Toast.LENGTH_SHORT).show();
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
                        parametros.put("idArtista", String.valueOf(idGrupoRecibido));
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
                //paquetes registrados
                //Fin consulta paquetes

        RequestQueue queue1 = Volley.newRequestQueue(getContext());
                btnContratacion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        progreso = new ProgressDialog(getContext());
                        progreso.setMessage("Cargando...");
                        progreso.show();


                        String url = "https://precatory-levels.000webhostapp.com/Contrataciones.php";

                        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.trim().equalsIgnoreCase("Registra")){
                                    progreso.hide();
                                    insertarFecha.setText("");
                                    insertarHora.setText("");

                                    Toast.makeText(getContext(),"Se ha recervado su evento con exito",Toast.LENGTH_LONG).show();


                                    bundle.putInt("idArtista", Integer.parseInt(idUsuarioRecibido));
                                    Navigation.findNavController(view).navigate(R.id.action_contratacion_to_gruposMusicales,bundle);
                                }else
                                {

                                    Toast.makeText(getContext(),"No se ha podido recervar su evento"+response,Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),"No se ha podido conecatar al servidor",Toast.LENGTH_LONG).show();
                                progreso.hide();

                            }
                        }
                        ){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                String sFechaContratacion = insertarFecha.getText().toString();
                                String sHoraContratcion   = insertarHora.getText().toString();

                                Map<String,String> parametros = new HashMap<>();
                                parametros.put("idArtista", String.valueOf(idGrupoRecibido));
                                parametros.put("idUsuario",idUsuarioRecibido);
                                parametros.put("idPaquete", String.valueOf(PaqueteAConsultar));
                                parametros.put("fechaContratacion",sFechaContratacion);
                                parametros.put("horaContratacion",sHoraContratcion);

                                return parametros;
                            }
                        };

                        queue1.add(stringRequest);



                    }
                });

                Bfecha.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {

                        Calendar c= Calendar.getInstance();
                        ano = c.get(Calendar.YEAR);
                        mes = c.get(Calendar.MONTH);
                        dia = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                insertarFecha.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        },ano,mes,dia);
                        datePickerDialog.show();

                    }
                });


                Bhora.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar c= Calendar.getInstance();
                        hora = c.get(Calendar.HOUR_OF_DAY);
                        minutos = c.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                 insertarHora.setText(hourOfDay+":"+minute);
                            }
                        },hora,minutos,false);
                        timePickerDialog.show();

                    }
                });






            }
        }




