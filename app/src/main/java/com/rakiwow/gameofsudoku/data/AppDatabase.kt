package com.rakiwow.gameofsudoku.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(SudokuStats::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun sudokuStatsDao(): SudokuStatsDao

    companion object{
        // Companion is like the static keyword in java. This instance is a singleton.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "app_database")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.sudokuStatsDao())
                }
            }
        }

        suspend fun populateDatabase(statsDao: SudokuStatsDao){

            statsDao.deleteAll()
        }
    }
}