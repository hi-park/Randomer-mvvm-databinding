package com.hipark.randomer.main

import android.databinding.BindingAdapter
import android.widget.ListView
import com.hipark.randomer.data.Item

object ItemsListBindings {

    @BindingAdapter("app:items")
    @JvmStatic fun setItems(listView: ListView, items: List<Item>) {
        with(listView.adapter as MainAdapter) {
            replaceData(items)
        }
    }
}