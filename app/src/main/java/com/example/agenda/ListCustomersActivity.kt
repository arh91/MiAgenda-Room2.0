package com.example.agenda

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ListCustomersActivity: AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    lateinit var myViewModel: MyViewModel
    lateinit var customerAdapter: CustomersAdapter
    private lateinit var atras: Button
    private lateinit var textViewNombre: TextView
    private lateinit var nombreCliente: EditText
    private lateinit var ok: Button
    private lateinit var buscarCliPorNombre: Button
    private lateinit var volverListaCli: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_customers)

        textViewNombre = findViewById(R.id.textView_nombreCli)
        nombreCliente = findViewById(R.id.editTextNombreCli)
        ok = findViewById(R.id.btnOkCli)
        volverListaCli = findViewById(R.id.btnVolverListaCli)

        textViewNombre.visibility = View.GONE
        nombreCliente.visibility = View.GONE
        ok.visibility = View.GONE
        volverListaCli.visibility = View.GONE

        val customerRepository =
            CustomerRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val supplierRepository =
            SupplierRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val factory = MyViewModelFactory(customerRepository, supplierRepository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

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
        atras.setOnClickListener() {
            val toFifth = Intent(this, FifthActivity::class.java)
            startActivity(toFifth)
        }

        buscarCliPorNombre = findViewById(R.id.findCustomerByName)
        buscarCliPorNombre.setOnClickListener() {
            recyclerView.visibility = View.GONE
            atras.visibility = View.GONE
            buscarCliPorNombre.visibility = View.GONE
            textViewNombre.visibility = View.VISIBLE
            nombreCliente.visibility = View.VISIBLE
            ok.visibility = View.VISIBLE
            volverListaCli.visibility = View.VISIBLE
        }

        ok.setOnClickListener() {
            val nombre = nombreCliente.text.toString()

            if(TextUtils.isEmpty(nombre)){
                mostrarToastEnLaMitadDeLaPantalla("Por favor, introduzca un nombre.")
            }else{
                myViewModel.loadCustomerByName(nombre)

                myViewModel.customer.observeOnce(this, Observer { customers ->
                    customerAdapter.setCustomers(customers)
                })

                textViewNombre.visibility = View.GONE
                nombreCliente.visibility = View.GONE
                ok.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                myViewModel.viewModelScope.launch{
                    if(myViewModel.obtenerNumeroClientesPorNombre(nombre)==0){
                        mostrarToastEnLaMitadDeLaPantalla("No existe ningún cliente con ese nombre en la base de datos.")
                    }
                }
            }
        }

        volverListaCli.setOnClickListener() {
            myViewModel.loadCustomers()
            myViewModel.customers.observeOnce(this, Observer { customers ->
                customerAdapter.setCustomers(customers)
            })

            textViewNombre.visibility = View.GONE
            nombreCliente.visibility = View.GONE
            ok.visibility = View.GONE
            volverListaCli.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            atras.visibility = View.VISIBLE
            buscarCliPorNombre.visibility = View.VISIBLE
        }

        // Obtener datos de la base de datos y actualizar el RecyclerView
        myViewModel.loadCustomers()

        myViewModel.customers.observeOnce(this, Observer { customers ->
            customerAdapter.setCustomers(customers)
        })

        myViewModel.viewModelScope.launch {
            if (myViewModel.obtenerNumeroClientes()==0) {
                mostrarToastEnLaMitadDeLaPantalla("No existe ningún cliente en la base de datos")
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

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<in T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}