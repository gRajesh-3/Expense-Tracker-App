package com.example.wallet.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.example.wallet.R;

import java.time.LocalDateTime;

@Entity(tableName = "expense")
public class ExpenseModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String category;
    private Double amount;
    private LocalDateTime createdAt;

    public ExpenseModel(String description, String category, Double amount, LocalDateTime createdAt) {
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getColor(Context context) {
        int color;
        switch (category) {
            case "Personal care":
                color = ContextCompat.getColor(context, R.color.red);
                break;
            case "Groceries":
                color = ContextCompat.getColor(context, R.color.green);
                break;
            case "Clothing":
                color = ContextCompat.getColor(context, R.color.blue);
                break;
            case "Food":
                color = ContextCompat.getColor(context, R.color.purple);
                break;
            case "Entertainment":
                color = ContextCompat.getColor(context, R.color.pink);
                break;
            case "Fuel":
                color = ContextCompat.getColor(context, R.color.orange);
                break;
            default:
                color = ContextCompat.getColor(context, R.color.red);
        }

        return color;
    }

    @Override
    public String toString() {
        return "ExpenseModel{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}