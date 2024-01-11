package com.phc.project.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.phc.project.R


class Utils {
    companion object{
        fun showLongMessage(context: Context, s: String) {
            Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
        }
        fun showShortMessage(context: Context, s: String) {
            Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
        }



        fun progressDialog(context: Context?): Dialog {
            val dialog = Dialog(context!!)
            val inflate = LayoutInflater.from(context).inflate(R.layout.progress_bar, null)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog
        }
    }
}