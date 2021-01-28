package fr.francois.kotlin_exam.data

import androidx.room.*

@Entity
data class Liaison(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var idSearch: Long? = null,
    var idCompany: Long? = null
) {
}