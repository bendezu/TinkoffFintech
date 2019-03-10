package com.bendezu.tinkofffintech.courses.performance_details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bendezu.tinkofffintech.R
import kotlinx.android.synthetic.main.activity_performance_detail.*

class PerformanceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountListFragment())
                .commit()
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
        val accountsFragment = supportFragmentManager.findFragmentById(R.id.container) as AccountListFragment
        when (item?.itemId) {
            R.id.switch_view -> {
                accountsFragment.switchView()
                return true
            }
            R.id.add_account_action -> {
                accountsFragment.addAccount()
                return true
            }
            R.id.remove_account_action -> {
                accountsFragment.removeAccount()
                return true
            }
            R.id.shuffle_accounts_action -> {
                accountsFragment.shuffleAccounts()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
