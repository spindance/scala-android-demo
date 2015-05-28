package com.spindance.demo.scala.data

import java.util.Date

/** Scala Task class */
case class TodoSTask(var taskName: String, var priority: Int, var dueDate: Date, id: Int)

/** Simple companion object to maintain id counter and a factory for creating tasks */
object TodoSTask {
  var idCounter = 0    // counter to create unique ids as we create tasks
  def apply(taskName: String, priority: Int, dueDate: Date): TodoSTask = {
    idCounter += 1
    new TodoSTask(taskName, priority, dueDate, idCounter)
  }
}
