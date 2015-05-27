package com.spindance.demo.scaloid.activity

import android.widget.{Button, EditText}
import com.spindance.demo.R
import com.spindance.demo.activity.LoginActivity
import org.scaloid.common._

/**
 * Root activity for launching Java or Scaloid path through demo
 *
 * This is an example of a Scaloid Activity that uses traditional XML layouts but still uses
 * some useful Scaloid shortcuts for finding and mapping widgets to listeners
 */
class LaunchDemoSActivity extends SActivity {

  onCreate {
    setContentView(R.layout.activity_launch)
    find[Button](R.id.demo_java).setOnClickListener(loginJava)
    find[Button](R.id.demo_scaloid).setOnClickListener(loginScaloid)
  }

  def loginJava = {
    startActivity(SIntent[LoginActivity])
  }

  def loginScaloid = {
    startActivity(SIntent[LoginSActivity])
  }
}


