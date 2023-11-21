package com.example.wallet.repository;

import android.content.Context;
import android.util.Log;

import com.example.wallet.dao.ExpenseDao;
import com.example.wallet.database.ExpenseDatabase;
import com.example.wallet.model.ExpenseModel;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExpenseRepository {
    public static final String TAG = "Expense Repo";

    private ExpenseDao expenseDao;

    public ExpenseRepository(Context context) {
        ExpenseDatabase expenseDatabase = ExpenseDatabase.getInstance(context);
        expenseDao = expenseDatabase.getExpenseDao();
    }

    public Flowable<List<ExpenseModel>> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }

    public Flowable<ExpenseModel> getExpense(int expenseId) { return expenseDao.getExpense(expenseId); }

    public Flowable<Double> getTotalExpense() { return expenseDao.getTotalExpense(); }

    public void insertExpense(ExpenseModel expense) {
        Completable.fromAction(() -> expenseDao.insert(expense)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG, "onSubscribe: Called");
                }
                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: Called");
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: "+e.getMessage());
                }
            });
    }

    public void updateExpense(ExpenseModel expense) {
        Completable.fromAction(() -> expenseDao.update(expense)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG, "onSubscribe: Called");
                }
                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: Called");
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: "+e.getMessage());
                }
            });
    }

    public void deleteExpense(ExpenseModel expense) {
        Completable.fromAction(() -> expenseDao.delete(expense)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new CompletableObserver() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG, "onSubscribe: Called");
                }
                @Override
                public void onComplete() {
                    Log.d(TAG, "onComplete: Called");
                }
                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "onError: "+e.getMessage());
                }
            });
    }
}
