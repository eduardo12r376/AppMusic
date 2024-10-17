package com.example.appmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.appmusic.R;
import com.example.appmusic.entidades.Historial;

import java.util.ArrayList;


public class HistoarialAdapter extends RecyclerView.Adapter<HistoarialAdapter.HistorialHolder> implements View.OnClickListener{



    ArrayList<Historial> listaHistorial;
    RequestQueue request;
    Context context;
    private View.OnClickListener listener;

    public HistoarialAdapter(ArrayList<Historial> listaHistorial, Context context) {
        this.listaHistorial = listaHistorial;
        this.context=context;
        request= Volley.newRequestQueue(context);
    }

    @Override
    public HistorialHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_list,parent,false);
        vista.setOnClickListener(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new HistorialHolder(vista);
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(HistorialHolder holder, int position) {
        holder.txtNombreusuario.setText(listaHistorial.get(position).getNombre().toString()+" "+listaHistorial.get(position).getApellidopaternoP().toString()+" "+listaHistorial.get(position).getApellidomaternoP().toString());
        holder.txtTipoPaquete.setText(listaHistorial.get(position).getTipoPaquete().toString());
        holder.txtHoraEvento.setText(listaHistorial.get(position).getHoraEvento().toString());
        holder.txtFechaEvento.setText(listaHistorial.get(position).getFechaEvento().toString());
        holder.fecharegistro.setText(listaHistorial.get(position).getFechareservada().toString());
    }



    @Override
    public int getItemCount() {
        return listaHistorial.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }


    public class HistorialHolder extends RecyclerView.ViewHolder{

        TextView txtNombreusuario,txtTipoPaquete,txtHoraEvento,txtFechaEvento,fecharegistro;


        public HistorialHolder(@NonNull final View itemView) {
            super(itemView);
            txtNombreusuario    = (TextView)  itemView.findViewById(R.id.idNombreUsuario);
            txtTipoPaquete   = (TextView)  itemView.findViewById(R.id.idPaqueteCotratado);
            txtHoraEvento  = (TextView)  itemView.findViewById(R.id.idHoraEvento);
            txtFechaEvento         = (TextView)  itemView.findViewById(R.id.idfechaEvento);
            fecharegistro           = (TextView) itemView.findViewById(R.id.idReservado);

        }

}}
