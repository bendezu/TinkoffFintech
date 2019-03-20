package com.bendezu.tinkofffintech.courses.performance_details

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bendezu.tinkofffintech.R

class GridItemDecoration(context: Context, private val numColumns: Int): RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private val divider = ColorDrawable(ContextCompat.getColor(context, R.color.colorBackground))
    private val dp = context.resources.displayMetrics.density.toInt()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            //vertical line
            if ((i + 1) % numColumns != 0) {
                val top = bounds.top
                val right = bounds.right
                val left = right - dp
                val bottom = bounds.bottom
                divider.setBounds(left, top, right, bottom)
                divider.draw(c)
            }
            //horizontal line
            val left = bounds.left
            val right = bounds.right
            val bottom = bounds.bottom
            val top = bottom - dp

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val right = if ((parent.getChildAdapterPosition(view) + 1) % numColumns != 0) dp else 0
        outRect.set(0,0,right, dp)
    }
}