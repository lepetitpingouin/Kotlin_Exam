package fr.francois.kotlin_exam.data

import androidx.room.*
import java.io.Serializable

@Entity
data class Company(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        var company_name: String? ="",
        var siret: Long =0,
        var created_date: String? ="",
        var company_category: String? ="",
        var nature_juridique: String? ="",
        var geo_adresse: String? ="",
        var departement: String = ""
):Serializable {
        override fun toString(): String {
                if(departement == ""){
                        return "$company_name - département inconnue"
                }else{
                        return "$company_name - $departement"
                }
        }
}