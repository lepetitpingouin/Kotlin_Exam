package fr.francois.kotlin_exam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.text.ParseException

@Database(entities = [SearchCompany::class, Company::class], version = 2)

abstract class SearchCompanyDatabase : RoomDatabase() {
    abstract fun searchCompanyDAO(): SearchCompanyDAO
    abstract fun companyDAO(): CompanyDAO

    fun seed() {
        try {
            if (searchCompanyDAO().getCountSearch() == 0) {
                val seedSearchCompany = SearchCompany(search = "esimed")
                val idCompany = searchCompanyDAO().insert(seedSearchCompany)

                if (companyDAO().getCountCompany() == 0) {
                    val company = Company(
                        id_search_company = idCompany,
                        company_name = "esimed",
                        siren = 4428243026,
                        created_date = "20000101",
                        company_category = "PME"
                    )

                    companyDAO().insert(company)
                }
            }
        } catch (pe: ParseException) {
        }
    }

    companion object {
        var INSTANCE: SearchCompanyDatabase? = null

        fun getDatabase(context: Context): SearchCompanyDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    SearchCompanyDatabase::class.java,
                    "search_company.db"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
        }
    }
}