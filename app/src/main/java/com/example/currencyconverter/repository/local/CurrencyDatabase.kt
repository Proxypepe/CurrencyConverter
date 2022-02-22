package com.example.currencyconverter.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyconverter.repository.local.dao.CurrencyDao
import com.example.currencyconverter.repository.local.entity.CurrencyDTO

@Database(entities = [CurrencyDTO::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase(){
    abstract fun currencyDao(): CurrencyDao

    companion object {

        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getDatabase(
            context: Context
        ): CurrencyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyDatabase::class.java,
                    "currencies"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }

}