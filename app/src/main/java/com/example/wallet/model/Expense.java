package com.example.wallet.model;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.wallet.R;

import java.util.Date;
import java.util.UUID;

public class Expense {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String desc;
    private String category;
    private Double amount;
    private Date date;

    public Expense(String desc, String category, Double amount, Date date) {
        this.id = UUID.randomUUID().toString();
        this.desc = desc;
        this.category = category;
        this.amount = amount;
        this.date = date;
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

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "desc='" + desc + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
