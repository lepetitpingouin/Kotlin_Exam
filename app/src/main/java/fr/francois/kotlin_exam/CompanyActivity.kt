package fr.francois.kotlin_exam

import android.app.ActionBar
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import fr.francois.kotlin_exam.data.Company
import fr.francois.kotlin_exam.data.SearchCompany


class CompanyActivity : AppCompatActivity() {
    inner class QueryCompanyTask(
        private val service: CompanyService,
        private val layout: LinearLayout
    ) : AsyncTask<SearchCompany, Void, Company>() {
        private val dlg = Dialog(this@CompanyActivity)

        override fun onPreExecute() {
            layout.visibility = View.INVISIBLE
            dlg.window?.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT
            )
            dlg.setContentView(R.layout.company)
            dlg.show()
        }

        override fun doInBackground(vararg params: SearchCompany?): Company? {
            val query = params[0] ?: return null
            return service.getCompany(query)
        }

        override fun onPostExecute(result: Company?) {
            if (result?.company_name == null || result?.company_name == "" ) {
                layout.findViewById<TextView>(R.id.company_name).text =
                    String.format(getString(R.string.company_name), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.company_name).text =
                    String.format(getString(R.string.company_name), result?.company_name)
            }

            if (result?.siret == null || result?.siret == 0.toLong()) {
                layout.findViewById<TextView>(R.id.siret).text =
                    String.format(getString(R.string.siret), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.siret).text =
                    String.format(getString(R.string.siret), result?.siret)
            }

            if (result?.created_date == null || result?.created_date == "") {
                layout.findViewById<TextView>(R.id.created_date).text =
                    String.format(getString(R.string.created_date), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.created_date).text =
                    String.format(getString(R.string.created_date), result?.created_date)
            }

            if (result?.company_category == null || result?.company_category == "") {
                layout.findViewById<TextView>(R.id.company_category).text =
                    String.format(getString(R.string.company_category), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.company_category).text =
                    String.format(getString(R.string.company_category), result?.company_category)
            }

            if (result?.nature_juridique == null || result?.nature_juridique == "") {
                layout.findViewById<TextView>(R.id.nature_juridique).text =
                    String.format(getString(R.string.nature_juridique), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.nature_juridique).text =
                    String.format(getString(R.string.nature_juridique), result?.nature_juridique)
            }

            if (result?.geo_adresse == null || result?.geo_adresse == "") {
                layout.findViewById<TextView>(R.id.geo_adresse).text =
                    String.format(getString(R.string.geo_adresse), "Inconnue")
            } else {
                layout.findViewById<TextView>(R.id.geo_adresse).text =
                    String.format(getString(R.string.geo_adresse), result?.geo_adresse)
            }

            layout.visibility = View.VISIBLE
            dlg.dismiss()
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company)

        val svc = CompanyService()
        val search = intent.getSerializableExtra("Entreprise") as SearchCompany

        val layout = findViewById<LinearLayout>(R.id.layout_company)
        QueryCompanyTask(svc, layout).execute(search)
    }
}