package fr.francois.kotlin_exam.data

import androidx.room.*

@Dao
interface LiaisonDAO {
    @Query("SELECT idCompany FROM Liaison WHERE idSearch = :search")
    fun getIdCompany(search: Long?): List<Long>

    @Insert
    fun insert(liaison: Liaison): Long

    @Update
    fun update(liaison: Liaison)

    @Delete
    fun delete(liaison: Liaison)
}