package com.arny.arnylib.utils

import android.annotation.SuppressLint
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.arny.arnylib.interfaces.AlertDialogListener
import com.arny.arnylib.interfaces.ChoiseDialogListener
import com.arny.arnylib.interfaces.ConfirmDialogListener
import com.arny.arnylib.interfaces.ListDialogListener


@SuppressLint("RestrictedApi")
@JvmOverloads
fun alertDialog(context: Context, title: String, content: String? = null, dialogListener: AlertDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title)
            .cancelable(false)
            .onPositive { _, _ -> dialogListener?.onConfirm() }
            .build()
    if (content != null) {
        dlg.setContent(content)
    }
    dlg.show()
}

@JvmOverloads
fun listDialog(context: Context, vararg items: String, title: String? = null, cancelable: Boolean = false, dialogListener: ListDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title.toString())
            .cancelable(cancelable)
            .items(items.toString())
            .itemsCallback({ _, _, which, _ ->
                dialogListener?.onClick(which)
            })
            .build()
    dlg.show()
}

@JvmOverloads
fun checkDialog(context: Context, title: String? = null, items: Array<String>, itemsSelected: Array<Int>? = null, cancelable: Boolean = false, dialogListener: ChoiseDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title.toString())
            .cancelable(cancelable)
            .items(items.asList())
            .itemsCallbackMultiChoice(itemsSelected, { _, which, _ ->
                dialogListener?.onChoise(which)
                true
            })
            .positiveText(android.R.string.ok)
            .build()
    dlg.show()
}


@JvmOverloads
fun confirmDialog(context: Context, title: String, content: String? = null, btnOkText: String? = context.getString(android.R.string.ok), btnCancelText: String? = context.getString(android.R.string.cancel), cancelable: Boolean = false, dialogListener: ConfirmDialogListener? = null) {
    val dlg = MaterialDialog.Builder(context)
            .title(title)
            .cancelable(cancelable)
            .positiveText(btnOkText.toString())
            .negativeText(btnCancelText.toString())
            .onPositive { _, _ ->
                dialogListener?.onConfirm()
            }
            .onNegative { _, _ ->
                dialogListener?.onCancel()
            }
            .build()
    if (content != null) {
        dlg.setContent(content)
    }
    dlg.show()
}