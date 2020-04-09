package com.project.e_room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Result> results;

    public RecyclerViewAdapter(Context context, List<Result> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.textViewRuang.setText(result.getRuang());
        holder.textViewNamaPeminjam.setText(result.getNamaPeminjam()+" - "+result.getKeperluan());
        holder.textViewWaktu.setText(result.getTanggal()+" "+result.getJamAwal()+" s/d "+result.getJamAkhir());
        holder.textViewStatusPeminjaman.setText(result.getStatusPeminjaman());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.doorLogo) ImageView imageViewDoorLogo;
        @BindView(R.id.textRuang)TextView textViewRuang;
        @BindView(R.id.textNamaPeminjam)TextView textViewNamaPeminjam;
        @BindView(R.id.textWaktu) TextView textViewWaktu;
        @BindView(R.id.textStatusPeminjaman)TextView textViewStatusPeminjaman;

        public ViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
