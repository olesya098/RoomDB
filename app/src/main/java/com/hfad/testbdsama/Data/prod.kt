package com.hfad.testbdsama.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class prod(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val descriptoin: String

) {
}
