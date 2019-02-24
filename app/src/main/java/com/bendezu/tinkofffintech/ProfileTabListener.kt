package com.bendezu.tinkofffintech

interface ProfileTabListener {
    fun onEditButtonClicked(firstName: String, secondName: String, patronymic: String)
    fun onSaveButtonClicked()
    fun onCancelButtonClicked()
}