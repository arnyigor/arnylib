package com.arny.arnylib.utils

import android.R
import android.app.AlertDialog
import android.content.Context
import android.support.v7.view.ContextThemeWrapper
import com.arny.arnylib.interfaces.AlertDialogListener

@JvmOverloads
fun alertDialog(context: Context, title: String, content: String? = null, btnOkText: String? = context.getString(android.R.string.ok), dialogListener: AlertDialogListener? = null) {
    val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.Theme_Holo_Light_Dialog))
    builder.setTitle(title)
    if (content != null) {
        builder.setMessage(content)
    }
    builder.setPositiveButton(btnOkText) { dialog, which ->
        dialog.dismiss()
        dialogListener?.onConfirm()
    }
    val dialog = builder.create()
    dialog.setCancelable(false)
    dialog.show()
}