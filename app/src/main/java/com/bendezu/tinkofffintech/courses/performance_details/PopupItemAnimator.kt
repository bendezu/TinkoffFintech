package com.bendezu.tinkofffintech.courses.performance_details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.addListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class PopupItemAnimator: DefaultItemAnimator() {

    private val animDuration = 300L

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val animScaleX = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 0f, 1f)
        val animScaleY = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 0f, 1f)
        val animAlpha = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f)
        val anim = AnimatorSet().apply {
            playTogether(animScaleX, animScaleY, animAlpha)
            interpolator = OvershootInterpolator()
            duration = animDuration
            addListener(
                onStart =   { dispatchAddStarting(holder) },
                onEnd =     { dispatchAddFinished(holder) },
                onCancel =  { resetView(holder.itemView) })
        }
        anim.start()
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val animScaleX = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_X, 1f, 0f)
        val animScaleY = ObjectAnimator.ofFloat(holder.itemView, View.SCALE_Y, 1f, 0f)
        val animAlpha = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f, 0f)
        val anim = AnimatorSet().apply {
            playTogether(animScaleX, animScaleY, animAlpha)
            interpolator = AnticipateInterpolator()
            duration = animDuration
            addListener(
                onStart =   { dispatchRemoveStarting(holder) },
                onEnd =     { dispatchRemoveFinished(holder); resetView(holder.itemView) },
                onCancel =  { resetView(holder.itemView) })
        }
        anim.start()
        return true
    }

    private fun resetView(view: View) {
        view.apply { scaleX = 1f; scaleY = 1f; alpha = 1f }
    }
}
