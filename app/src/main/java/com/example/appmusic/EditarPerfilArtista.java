package com.example.appmusic;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.content.FileProvider;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditarPerfilArtista#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditarPerfilArtista extends Fragment {
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private ArrayAdapter<String> adapter;
    int tiposervicio,generomuesical;
    Spinner servicio,genero;
    EditText nombregrupo,direccion,logo,municipio,correogrupo,telefonogrupo,descripcion;

    ImageView Imagenlogo;
    ImageButton cargarImagenLogo;
    private final String CARPETA_RAIZ="imagenesAppMusic/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misLogos";
    private String path;
    Bitmap bitmap;
    ProgressDialog progreso;
    Bundle bundle = new Bundle();
    String idArtistaRecibido;
    private SharedPreferences preferences;
    String HttpURI = "https://precatory-levels.000webhostapp.com/ArtistaLogeado.php";
    String HttpURI2 = "https://precatory-levels.000webhostapp.com/DetalleArtista.php";
    StringRequest stringRequest;
    ProgressDialog progressDialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditarPerfilArtista() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditarPerfilArtista.
     */
    // TODO: Rename and change types and number of parameters
    public static EditarPerfilArtista newInstance(String param1, String param2) {
        EditarPerfilArtista fragment = new EditarPerfilArtista();
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
        return inflater.inflate(R.layout.fragment_editar_perfil_artista, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton regresarAPerfilArtista = (ImageButton) view.findViewById(R.id.idEditarPerfilAperfilArtista);

        Button btnGuardarDatos =(Button) view.findViewById(R.id.idConfirmarEditar);
        Button btnCancelarEdicion =(Button) view.findViewById(R.id.idCancelarPerfilEdision);

        ImageButton DenuevoArtistaABienvenida =(ImageButton) view.findViewById(R.id.idNuevoArABievenida);
        Imagenlogo       = (ImageView) view.findViewById(R.id.idImageLogoPerfilEditar);
        servicio         = (Spinner)  view.findViewById(R.id.idTipoServicioPerfilEditar);
        genero           = (Spinner)  view.findViewById(R.id.idGeneroMusicalPerfilEditar);
        nombregrupo      = (EditText) view.findViewById(R.id.idNombreGrupoPerfilEditar);
        descripcion      =(EditText)  view.findViewById(R.id.idDescripcionPerfilEditar);
        logo             = (EditText) view.findViewById(R.id.idLogoPEditar);
        direccion        = (EditText) view.findViewById(R.id.idDireccionPerfilEditar);
        municipio        = (EditText) view.findViewById(R.id.idMunicipioPerfilEditar);
        correogrupo      = (EditText) view.findViewById(R.id.idCorreoGrupoPerfilEditar);
        telefonogrupo    = (EditText) view.findViewById(R.id.idTelefonoGrupoPerfilEditar);
        cargarImagenLogo = (ImageButton) view.findViewById(R.id.idCargarImagenPerfilEditar);




        //recibo el id
        Bundle objetoPersona = getArguments();
        //validacion para verificar si existen argumentos para mostrar
        if(objetoPersona !=null){

            idArtistaRecibido = String.valueOf(objetoPersona.getInt("idArtista"));

        }
//fin recepcion
regresarAPerfilArtista.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        bundle.putInt("idArtista",Integer.parseInt(idArtistaRecibido));
        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_perfilArtista,bundle);
    }
});


        NavigationView navigationView = (NavigationView) view.findViewById(R.id.idNavegationViewArtista);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getContext());
        TextView NombreArtistaLogeado = (TextView) navigationView.getHeaderView(0).findViewById(R.id.idNombreArtistaLogeado);
        ImageView fotoDelArtistaLogeado = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.idLogoArtistaLogeado);
        //Se agrego el menu lateral

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.topAppBarEditarPerfilArtista);
        DrawerLayout drawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_editarperfil);
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
                Toast.makeText(getContext().getApplicationContext(), "No se puede conectar con el servicor",
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
                        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_perfilArtista,bundle);
                        break;
                    case R.id.nav_search:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_historialContrataciones,bundle);
                        break;
                    case R.id.nav_user:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_paquetes,bundle);
                        break;

                    case R.id.nav_perfil:
                        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                        Navigation.findNavController(view).navigate(R.id.editarPerfilArtista,bundle);
                        break;

                    case R.id.nav_salir:
                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("Usuario_id");
                        editor.remove("Usuario_tipo");
                        editor.remove("Usuario_nombre");
                        editor.apply();
                        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_mainFragment);

                        break;
                }
                return true;
            }
        });
        //fin menu lateral
        if (validaPermisos()){

            cargarImagenLogo.setEnabled(true);
        }else{

            cargarImagenLogo.setEnabled(false);
        }

        String[] valoresServicio = {"","1.Solista","2.Dueto","3.Trio","4.Agrupacion","5.Sonido"};
        servicio.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresServicio));
        servicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position1), Toast.LENGTH_SHORT).show();

                tiposervicio=position1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });


        String[] valoresGenero = {"","1.Banda","2.Cumbia","3.Norteña","4.Corridos","5.Grupera","6.Versatil"};
        genero.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresGenero));
        genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position2, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position2), Toast.LENGTH_SHORT).show();

                generomuesical=position2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });


        cargarImagenLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarImagen();
            }
        });

        RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());

        stringRequest = new StringRequest(Request.Method.POST, HttpURI2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String serverResponse) {
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(serverResponse);

                            Boolean error     = obj.getBoolean("error");
                            int IDGrupo       = obj.getInt("idArtista");
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
                                        Imagenlogo.setImageBitmap(response);

                                    }
                                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext().getApplicationContext(),"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                requestQueue2.add(imageRequest);

                                nombregrupo.setText(NomG);

                                descripcion.setText(DescriP);
                                telefonogrupo.setText(TelefonoP);
                                correogrupo.setText(CorreoP);
                                direccion.setText(DireccionP);
                                municipio.setText(MunicipioP);
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


        //fin consulta datos artista

        RequestQueue queue1 = Volley.newRequestQueue(getContext());

        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progreso = new ProgressDialog(getContext());
                progreso.setMessage("Cargando...");
                progreso.show();


                String url = "https://precatory-levels.000webhostapp.com/EditarPerfilArtista.php";


               StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progreso.hide();

                        if(response.trim().equalsIgnoreCase("Registra")){

                            String[] valoresServicio = {"","1.Solista","2.Dueto","3.Trio","4.Agrupacion","5.Sonido"};
                            servicio.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresServicio));
                            servicio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long id)
                                {
                                    Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position1), Toast.LENGTH_SHORT).show();
                                    tiposervicio=position1;

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                    // vacio

                                }
                            });


                            String[] valoresGenero = {"","1.Banda","2.Cumbia","3.Tejana","4.Corridos","5.Grupera","6.Versatil"};
                            genero.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,valoresGenero));
                            genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position2, long id)
                                {
                                    Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position2), Toast.LENGTH_SHORT).show();

                                    generomuesical=position2;

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {
                                    // vacio

                                }
                            });

                            nombregrupo.setText("");
                            descripcion.setText("");
                            direccion.setText("");
                            municipio.setText("");
                            correogrupo.setText("");
                            telefonogrupo.setText("");
                            logo.setText("");
                            Imagenlogo.setImageResource(android.R.drawable.ic_menu_report_image);

                            Toast.makeText(getContext(),"Se ha Actualizado su perfil con exito"+nombregrupo,Toast.LENGTH_LONG).show();
                            bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
                            Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_perfilArtista,bundle);
                        }else
                        {

                            Toast.makeText(view.getContext(),response,Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view.getContext(),"No se ha podido conecatar",Toast.LENGTH_LONG).show();
                        progreso.hide();

                    }
                }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        String sNombreGrupo   = nombregrupo.getText().toString();
                        String sDescripcion   = descripcion.getText().toString();
                        String sLogo          = convertirImgString(bitmap);
                        String sDireccion     = direccion.getText().toString();
                        String sMunicipio     = municipio.getText().toString();
                        String sCorreoGrupo   = correogrupo.getText().toString();
                        String sTelefonoGrupo = telefonogrupo.getText().toString();

                        Map<String,String> parametros = new HashMap<>();
                        parametros.put("idArtista",idArtistaRecibido);
                        parametros.put("tipoServicio",String.valueOf(tiposervicio));
                        parametros.put("nombreGrupo",sNombreGrupo);
                        parametros.put("descripcion",sDescripcion);
                        parametros.put("logo",sLogo);
                        parametros.put("direccion",sDireccion);
                        parametros.put("municipio",sMunicipio);
                        parametros.put("generoMusical",String.valueOf(generomuesical));
                        parametros.put("correo",sCorreoGrupo);
                        parametros.put("telefonoGrupo",sTelefonoGrupo);

                        return parametros;
                    }
                };



                queue1.add(stringRequest1);




            }
        });

btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        bundle.putInt("idArtista", Integer.parseInt(idArtistaRecibido));
        Navigation.findNavController(view).navigate(R.id.action_editarPerfilArtista_to_perfilArtista,bundle);
    }
});
    }

    private String convertirImgString(Bitmap bitmap) {

        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


    private boolean validaPermisos(){

        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){

            return true;

        }
        if((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA))||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){

            cargarDialogoRecomendacion();

        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                cargarImagenLogo.setEnabled(true);

            }else{

                solicitarPermisosManual();
            }

        }
    }

    private void solicitarPermisosManual() {

        final CharSequence[] opciones ={"Si","No"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¿Descea configurar los permisos de forma manual?");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (opciones[i].equals("Si")){


                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$VIEWPERMISSIONUSAGE" + ""));
                    Uri uri2 = Uri.fromParts("pakage",getContext().getPackageName(),null);
                    intent.setData(uri2);
                    startActivity(intent);

                }
                else {

                    Toast.makeText(getContext(),"Los Permisos no fueron aceptados",Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debes aceptar los permisos correspondientes para el correcto funcioanmiento de la app");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){

                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA},100);

            }
        });
        dialogo.show();
    }

    private void cargarImagen(){
        final CharSequence[] opciones ={"Tomar foto","Cargar Logo","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (opciones[i].equals("Tomar foto")){

                    tomarFotografia();

                }else {
                    if (opciones[i].equals("Cargar Logo")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);

                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    private  void tomarFotografia(){
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada = fileImagen.exists();
        String nombreImagen="";
        if (isCreada == false){

            isCreada = fileImagen.mkdirs();
        }

        if (isCreada == true){

            nombreImagen = (System.currentTimeMillis()/1000)+".jpg";
            logo.setText(nombreImagen);
        }

        path = Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen =new File(path);

        Intent intentCam=null;
        intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){

            String authorities= getContext().getApplicationContext().getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(getContext(),authorities,imagen);
            intentCam.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        }else{
            intentCam.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagen));
        }

        startActivityForResult(intentCam,COD_FOTO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case 10:
                Uri miPath = data.getData();
                Imagenlogo.setImageURI(miPath);


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    Imagenlogo.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case 20:

                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                                Log.i("Ruta de almacenamiento", "Path: "+path);

                            }
                        });

                bitmap = BitmapFactory.decodeFile(path);
                Imagenlogo.setImageBitmap(bitmap);
                break;
        }


        bitmap=redimencionarImagen(bitmap,600,800);

    }

    private Bitmap redimencionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if(ancho>anchoNuevo || alto>altoNuevo){

            float escalaAncho = anchoNuevo/ancho;
            float escalaAlto  = altoNuevo/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{

            return bitmap;
        }


    }}