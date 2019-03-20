package com.bendezu.tinkofffintech.courses

import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference
import kotlin.random.Random

interface AccountsListener {
    fun onAccountsUpdated()
}

class RandomizeAccountThread(val fragment: WeakReference<PerformanceFragment>): Thread() {

    override fun run() {
        val accounts = fragment.get()?.accounts ?: return
        for (account in accounts) {
            Thread.sleep(1000)
            account.points = Random.nextInt(0, 11)
        }
        Handler(Looper.getMainLooper()).post {
            fragment.get()?.updateAccounts()
        }
    }
}