package com.menesdurak.e_ticaret_uygulamasi.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location

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
    duration: Long = 500L,
) {
    var isViewShown = true
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            when {
                dy > 0 && isViewShown -> {
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

fun String.getFirstTwoChar(): String {
    return "${this[0]}${this[1]}"
}

fun String.getLastTwoChar(): String {
    return "${this.substring(this.length - 2)}"
}

fun String.hideCreditCardNumber(): String {
    return "${this.substring(0, 4)} **** **** ${this.substring(this.length - 4)}"
}

fun Location.calculateDistance(index: Int, latLng: LatLng, pair: Pair<Int, Double>): Pair<Int, Double> {
    val distance =
        ((latitude - latLng.latitude) * (latitude - latLng.latitude)) +
                ((longitude - latLng.longitude) * (longitude - latLng.longitude))
    if (distance < pair.second || pair.second == 0.0) {
        return Pair(index, distance)
    }
    return pair
}