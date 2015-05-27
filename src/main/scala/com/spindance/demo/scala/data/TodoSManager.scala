package com.spindance.demo.scala.data


import scala.collection.mutable.ArrayBuffer

/**
 * Utility for managing list of TodoTasks. They are just stored in memory with no persistence
 */
object TodoSManager {
  var taskList:ArrayBuffer[TodoSTask] = ArrayBuffer()

  def getTodoList: Array[TodoSTask] = {
    taskList.toArray
  }

  def addTask(task:TodoSTask) = {
    taskList += task
  }

  def getTask(id: Int): TodoSTask = {
    taskList.find(_.id == id).getOrElse(null)
  }

  def deleteTask(id: Int): Unit = {
    taskList = taskList.filter( _.id != id)
  }
}