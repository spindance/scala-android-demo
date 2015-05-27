package com.spindance.demo.scala.data

import java.util.Date

class TodoSTask(var taskName:String, var priority:Int, var dueDate:Date, val id:Int) {
}

object TodoSTask {
  var idCounter = 0    // counter to create unique ids as we create tasks
  def apply(taskName: String, priority:Int, dueDate:Date): TodoSTask = {
    idCounter += 1
    new TodoSTask(taskName, priority, dueDate, idCounter)
  }
}
