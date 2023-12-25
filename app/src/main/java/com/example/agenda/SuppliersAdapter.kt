package com.example.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SuppliersAdapter (private val onItemClickListener: (String) -> Unit) :
    ListAdapter<Supplier, SuppliersAdapter.SupplierViewHolder>(SupplierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_suppliers, parent, false)
        return SupplierViewHolder(itemView)
    }

    fun setSuppliers(suppliers: List<Supplier>) {
        submitList(suppliers.toMutableList())
    }

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val supplier = getItem(position)
        holder.bind(supplier, onItemClickListener)
    }

    /*override fun getItemCount(): Int {
        return data.size
    }*/

    class SupplierViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val codeTextView: TextView = itemView.findViewById(R.id.tvCode)

        fun bind(supplier: Supplier, onItemClickListener: (String) -> Unit) {
            codeTextView.text = supplier.codigoProv.toString()

            // Configurar el clic en un elemento
            itemView.setOnClickListener {
                onItemClickListener(supplier.codigoProv.toString())
            }
        }
    }

    class SupplierDiffCallback : DiffUtil.ItemCallback<Supplier>() {
        override fun areItemsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            return oldItem.codigoProv == newItem.codigoProv
        }

        override fun areContentsTheSame(oldItem: Supplier, newItem: Supplier): Boolean {
            return oldItem == newItem
        }
    }
}