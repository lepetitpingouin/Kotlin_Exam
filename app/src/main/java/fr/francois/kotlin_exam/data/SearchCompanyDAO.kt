package fr.francois.kotlin_exam.data

import androidx.room.*

@Dao
interface SearchCompanyDAO {
    @Query("SELECT * FROM SearchCompany")
    fun getAll(): List<SearchCompany>

    @Query("SELECT * FROM SearchCompany WHERE search = :search")
    fun getByURL(search: String): SearchCompany

    @Query("SELECT id FROM SearchCompany WHERE search = :search")
    fun getIdByUrl(search: String): Long

    @Query("SELECT id FROM SearchCompany")
    fun getId(): Long

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