package com.example.appmusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.R;
import com.example.appmusic.entidades.Artista;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class registrosImagenUrlAdapter extends RecyclerView.Adapter<registrosImagenUrlAdapter.ArtistaHolder> implements View.OnClickListener{



    ArrayList<Artista> listaArtista;
    ArrayList<Artista> listaOriginal;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public registrosImagenUrlAdapter(ArrayList<Artista> listaArtista, Context context) {
        this.listaArtista = listaArtista;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaArtista);
        this.context=context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public ArtistaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.artista_list,parent,false);
        vista.setOnClickListener(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ArtistaHolder(vista);
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(ArtistaHolder holder, int position) {
        holder.txtNombreGrupo.setText(listaArtista.get(position).getNombreGrupo().toString());
        holder.txtDescripcion.setText(listaArtista.get(position).getDescripcion().toString());
        holder.txtGeneroMusical.setText(listaArtista.get(position).getGeneroMusical().toString());
        holder.txtCorreo.setText(listaArtista.get(position).getCorreoGrupo().toString());

        if (listaArtista.get(position).getRutaLogo()!=null){
            //
            cargarImagenWebService(listaArtista.get(position).getRutaLogo(),holder);
        }else{

            holder.logo.setImageResource(R.drawable.img);
        }
    }



    public void filtrado(String txtBuscar){
        int longitud = txtBuscar.length();
        if (longitud==0) {

            listaArtista.clear();
            listaArtista.addAll(listaOriginal);
        }else{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            List<Artista> Colecion = listaArtista.stream().filter(i -> i.getGeneroMusical().toLowerCase()
                    .contains(txtBuscar.toLowerCase())).collect(Collectors.toList());


            listaArtista.clear();
            listaArtista.addAll(Colecion);
        }else{

                for (Artista C: listaOriginal){
                    if (C.getGeneroMusical().toLowerCase().contains(txtBuscar.toLowerCase())){
                        listaArtista.add(C);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    private void cargarImagenWebService(String rutaLogo, final ArtistaHolder holder) {



        String urlImagen="https://precatory-levels.000webhostapp.com/"+rutaLogo;
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.logo.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
            }
        });
        request.add(imageRequest);
        //VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaArtista.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }


    public class ArtistaHolder extends RecyclerView.ViewHolder{

        TextView txtNombreGrupo,txtDescripcion,txtGeneroMusical,txtCorreo;
        ImageView logo;

        public ArtistaHolder(@NonNull final View itemView) {
            super(itemView);
            txtNombreGrupo    = (TextView)  itemView.findViewById(R.id.idNombreGrupoArtist);
            txtDescripcion    = (TextView)  itemView.findViewById(R.id.idDescripcionArtist);
            txtGeneroMusical  = (TextView)  itemView.findViewById(R.id.idGeneroMusicalArtist);
            txtCorreo         = (TextView)  itemView.findViewById(R.id.idCorreoArtist);
            logo              = (ImageView) itemView.findViewById(R.id.idLogoArtista);

        }

}}
