package com.paul.android.quizapp.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = Difficulty.TABLE_NAME,
    indices = [Index(value = [Difficulty.NAME], unique = true)]
)
data class Difficulty(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) val id: Long? = null,
    @ColumnInfo(name = NAME) val name: String
) {
    companion object{
        const val TABLE_NAME = "difficulty"
        const val ID = "id"
        const val NAME = "name"
    }
}