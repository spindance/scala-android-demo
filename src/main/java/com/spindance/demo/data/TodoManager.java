package com.spindance.demo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Class for managing list of TodoTasks. They are just stored in memory with no persistence
 */
public class TodoManager {

    private static List<TodoTask> mTodoList = new ArrayList<TodoTask>();

    static {
        // Create some dummy tasks to start with
        mTodoList.add(new TodoTask("Grocery shopping", 2, daysFromToday(3)));
        mTodoList.add(new TodoTask("Mow lawn", 1, daysFromToday(5)));
        mTodoList.add(new TodoTask("Do taxes", 3, daysFromToday(2)));
    }

    public static List<TodoTask> getTodoList() {
        return Collections.unmodifiableList(mTodoList);
    }

    public static void addItem(TodoTask t) {
        mTodoList.add(t);
    }

    public static TodoTask getTask(int id) {
        for (TodoTask t : mTodoList) {
            if (t.getId() == id)
                return t;
        }
        return null;
    }

    public static void deleteTask(int id) {
        for (Iterator<TodoTask> iter = mTodoList.iterator(); iter.hasNext(); ) {
            if (iter.next().getId() == id) {
                iter.remove();;
                return;
            }
        }
    }

    private static Date daysFromToday(int days) {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days);
    }
}
