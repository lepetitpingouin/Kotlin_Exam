package fr.francois.kotlin_exam

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import fr.francois.kotlin_exam.data.Company
import fr.francois.kotlin_exam.data.LiaisonDAO
import fr.francois.kotlin_exam.data.SearchCompanyDatabase
import fr.francois.kotlin_exam.data.SearchCompanyDatabase_Impl


class MainActivity : AppCompatActivity() {
    inner class QueryCompanyTask(
        private val service: CompanyService,
        private val listView: ListView
    ) : AsyncTask<String, Void, List<Company>>() {
        override fun onPreExecute() {
            listView.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg params: String?): List<Company> {
            val query = params[0] ?: return emptyList()
            return service.getAll(query)
        }

        override fun onPostExecute(result: List<Company>?) {
            listView.adapter = ArrayAdapter<Company>(
                applicationContext,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                result!!
            )
            listView.visibility = View.VISIBLE
            super.onPostExecute(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<ListView>(R.id.listResults)

        val db = SearchCompanyDatabase.getDatabase(this)
        val liaisonDAO = db.liaisonDAO()
        val companyDAO = db.companyDAO()
        val searchDAO = db.searchCompanyDAO()

        val svc = CompanyService(searchDAO, companyDAO, liaisonDAO)
        findViewById<ImageButton>(R.id.imagebuttonsearch).setOnClickListener {
            val editQuery = findViewById<EditText>(R.id.edit_query).text.toString()
            if (editQuery.isEmpty() || editQuery.isBlank()){
                Toast.makeText(this, "Veuillez remplir la recherche", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            QueryCompanyTask(svc, list).execute(editQuery)
        }

        list.setOnItemClickListener { parent, view, position, id ->
            val company = list.getItemAtPosition(position) as Company
            val siret = company.siret

            intent = Intent(this, CompanyActivity::class.java)
            intent.putExtra("Entreprise", siret)
            this.startActivity(intent)
        }


    }
}