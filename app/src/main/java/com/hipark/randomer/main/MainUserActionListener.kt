package com.hipark.randomer.main

import com.hipark.randomer.data.Item

interface MainUserActionListener {
    fun onItemClicked(item: Item)
}