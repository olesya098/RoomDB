package com.hfad.testbdsama.Data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [prod::class],
    version = 2
)
abstract class MainDB: RoomDatabase() {
    abstract val dao: com.hfad.testbdsama.Data.Dao
}
