package com.example.myapplication.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.myapplication.R

/**
 * Created by NguyenLinh on 23,June,2022
 */
object LocationPopupUtils {
    fun dialogLocationDisclosures(
        context: Context,
        title: String,
        message: String,
        textNeg: String,
        textPos: String,
        onClickNeg: View.OnClickListener?,
        onClickPos: View.OnClickListener?
    ) {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setContentView(R.layout.popup_location_disclosures)
            val btnNeg = dialog.findViewById<Button>(R.id.btnNeg)
            val btnPos = dialog.findViewById<Button>(R.id.btnPos)
            val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
            val tvContent = dialog.findViewById<TextView>(R.id.tvContent)

            btnNeg.text = textNeg
            btnPos.text = textPos
            tvTitle.text = title
            if (title.isEmpty()) {
                tvTitle.visibility = View.GONE
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.text = Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(message)
            }
            btnNeg.setOnClickListener {
                dialog.dismiss()
                onClickNeg?.onClick(it)
            }
            btnPos.setOnClickListener {
                dialog.dismiss()
                onClickPos?.onClick(it)
            }
            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        } catch (ex: Exception) {
        }
    }
}