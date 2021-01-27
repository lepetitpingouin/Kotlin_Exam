package fr.francois.kotlin_exam

import android.util.JsonReader
import android.util.JsonToken
import fr.francois.kotlin_exam.data.Company
import fr.francois.kotlin_exam.data.SearchCompany
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CompanyService {
    private val apiUrl = "https://entreprise.data.gouv.fr"
    private val queryUrl = "$apiUrl//api/sirene/v1/full_text/%s"

    fun getSearchCompany(query: String): List<SearchCompany> {
        val url = URL(String.format(queryUrl, query))
        var connection: HttpsURLConnection? = null

        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.connect()

            val code = connection.responseCode
            if (code != HttpsURLConnection.HTTP_OK) {
                return emptyList()
            }

            val inputStream = connection.inputStream ?: return emptyList()
            val reader = JsonReader(inputStream.bufferedReader())

            val listCompany = mutableListOf<SearchCompany>()
            reader.beginObject()

            while (reader.hasNext()) {
                if (reader.nextName() == "etablissement") {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        val searchCompany = SearchCompany()
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "nom_raison_sociale" -> searchCompany.search = reader.nextString()
                                "departement" -> {
                                    if (reader.peek() == JsonToken.NULL) {
                                        reader.nextNull()
                                    } else {
                                        searchCompany.departement = reader.nextInt()
                                    }
                                }
                                "siret" -> searchCompany.siret = reader.nextLong()
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        listCompany.add(searchCompany)
                    }

                    reader.endArray()
                } else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return listCompany
        } catch (e: IOException) {
            return emptyList()
        } finally {
            connection?.disconnect()
        }
    }

    fun getCompany(query: SearchCompany): Company? {
        val querySiret = query.siret
        val queryCompanyUrl = "$apiUrl/api/sirene/v1/siret/$querySiret"

        val url = URL(String.format(queryCompanyUrl, query))
        var connection: HttpsURLConnection? = null

        try {
            connection = url.openConnection() as HttpsURLConnection
            connection.connect()
            val code = connection.responseCode
            if (code != HttpsURLConnection.HTTP_OK) {
                return Company()
            }
            val inputStream = connection.inputStream ?: return Company()
            val reader = JsonReader(inputStream.bufferedReader())
            val results = Company()

            reader.beginObject()
            while (reader.hasNext()) {
                if (reader.nextName() == "etablissement") {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            "siret" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.siret = reader.nextString().toLong()
                                }
                            }

                            "nom_raison_sociale" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.company_name = reader.nextString()
                                }
                            }

                            "geo_adresse" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.geo_adresse = reader.nextString()
                                }
                            }
                            "libelle_activite_principale" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.company_category = reader.nextString()
                                }
                            }
                            "libelle_nature_juridique_entreprise" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.nature_juridique = reader.nextString()
                                }
                            }

                            "date_creation_entreprise" -> {
                                if (reader.peek() == JsonToken.NULL) {
                                    reader.nextNull()
                                } else {
                                    results.created_date = reader.nextString()
                                }
                            }
                            else -> reader.skipValue()
                        }
                    }
                }
            }
            reader.endObject()
            return results
        } catch (e: IOException) {
            return null
        } finally {
            connection?.disconnect()
        }
    }

}