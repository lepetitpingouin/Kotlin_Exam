package fr.francois.kotlin_exam.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class SearchCompany(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var search: String = "",
    var departement: Int = 0,
    var siret: Long = 0
) : Serializable {
    override fun toString(): String {
        if(departement == 0){
            return "$search - département inconnue"
        }else{
            return "$search - $departement"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (javaClass != other?.javaClass) {
            return false
        }

        other as SearchCompany

        if (id != other.id) {
            return false
        }
        return true

    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }


}