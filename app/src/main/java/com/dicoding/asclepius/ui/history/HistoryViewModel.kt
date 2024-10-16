package com.dicoding.asclepius.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HistoryRepository = HistoryRepository(application)

    fun getHistory(): LiveData<List<History>> = repository.getHistory()

    fun addHistory(history: History) {
        repository.addHistory(history)
    }
}
