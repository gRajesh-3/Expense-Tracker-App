package com.example.wallet.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.helper.OnItemClickListener;
import com.example.wallet.model.Expense;

import java.util.List;

import kotlin.Pair;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    List<Pair<String, List<Expense>>> sections;
    Context context;
    private final OnItemClickListener onItemClickListener;

    public TransactionAdapter(List<Pair<String, List<Expense>>> sections, Context context, OnItemClickListener onItemClickListener) {
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
        Pair<String, List<Expense>> section = sections.get(position);
        String sectionName = section.getFirst();
        List<Expense> sectionExpenses = section.getSecond();

        holder.tvSectionHeader.setText(sectionName);
        holder.tvSectionPrice.setText(getSectionExpense(sectionExpenses));
        SectionAdapter sectionAdapter = new SectionAdapter(sectionName, sectionExpenses, context, onItemClickListener);
        holder.rvSection.setAdapter(sectionAdapter);
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionHeader;
        TextView tvSectionPrice;
        RecyclerView rvSection;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionHeader = itemView.findViewById(R.id.tvSectionHeader);
            tvSectionPrice = itemView.findViewById(R.id.tvSectionPrice);
            rvSection = itemView.findViewById(R.id.rvSection);
        }
    }

    private String getSectionExpense(List<Expense> section) {
        Double expenseAmount = 0.0;
        for (Expense expense: section) { expenseAmount += expense.getAmount(); }
        return "₹"+expenseAmount;
    }
}
