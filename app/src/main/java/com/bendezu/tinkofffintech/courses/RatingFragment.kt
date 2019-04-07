package com.bendezu.tinkofffintech.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bendezu.tinkofffintech.R
import com.bendezu.tinkofffintech.courses.rating_details.RatingDetailsActivity
import kotlinx.android.synthetic.main.fragment_rating.*

class RatingFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsButton.setOnClickListener {
            startActivity(Intent(context, RatingDetailsActivity::class.java))
        }
    }
}