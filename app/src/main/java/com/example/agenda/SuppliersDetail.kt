package com.example.agenda

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class SuppliersDetail : AppCompatActivity() {

    lateinit var myViewModel: MyViewModel

    lateinit var codigoProveedor: EditText
    lateinit var nombreProveedor: EditText
    lateinit var telefonoProveedor: EditText
    lateinit var direccionProveedor: EditText
    lateinit var eliminarProveedor: Button
    lateinit var modificarProveedor: Button
    lateinit var limpiar: Button
    lateinit var atras: Button

    lateinit var nombreAntiguo: String
    lateinit var direccionAntigua: String
    lateinit var telefonoAntiguo: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suppliers_detail)

        val customerRepository = CustomerRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val supplierRepository = SupplierRepository(AgendaDatabase.getInstance(applicationContext).peticionesDao())
        val factory = MyViewModelFactory(customerRepository, supplierRepository)
        myViewModel = ViewModelProvider(this, factory).get(MyViewModel::class.java)

        codigoProveedor = findViewById(R.id.editText_codigo_prov)
        nombreProveedor = findViewById(R.id.editText_nombre_prov)
        direccionProveedor = findViewById(R.id.editText_direccion_prov)
        telefonoProveedor = findViewById(R.id.editText_telefono_prov)

        eliminarProveedor = findViewById(R.id.btn_Eliminar_Prov)
        modificarProveedor = findViewById(R.id.btn_Modificar_Prov)
        atras = findViewById(R.id.btn_Atras_Lista_Prov)

        eliminarProveedor.setOnClickListener(){
            eliminarProveedor(this)
        }

        modificarProveedor.setOnClickListener(){
            modificarProveedor(this)
        }

        atras.setOnClickListener(){
            val intentListSuppliers = Intent(this, ListSuppliersActivity::class.java)
            startActivity(intentListSuppliers)
        }

        listarDatosProveedor(this)
    }


    private fun listarDatosProveedor(context: Context) {
        val code = intent.getStringExtra("code").toString()

        myViewModel.loadSupplierDetails(code)

        myViewModel.supplierDetails.observe(this, Observer { supplier ->
            // Aquí actualizas tu interfaz de usuario con los detalles del cliente
            // Puedes acceder a los campos de customer, por ejemplo, customer.nombre, customer.direccion, etc.
            val nombre = supplier.nombreProv
            val direccion = supplier.direccionProv
            val telefono = supplier.telefonoProv

            codigoProveedor.setText(code)
            nombreProveedor.setText(nombre)
            direccionProveedor.setText(direccion)
            telefonoProveedor.setText(telefono)

            codigoProveedor.isEnabled = false

            nombreAntiguo = nombreProveedor.text.toString()
            direccionAntigua = direccionProveedor.text.toString()
            telefonoAntiguo = telefonoProveedor.text.toString()
        })
    }


    private fun eliminarProveedor(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoProveedor.text.toString()

        myViewModel.existeCodigoProveedor(codigo).observe(this, Observer { codigoExists ->
            if (!codigoExists) {
                // Si el código no existe en la base de datos, lanzar mensaje de aviso al usuario
                Toast.makeText(this@SuppliersDetail, "El código introducido no existe en la base de datos.", Toast.LENGTH_SHORT).show()
            }
            else {
                alertDialog.apply {
                    setTitle("Advertencia")
                    setMessage("¿Está seguro que desea eliminar el proveedor "+codigo+"?")
                    setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                        myViewModel.deleteSupplier(codigo)
                        limpiarTodosLosCampos()
                        volverAListaProveedores()
                        Toast.makeText(this@SuppliersDetail, "El proveedor ha sido eliminado de la base de datos.", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Cancelar") { _, _ ->
                        Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                    }
                }.create().show()
            }
        })
    }


    private fun modificarProveedor(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        val codigo = codigoProveedor.text.toString()
        val direccion = direccionProveedor.text.toString()
        val nombre = nombreProveedor.text.toString()
        val telefono = telefonoProveedor.text.toString()

        myViewModel.existeCodigoProveedor(codigo).observe(this, Observer { codigoExists ->
            if (!codigoExists) {
                // Si el código no existe en la base de datos, lanzar mensaje de aviso al usuario
                Toast.makeText(this@SuppliersDetail, "El código introducido no existe en la base de datos.", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(direccion)) {
                // Si alguno de los campos está sin rellenar, lanzamos aviso al usuario para que los rellene todos.
                Toast.makeText(this@SuppliersDetail, "Por favor, rellena todos los campos.", Toast.LENGTH_SHORT).show()
            }
            else if(nombre==nombreAntiguo && direccion==direccionAntigua && telefono==telefonoAntiguo){
                Toast.makeText(this@SuppliersDetail, "No se ha modificado ningún campo.", Toast.LENGTH_SHORT).show()
            }
            else{
                alertDialog.apply {
                    setTitle("Advertencia")
                    setMessage("¿Está seguro que desea modificar el proveedor "+codigo+"?")
                    setPositiveButton("Aceptar") { _: DialogInterface?, _: Int ->
                        myViewModel.updateSupplier(codigo, nombre, direccion, telefono)
                        Toast.makeText(this@SuppliersDetail, "Registro actualizado correctamente.", Toast.LENGTH_SHORT).show()
                        limpiarTodosLosCampos()
                        volverAListaProveedores()
                    }
                    setNegativeButton("Cancelar") { _, _ ->
                        Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
                    }
                }.create().show()
            }
        })
    }


    private fun limpiarTodosLosCampos(){
        codigoProveedor.setText("")
        nombreProveedor.setText("");
        direccionProveedor.setText("");
        telefonoProveedor.setText("");
    }

    private fun volverAListaProveedores(){
        val intentListSuppliers = Intent(this, ListSuppliersActivity::class.java)
        startActivity(intentListSuppliers)
    }
}