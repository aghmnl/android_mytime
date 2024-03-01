package com.followapp.mytime

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.inputmethod.InputMethodManager
//import com.followapp.mytime.dataClasses.Chronometer
//import android.content.Context
//import android.widget.Toast

class Utilities {
//class Utilities(private val context: Context) {
//    private fun targetAchieved() {
//        Toast.makeText(context, context.getString(R.string.times_up), Toast.LENGTH_SHORT).show()
//    }

    // This is used just for debugging purposes to print all the chronometers in the log
    companion object {
//        fun printAll(chronometers: MutableList<Chronometer>, message: String) {
//            println(message)
//            chronometers.forEachIndexed { index, chron -> print("$index "); chron.print() }
//        }

        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            if (view != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }


        fun createAlertDialog(
            context: Context,
            message: String,
            positiveButtonMessage: String,
            negativeButtonMessage: String,
            positiveAction: () -> Unit
        ) {
            val dialog = AlertDialog.Builder(context, R.style.AlertDialogCustom).apply {
                setMessage(message)
                setPositiveButton(positiveButtonMessage) { _, _ ->
                    positiveAction()
                }
                setNegativeButton(negativeButtonMessage, null)
            }.create()

            dialog.setOnShowListener {
                // val hintColor = ContextCompat.getColor(context, R.color.hint)
                val hintColor = context.resources.getColor(R.color.hint, null)
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setTextColor(hintColor)
                    background = ColorDrawable(Color.TRANSPARENT)
                }

                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setTextColor(hintColor)
                    background = ColorDrawable(Color.TRANSPARENT)
                }
            }
            dialog.show()
        }

    }

}
