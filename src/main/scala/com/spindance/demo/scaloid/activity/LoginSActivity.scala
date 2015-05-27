package com.spindance.demo.scaloid.activity

import android.app.ProgressDialog
import android.view.Gravity
import android.widget.ImageView.ScaleType
import com.spindance.demo.R
import org.scaloid.common._

import scala.concurrent.Future
import AndroidExecutionContext.exec

/**
 * Scaloid Activity for Login to task list demo app
 */
class LoginSActivity extends SActivity {

  onCreate {
    contentView = new SFrameLayout {
      SImageView().fill.scaleType(ScaleType.CENTER_CROP).imageResource(R.drawable.nyc)
      this += new SVerticalLayout {
        SImageView().<<.wrap.marginBottom(40 dip).marginTop(40 dip).Gravity(Gravity.CENTER_HORIZONTAL).>>.setImageResource(R.drawable.logo)
        val email = SEditText().<<.margin(10 dip).>>.inputType(TEXT_EMAIL_ADDRESS).hint(R.string.email)
        val password = SEditText().<<.margin(10 dip).>>.inputType(TEXT_PASSWORD).hint(R.string.password)
        SButton(R.string.login, performLogin(email.text.toString, password.text.toString))
          .<<.wrap.marginRight(40 dip).marginLeft(40 dip).Gravity(Gravity.CENTER_HORIZONTAL).>>.padding(30 dip, 0, 30 dip, 0).background(R.drawable.bg_selector)
      }.<<.Gravity(Gravity.CENTER).>>.padding(30 dip)
    }

    longToast(R.string.login_intro_msg)
  }

  def performLogin(uname:String, pass:String) = {
    val dlg = ProgressDialog.show(this, null, getString(R.string.busy_login), true, true)

    val f:Future[Unit] = Future {
      Thread.sleep(1000)   // fake some network delay
      scala.io.Source.fromURL(getString(R.string.dummy_login_url)).mkString
    }

    f.onSuccess {
      case _ => dlg.dismiss
        startActivity(SIntent[TodoListSActivity])
        finish
    }
    f.onFailure {
      case t => dlg.dismiss
                toast("Failure: " + t.getMessage)
    }
  }
}
