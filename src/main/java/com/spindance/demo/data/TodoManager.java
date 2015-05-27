package com.spindance.demo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Class for managing list of TodoTasks. They are just stored in memory with no persistence
 */
public class TodoManager {

    private static List<TodoTask> mTodoList = new ArrayList<TodoTask>();

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
}
