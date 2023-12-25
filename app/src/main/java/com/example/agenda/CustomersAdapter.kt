package com.example.agenda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CustomersAdapter(private val onItemClickListener: (String) -> Unit) :
    ListAdapter<Customer, CustomersAdapter.CustomerViewHolder>(CustomerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_customers, parent, false)
        return CustomerViewHolder(itemView)
    }

    fun setCustomers(customers: List<Customer>) {
        submitList(customers.toMutableList())
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = getItem(position)
        holder.bind(customer, onItemClickListener)
    }

    /*override fun getItemCount(): Int {
        return data.size
    }*/

    class CustomerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val codeTextView: TextView = itemView.findViewById(R.id.tvCode)

        fun bind(customer: Customer, onItemClickListener: (String) -> Unit) {
            codeTextView.text = customer.codigoCli.toString()

            // Configurar el clic en un elemento
            itemView.setOnClickListener {
                onItemClickListener(customer.codigoCli.toString())
            }
        }
    }

    class CustomerDiffCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem.codigoCli == newItem.codigoCli
        }

        override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
            return oldItem == newItem
        }
    }
}