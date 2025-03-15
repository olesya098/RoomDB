package com.hfad.testbdsama.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "product")
data class prod(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val descriptoin: String
)

@Entity(tableName = "image")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val productId: Int,
    @ColumnInfo(name = "imageData", typeAffinity = ColumnInfo.BLOB)
    val imageData: ByteArray
)