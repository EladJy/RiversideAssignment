package com.ej.riversideassignment.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ej.riversideassignment.model.TitleDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TitleDetailsDao {
    @Query("SELECT * FROM titledetails WHERE imdbID == :titleId")
    fun getTitleById(titleId: String): Flow<TitleDetails?>

    @Query("select * FROM titledetails WHERE title LIKE '%' || :title || '%' ORDER BY year DESC")
    fun getTitlesByName(title: String): Flow<List<TitleDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg titles: TitleDetails)

    @Query("UPDATE titledetails SET isFavourite = :isFavourite WHERE imdbID = :titleId")
    suspend fun updateFavourite(titleId: String, isFavourite: Boolean)
}