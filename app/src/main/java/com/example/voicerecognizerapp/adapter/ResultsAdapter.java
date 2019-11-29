package com.example.voicerecognizerapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicerecognizerapp.Listener.RvResultsClickListener;
import com.example.voicerecognizerapp.R;
import com.example.voicerecognizerapp.model.Item;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultViewHolder> {


    private static final String TAG = "ResultsAdapter";
    private List<Item> mList;
    private RvResultsClickListener clickListener;



    public ResultsAdapter(List<Item> list){
        mList = list;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new ResultViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Item model = mList.get(position);
        holder.BindViews(model);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
    public void updateList(List<Item> list){
        mList = list;
        notifyDataSetChanged();
    }
    public void addNewItem(Item item){
        mList.add(item);
        notifyItemInserted(mList.size()-1);
    }
    public void addNewItens(List<Item> items){
        int i = mList.size()-1;
        mList.addAll(items);
        notifyItemRangeInserted(i,mList.size()-1-i);
    }
}
