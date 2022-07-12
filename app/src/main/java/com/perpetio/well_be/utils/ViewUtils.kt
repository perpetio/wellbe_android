package com.perpetio.well_be.utils

import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.widget.EditText
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object ViewUtils {

    fun EditText.showHidePassword() {
        if (this.transformationMethod.javaClass.simpleName == "PasswordTransformationMethod") {
            this.transformationMethod = SingleLineTransformationMethod()
        } else this.transformationMethod = PasswordTransformationMethod()
        this.setSelection(this.text.length)
    }

    fun String.toEditable(): Editable =
        Editable.Factory.getInstance().newEditable(this)

    fun String.adjustTimePattern(): String {
        val time = LocalDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy")
        return time.format(formatter)
    }
}