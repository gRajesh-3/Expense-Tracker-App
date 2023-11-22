package com.example.wallet.view.newexpense;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wallet.model.Expense;
import com.example.wallet.repository.ExpenseRepository;

import java.util.Objects;

import io.reactivex.rxjava3.core.Flowable;

public class NewExpenseViewModel extends ViewModel {
    ExpenseRepository expenseRepository;
    private final MutableLiveData<Double> etAmount = new MutableLiveData<>(0.0);
    private final MutableLiveData<String> etDescription = new MutableLiveData<>("");
    private final MutableLiveData<String> etCategory = new MutableLiveData<>("");
    private final MutableLiveData<String> etDate = new MutableLiveData<>("");
    private final MutableLiveData<String> etTime = new MutableLiveData<>("");
    public MutableLiveData<Boolean> isEnabled = new MutableLiveData<>(false);

    public NewExpenseViewModel(Context context) {
        expenseRepository = new ExpenseRepository(context);
    }
    public Flowable<Expense> getExpense(int expenseId) { return expenseRepository.getExpense(expenseId); }
    public void insertExpense(Expense expense) {
        expenseRepository.insertExpense(expense);
    }
    public void updateExpense(Expense expense) {
        expenseRepository.updateExpense(expense);
    }
    public void setEtAmount(String amount) {
        double amountInDouble = 0.0;
        if (!Objects.equals(amount, "")) {
            amountInDouble = Double.parseDouble(amount);
        }
        this.etAmount.setValue(amountInDouble);
        isButtonEnabled();
    }
    public void setEtDescription(String description) {
        this.etDescription.setValue(description);
        isButtonEnabled();
    }
    public void setEtCategory(String category) {
        this.etCategory.setValue(category);
        isButtonEnabled();
    }
    public void setEtDate(String date) {
        this.etDate.setValue(date);
        isButtonEnabled();
    }
    public void setEtTime(String time) {
        this.etTime.setValue(time);
        isButtonEnabled();
    }
    public Double getEtAmount() {
        return etAmount.getValue();
    }
    public String getEtDescription() {
        return etDescription.getValue();
    }
    public String getEtCategory() {
        return etCategory.getValue();
    }
    public String getEtDate() {
        return etDate.getValue();
    }
    public String getEtTime() {
        return etTime.getValue();
    }
    public void isButtonEnabled() {
        isEnabled.setValue((Objects.requireNonNull(etAmount.getValue()) > 0) && (Objects.requireNonNull(etDescription.getValue()).length() >= 3) &&
                (Objects.requireNonNull(etCategory.getValue()).length() >= 1) && (Objects.requireNonNull(etDate.getValue()).length() >= 1) &&
                (Objects.requireNonNull(etTime.getValue()).length() >= 1));
    }
}
