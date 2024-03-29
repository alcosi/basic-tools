/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util

import android.app.Activity
import android.os.Build
import android.text.method.KeyListener
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import androidx.annotation.RequiresApi

fun disableView(view: EditText) {
    view.setText(view.text.toString().trim())
    if (view.keyListener != null) {
        view.tag = view.keyListener
        view.keyListener = null
    }
}

fun enableView(view: EditText) {
    if (view.tag != null)
        view.keyListener = view.tag as KeyListener
}


@RequiresApi(Build.VERSION_CODES.R)
fun hideSystemUIR(activity: Activity?) {
    // You have to wait for the view to be attached to the
    // window (otherwise, windowInsetController will be null)
    activity?.window?.insetsController?.let { controller ->
        controller.hide(WindowInsets.Type.systemBars())
        controller.hide(WindowInsets.Type.ime())
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun hideSystemUIR(view: View) {
    // You have to wait for the view to be attached to the
    // window (otherwise, windowInsetController will be null)
    view.windowInsetsController?.let { controller ->
        controller.hide(WindowInsets.Type.systemBars())
        controller.hide(WindowInsets.Type.ime())
    }
}

/*    Standard    */
fun hideSystemUI(activity: Activity?) {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    if (activity != null) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}

fun hideSystemUIUnstable(activity: Activity?) {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    if (activity != null) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                //or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}