package com.hfad.testbdsama.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertProd(prod: prod)

    @Query("SELECT * FROM product")
    fun gettAll(): Flow<List<prod>>

    @Delete
    suspend fun deleteProd(prod: prod)


    @Update
    suspend fun updateProd(prod: prod)
}
