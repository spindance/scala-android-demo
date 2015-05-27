package com.spindance.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.spindance.demo.R;
import com.spindance.demo.data.TodoManager;
import com.spindance.demo.data.TodoTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Standard Java Activity for Login to task list demo app
 */
public class LoginActivity extends Activity {
    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mEmailText = (EditText)findViewById(R.id.form_login_email);
        mPasswordText = (EditText)findViewById(R.id.form_login_password);
        mLoginButton = (Button)findViewById(R.id.form_login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin(mEmailText.getText().toString(), mPasswordText.getText().toString());
            }
        });

        Toast.makeText(this, R.string.login_intro_msg, Toast.LENGTH_LONG).show();
    }


    private void performLogin(String email, String password) {
        final ProgressDialog dlg = ProgressDialog.show(this, null, getString(R.string.busy_login), true, true);

        AsyncTask<Void, Void, List<TodoTask>> loadingTask = new AsyncTask<Void, Void, List<TodoTask>>() {
            @Override
            protected List<TodoTask> doInBackground(Void... params) {
                return loadTasks();
            }

            @Override
            protected void onPostExecute(List<TodoTask> todoTasks) {
                dlg.dismiss();
                for (TodoTask task : todoTasks) {
                    TodoManager.addItem(task);
                }
                startActivity(new Intent(LoginActivity.this, TodoListActivity.class));
                finish();
            }

            @Override
            protected void onCancelled(List<TodoTask> todoTasks) {
                dlg.dismiss();
            }
        };

        loadingTask.execute();
    }

    private List<TodoTask> loadTasks() {
        try { Thread.sleep(1000); } catch (Exception e) {}
        List<TodoTask> taskList = new ArrayList<TodoTask>();
        taskList.add(new TodoTask("Grocery Shopping", 2, daysFromToday(3)));
        taskList.add(new TodoTask("Mow Lawn", 1, daysFromToday(5)));
        taskList.add(new TodoTask("Do Taxes", 3, daysFromToday(2)));
        return taskList;
    }

    private static Date daysFromToday(int days) {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days);
    }
}
