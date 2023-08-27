package com.example.myapplication

import android.content.Context
import android.widget.Toast

fun Context.makeToast(string: String) = Toast.makeText(this, string, Toast.LENGTH_SHORT).show()