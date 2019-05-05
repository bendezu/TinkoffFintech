package com.bendezu.tinkofffintech.view

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.account_badge.view.*

class AccountBadgeView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var points: String
        get() = pointsTextView.text.toString()
        set(value) {
            pointsTextView.text = value
        }
    var name: String
        get() = nameTextView.text.toString()
        set(value) {
            nameTextView.text = value
        }
    var avatarRes: Int = 0
        set(@DrawableRes value) {
            field = value
            avatarImageView.setImageResource(value)
        }
    var avatarBackground: Drawable
        get() = avatarImageView.drawable
        set(value) {
            avatarImageView.setImageDrawable(value)
        }
    var initials: String
        get() = avatarImageView.initials
        set(value) {
            avatarImageView.initials = value
        }
    var highlighted: Boolean = false
        set(value) {
            field = value
            if (value) {
                nameTextView.setTypeface(null, Typeface.BOLD)
                avatarImageView.highlightColor = ContextCompat.getColor(context, R.color.colorAccent)
            } else {
                nameTextView.setTypeface(null, Typeface.NORMAL)
                avatarImageView.highlightColor = Color.TRANSPARENT
            }
        }
    var badgeVisibility: Int
        get() = pointsTextView.visibility
        set(value) {
            pointsTextView.visibility = value
        }

    init {

        inflate(context, R.layout.account_badge, this)

        context.theme.obtainStyledAttributes(attrs, R.styleable.AccountBadgeView, 0, 0).apply {
            try {
                points = getString(R.styleable.AccountBadgeView_points) ?: ""
                avatarRes = getResourceId(R.styleable.AccountBadgeView_avatar, 0)
                name = getString(R.styleable.AccountBadgeView_name) ?: ""
                highlighted = getBoolean(R.styleable.AccountBadgeView_highlighted, false)
            } finally {
                recycle()
            }
        }
    }
}