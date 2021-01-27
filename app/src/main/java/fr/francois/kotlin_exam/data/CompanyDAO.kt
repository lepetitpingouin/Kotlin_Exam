package fr.francois.kotlin_exam.data

import androidx.room.*

@Dao
interface CompanyDAO {
    @Query("SELECT * FROM Company")
    fun getAll(): List<Company>

    @Query("SELECT COUNT(*) FROM Company")
    fun getCountCompany(): Int

    @Query("SELECT * FROM Company ORDER BY siret  LIMIT 1 OFFSET :position")
    fun getByPosition(position: Int): Company?

    @Query("SELECT * FROM Company  WHERE id_search_company = :id_search ORDER BY siret, company_name LIMIT 1 OFFSET :position")
    fun getCompanyByPositionSearch(id_search: Long, position: Int): Company?

    @Insert
    fun insert(company: Company): Long

    @Update
    fun update(company: Company)

    @Delete
    fun delete(company: Company)
}