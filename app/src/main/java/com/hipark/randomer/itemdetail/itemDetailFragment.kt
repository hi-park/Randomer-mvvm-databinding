package com.hipark.randomer.itemdetail

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipark.randomer.R
import com.hipark.randomer.databinding.FragmentItemdetailBinding
import com.hipark.randomer.util.setupSnackbar

class ItemDetailFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentItemdetailBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.fab_edit_item)?.setOnClickListener {
            viewDataBinding.viewmodel?.editItem()
        }
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewmodel?.start(arguments?.getString(ARGUMENT_ITEM_ID))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_itemdetail, container, false)
        viewDataBinding = FragmentItemdetailBinding.bind(view).apply {
            viewmodel = (activity as ItemDetailActivity).obtainViewModel()
            listener = object : ItemDetailUserActionsListener {
                override fun onDelete(v: View) {
                    viewmodel?.deleteItem()
                }
            }

        }
        return view
    }

    companion object {

        const val ARGUMENT_ITEM_ID = "ITEM_ID"
        const val REQUEST_EDIT_ITEM = 1

        fun newInstance(itemId: String?) = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARGUMENT_ITEM_ID, itemId)
                    }
                }
    }
}