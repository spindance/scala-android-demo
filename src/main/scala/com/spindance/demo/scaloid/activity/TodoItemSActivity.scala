package com.spindance.demo.scaloid.activity

import java.text.DateFormat
import java.util.{Calendar, GregorianCalendar, Date}

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.text.TextUtils
import android.view.{View, MenuItem, Gravity}
import android.widget.DatePicker
import com.spindance.demo.scala.data.{TodoSTask, TodoSManager}
import org.scaloid.common._
import com.spindance.demo.R
import language.postfixOps

/** Scaloid Activity for adding or editing a TodoTask */
class TodoItemSActivity extends SActivity {

  private var mPrioritySpinner: SSpinner = _
  private var mDueDateButton: SButton = _
  private var mDeleteButton: SButton = _
  private var mTaskName: SEditText = _

  private val mDateFormat: DateFormat = DateFormat.getDateInstance
  private var mTask: Option[TodoSTask] = None

  onCreate {
    val pad: Int = getResources.getDimensionPixelSize(R.dimen.layout_padding)

    contentView = new SFrameLayout {
      this += new SVerticalLayout {
        mTaskName = SEditText()
          .<<.marginBottom(pad).>>
          .hint(R.string.task_name)
          .fw

        this += new SLinearLayout {
                    STextView(R.string.priority).wrap.marginRight(pad)
                    mPrioritySpinner = SSpinner().wrap.prompt(R.string.priority)
                  }.wrap.<<.marginBottom(pad).>>

        this += new SLinearLayout {
                  STextView(R.string.due_date).wrap.marginRight(pad)
                  mDueDateButton = SButton(mDateFormat.format(new Date()), dueDatePressed()).wrap
                }.wrap.<<.marginBottom(pad).>>

        this += new SLinearLayout {
          SButton(R.string.save, savePressed())
            .padding(30 dip, 0, 30 dip, 0)
            .background(R.drawable.bg_selector)

          mDeleteButton = SButton(R.string.delete, deletePressed())
            .<<.marginLeft(20 dip).>>
            .padding(30 dip, 0, 30 dip, 0)
            .background(R.drawable.bg_selector)

        }.wrap.<<.Gravity(Gravity.CENTER_HORIZONTAL).>>
      }.backgroundResource(R.color.background).fill.padding(pad)
    }

    val adapter = SArrayAdapter(getResources.getStringArray(R.array.priorities)).dropDownStyle(_.textSize(20 dip).padding(15 dip))
    mPrioritySpinner.setAdapter(adapter)

    mTask = TodoSManager.getTask(getIntent.getIntExtra("task_id", -1))

    getActionBar.setDisplayHomeAsUpEnabled(true)

    initView()
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home =>
        finish()
        true
      case _ =>
        super.onOptionsItemSelected(item)
    }
  }

  private def dueDatePressed() = {
    val cal: GregorianCalendar = new GregorianCalendar
    cal.setTime(mDateFormat.parse(mDueDateButton.getText.toString))

    val dlg: DatePickerDialog = new DatePickerDialog(this, dateSelected, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
    dlg.show()
  }

  private def deletePressed() = {
    new AlertDialogBuilder(null, getString(R.string.delete_confirmation)) {
      positiveButton(R.string.ok, deleteConfirmed())
      negativeButton(R.string.cancel)
    }.show()
  }

  private def deleteConfirmed() = {
    for (t <- mTask) { TodoSManager.deleteTask(t.id) }
    finish()
  }

  private def savePressed() = {
    if (!TextUtils.isEmpty(mTaskName.getText.toString)) {
      mTask match {
        case Some(t) =>
          t.dueDate = mDateFormat.parse(mDueDateButton.getText.toString)
          t.priority = mPrioritySpinner.getSelectedItemPosition
          t.taskName = mTaskName.getText.toString
        case None =>
          TodoSManager.addTask(TodoSTask(mTaskName.getText.toString, mPrioritySpinner.getSelectedItemPosition, mDateFormat.parse(mDueDateButton.getText.toString)))
      }
    }
    finish()
  }

  private def initView() = {
    mTask match {
      case Some(t) =>
        setTitle(R.string.edit_task)
        mTaskName.setText(t.taskName)
        mPrioritySpinner.setSelection(t.priority)
        mDueDateButton.setText(mDateFormat.format(t.dueDate))
      case None =>
        setTitle(R.string.create_task)
        mDeleteButton.setVisibility(View.GONE)
    }
  }

  private val dateSelected: OnDateSetListener = new OnDateSetListener {
    override def onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int): Unit = {
      val cal: GregorianCalendar = new GregorianCalendar
      cal.set(Calendar.YEAR, year)
      cal.set(Calendar.MONTH, monthOfYear)
      cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
      mDueDateButton.setText(mDateFormat.format(cal.getTime))
    }
  }
}