package com.example.agenda

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class CustomersDetail : AppCompatActivity() {

    lateinit var myViewModel: MyViewModel

    lateinit var codigoCliente: EditText
    lateinit var nombreCliente: EditText
    lateinit var telefonoCliente: EditText
    lateinit var direccionCliente: EditText
    lateinit var eliminarCliente: Button
    lateinit var modificarCliente: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    lateinit var nombreAntiguo: String
    lateinit var direccionAntigua: String
    lateinit var telefonoAntiguo: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customers_detail)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        codigoCliente = findViewById(R.id.editText_codigo_cli)
        nombreCliente = findViewById(R.id.editText_nombre_cli)
        direccionCliente = findViewById(R.id.editText_direccion_cli)
        telefonoCliente = findViewById(R.id.editText_telefono_cli)

        eliminarCliente = findViewById(R.id.btn_Eliminar_cli)
        modificarCliente = findViewById(R.id.btn_Modificar_cli)
        atras = findViewById(R.id.btn_Atras_Lista_Cli)

        eliminarCliente.setOnClickListener(){
            eliminarCliente(this)
        }

        modificarCliente.setOnClickListener(){
            modificarCliente(this)
        }

        atras.setOnClickListener(){
            val intentListCustomers = Intent(this, ListCustomersActivity::class.java)
            startActivity(intentListCustomers)
        }

        listarDatosCliente(this)
    }


    private fun listarDatosCliente(context: Context) {
        val code = intent.getStringExtra("code").toString()

        myViewModel.loadCustomerDetails(code)

        myViewModel.customerDetails.observe(this, Observer { customer ->
            // Aquí actualizas tu interfaz de usuario con los detalles del cliente
            // Puedes acceder a los campos de customer, por ejemplo, customer.nombre, customer.direccion, etc.
            val nombre = customer.nombreCli
            val direccion = customer.direccionCli
            val telefono = customer.telefonoCli

            codigoCliente.setText(code)
            nombreCliente.setText(nombre)
            direccionCliente.setText(direccion)
            telefonoCliente.setText(telefono)

            codigoCliente.isEnabled = false

            nombreAntiguo = nombreCliente.text.toString()
            direccionAntigua = direccionCliente.text.toString()
            telefonoAntiguo = telefonoCliente.text.toString()
        })
    }


    private fun eliminarCliente(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoCliente.text.toString()

        myViewModel.existeCodigoCliente(codigo).observe(this, Observer { codigoExists ->
            if (!codigoExists) {
                // Si el código no existe en la base de datos, lanzar mensaje de aviso al usuario
                Toast.makeText(this@CustomersDetail, "El código introducido no existe en la base de datos.", Toast.LENGTH_SHORT).show()
            }
            else {
                alertDialog.apply {
                    setTitle("Advertencia")
                    setMessage("¿Está seguro que desea eliminar el cliente " + codigo + "?")
                    setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                        myViewModel.deleteCustomer(codigo)
                        limpiarTodosLosCampos()
                        volverAListaClientes()
                        Toast.makeText(
                            this@CustomersDetail,
                            "El cliente ha sido eliminado de la base de datos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    setNegativeButton("Cancelar") { _, _ ->
                        Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                    }
                }.create().show()
            }
        })
    }


    private fun modificarCliente(context: Context) {
        //databaseReference = firebaseDatabase!!.getReference("MyDatabase")
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoCliente.text.toString()
        val nombre = nombreCliente.text.toString()
        val direccion = direccionCliente.text.toString()
        val telefono = telefonoCliente.text.toString()

        myViewModel.existeCodigoCliente(codigo).observe(this, Observer { codigoExists ->
            if (!codigoExists) {
                // Si el código no existe en la base de datos, lanzar mensaje de aviso al usuario
                Toast.makeText(
                    this@CustomersDetail,
                    "El código introducido no existe en la base de datos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
                // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
                Toast.makeText(this@CustomersDetail, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            } else if(nombre==nombreAntiguo && direccion==direccionAntigua && telefono==telefonoAntiguo){
            Toast.makeText(this@CustomersDetail, "No se ha modificado ningún campo.", Toast.LENGTH_SHORT).show()
            } else {
                alertDialog.apply {
                    setTitle("Advertencia")
                    setMessage("¿Está seguro que desea modificar el cliente " + codigo + "?")
                    setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                        myViewModel.updateCustomer(codigo, nombre, direccion, telefono)
                        Toast.makeText(this@CustomersDetail, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                        limpiarTodosLosCampos()
                        volverAListaClientes()
                    }
                    setNegativeButton("Cancelar") { _, _ ->
                        Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                    }
                }.create().show()
            }
        })
    }


    private fun limpiarTodosLosCampos(){
        codigoCliente.setText("")
        nombreCliente.setText("");
        direccionCliente.setText("");
        telefonoCliente.setText("");
    }

    private fun volverAListaClientes(){
        val intentListCustomers = Intent(this, ListCustomersActivity::class.java)
        startActivity(intentListCustomers)
    }

}