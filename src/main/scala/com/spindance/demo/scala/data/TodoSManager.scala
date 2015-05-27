package com.spindance.demo.scala.data

import java.util.Date

import scala.collection.mutable.ArrayBuffer

/**
 * Utility for managing list of TodoTasks. They are just stored in memory with no persistence
 */
object TodoSManager {
  // Create some dummy tasks to start with
  var taskList:ArrayBuffer[TodoSTask] = ArrayBuffer(TodoSTask("Mow lawn", 1, daysFromToday(2)),
                                              TodoSTask("Do taxes", 3, daysFromToday(4)),
                                              TodoSTask("Grocery shopping", 2, daysFromToday(3)))

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

  private def daysFromToday(days: Int): Date = {
    return new Date(System.currentTimeMillis + 1000 * 60 * 60 * 24 * days)
  }
}