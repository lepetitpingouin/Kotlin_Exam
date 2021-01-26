package fr.francois.kotlin_exam.data

import androidx.room.*
import java.io.Serializable

@Entity
data class Company(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        var id_search_company: Long =0,
        var company_name: String ="",
        var siren: Long =0,
        var created_date: String ="",
        var company_category: String ="",
        var nature_juridique: String ="",
        var geo_adresse: String =""
):Serializable {

}