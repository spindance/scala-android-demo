package com.spindance.demo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.spindance.demo.data.TodoManager;
import com.spindance.demo.data.TodoTask;

import com.spindance.demo.R;

/**
 * Standard Java Activity for adding or editing a TodoTask
 */
public class TodoItemActivity extends Activity {
    public static final String EXTRA_TASK = "TodoItem.Task";

    private TodoTask mTask;
    private EditText mName;
    private Spinner mPriority;
    private Button mDueDate;
    private Button mSave;
    private Button mDelete;

    public static Intent newCreateIntent(Context ctx) {
        Intent intent = new Intent(ctx, TodoItemActivity.class);
        return intent;
    }

    public static Intent newEditIntent(Context ctx, int taskId) {
        Intent intent = new Intent(ctx, TodoItemActivity.class);
        intent.putExtra(EXTRA_TASK, taskId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todoitem);
        mDueDate = (Button) findViewById(R.id.due_date);
        mPriority = (Spinner) findViewById(R.id.priority);
        mName = (EditText) findViewById(R.id.task_name);
        mSave = (Button) findViewById(R.id.save_button);
        mDelete = (Button) findViewById(R.id.delete_button);

        mTask = TodoManager.getTask(getIntent().getIntExtra(EXTRA_TASK, -1));
        initView();

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {
        ActionBar ab = getActionBar();
        ab.setTitle(mTask == null ? R.string.create_task : R.string.edit_task);
        if (mTask != null) {
            mName.setText(mTask.getTaskName());
            mPriority.setSelection(mTask.getPriority());
            mDueDate.setText(SimpleDateFormat.getDateInstance().format(mTask.getDueDate()));
        } else {
            mDueDate.setText(SimpleDateFormat.getDateInstance().format(new Date()));
            mDelete.setVisibility(View.GONE);
        }

        mSave.setOnClickListener(saveClicked);
        mDueDate.setOnClickListener(dateClicked);
        mDelete.setOnClickListener(deleteClicked);
    }


    private View.OnClickListener saveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Date dueDate = SimpleDateFormat.getDateInstance().parse(mDueDate.getText().toString());
                if (mTask == null) {
                    mTask = new TodoTask(mName.getText().toString(), mPriority.getSelectedItemPosition(), dueDate);
                    TodoManager.addItem(mTask);
                } else {
                    mTask.setTaskName(mName.getText().toString());
                    mTask.setDueDate(dueDate);
                    mTask.setPriority(mPriority.getSelectedItemPosition());
                }
            } catch (Exception e) {
                Log.e(TodoItemActivity.class.getName(), e.getMessage());
            }
            finish();
        }
    };

    private View.OnClickListener deleteClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(TodoItemActivity.this)
                    .setMessage(R.string.delete_confirmation)
                    .setPositiveButton(R.string.ok, deleteConfirmed)
                    .setNegativeButton(R.string.cancel, cancelDelete)
                    .show();
        }
    };

    private DialogInterface.OnClickListener deleteConfirmed = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            TodoManager.deleteTask(mTask.getId());
            finish();
        }
    };

    private DialogInterface.OnClickListener cancelDelete = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private View.OnClickListener dateClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(SimpleDateFormat.getDateInstance().parse(mDueDate.getText().toString()));

                DatePickerDialog dlg = new DatePickerDialog(TodoItemActivity.this, dateSelected, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dlg.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener dateSelected = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mDueDate.setText(SimpleDateFormat.getDateInstance().format(cal.getTime()));
        }
    };
}