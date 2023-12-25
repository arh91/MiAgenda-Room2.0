package com.example.agenda

import androidx.lifecycle.LiveData

class SupplierRepository (private val peticionesDao: PeticionesDao) {

    suspend fun insertSupplier(s: Supplier) {
        peticionesDao.insertSupplier(s)
    }

    fun getAllSuppliers(): List<Supplier> {
        return peticionesDao.getAllSuppliers()
    }

    suspend fun getSupplierDetails(supplierId: String): Supplier {
        return peticionesDao.getSupplierById(supplierId)
    }

    suspend fun deleteSupplierById(supplierId: String){
        peticionesDao.deleteSupplierById(supplierId)
    }

    suspend fun updateSupplierById(codigo: String, nuevoNombre: String, nuevaDireccion: String, nuevoTelefono: String) {
        peticionesDao.updateSupplierById(codigo, nuevoNombre, nuevaDireccion, nuevoTelefono)
    }


    fun isCodigoSupplierExists(codigo: String): LiveData<Boolean> {
        return peticionesDao.isCodigoSupplierExists(codigo)
    }
}
