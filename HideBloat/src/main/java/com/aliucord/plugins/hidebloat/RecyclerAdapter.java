package com.aliucord.plugins.hidebloat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aliucord.plugins.hidebloat.widgets.SwitchItem;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) { super(itemView); }
    }

    private final List<SwitchItem> switches;

    public RecyclerAdapter(List<SwitchItem> switches) {
        this.switches = switches;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new LinearLayout(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((LinearLayout) holder.itemView).addView(switches.get(position));
    }

    @Override
    public int getItemCount() {
        return switches.size();
    }
}