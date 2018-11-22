package com.hipark.randomer.main

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hipark.randomer.data.Item

import com.hipark.randomer.databinding.ItemBinding


class MainAdapter(
    private var items: List<Item>,
    private val itemsViewModel: MainViewModel
) : BaseAdapter() {

    fun replaceData(items: List<Item>) {
        setList(items)
    }

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val binding: ItemBinding

        if(view == null) {
            val inflater = LayoutInflater.from(viewGroup.context)

            binding = ItemBinding.inflate(inflater, viewGroup, false)
        } else {
            binding = DataBindingUtil.getBinding<ItemBinding>(view)!!
        }

        val userActionListener = object : MainUserActionListener {
            override fun onItemClicked(item: Item) {

                itemsViewModel.openItemEvent.value = item.id
            }
        }

        with(binding) {
            item = items[position]
            listener = userActionListener
            executePendingBindings()
        }

        return binding.root
    }

    private fun setList(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

}