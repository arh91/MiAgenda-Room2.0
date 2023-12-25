package com.example.agenda

import androidx.lifecycle.LiveData

class CustomerRepository (private val peticionesDao: PeticionesDao) {

    suspend fun insertCustomer(c: Customer) {
        peticionesDao.insertCustomer(c)
    }

    fun getAllCustomers(): List<Customer> {
        return peticionesDao.getAllCustomers()
    }

    suspend fun getCustomerDetails(customerId: String): Customer {
        return peticionesDao.getCustomerById(customerId)
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

}
