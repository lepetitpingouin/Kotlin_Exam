package fr.francois.kotlin_exam.data

import androidx.room.*

@Dao
interface SearchCompanyDAO {
    @Query("SELECT * FROM SearchCompany")
    fun getAll(): List<SearchCompany>

    @Query("SELECT COUNT(*) FROM SearchCompany")
    fun getCountSearch(): Int

    @Query("SELECT * FROM SearchCompany ORDER BY id  LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): SearchCompany?

    @Insert
    fun insert(search: SearchCompany): Long

    @Update
    fun update(search: SearchCompany)

    @Delete
    fun delete(search: SearchCompany)
}