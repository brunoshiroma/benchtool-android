package com.brunoshiroma.benchtool_android.model

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.brunoshiroma.benchtool_android.R

object BindingExtensions{

    /*
    Set the values of platform "combo"
     */
    @BindingAdapter("platformAdapter")
    @JvmStatic
    fun platformAdapter(view: AutoCompleteTextView, platformAdapter: String?){

        if(view.adapter == null || view.adapter?.isEmpty == true){
            val items = listOf("java", "go", "rust")
            val arrayAdapter = ArrayAdapter(view.context, R.layout.list_item, items)
            view.setAdapter(arrayAdapter)
        }
    }

}