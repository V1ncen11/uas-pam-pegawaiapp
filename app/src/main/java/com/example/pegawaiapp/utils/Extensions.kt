package com.example.namaproject.utils

import android.widget.Toast
import android.content.Context

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
