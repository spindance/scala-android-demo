package com.spindance.demo.scala.data

/**
 * Utility for managing list of TodoTasks. They are just stored in memory with no persistence
 */
object TodoSManager {
  var taskList: List[TodoSTask] = List()

  def getTodoList: Array[TodoSTask] = {
    taskList.toArray
  }

  def addTask(task: TodoSTask): Unit = {
    taskList = task :: taskList
  }

  def getTask(id: Int): Option[TodoSTask] = {
    taskList find (_.id == id)
  }

  def deleteTask(id: Int): Unit = {
    taskList = taskList filter (_.id != id)
  }
}