package com.menesdurak.e_ticaret_uygulamasi.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.menesdurak.e_ticaret_uygulamasi.R

infix fun Double.round(decimals: Int): Double {
    var multiplier = 1
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}

fun Double.addCurrencySign(): String {
    return "$ $this"
}

fun RecyclerView.addOnScrollHiddenView(
    hiddenView: View,
    duration: Long = 500L
) {
    var isViewShown = true
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            when {
                dy > 30 && isViewShown -> {
                    isViewShown = false
                    hiddenView.animate()
                        .alpha(0.0F)
                        .setDuration(duration)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hiddenView.visibility = View.GONE
                            }

                        })

                }

                dy < 0 && !isViewShown -> {
                    isViewShown = true
                    hiddenView.animate()
                        .alpha(1.0F)
                        .setDuration(duration)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hiddenView.visibility = View.VISIBLE
                            }

                        })

                }
            }
        }

    })
}