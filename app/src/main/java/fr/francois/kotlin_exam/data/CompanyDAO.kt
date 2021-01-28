package fr.francois.kotlin_exam.data

import androidx.room.*

@Dao
interface CompanyDAO {
    @Query("SELECT * FROM Company")
    fun getAll(): List<Company>

    @Query("SELECT * FROM Company WHERE id = :id")
    fun getAllById(id: Long): Company

    @Query("SELECT id FROM Company WHERE siret = :siret")
    fun getIdBySiret(siret: Long): Long

    @Query("SELECT COUNT(*) FROM Company")
    fun getCountCompany(): Int

    @Query("SELECT * FROM Company WHERE siret = :siret")
    fun getBySiret(siret: Long): Company

    @Query("SELECT * FROM Company ORDER BY siret  LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): Company?

    @Insert
    fun insert(company: Company): Long

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)
}