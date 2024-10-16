package com.dicoding.asclepius.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "History")
data class History (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "image_uri")
    var imageUri: String,

    @ColumnInfo(name = "result")
    var result: String,

    @ColumnInfo(name = "date")
    var date: String
) : Parcelable