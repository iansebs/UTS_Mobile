package com.example.uts_31862_sebastian2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class adapter extends RecyclerView.Adapter<adapter.LaguViewHolder> {

    private final LinkedList<String> mDaftarLagu;
    private LayoutInflater mInflater;
    private ArrayList<File> arrayDaftarLagu;

    adapter(Context context, LinkedList<String> daftarLagu, ArrayList<File> songsMine) {
        mInflater = LayoutInflater.from(context);
        mDaftarLagu = daftarLagu;
        arrayDaftarLagu = songsMine;
    }


    @NonNull
    @Override
    public adapter.LaguViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.daftarlagu_item, parent, false);
        return new LaguViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.LaguViewHolder holder, int position) {
        String mCurrent = mDaftarLagu.get(position);
        holder.LaguItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mDaftarLagu.size();
    }

    class LaguViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView LaguItemView;
        final adapter mAdapter;

        public LaguViewHolder(@NonNull View itemView,
                              adapter adapter){
            super(itemView);
            LaguItemView = itemView.findViewById(R.id.daftarlagu);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int songPosition = getLayoutPosition();
            String element = mDaftarLagu.get(songPosition);

            Intent ssong = new Intent(v.getContext(), PlayerActivity.class);
            ssong.putExtra("songs", arrayDaftarLagu);
            ssong.putExtra("songname", element);
            ssong.putExtra("pos", songPosition);
            v.getContext().startActivity(ssong);
        }
    }

}
