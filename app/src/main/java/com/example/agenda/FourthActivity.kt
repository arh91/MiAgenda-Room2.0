package com.example.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class FourthActivity : AppCompatActivity() {

    lateinit var myViewModel: MyViewModel
    // Creamos variables para editText y Buttons
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var codigoProveedor: EditText

    lateinit var insertarDatos: Button
    lateinit var atras: Button
    lateinit var listaProveedores: Button


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        //Inicializamos variables para identificar los editText y Buttons del layout
        codigoProveedor = findViewById<EditText>(R.id.edtCodigoProveedor)
        nombreProveedor = findViewById<EditText>(R.id.edtNombreProveedor)
        telefonoProveedor = findViewById<EditText>(R.id.edtTelefonoProveedor)
        direccionProveedor = findViewById<EditText>(R.id.edtDireccionProveedor)

        insertarDatos = findViewById<Button>(R.id.btnEnviarProveedor)
        atras = findViewById<Button>(R.id.btnAtrasFourth)
        listaProveedores = findViewById<Button>(R.id.btnListaProveedores)

        val customerRepository = CustomerRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val supplierRepository = SupplierRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val factory = MyViewModelFactory(customerRepository, supplierRepository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        //Añadimos evento al botón insertarDatos

        insertarDatos.setOnClickListener {
            // Capturamos cadenas introducidas por usuario y las almacenamos en variables
            var codigoProveedor: String = codigoProveedor.text.toString()
            var nombreProveedor: String = nombreProveedor.text.toString()
            var telefonoProveedor: String = telefonoProveedor.text.toString()
            var direccionProveedor: String = direccionProveedor.text.toString()

            myViewModel.existeCodigoProveedor(codigoProveedor).observeOnce(this, Observer { codigoExists ->
                if (codigoExists) {
                    // Si el código ya existe en la base de datos, lanzar mensaje de aviso al usuario
                    Toast.makeText(this@FourthActivity, "El código introducido ya existe en la base de datos.", Toast.LENGTH_SHORT).show()
                }
                else if (TextUtils.isEmpty(codigoProveedor) || TextUtils.isEmpty(nombreProveedor) || TextUtils.isEmpty(telefonoProveedor) || TextUtils.isEmpty(direccionProveedor)) {
                    // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
                    Toast.makeText(this@FourthActivity, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
                }
                else {
                    //En caso contrario, llamamos al método que añadirá los datos introducidos a Firebase, y posteriormente dejamos en blanco otra vez todos los campos
                    insertarDatos(codigoProveedor, nombreProveedor, direccionProveedor, telefonoProveedor)
                    limpiarTodosLosCampos()
                }
            })
        }

        //Añadimos evento al botón opcionesProveedor
        listaProveedores.setOnClickListener{
            val toListSuppliers = Intent(this, ListSuppliersActivity::class.java)
            startActivity(toListSuppliers)
        }

        //Añadimos evento al botón atrás
        atras.setOnClickListener{
            val toThird = Intent(this, ThirdActivity::class.java)
            startActivity(toThird)
        }
    }


    private fun insertarDatos(codigo: String, nombre: String, direccion: String, telefono: String) {
        val supplier = Supplier(codigoProv = codigo, nombreProv = nombre, direccionProv = direccion, telefonoProv = telefono)
        myViewModel.insertSupplier(supplier)
        Toast.makeText(this@FourthActivity, "Datos guardados.", Toast.LENGTH_SHORT).show()
    }

    //Método que vuelve a dejar en blanco todos los campos del layout
    fun limpiarTodosLosCampos(){
        codigoProveedor.setText("")
        nombreProveedor.setText("")
        direccionProveedor.setText("")
        telefonoProveedor.setText("")
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


