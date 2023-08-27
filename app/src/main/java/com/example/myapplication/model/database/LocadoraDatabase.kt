package com.example.myapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.database.dao.ClientDAO
import com.example.myapplication.model.database.dao.FilmeDAO
import com.example.myapplication.model.database.entity.ClientEntity
import com.example.myapplication.model.database.entity.MovieEntity

@Database(entities = [ClientEntity::class, MovieEntity::class], version = 2, exportSchema = false)
abstract class LocadoraDatabase : RoomDatabase() {

    abstract fun clientDao(): ClientDAO

    abstract fun movieDao(): FilmeDAO

    companion object {
        const val DATABASE_NAME = "locadora-database"

        private var instance: LocadoraDatabase? = null

        @Synchronized
        fun instance(context: Context): LocadoraDatabase {
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocadoraDatabase::class.java, DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }

}