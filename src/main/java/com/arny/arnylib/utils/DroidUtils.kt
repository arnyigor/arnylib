package com.arny.arnylib.utils

import android.annotation.SuppressLint
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.arny.arnylib.interfaces.AlertDialogListener
import com.arny.arnylib.interfaces.ConfirmDialogListener

@SuppressLint("RestrictedApi")
@JvmOverloads
fun alertDialog(context: Context, title: String, content: String? = null, btnOkText: String? = context.getString(android.R.string.ok), cancelable: Boolean = false, dialogListener: AlertDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title)
            .cancelable(cancelable)
            .positiveText(btnOkText.toString())
            .onPositive { dialog, _ ->
                dialog.dismiss()
                dialogListener?.onConfirm()
            }
            .onNegative { dialog, _ -> dialog.dismiss() }
            .build()
    if (content != null) {
        dlg.setContent(content)
    }
    dlg.show()
}

@JvmOverloads
fun confirmDialog(context: Context, title: String, content: String? = null, btnOkText: String? = context.getString(android.R.string.ok), btnCancelText: String? = context.getString(android.R.string.cancel), cancelable: Boolean = false, dialogListener: ConfirmDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title)
            .cancelable(cancelable)
            .positiveText(btnOkText.toString())
            .negativeText(btnCancelText.toString())
            .onPositive { dialog, _ ->
                dialog.dismiss()
                dialogListener?.onConfirm()
            }
            .onNegative { dialog, _ ->
                dialog.dismiss()
                dialogListener?.onCancel()
            }
            .build()
    if (content != null) {
        dlg.setContent(content)
    }
    dlg.show()
}