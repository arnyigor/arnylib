package com.arny.arnylib.utils

import android.text.Editable
import android.text.TextWatcher

interface SimpleTextWatcher : TextWatcher {
    override fun afterTextChanged(var1: Editable)

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}