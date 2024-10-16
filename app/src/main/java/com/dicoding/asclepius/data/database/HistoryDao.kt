package com.dicoding.asclepius.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addHistory(history: History)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getHistory(): LiveData<List<History>>
}