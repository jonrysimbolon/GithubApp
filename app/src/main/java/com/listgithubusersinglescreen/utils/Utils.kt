package com.listgithubusersinglescreen.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar



fun String.showSnackBarAppearBriefly(view: View) {
    Snackbar.make(view, this, Snackbar.LENGTH_SHORT).show()
}