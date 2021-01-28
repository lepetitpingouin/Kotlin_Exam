package fr.francois.kotlin_exam

import android.util.JsonReader
import android.util.JsonToken
import fr.francois.kotlin_exam.data.*
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CompanyService(
    val searchDao: SearchCompanyDAO,
    val companyDAO: CompanyDAO,
    val liaisonDAO: LiaisonDAO
) {
    private val apiUrl = "https://entreprise.data.gouv.fr"
    private val queryUrl = "$apiUrl//api/sirene/v1/full_text/%s"

    fun getAll(query: String): List<Company> {
        val url = URL(String.format(queryUrl, query))
        val urlToString = String.format(queryUrl, query)

        var urlByID = searchDao.getByURL(urlToString)

        val listCompany = mutableListOf<Company>()

        if (urlByID != null) {
            var idCompany = liaisonDAO.getIdCompany(urlByID.id)
            for (id in idCompany) {
                listCompany.add(companyDAO.getAllById(id))
            }
            return listCompany
        } else {

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

                reader.beginObject()

                val search = SearchCompany(search = urlToString)
                searchDao.insert(search)

                while (reader.hasNext()) {
                    if (reader.nextName() == "etablissement") {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            reader.beginObject()
                            var company = Company()
                            while (reader.hasNext()) {
                                when (reader.nextName()) {
                                    "siret" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.siret = reader.nextString().toLong()
                                        }
                                    }

                                    "nom_raison_sociale" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.company_name = reader.nextString()
                                        }
                                    }

                                    "geo_adresse" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.geo_adresse = reader.nextString()
                                        }
                                    }
                                    "libelle_activite_principale" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.company_category = reader.nextString()
                                        }
                                    }
                                    "libelle_nature_juridique_entreprise" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.nature_juridique = reader.nextString()
                                        }
                                    }

                                    "date_creation" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.created_date = reader.nextString()
                                        }
                                    }

                                    "departement" -> {
                                        if (reader.peek() == JsonToken.NULL) {
                                            reader.nextNull()
                                        } else {
                                            company.departement = reader.nextString()
                                        }
                                    }

                                    else -> reader.skipValue()
                                }
                            }
                            reader.endObject()
                            listCompany.add(company)
                            companyDAO.insert(company)
                            val idCompany = companyDAO.getIdBySiret(company.siret)
                            val idSearch = searchDao.getIdByUrl(urlToString)
                            val liaison = Liaison(idSearch = idSearch, idCompany = idCompany)
                            liaisonDAO.insert(liaison)
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
    }

    fun getCompany(siret: Long): Company{
        val company = companyDAO.getBySiret(siret)
        return company
    }
}