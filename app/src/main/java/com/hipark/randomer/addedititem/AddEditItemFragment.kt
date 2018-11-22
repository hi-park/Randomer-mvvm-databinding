package com.hipark.randomer.addedititem

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hipark.randomer.R
import com.hipark.randomer.databinding.FragmentAddEditItemBinding
import com.hipark.randomer.util.setupSnackbar

class AddEditItemFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentAddEditItemBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
        loadData()
    }

    private fun loadData() {
        viewDataBinding.viewmodel?.start(arguments?.getString(ARGUMENT_EDIT_ITEM_ID))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_add_edit_item, container, false)

        viewDataBinding = FragmentAddEditItemBinding.bind(root).apply {
            viewmodel = (activity as AddEditItemActivity).obtainViewModel()
        }

        retainInstance = false
        return viewDataBinding.root
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_item_done)?.apply {
            setOnClickListener {
                viewDataBinding.viewmodel?.saveItem()
            }
        }
    }

    companion object {
        const val ARGUMENT_EDIT_ITEM_ID = "EDIT_ITEM_ID"

        fun newInstance() = AddEditItemFragment()
    }
}