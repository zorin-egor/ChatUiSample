package com.sample.libchat.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.showKeyboard(isShowForce: Boolean = false) {
    isFocusableInTouchMode = true
    requestFocus()
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        toggleSoftInput(if (isShowForce) InputMethodManager.SHOW_FORCED else InputMethodManager.HIDE_IMPLICIT_ONLY,
            InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}

fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(windowToken, 0)
    }
}