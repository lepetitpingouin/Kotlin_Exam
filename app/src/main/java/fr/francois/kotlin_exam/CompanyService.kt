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

            val listLocation = mutableListOf<SearchCompany>()
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
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        listLocation.add(searchCompany)
                    }

                    reader.endArray()
                } else {
                    reader.skipValue()
                }
            }
            reader.endObject()
            return listLocation
        } catch (e: IOException) {
            return emptyList()
        } finally {
            connection?.disconnect()
        }
    }

    fun getCompany(query: String): List<Company> {
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
            val results = mutableListOf<Company>()

            reader.beginObject()
            reader.beginArray()
            while (reader.hasNext()) {
                val company = Company()
                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "siren" -> company.siren = reader.nextInt().toLong()
                        "enseigne" -> company.company_name = reader.nextString()
                        "geo_adresse" -> company.geo_adresse = reader.nextString()
                        "libelle_activite_principale" -> company.company_category =
                            reader.nextString()
                        "libelle_nature_juridique_entreprise" -> company.nature_juridique =
                            reader.nextString()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()
                results.add(company)
            }
            reader.endArray()
            reader.endObject()
            return results
        } catch (e: IOException) {
            return emptyList()
        } finally {
            connection?.disconnect()
        }
    }

}