package com.example.githubapp

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.githubapp.Data.GitHub
import com.example.githubapp.Data.GitHubAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
/*This main activity implements to show the user list
 of Repositories from API created in the last 30 days and can use a search filter*/

class MainActivity : AppCompatActivity() {
    val repList: MutableList<GitHub> = ArrayList()
    var per_page=1
    var isLoading = false
    lateinit var adapter: GitHubAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerView:RecyclerView
    lateinit var progressBar:ProgressBar
    lateinit var main_toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_toolbar = findViewById(R.id.main_toolbar)
        main_toolbar.title = "Welcome"
        setSupportActionBar(main_toolbar)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //For load data from API use getPage() function
        getPage()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount
                if (!isLoading) {
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        per_page++
                        Log.d("msg","Load more")
                        getPage(per_page)
                    }

                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }
/*get Page() function takes page per as a parameter as Integer value as default is 1.
  the main goal of this method is to connect with API and get rep and put in repList and showing data in RecyclerView*/
    fun getPage(per_page:Int=1) {
        isLoading = true
        progressBar.visibility = View.VISIBLE
        val myQueue = Volley.newRequestQueue(this)
        val url :String ="https://api.github.com/search/repositories?q=created:>"+getData()+"&sort=stars&order=desc&page=$per_page"
        Log.d("msg","page $per_page")
        val request: JsonObjectRequest = JsonObjectRequest(Request.Method.GET,url,null,{ response->
            try{
                val item =response.getJSONArray("items")
                for (i in 0..item.length()-1){
                    val  index =item.getJSONObject(i)
                    val rep_name =index.getString("name")
                    val rep_des=index.getString("description")
                    val created_at=index.getString("created_at")
                    val stargazers_count=index.getInt("stargazers_count")
                    val open_issues =index.getInt("open_issues")
                    val owner =index.getJSONObject("owner")
                    val name_owner=owner.getString("login")
                    val img_owner=owner.getString("avatar_url")
                    val html_url= index.getString("html_url")
                    repList.add(GitHub(name_owner,img_owner,rep_name,rep_des,created_at,stargazers_count,open_issues,html_url))
                }
            }catch (e:Exception){
                Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show()
            }

        }, { e->
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show()
        })
        myQueue.add(request)
        Handler().postDelayed({
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
            } else {
                adapter = GitHubAdapter(this,repList)
                recyclerView.adapter = adapter
            }
            isLoading = false
            progressBar.visibility = View.GONE
        }, 3000)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        val item : MenuItem = menu!!.findItem(R.id.app_bar_search)
        val s : SearchView = item.actionView as SearchView
        s.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("mas",query)
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                Log.d("mas",newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val alertDialog : AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmation")
            .setMessage("Are You sure ?")
            .setPositiveButton("yes") { dialog, which ->
                super.onBackPressed()
            }.setNegativeButton("NO"){ dialog, which ->
                dialog.dismiss()
            }
        alertDialog.create()
        alertDialog.show()
    }
    // this function used to calculate the
    // date right Now and subtract 30 days and return in the form YYYY-MM-DD as a String
    private fun getData(sud:Int=-30):String{
        val calendar : Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE ,sud)
        val date : Date =calendar.time
        val df = SimpleDateFormat ("yyyy-MM-dd", Locale.getDefault())
        val formattedDate :String =df.format(date)
        return formattedDate
    }
//This function used to filter rep in RecyclerView dependent on the name owner
    fun filterList(text:String){
        val filterList:MutableList<GitHub> =ArrayList()

        for ( i :GitHub in repList){
            if(i.Owner_name().toLowerCase().contains(text.toLowerCase())){
                filterList.add(i)
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this,"The list is empyt",Toast.LENGTH_LONG).show()
        }else {
            adapter.SerchFilterList(filterList)
        }
    }

}