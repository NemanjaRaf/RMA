package com.nemanja02.rma.core

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun showDialog(
    context: Context?, title: String?, message: String?,
    showNegativeButton: Boolean = false,
    okButtonText: String? = "OK", cancelButtonText: String? = "Otkaži",
    onClickListener: DialogInterface.OnClickListener?,
    onNegativeClickListener: DialogInterface.OnClickListener? = null
) {
    val dialog: AlertDialog.Builder = AlertDialog.Builder(context)
    dialog.setTitle(title)
    dialog.setMessage(message)
    dialog.setPositiveButton("OK", onClickListener)
    if (showNegativeButton)
        dialog.setNegativeButton("Cancel", onNegativeClickListener)

    dialog.show()
}
