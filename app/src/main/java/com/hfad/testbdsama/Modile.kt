package com.hfad.testbdsama

import android.app.Application
import androidx.room.Room
import com.hfad.testbdsama.Data.MainDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modile {
    @Provides
    @Singleton
    fun provMainDB(app: Application): MainDB {
        return Room.databaseBuilder(
            app,
            MainDB::class.java,
            "products.db"
        ).build()

    }
}


