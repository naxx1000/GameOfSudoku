package com.rakiwow.gameofsudoku.data

import androidx.room.*
import java.lang.StringBuilder

@Entity(tableName = "stats")
@TypeConverters(SudokuGridConverter::class)
data class SudokuStats(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "difficulty") val difficulty: Int?,
    @ColumnInfo(name = "date") val date: Long?,
    @ColumnInfo(name = "completedTime") val completedTime: Int?,
    @ColumnInfo(name = "grid") val grid: Array<IntArray>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuStats

        if (grid != null) {
            if (other.grid == null) return false
            if (!grid.contentDeepEquals(other.grid)) return false
        } else if (other.grid != null) return false

        return true
    }

    override fun hashCode(): Int {
        return grid?.contentDeepHashCode() ?: 0
    }
}

class SudokuGridConverter {

    @TypeConverter
    fun fromString(value: String): Array<IntArray> {
        val grid = Array(9) { IntArray(9) }
        val s = value.split(",")
        var k = 0
        for (i in 0 until 9){
            for (j in 0 until 9){
                grid[i][j] = s[k].toInt()
                k++
            }
        }
        return grid
    }

    @TypeConverter
    fun fromArrayList(list: Array<IntArray>): String {
        var stringbuilder = StringBuilder()
        for (i in 0 until 9){
            for (j in 0 until 9){
                stringbuilder.append(list[i][j].toString() + ",")
            }
        }
        return stringbuilder.toString()
    }
}