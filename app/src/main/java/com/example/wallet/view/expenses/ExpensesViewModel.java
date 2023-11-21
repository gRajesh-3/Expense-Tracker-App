package com.example.wallet.view.expenses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.dao.ExpenseDao;
import com.example.wallet.database.ExpenseDatabase;
import com.example.wallet.model.Expense;
import com.example.wallet.model.ExpenseModel;
import com.example.wallet.model.Section;
import com.example.wallet.repository.ExpenseRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Pair;

public class ExpensesViewModel extends ViewModel {
    private ExpenseRepository expenseRepository;
    private Flowable<List<ExpenseModel>> expenses;
    private Flowable<List<Pair<String, List<ExpenseModel>>>> groupedExpenses;
    private ExpenseDao expenseDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ExpensesViewModel(Context context) {
        expenseRepository = new ExpenseRepository(context);
    }

    public Flowable<List<ExpenseModel>> getAllExpenses() {
        return expenseRepository.getAllExpenses();
    }

    public Flowable<Double> getTotalExpense() { return expenseRepository.getTotalExpense(); }

    public void insertExpense(ExpenseModel expense) {
        expenseRepository.insertExpense(expense);
    }

    public void updateExpense(ExpenseModel expense) {
        expenseRepository.updateExpense(expense);
    }

    public void deleteExpense(ExpenseModel expense) {
        expenseRepository.deleteExpense(expense);
    }

    public ExpenseModel getExpense(int id, String sectionName, List<Pair<String, List<ExpenseModel>>> data) {
        List<ExpenseModel> sectionExpenses = null;
        for(Pair<String, List<ExpenseModel>> sec: data) {
            if (sec.getFirst().equals(sectionName)) {
                sectionExpenses = sec.getSecond();
            }
        }
        ExpenseModel expense = null;
        for (ExpenseModel exp: sectionExpenses) {
            if (exp.getId() == id) {
                expense = exp;
            }
        }
        return expense;
    }

    public List<Pair<String, List<ExpenseModel>>> convertToSections(List<ExpenseModel> expenses) {
        List<Pair<String, ExpenseModel>> pairs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        for (ExpenseModel expense: expenses) {
            LocalDateTime localDateTime = expense.getCreatedAt();
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date d = Date.from(instant);
            String date = dateFormat.format(d);
            pairs.add(new Pair<>(date, expense));
        }

        Map<String, List<ExpenseModel>> groups = new HashMap<>();
        for (Pair<String, ExpenseModel> pair: pairs) {
            String date = pair.getFirst();
            if (!groups.containsKey(date)) {
                groups.put(date, new ArrayList<>());
            }
            Objects.requireNonNull(groups.get(date)).add(pair.getSecond());
        }

        List<Pair<String, List<ExpenseModel>>> sections = new ArrayList<>();
        for (Map.Entry<String, List<ExpenseModel>> entry: groups.entrySet()) {
            String date = entry.getKey();
            List<ExpenseModel> expenseList = entry.getValue();

            if (date != null && expenseList != null) {
                sections.add(new Pair<>(date, expenseList));
            }
        }
        sections.sort((Comparator<Pair<String, List<ExpenseModel>>>) (o1, o2) -> {
            try {
                Date date1 = dateFormat.parse(o1.getFirst());
                Date date2 = dateFormat.parse(o2.getFirst());

                return date2.compareTo(date1);
            } catch (Exception e) {
                System.out.println(e);
            }
            return 0;
        });

        return sections;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}