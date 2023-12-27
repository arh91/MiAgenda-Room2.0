package com.example.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListCustomersActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    lateinit var myViewModel: MyViewModel
    lateinit var customerAdapter: CustomersAdapter
    private lateinit var atras: Button
    //private lateinit var firebaseDatabase: FirebaseDatabase
    //private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_customers)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        val onItemClickListener: (String) -> Unit = { code ->
            // Manejar el clic en un elemento para abrir una nueva pantalla con detalles
            val intent = Intent(this, CustomersDetail::class.java)
            intent.putExtra("code", code)
            startActivity(intent)
        }

        customerAdapter = CustomersAdapter(onItemClickListener)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customerAdapter

        atras = findViewById(R.id.btnAtrasRegistroClientes)
        atras.setOnClickListener(){
            val toFifth = Intent(this, FifthActivity::class.java)
            startActivity(toFifth)
        }

        //firebaseDatabase = FirebaseDatabase.getInstance()

        // Inicializar la referencia a la base de datos de Firebase
        //databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Clientes")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        myViewModel.loadCustomers()

        myViewModel.customers.observe(this, Observer { customers ->
            customerAdapter.setCustomers(customers)
        })
    }

    /*private fun listarRegistrosClientes() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val code = dataSnapshot.child("codigo").getValue(String::class.java)
                    code?.let { dataList.add(it) }
                }

                val adapter = CustomersAdapter(dataList, this@ListCustomersActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListCustomersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }*/

    /*override fun accederDatosCliente(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, CustomersDetail::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }*/
}