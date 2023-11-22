package com.example.wallet.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.wallet.dao.ExpenseDao;
import com.example.wallet.helper.LocalDateTimeConverter;
import com.example.wallet.model.Expense;

@Database(entities = {Expense.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao getExpenseDao();

    private static ExpenseDatabase sharedInstance;

    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = Room.databaseBuilder(
                context.getApplicationContext(),
                ExpenseDatabase.class,
                "ExpenseTracker"
            )
            .build();
        }

        return sharedInstance;
    }
}
