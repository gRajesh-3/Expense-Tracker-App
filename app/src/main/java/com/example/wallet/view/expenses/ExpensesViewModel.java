package com.example.wallet.view.expenses;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.model.Expense;
import com.example.wallet.repository.ExpenseRepository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.Pair;

public class ExpensesViewModel extends ViewModel {
    private final ExpenseRepository expenseRepository;
    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>();
    public MutableLiveData<List<Pair<String, List<Expense>>>> filteredExpenses = new MutableLiveData<>();
    private final MutableLiveData<String> searchKey = new MutableLiveData<>("");
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ExpensesViewModel(Context context) {
        expenseRepository = new ExpenseRepository(context);
        getExpenses();
    }

    public String getSearchKey() {
        return searchKey.getValue();
    }

    public void setSearchKey(String key) {
        searchKey.setValue(key);
        filterExpenses();
    }

    public void getExpenses() {
        Disposable disposable =expenseRepository.getAllExpenses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(sections -> {
                expenses.setValue(sections);
                filterExpenses();
            }, throwable -> System.out.println("onError" + throwable.getMessage()));

        compositeDisposable.add(disposable);
    }

    private void filterExpenses() {
        List<Expense> newExpenses = expenses.getValue();
        if (!Objects.equals(searchKey.getValue(), "")) {
            newExpenses = newExpenses.stream()
                    .filter(obj -> obj.getDescription().toLowerCase().contains(searchKey.getValue().toLowerCase()) ||
                            obj.getCategory().toLowerCase().contains(searchKey.getValue().toLowerCase()))
                    .collect(Collectors.toList());
        }
        filteredExpenses.setValue(convertToSections(newExpenses));
    }

    public Flowable<Double> getTotalExpense() { return expenseRepository.getTotalExpense(); }

    public void deleteExpense(Expense expense) {
        expenseRepository.deleteExpense(expense);
    }

    public List<Pair<String, List<Expense>>> convertToSections(List<Expense> expenses) {
        List<Pair<String, Expense>> pairs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        for (Expense expense: expenses) {
            LocalDateTime localDateTime = expense.getCreatedAt();
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            Date d = Date.from(instant);
            String date = dateFormat.format(d);
            pairs.add(new Pair<>(date, expense));
        }

        Map<String, List<Expense>> groups = new HashMap<>();
        for (Pair<String, Expense> pair: pairs) {
            String date = pair.getFirst();
            if (!groups.containsKey(date)) {
                groups.put(date, new ArrayList<>());
            }
            Objects.requireNonNull(groups.get(date)).add(pair.getSecond());
        }

        List<Pair<String, List<Expense>>> sections = new ArrayList<>();
        for (Map.Entry<String, List<Expense>> entry: groups.entrySet()) {
            String date = entry.getKey();
            List<Expense> expenseList = entry.getValue();

            if (date != null && expenseList != null) {
                sections.add(new Pair<>(date, expenseList));
            }
        }
        sections.sort((o1, o2) -> {
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