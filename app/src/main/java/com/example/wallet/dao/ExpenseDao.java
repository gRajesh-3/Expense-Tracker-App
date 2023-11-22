package com.example.wallet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wallet.model.Expense;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM Expense ORDER BY createdAt DESC")
    Flowable<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM Expense WHERE id = :expenseId")
    Flowable<Expense> getExpense(int expenseId);

    @Query("SELECT SUM(amount) from Expense")
    Flowable<Double> getTotalExpense();

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);
}