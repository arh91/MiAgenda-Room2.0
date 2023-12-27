package com.example.agenda

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class, Supplier::class], version = 1)
abstract class AgendaDatabase : RoomDatabase() {
    abstract fun peticionesDao(): PeticionesDao

    companion object {
        private var instance: AgendaDatabase? = null

        fun getInstance(context: Context): AgendaDatabase {
            if (instance == null) {
                synchronized(AgendaDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AgendaDatabase::class.java,
                        "app_database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}