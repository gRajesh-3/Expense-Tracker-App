package com.example.wallet.view.expenses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.R;
import com.example.wallet.helper.Helper;
import com.example.wallet.helper.ViewModelFactory;
import com.example.wallet.model.BottomSheet;
import com.example.wallet.model.Constant;
import com.example.wallet.model.ExpenseModel;
import com.example.wallet.view.adapter.TransactionAdapter;
import com.example.wallet.view.newexpense.NewExpenseActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ExpensesActivity extends AppCompatActivity {
    private final String TAG = "My ExpenseActivity";
    SharedPreferences sharedPreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ExpensesViewModel expensesViewModel;
    TextView profileIcon, tvName;
    private RecyclerView rvTransaction;
    private ExpenseModel tappedExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        sharedPreferences = getSharedPreferences(Constant.PREFERENCE, Context.MODE_PRIVATE);
        ViewModelFactory<ExpensesViewModel> factory = new ViewModelFactory<>(this, ExpensesViewModel.class);
        expensesViewModel = new ViewModelProvider(this, factory).get(ExpensesViewModel.class);

        profileIcon = findViewById(R.id.profileIcon);
        tvName = findViewById(R.id.tvName);
        ImageButton ibEditIcon = findViewById(R.id.ibEditIcon);
        ImageButton btnAddExpense = findViewById(R.id.btnAddExpense);
        TextView tvExpenseAmount = findViewById(R.id.tvExpenseAmount);
        ImageButton ibSearchIcon = findViewById(R.id.ibSearchIcon);
        rvTransaction = findViewById(R.id.rvTransaction);

        String username = getUsername();
        profileIcon.setText(""+username.charAt(0));
        tvName.setText("Hello "+username);
        ibEditIcon.setOnClickListener(v -> showBottomSheet(BottomSheet.NAME));
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpensesActivity.this, NewExpenseActivity.class);
            startActivity(intent);
        });
        ibSearchIcon.setOnClickListener(v -> showBottomSheet(BottomSheet.SEARCH));

        Disposable disposable1 = expensesViewModel.getTotalExpense()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(amount -> tvExpenseAmount.setText("₹"+amount));
        compositeDisposable.add(disposable1);
        Disposable disposable2 = expensesViewModel.getAllExpenses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(expenses -> expensesViewModel.convertToSections(expenses))
            .subscribe(sections -> {
                TransactionAdapter transactionAdapter = new TransactionAdapter(sections, this, (expense) -> {
                    this.tappedExpense = expense;
                    showBottomSheet(BottomSheet.INFO);
                });
                rvTransaction.setAdapter(transactionAdapter);
            }, throwable -> System.out.println("onError" + throwable.getMessage()));
        compositeDisposable.add(disposable2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
    }

    private String getUsername() {
        return sharedPreferences.getString(Constant.PREFERENCE_NAME, "Guest");
    }
    private void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constant.PREFERENCE_NAME, username);
        editor.apply();
    }
    private void showBottomSheet(BottomSheet bottomSheet) {
        Dialog d, dialog = null;

        switch (bottomSheet) {
            case NAME:
                dialog = getDialog(R.layout.bottom_sheet_name);
                EditText etName = dialog.findViewById(R.id.etName);
                Button btnAddName = dialog.findViewById(R.id.btnAddName);
                d = dialog;
                btnAddName.setOnClickListener(v -> {
                    String username = String.valueOf(etName.getText());
                    setUsername(username);
                    profileIcon.setText(""+username.charAt(0));
                    tvName.setText("Hello "+username);
                    d.dismiss();
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

                d = dialog;
                if (tappedExpense != null) {
                    tvInfoDesc.setText(tappedExpense.getDescription());
                    tvInfoCategory.setText(tappedExpense.getCategory());
                    tvInfoCategory.setTextColor(tappedExpense.getColor(this));
                    tvInfoAmount.setText(tappedExpense.getAmount().toString());
                    tvInfoDate.setText(Helper.formatDateTime(tappedExpense.getCreatedAt()));

                    ibInfoEdit.setOnClickListener(v -> {
                        Intent intent = new Intent(ExpensesActivity.this, NewExpenseActivity.class);
                        intent.putExtra("expenseId", tappedExpense.getId());
                        d.dismiss();
                        startActivity(intent);
                    });

                    btnInfoDelete.setOnClickListener(v -> {
                        expensesViewModel.deleteExpense(tappedExpense);
                        d.dismiss();
                    });
                }

                break;
            case SEARCH:
                dialog = getDialog(R.layout.bottom_sheet_search);
                EditText etFilter = dialog.findViewById(R.id.etFilter);
                Button btnFilter = dialog.findViewById(R.id.btnFilter);
                btnFilter.setOnClickListener(v -> {
//                    Log.i(TAG, svFilter.);
                });
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

        dialog.setOnDismissListener(dialog1 -> rootView.removeView(view));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    // TODO
    /*
    1. Name feature
        4. Add swipe-able feature *
        9. Add filter expenses by search

        -> create two
    */

}