package com.bendezu.tinkofffintech.courses.performance_details

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.transition.TransitionManager
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.activity_performance_detail.*

class PerformanceDetailActivity : AppCompatActivity() {

    lateinit var accountsFragment: AccountListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchView.apply {
            layoutParams = Toolbar.LayoutParams(Gravity.END)
            setOnSearchClickListener {
                TransitionManager.beginDelayedTransition(searchView)
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    accountsFragment.query = query
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    accountsFragment.query = newText
                    return true
                }
            })
        }
        if (savedInstanceState == null) {
            accountsFragment = AccountListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, accountsFragment)
                .commit()
        } else {
            accountsFragment = supportFragmentManager.findFragmentById(R.id.container) as AccountListFragment
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.account_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sort_alphabetically -> {
                accountsFragment.sort = SortType.ALPHABETICALLY
                return true
            }
            R.id.action_sort_by_mark -> {
                accountsFragment.sort = SortType.BY_MARK
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
