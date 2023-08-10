package com.menesdurak.e_ticaret_uygulamasi.common

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubeTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val deltaY = 0.5f
        val pivotX = if (position < 0f) view.width.toFloat() else 0f
        val pivotY: Float = view.height * deltaY
        val rotationY = 45f * position

        view.pivotX = pivotX
        view.pivotY = pivotY
        view.rotationY = rotationY
    }
}