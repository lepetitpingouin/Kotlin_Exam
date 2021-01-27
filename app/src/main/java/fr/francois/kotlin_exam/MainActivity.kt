package fr.francois.kotlin_exam

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import fr.francois.kotlin_exam.data.Company
import fr.francois.kotlin_exam.data.SearchCompany

class MainActivity : AppCompatActivity() {
    inner class QueryCompanyTask(
        private val service: CompanyService,
        private val listView: ListView
    ) : AsyncTask<String, Void, List<SearchCompany>>() {
        override fun onPreExecute() {
            listView.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg params: String?): List<SearchCompany> {
            val query = params[0] ?: return emptyList()
            return service.getSearchCompany(query)
        }

        override fun onPostExecute(result: List<SearchCompany>?) {
            listView.adapter = ArrayAdapter<SearchCompany>(
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


        val svc = CompanyService()
        findViewById<ImageButton>(R.id.imagebuttonsearch).setOnClickListener {
            val editQuery = findViewById<EditText>(R.id.edit_query).text.toString()
            QueryCompanyTask(svc, list).execute(editQuery)
        }

        list.setOnItemClickListener { parent, view, position, id ->
            val search = list.getItemAtPosition(position) as SearchCompany
            intent = Intent(this,CompanyActivity::class.java)
            intent.putExtra("Entreprise", search)
            this.startActivity(intent)
        }
    }
}