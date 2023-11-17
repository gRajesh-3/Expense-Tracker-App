package com.example.wallet.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.model.Expense;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    String sectionId;
    List<Expense> expenses;
    Context context;
    OnItemClickListener onItemClickListener;

    public SectionAdapter(String sectionId, List<Expense> expenses, Context context, OnItemClickListener onItemClickListener) {
        this.sectionId = sectionId;
        this.expenses = expenses;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_item, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.tvItemDesc.setText(expense.getDesc());
        holder.tvItemCategory.setText(expense.getCategory());
        holder.tvItemCategory.setTextColor(expense.getColor(context));
        holder.tvItemAmount.setText(expense.getAmount().toString());
    }

    @Override
    public int getItemCount() {
        System.out.println(expenses.size());
        return expenses.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout item;
        TextView tvItemDesc;
        TextView tvItemCategory;
        TextView tvItemAmount;
        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvItemDesc = itemView.findViewById(R.id.tvItemDesc);
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory);
            tvItemAmount = itemView.findViewById(R.id.tvItemAmount);

            item.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(sectionId, expenses.get(position).getId());
                }
            });
        }
    }
}
