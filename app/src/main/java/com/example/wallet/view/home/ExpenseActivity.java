package com.example.wallet.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wallet.R;
import com.example.wallet.model.BottomSheet;
import com.example.wallet.model.Expense;
import com.example.wallet.model.Section;
import com.example.wallet.view.adapter.OnItemClickListener;
import com.example.wallet.view.adapter.TransactionAdapter;
import com.example.wallet.view.newexpense.NewExpenseActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private final String TAG = "My ExpenseActivity";
    private List<Section> sections = new ArrayList<>();
    private TextView tvName;
    private ImageButton ibEditIcon;
    private ImageButton btnAddExpense;
    private TextView tvExpenseAmount;
    private ImageButton ibSearchIcon;
    private ImageButton ibChartIcon;
    private RecyclerView rvTransaction;
    private String sectionId, expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        tvName = findViewById(R.id.tvName);
        ibEditIcon = findViewById(R.id.ibEditIcon);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        tvExpenseAmount = findViewById(R.id.tvExpenseAmount);
        ibSearchIcon = findViewById(R.id.ibSearchIcon);
        ibChartIcon = findViewById(R.id.ibChartIcon);
//        ibFilterIcon = findViewById(R.id.ibFilterIcon);
        rvTransaction = findViewById(R.id.rvTransaction);

        getExpenses();

        ibEditIcon.setOnClickListener(v -> {
            showBottomSheet(BottomSheet.NAME);
        });
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseActivity.this, NewExpenseActivity.class);
            startActivity(intent);
        });
        ibSearchIcon.setOnClickListener(v -> {
            showBottomSheet(BottomSheet.SEARCH);
        });
        ibChartIcon.setOnClickListener(v -> {
            showBottomSheet(BottomSheet.CHART);
        });
//        ibFilterIcon.setOnClickListener(v -> {
//            // Open Bottom Sheet for filters
//        });
        TransactionAdapter transactionAdapter = new TransactionAdapter(sections, this, (sectionId, expenseId) -> {
            this.sectionId = sectionId;
            this.expenseId = expenseId;
            showBottomSheet(BottomSheet.INFO);
        });


        rvTransaction.setAdapter(transactionAdapter);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    private void getExpenses() {
        List<Expense> todayExpenses = new ArrayList<>();
        todayExpenses.add(new Expense("Biriyani", "Food", 150.0, new Date()));
        todayExpenses.add(new Expense("Shirt", "Clothing", 1700.0, new Date()));
        Section section1 = new Section(
                "Today",
                todayExpenses
        );

        List<Expense> yesterdayExpenses = new ArrayList<>();
        yesterdayExpenses.add(new Expense("Biriyani", "Personal care", 150.0, new Date()));
        yesterdayExpenses.add(new Expense("Shirt", "Fuel", 1700.0, new Date()));
        Section section2 = new Section(
                "Yesterday",
                yesterdayExpenses
        );

        List<Expense> otherExpenses = new ArrayList<>();
        otherExpenses.add(new Expense("Biriyani", "Personal care", 150.0, new Date()));
        otherExpenses.add(new Expense("Shirt", "Fuel", 1700.0, new Date()));
        Section section3 = new Section(
                "13 Nov, 2023",
                yesterdayExpenses
        );

        sections.add(section1);
        sections.add(section2);
        sections.add(section3);
    }

    private void showBottomSheet(BottomSheet bottomSheet) {
        Dialog dialog = null;

        switch (bottomSheet) {
            case NAME:
                dialog = getDialog(R.layout.bottom_sheet_name);
                EditText etName = dialog.findViewById(R.id.etName);
                Button btnAddName = dialog.findViewById(R.id.btnAddName);
                btnAddName.setOnClickListener(v -> {
                    Log.i(TAG, etName.toString());
                });
                break;
            case INFO:
                dialog = getDialog(R.layout.bottom_sheet_info);
                TextView tvInfoDesc = dialog.findViewById(R.id.tvInfoDesc);
                ImageButton ibInfoEdit = dialog.findViewById(R.id.ibInfoEdit);
                TextView tvInfoCategory = dialog.findViewById(R.id.tvInfoCategory);
                TextView tvInfoAmount = dialog.findViewById(R.id.tvInfoAmount);
                TextView tvInfoDate = dialog.findViewById(R.id.tvInfoDate);
                Button btnInfoDelete = dialog.findViewById(R.id.btnInfoDelete);

                Section selectedSection = getSection();
                Expense selectedExpense = getExpense(selectedSection);

                tvInfoDesc.setText(selectedExpense.getDesc());
                tvInfoCategory.setText(selectedExpense.getCategory());
                tvInfoCategory.setTextColor(selectedExpense.getColor(this));
                tvInfoAmount.setText(selectedExpense.getAmount().toString());
                tvInfoDate.setText(selectedExpense.getDate().toString());

                ibInfoEdit.setOnClickListener(v -> {
                    // Call Add Page With Data
                });

                btnInfoDelete.setOnClickListener(v -> {
                    // Delete the expense from DB
                });

                break;
            case SEARCH:
                dialog = getDialog(R.layout.bottom_sheet_search);
                EditText etFilter = dialog.findViewById(R.id.etFilter);
                Button btnFilter = dialog.findViewById(R.id.btnFilter);
                btnFilter.setOnClickListener(v -> {
//                    Log.i(TAG, svFilter.);
                });
                break;
            case CHART:
                dialog = getDialog(R.layout.bottom_sheet_chart);
                PieChart pieChart = dialog.findViewById(R.id.pieChart);
                pieChart.addPieSlice(
                        new PieModel("Personal care", 15, Color.parseColor("#FF2F92"))
                );
                pieChart.addPieSlice(
                        new PieModel("Groceries", 10, Color.parseColor("#009192"))
                );
                pieChart.addPieSlice(
                        new PieModel("Clothing", 35, Color.parseColor("#0096FF"))
                );
                pieChart.addPieSlice(
                        new PieModel("Food", 25, Color.parseColor("#D783FF"))
                );
                pieChart.addPieSlice(
                        new PieModel("Entertainment", 5, Color.parseColor("#FF2F92"))
                );
                pieChart.addPieSlice(
                        new PieModel("Fuel", 10, Color.parseColor("#fb8500"))
                );

                pieChart.startAnimation();

                break;
        }

        if(dialog != null) {
            dialog.show();
            View backdrop = getBackdrop();
            addToRootView(backdrop, dialog);
        }
    }

    private View getBackdrop() {
        View backdrop = new View(this);
        backdrop.setBackgroundColor(Color.BLACK);
        backdrop.setAlpha(0.5f);
        backdrop.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );

        return backdrop;
    }

    private void addToRootView(View view, Dialog dialog) {
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.addView(view);

        dialog.setOnDismissListener(dialog1 -> {
            rootView.removeView(view);
        });
    }

    private Dialog getDialog(int layout) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }

    private Section getSection() {
        Section selectedSection = null;
        for (Section section: sections) {
            if (section.getId().equals(sectionId)) {
                selectedSection = section;
            }
        }

        return selectedSection;
    }

    private Expense getExpense(Section section) {
        List<Expense> expenses = section.getExpenses();
        Expense selectedExpense = null;
        for (Expense expense: expenses) {
            if (expense.getId().equals(expenseId)) {
                selectedExpense = expense;
            }
        }
        return selectedExpense;
    }

    // TODO
    /*
        1. Add app icon similar to one done in Swift and colors - DONE
        3. Add parent and child recycler view for sectioned list - DONE
        4. Add swipe-able feature *
        4.5 add chart - done
        5. Add FBI - DONE
        6. Add popover - done
        7. Add add new expense page with animation - done
        8. Add Room database
        9. Add filter expenses by search
    */

    // TODO
    /*
    Viewmodel
    1. sections - live data for
    2. filteredSections - live data for display
    2. selectedExpense - livedata for popup and edit if needed
     */
}