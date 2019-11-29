package com.example.voicerecognizerapp.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicerecognizerapp.Listener.RvResultsClickListener;
import com.example.voicerecognizerapp.R;
import com.example.voicerecognizerapp.model.Item;

public class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private TextView tvLink;
    private TextView tvDesc;
    private TextView tvTitulo;

    private RvResultsClickListener mClickListener;

    public ResultViewHolder(@NonNull View iV, RvResultsClickListener clickListener) {
        super(iV);
        tvLink = iV.findViewById(R.id.tv_link);
        tvDesc = iV.findViewById(R.id.tv_desc);
        tvTitulo = iV.findViewById(R.id.tv_titulo);
        mClickListener = clickListener;
    }

    public void BindViews(Item item){

    }


    @Override
    public void onClick(View view) {
        mClickListener.RvOnClickListener(getAdapterPosition());
    }
}
