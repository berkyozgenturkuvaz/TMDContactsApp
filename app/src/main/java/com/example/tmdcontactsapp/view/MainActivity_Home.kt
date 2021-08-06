package com.example.tmdcontactsapp.view

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapter
import com.example.tmdcontactsapp.model.ContactsModel

class MainActivity_Home : AppCompatActivity() {

    lateinit var homeRecyclerView: RecyclerView
    private val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    private var contactModels: ArrayList<ContactsModel>? = null
    private var filteredList: ArrayList<ContactsModel>? = ArrayList()
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    lateinit var searchText: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, ContactsListFragment())
                .commit()

        }

        // loadData()


    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId == R.id.searchButton){
            Toast.makeText(this,"Search",Toast.LENGTH_LONG).show()
        }
        else if(item?.itemId ==R.id.deleteButton){
            Toast.makeText(this,"Delete",Toast.LENGTH_LONG).show()
        }
        else if(item?.itemId == R.id.addButton) {
            Toast.makeText(this, "Add", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
*/
    //Pull&Edit data from API with loadData Function
/*
    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call = service.getData(userId = userId!!)

        call.enqueue(object : Callback<List<ContactsModel>> {
            override fun onResponse(
                call: Call<List<ContactsModel>>,
                response: Response<List<ContactsModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {

                        contactModels = ArrayList(it)
                        contactModels?.let {contactsmodel->
                            recyclerViewAdapter = RecyclerViewAdapter(contactsmodel)
                            recyclerViewAdapter?.setType(RecyclerViewAdapter.VIEW_TYPE_ONE)
                            recyclerViewAdapter?.setListener(object :RecyclerViewAdapter.Listener{
                                override fun onItemClick(contactsModel: ContactsModel) {
                                    Toast.makeText(baseContext,"${contactsModel.name}",Toast.LENGTH_LONG).show()
                                }

                            })
                            homeRecyclerView.adapter = recyclerViewAdapter
                        }


                        /*for(contactModel : ContactsModel in contactModels!!){
                            println(contactModel.name)
                            println(contactModel.email)
                        }*/ 
                    }

                }
            }

            override fun onFailure(call: Call<List<ContactsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

 */
/*
    fun getData(){
        val gson = Gson()
        val userResponse = gson.fromJson<PostContactsModel>(intent.getStringExtra("userResponseData"), PostContactsModel::class.java)
    }
*/

    /*
    //SearchView Part

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_group_detail, menu)

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {

        return true

    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText!!.isNotEmpty()) {
            displayList?.clear()
            var search = newText.toLowerCase(Locale.getDefault())
            contactModels!!.forEach {
                if (it.title.toLowerCase(Locale.getDefault()).contains(search)) {
                    displayList!!.add(it)
                }
            }
            homeRecyclerView.adapter!!.notifyDataSetChanged()

        }else{
            displayList?.clear()
            contactModels?.let { displayList?.addAll(it) }
            homeRecyclerView.adapter!!.notifyDataSetChanged()

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
*/

}