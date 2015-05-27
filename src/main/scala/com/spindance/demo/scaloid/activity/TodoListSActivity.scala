package com.spindance.demo.scaloid.activity

import java.text.DateFormat

import android.content.{Context, Intent}
import android.view._
import android.widget.AdapterView.OnItemClickListener
import android.widget._
import com.spindance.demo.scala.data.{TodoSManager, TodoSTask}
import org.scaloid.common._
import com.spindance.demo.R

/** Scaloid Activity for viewing list of TodoTasks */
class TodoListSActivity extends SActivity with OnItemClickListener {

  private var mSortBy:SSpinner = null
  private var mListView:SListView = null

  private val mDateFormat: DateFormat = DateFormat.getDateInstance
  private var mTaskList: Array[TodoSTask] = Array()

  onCreate {

    val pad: Int = getResources.getDimensionPixelSize(R.dimen.layout_padding)

    contentView = new SVerticalLayout {
      this += new SLinearLayout {
        STextView(R.string.sort_by).wrap
        mSortBy = SSpinner().wrap
      }.padding(0, 0, 0, 10 dip).orientation(HORIZONTAL)
      mListView = SListView().fw.backgroundResource(R.drawable.rounded_white_rectangle).divider(R.color.black)
                             .dividerHeight(1 dip).choiceMode(AbsListView.CHOICE_MODE_SINGLE)
    }.padding(pad).backgroundResource(R.color.background)


    val adapter = SArrayAdapter(getResources.getStringArray(R.array.sort_options)).dropDownStyle(_.textSize(20 dip).padding(15 dip))
    mSortBy.setAdapter(adapter)
    mSortBy.onItemSelected(sortList)

    mListView.setOnItemClickListener(this)
  }

  onResume {
    sortList
  }

  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.menu.todolist_menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.action_newtask =>
        showNewTask
        return true
      case _ =>
        return super.onOptionsItemSelected(item)
    }
  }

  def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long) {
    val task_id = mTaskList(position).id
    new Intent().put(task_id).start[TodoItemSActivity]
  }

  private def sortList = {

    mTaskList = TodoSManager.getTodoList
    if (mSortBy.getSelectedItemPosition == 0) {
      mTaskList = mTaskList.sortBy(_.dueDate)
    }
    else {
      mTaskList = mTaskList.sortWith((lhs, rhs) => lhs.priority > rhs.priority)
    }

    mListView.setAdapter(new TodoTaskAdapter(mTaskList))
  }

  private def showNewTask = {
    startActivity(SIntent[TodoItemSActivity])
  }

  private class TodoTaskAdapter(itemArray: Array[TodoSTask]) extends ArrayAdapter[TodoSTask](ctx, 0, itemArray) {
    override def getView(position:Int, convertView: View, parent: ViewGroup): View = {
      var result = convertView
      if (result == null)
        result = LayoutInflater.from(parent.getContext).inflate(R.layout.todo_listitem, null)

      result.find[TextView](R.id.task_name).setText(getItem(position).taskName)
      result.find[TextView](R.id.due_date).setText(mDateFormat.format(getItem(position).dueDate))
      result.find[View](R.id.priority).getBackground.setLevel(getItem(position).priority)

      result
    }
  }
}