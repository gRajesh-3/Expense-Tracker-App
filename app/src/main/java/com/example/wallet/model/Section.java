package com.example.wallet.model;

import java.util.List;
import java.util.UUID;

public class Section {
    private String id;
    private String sectionName;
    private List<Expense> expenses;

    public Section(String sectionName, List<Expense> expenses) {
        this.id = UUID.randomUUID().toString();
        this.sectionName = sectionName;
        this.expenses = expenses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", expenses=" + expenses +
                '}';
    }
}
