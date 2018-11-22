package com.hipark.randomer.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hipark.randomer.R
import com.hipark.randomer.addedititem.AddEditItemActivity
import com.hipark.randomer.itemdetail.ItemDetailActivity
import com.hipark.randomer.util.obtainViewModel
import com.hipark.randomer.util.replaceFragmentInActivity

class MainActivity : AppCompatActivity(), ItemsNavigator {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewFragment()

        viewModel = obtainViewModel().apply {

            openItemEvent.observe(this@MainActivity, Observer<String> { itemId ->
                if (itemId != null) {
                    openItemDetails(itemId)
                }
            })

            newItemEvent.observe(this@MainActivity, Observer<Void> {
                this@MainActivity.addNewItem()
            })
        }
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame) ?:
            MainFragment.newInstance().let {
                replaceFragmentInActivity(it, R.id.contentFrame)
            }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.handleActivityResult(requestCode, resultCode)
    }

    override fun openItemDetails(itemId: String) {
        val intent = Intent(this, ItemDetailActivity::class.java).apply {
            putExtra(ItemDetailActivity.EXTRA_ITEM_ID, itemId)
        }
        startActivityForResult(intent, AddEditItemActivity.REQUEST_CODE)
    }

    override fun addNewItem() {
        val intent = Intent(this, AddEditItemActivity::class.java)
        startActivityForResult(intent, AddEditItemActivity.REQUEST_CODE)

    }

    fun obtainViewModel() : MainViewModel = obtainViewModel(MainViewModel::class.java)
}
