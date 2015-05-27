package com.spindance.demo.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Arrays;
import java.util.Comparator;

import com.spindance.demo.data.TodoManager;
import com.spindance.demo.data.TodoTask;

import com.spindance.demo.R;

/**
 * Standard Java Activity for viewing list of TodoTasks
 */
public class TodoListActivity extends ListActivity {
    private TodoListAdapter mAdapter;
    private Spinner mSortSpinner;

    private static DateFormat mDateFormat = DateFormat.getDateInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todolist);
        mSortSpinner = (Spinner)findViewById(R.id.sort_spinner);
        mSortSpinner.setOnItemSelectedListener(sortChanged);

        setupActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todolist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_newtask:
                showNewTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TodoTask task = mAdapter.getItem(position);
        Intent intent = TodoItemActivity.newEditIntent(this, task.getId());
        startActivity(intent);
    }

    private void setupActionBar() {
        ActionBar ab = getActionBar();
        ab.setTitle(R.string.todo_title);
    }

    private void setupAdapter() {
        TodoTask[] arr = new TodoTask[TodoManager.getTodoList().size()];
        TodoManager.getTodoList().toArray(arr);
        int idx = mSortSpinner.getSelectedItemPosition();
        if (idx == 0) {
            Arrays.sort(arr, new Comparator<TodoTask>() {
                @Override
                public int compare(TodoTask lhs, TodoTask rhs) {
                    return lhs.getDueDate().compareTo(rhs.getDueDate());
                }
            });
        }
        else if (idx == 1) {
            Arrays.sort(arr, new Comparator<TodoTask>() {
                @Override
                public int compare(TodoTask lhs, TodoTask rhs) {
                    return Integer.valueOf(rhs.getPriority()).compareTo(lhs.getPriority());
                }
            });
        }

        mAdapter = new TodoListAdapter(this, arr);
        setListAdapter(mAdapter);
    }

    private void showNewTask() {
        Intent intent = TodoItemActivity.newCreateIntent(this);
        startActivity(intent);
    }

    private AdapterView.OnItemSelectedListener sortChanged = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setupAdapter();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private static class TodoListAdapter extends ArrayAdapter<TodoTask> {
        public TodoListAdapter(Context context, TodoTask[] taskList) {
            super(context, R.layout.todo_listitem, taskList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_listitem, null);
            }
            TextView name = (TextView)convertView.findViewById(R.id.task_name);
            name.setText(getItem(position).getTaskName());
            TextView dueDate = (TextView)convertView.findViewById(R.id.due_date);
            dueDate.setText(mDateFormat.format(getItem(position).getDueDate()));
            View priority = convertView.findViewById(R.id.priority);
            priority.getBackground().setLevel(getItem(position).getPriority());
            return convertView;
        }
    }
}