package com.example.wallet.view.newexpense;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.wallet.R;
import com.example.wallet.helper.Helper;
import com.example.wallet.helper.OnTextChangeListener;
import com.example.wallet.helper.ViewModelFactory;
import com.example.wallet.model.ExpenseModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewExpenseActivity extends AppCompatActivity {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NewExpenseViewModel newExpenseViewModel;
    EditText etAmount;
    EditText etDescription;
    Spinner etCategory;
    private EditText etDate;
    private EditText etTime;
    Button btnAddExpense;
    private int updateExpenseId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        ViewModelFactory<NewExpenseViewModel> factory = new ViewModelFactory<>(this, NewExpenseViewModel.class);
        newExpenseViewModel = new ViewModelProvider(this, factory).get(NewExpenseViewModel.class);

        ImageButton btnClose = findViewById(R.id.btnClose);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        getIntentData();
        addTextChangeListener(etAmount, text -> newExpenseViewModel.setEtAmount(text));
        addTextChangeListener(etDescription, text -> newExpenseViewModel.setEtDescription(text));
        etCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                newExpenseViewModel.setEtCategory(selectedItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        addTextChangeListener(etDate, text -> newExpenseViewModel.setEtDate(text));
        addTextChangeListener(etTime, text -> newExpenseViewModel.setEtTime(text));
        btnClose.setOnClickListener(v -> finish());
        newExpenseViewModel.isEnabled.observe(this, btnAddExpense::setEnabled);
        btnAddExpense.setOnClickListener(v -> addExpense());

        addDatePickerToView();
        addTimePickerToView();
    }
    private void getIntentData() {
        Intent intent = getIntent();
        int expenseId = intent.getIntExtra("expenseId", -1);
        if (expenseId != -1) {
            Disposable disposable = newExpenseViewModel.getExpense(expenseId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(expense -> {
                        updateExpenseId = expense.getId();
                        etAmount.setText(expense.getAmount().toString());
                        etDescription.setText(expense.getDescription());
                        int position = getSpinnerPosition(expense.getCategory());
                        System.out.println(position);
                        etCategory.setSelection(position);
                        LocalDateTime dateTime = expense.getCreatedAt();
                        etDate.setText(Helper.formatDate(dateTime));
                        etTime.setText(Helper.formatTime(dateTime));
                        btnAddExpense.setText("Update");
                    });
            compositeDisposable.add(disposable);
        }
    }

    private int getSpinnerPosition(String category) {
        String[] categories = getResources().getStringArray(R.array.categories);
        int position = -1;
        for(int i=0;i<categories.length;i++) {
            if (categories[i].equals(category)) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void addExpense() {
        Double amount = newExpenseViewModel.getEtAmount();
        String description = newExpenseViewModel.getEtDescription();
        String category = newExpenseViewModel.getEtCategory();
        String date = newExpenseViewModel.getEtDate();
        String time = newExpenseViewModel.getEtTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date+"T"+time, dateTimeFormatter);
        ExpenseModel newExpense = new ExpenseModel(description, category, amount, localDateTime);

        if (updateExpenseId == -1) {
            newExpenseViewModel.insertExpense(newExpense);
        } else {
            newExpense.setId(updateExpenseId);
            newExpenseViewModel.updateExpense(newExpense);
        }

        finish();
    }

    private void addDatePickerToView() {
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    NewExpenseActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String date = dayOfMonth + "-" + (month1+1) + "-" + year1;
                        etDate.setText(date);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });
    }

    private void addTimePickerToView() {
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String hours = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                    String minutes = minute < 10 ? "0"+minute : ""+minute;
                    etTime.setText(hours + ":" + minutes);
                },
                mHour, mMinute, false);
            timePickerDialog.show();
        });
    }

    private void addTextChangeListener(EditText et, OnTextChangeListener onTextChangeListener) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { onTextChangeListener.onTextChange(s.toString()); }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}