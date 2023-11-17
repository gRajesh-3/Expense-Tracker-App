package com.example.wallet.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.model.Expense;
import com.example.wallet.model.Section;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    List<Section> sections;
    Context context;
    private OnItemClickListener onItemClickListener;

    public TransactionAdapter(List<Section> sections, Context context, OnItemClickListener onItemClickListener) {
        this.sections = sections;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Section section = sections.get(position);
        String sectionName = section.getSectionName();
        List<Expense> sectionExpenses = section.getExpenses();

        holder.tvSectionHeader.setText(sectionName);
        SectionAdapter sectionAdapter = new SectionAdapter(section.getId(), sectionExpenses, context, onItemClickListener);
        holder.rvSection.setAdapter(sectionAdapter);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionHeader;
        RecyclerView rvSection;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionHeader = itemView.findViewById(R.id.tvSectionHeader);
            rvSection = itemView.findViewById(R.id.rvSection);
        }
    }
}
