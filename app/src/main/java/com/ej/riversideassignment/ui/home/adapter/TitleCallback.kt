package com.ej.riversideassignment.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ej.riversideassignment.model.TitleDetails

class TitleCallback : DiffUtil.ItemCallback<TitleDetails>() {
    override fun areItemsTheSame(oldItem: TitleDetails, newItem: TitleDetails): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: TitleDetails, newItem: TitleDetails): Boolean {
        return oldItem == newItem
    }
}