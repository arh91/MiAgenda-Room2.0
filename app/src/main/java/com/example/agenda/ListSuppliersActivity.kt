package com.example.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListSuppliersActivity : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    lateinit var myViewModel: MyViewModel
    lateinit var supplierAdapter: SuppliersAdapter
    private lateinit var atras: Button
    private lateinit var textViewNombre: TextView
    private lateinit var nombreProveedor: EditText
    private lateinit var ok: Button
    private lateinit var buscarProvPorNombre: Button
    private lateinit var volverListaProv: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_suppliers)

        textViewNombre = findViewById(R.id.textView_nombreProv)
        nombreProveedor = findViewById(R.id.editTextNombreProv)
        ok = findViewById(R.id.btnOkProv)
        volverListaProv = findViewById(R.id.btnVolverListaProv)

        textViewNombre.visibility = View.GONE
        nombreProveedor.visibility = View.GONE
        ok.visibility = View.GONE
        volverListaProv.visibility = View.GONE

        val customerRepository = CustomerRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val supplierRepository = SupplierRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val factory = MyViewModelFactory(customerRepository, supplierRepository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        val onItemClickListener: (String) -> Unit = { code ->
            // Manejar el clic en un elemento para abrir una nueva pantalla con detalles
            val intent = Intent(this, SuppliersDetail::class.java)
            intent.putExtra("code", code)
            startActivity(intent)
        }

        supplierAdapter = SuppliersAdapter(onItemClickListener)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = supplierAdapter

        atras = findViewById(R.id.btnAtrasRegistroProveedores)
        atras.setOnClickListener(){
            val toFourth = Intent(this, FourthActivity::class.java)
            startActivity(toFourth)
        }

        buscarProvPorNombre = findViewById(R.id.findSupplierByName)
        buscarProvPorNombre.setOnClickListener(){
            recyclerView.visibility = View.GONE
            atras.visibility = View.GONE
            buscarProvPorNombre.visibility = View.GONE
            textViewNombre.visibility = View.VISIBLE
            nombreProveedor.visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
        }

        ok.setOnClickListener(){
            val nombre = nombreProveedor.text.toString()
            myViewModel.loadSupplierByName(nombre)
            myViewModel.supplier.observe(this, Observer { suppliers ->
                supplierAdapter.setSuppliers(suppliers)
            })

            textViewNombre.visibility = View.GONE
            nombreProveedor.visibility = View.GONE
            ok.visibility = View.GONE
            volverListaProv.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }

        volverListaProv.setOnClickListener(){
            myViewModel.loadSuppliers()
            myViewModel.suppliers.observe(this, Observer { suppliers ->
                supplierAdapter.setSuppliers(suppliers)
            })
            volverListaProv.visibility = View.GONE
            atras.visibility = View.VISIBLE
            buscarProvPorNombre.visibility = View.VISIBLE
        }

        myViewModel.loadSuppliers()
        myViewModel.suppliers.observe(this, Observer { suppliers ->
            supplierAdapter.setSuppliers(suppliers)
        })

        //firebaseDatabase = FirebaseDatabase.getInstance()

        // Inicializar la referencia a la base de datos de Firebase
        //databaseReference = firebaseDatabase!!.getReference("MyDatabase").child("Proveedores")

        // Obtener datos de la base de datos y actualizar el RecyclerView
        //listarRegistrosProveedores()
    }

    /*private fun listarRegistrosProveedores() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<String>()

                for (dataSnapshot in snapshot.children) {
                    val code = dataSnapshot.child("codigo").getValue(String::class.java)
                    code?.let { dataList.add(it) }
                }

                val adapter = SuppliersAdapter(dataList, this@ListSuppliersActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Si los datos no se han podido mostrar correctamente, lanzamos aviso al usuario
                Toast.makeText(this@ListSuppliersActivity, "No se pudieron obtener los datos. $error", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }*/

    /*override fun accederDatosProveedor(code: String) {
        // Al hacer clic en un elemento, abrir la actividad de detalles
        val intent = Intent(this, SuppliersDetail::class.java)
        intent.putExtra("code", code)
        startActivity(intent)
    }*/
}