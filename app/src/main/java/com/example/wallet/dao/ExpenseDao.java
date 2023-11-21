package com.example.wallet.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.wallet.model.ExpenseModel;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expense ORDER BY createdAt DESC")
    Flowable<List<ExpenseModel>> getAllExpenses();

    @Query("SELECT * FROM expense WHERE id = :expenseId")
    Flowable<ExpenseModel> getExpense(int expenseId);

    @Query("SELECT SUM(amount) from expense")
    Flowable<Double> getTotalExpense();

    @Insert
    void insert(ExpenseModel expense);

    @Update
    void update(ExpenseModel expense);

    @Delete
    void delete(ExpenseModel expense);
}