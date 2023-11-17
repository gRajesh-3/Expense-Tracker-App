package com.example.wallet.view.newexpense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wallet.R;

import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity {
    private ImageButton btnClose;
    private EditText etAmount;
    private EditText etDescription;
    private Spinner etCategory;
    private EditText etDate;
    private Button btnAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        btnClose = findViewById(R.id.btnClose);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        btnClose.setOnClickListener(v -> {
            finish();
        });
        btnAddExpense.setOnClickListener(v -> {
            // add expense to recycler view
        });

        addDatePickerToView();
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
                        String date = dayOfMonth + "-" + month1 +1 + "-" + year1;
                        etDate.setText(date);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });
    }
}