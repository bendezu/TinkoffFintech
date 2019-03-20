package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R

class ListItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private val divider = ColorDrawable(ContextCompat.getColor(context, R.color.colorBackground))
    private val dp = context.resources.displayMetrics.density.toInt()
    private var margin = 100

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        val left= margin*dp
        val right = parent.width

        val childCount = parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + Math.round(child.translationY)
            val top = bottom - dp
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
        c.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = dp
    }
}