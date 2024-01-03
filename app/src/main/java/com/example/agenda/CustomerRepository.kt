package com.example.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CustomerRepository (private val peticionesDao: PeticionesDao) {

    suspend fun insertCustomer(c: Customer) {
        peticionesDao.insertCustomer(c)
    }

    suspend fun getAllCustomers(): List<Customer> {
        return peticionesDao.getAllCustomers()
    }

    suspend fun getCustomerDetails(customerId: String): Customer {
        return peticionesDao.getCustomerById(customerId)
    }

    suspend fun getCustomerByName(customerName: String): List<Customer>{
        return peticionesDao.getCustomerByName(customerName)
    }

    suspend fun deleteCustomerById(customerId: String){
        peticionesDao.deleteCustomerById(customerId)
    }

    suspend fun updateCustomerById(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        peticionesDao.updateCustomerById(codigo, nuevoNombre, nuevaDireccion, nuevoTelefono)
    }


    fun isCodigoCustomerExists(codigo: String): LiveData<Boolean> {
        return peticionesDao.isCodigoCustomerExists(codigo)
    }

    suspend fun obtenerNumeroClientes(): Int {
        return peticionesDao.getNumberOfCustomers()
    }

    suspend fun obtenerNumeroClientesPorNombre(nombre: String): Int {
        return peticionesDao.getNumberOfCustomersByName(nombre)
    }

}
