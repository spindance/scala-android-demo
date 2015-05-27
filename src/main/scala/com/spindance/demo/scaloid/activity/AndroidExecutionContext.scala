package com.spindance.demo.scaloid.activity

import android.os.AsyncTask

import scala.concurrent.ExecutionContext

/**
 * implicit execution context for use with Future
 */
object AndroidExecutionContext {
  implicit val exec = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
}
