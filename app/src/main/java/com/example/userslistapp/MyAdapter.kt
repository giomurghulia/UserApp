package com.example.userslistapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userslistapp.databinding.LayoutRowBinding


class MyAdapter : ListAdapter<User, MyAdapter.MyViewHolder>(MyDiffUtil()) {

    private var callBack: CallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class MyViewHolder(
        private val binding: LayoutRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            val fullName = item.name + " " + item.lastName
            binding.fullNameText.text = fullName

            binding.editButton.setOnClickListener {
                callBack?.onEditClick(item.id)
            }

            binding.deleteButton.setOnClickListener {
                callBack?.onDeleteClick(item.id)
            }
        }
    }

    interface CallBack {
        fun onEditClick(id: String)
        fun onDeleteClick(id: String)
    }
}
