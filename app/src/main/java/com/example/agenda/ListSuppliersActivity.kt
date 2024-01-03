package com.example.agenda

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

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

            myViewModel.viewModelScope.launch{
                if(myViewModel.obtenerNumeroProveedoresPorNombre(nombre)==0){
                    mostrarToastEnLaMitadDeLaPantalla("No existe ningún proveedor con ese nombre en la base de datos.")
                }
            }
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

        myViewModel.viewModelScope.launch {
            if (myViewModel.obtenerNumeroProveedores()==0) {
                mostrarToastEnLaMitadDeLaPantalla("No existe ningún proveedor en la base de datos")
            }
        }
    }

    private fun mostrarToastEnLaMitadDeLaPantalla(mensaje: String) {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.custom_toast_root))

        // Crea un objeto Toast personalizado con la vista personalizada
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        layout.findViewById<TextView>(R.id.custom_toast_text).text = mensaje

        // Muestra el Toast personalizado
        toast.show()
    }
}