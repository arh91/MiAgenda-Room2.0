package com.example.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class FifthActivity : AppCompatActivity() {

    // Creamos variables para editText y Buttons
    lateinit var codigoCliente:EditText
    lateinit var nombreCliente:EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText

    lateinit var insertarDatos: Button
    lateinit var atras: Button
    lateinit var listaClientes: Button

    lateinit var myViewModel: MyViewModel
    lateinit var customerAdapter: CustomersAdapter
    //lateinit var peticionesDao: PeticionesDao

    //lateinit var firebaseDatabase: FirebaseDatabase

    //Creamos variable para referenciar nuestra base de datos
    //lateinit var databaseReference: DatabaseReference


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)


        val customerRepository = CustomerRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val supplierRepository = SupplierRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val factory = MyViewModelFactory(customerRepository, supplierRepository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)
        //customerAdapter = CustomersAdapter()

        //Inicializamos variables para identificar los editText y Buttons del layout
        codigoCliente = findViewById<EditText>(R.id.edtCodigoCliente)
        nombreCliente = findViewById<EditText>(R.id.edtNombreCliente)
        telefonoCliente = findViewById<EditText>(R.id.edtTelefonoCliente)
        direccionCliente = findViewById<EditText>(R.id.edtDireccionCliente)

        insertarDatos = findViewById<Button>(R.id.btnEnviarCliente)
        atras = findViewById<Button>(R.id.btnAtrasFifth)
        listaClientes = findViewById<Button>(R.id.btnListaClientes)


        //firebaseDatabase = FirebaseDatabase.getInstance()

        // Obtenemos la referencia a nuestra base de datos en Firebase
        //databaseReference = firebaseDatabase!!.getReference("MyDatabase")


        //Añadimos evento al botón insertarDatos

        insertarDatos.setOnClickListener {
            // Capturamos cadenas introducidas por usuario y las almacenamos en variables
            var codigoCliente: String = codigoCliente.text.toString()
            var nombreCliente: String = nombreCliente.text.toString()
            var telefonoCliente: String = telefonoCliente.text.toString()
            var direccionCliente: String = direccionCliente.text.toString()

            myViewModel.existeCodigoCliente(codigoCliente).observe(this, Observer { codigoExists ->
                if (codigoExists) {
                    // Si el código ya existe en la base de datos, lanzar mensaje de aviso al usuario
                    Toast.makeText(this@FifthActivity, "El código introducido ya existe en la base de datos.", Toast.LENGTH_SHORT).show()
                }
                else if (TextUtils.isEmpty(codigoCliente) || TextUtils.isEmpty(nombreCliente) || TextUtils.isEmpty(telefonoCliente) || TextUtils.isEmpty(direccionCliente)) {
                    // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
                    Toast.makeText(this@FifthActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
                }
                else {
                    //En caso contrario, llamamos al método que añadirá los datos introducidos a Firebase, y posteriormente dejamos en blanco otra vez todos los campos
                    insertarDatos(codigoCliente, nombreCliente, direccionCliente, telefonoCliente)
                    limpiarTodosLosCampos()
                }
            })
        }


        //Añadimos evento al boton masOpciones

        listaClientes.setOnClickListener(){
            val toCustomersList = Intent(this, ListCustomersActivity::class.java)
            startActivity(toCustomersList)
        }


        //Añadimos evento al botón atras

        atras.setOnClickListener{
            val toThird = Intent(this, ThirdActivity::class.java)
            startActivity(toThird)
        }
    }

    private fun insertarDatos(codigo: String, nombre: String, direccion: String, telefono: String) {
        val customer = Customer(codigoCli = codigo, nombreCli = nombre, direccionCli = direccion, telefonoCli = telefono)
        myViewModel.insertCustomer(customer)
        Toast.makeText(this@FifthActivity, "Datos guardados.", Toast.LENGTH_SHORT).show()
    }

    //Método que vuelve a dejar en blanco todos los campos del layout
    fun limpiarTodosLosCampos(){
       codigoCliente.setText("")
       nombreCliente.setText("")
       direccionCliente.setText("")
       telefonoCliente.setText("")
    }

}