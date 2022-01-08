package be.ap.edu.integratedprojectmobile

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import be.ap.edu.integratedprojectmobile.exam.NewExamActivity

//import kotlinx.android.synthetic.main.popup_window.*

class PopUpWindow : AppCompatActivity() {
    private var popupTitle = ""
    private var popupText = ""
    private var popupButton = ""
    private var darkStatusBar = false
    private var amount = 0
    private val title: TextView = findViewById<TextView>(R.id.popup_window_title)
    val text: TextView = findViewById<TextView>(R.id.popup_window_text)
    val button: Button = findViewById<Button>(R.id.popup_window_button)
    private val background: ConstraintLayout = findViewById<ConstraintLayout>(R.id.popup_window_background)
    private val viewWithBorder: CardView = findViewById<CardView>(R.id.popup_window_view_with_border)
    private val txtAmount: TextView = findViewById<TextView>(R.id.txtAmount)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up_window)

        // Get the data
        val bundle = intent.extras
        popupTitle = bundle?.getString("popuptitle", "Title") ?: ""
        popupText = bundle?.getString("popuptext", "Text") ?: ""
        popupButton = bundle?.getString("popupbtn", "Button") ?: ""
        darkStatusBar = bundle?.getBoolean("darkstatusbar", false) ?: false
        //amount = bundle?.getInt("amount", 0) ?: 0
        val intent = Intent(this, NewExamActivity::class.java)
        intent.putExtra("amount", txtAmount.text.toString())
        startActivity(intent);

        // Set the data

        //title.text = popupTitle
        text.text = popupText
        button.text = popupButton
        //txtAmount.text = amount.toString()

        // Set the Status bar appearance for different API levels
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, true)
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        // If you want dark status bar, set darkStatusBar to true
        if (darkStatusBar) {
            this.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        this.window.statusBarColor = Color.TRANSPARENT
        setWindowFlag(this, false)

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()


        // Fade animation for the Popup Window
        viewWithBorder.alpha = 0f
        viewWithBorder.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()


        // Close the Popup Window when you press the button
        button.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setWindowFlag(activity: Activity, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        win.attributes = winParams
    }


    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#000000"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            background.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        viewWithBorder.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

}