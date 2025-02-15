package com.ej.riversideassignment.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ej.riversideassignment.R
import com.ej.riversideassignment.databinding.TitleItemBinding
import com.ej.riversideassignment.model.TitleDetails

class TitleAdapter(
    private val onItemClicked: (TitleDetails) -> Unit,
    private val onFavouriteClicked: (String, Boolean) -> Unit,
) : ListAdapter<TitleDetails, TitleAdapter.ViewHolder>(TitleCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TitleItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TitleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun bind(item: TitleDetails) {
            binding.apply {
                title.text = item.title
                year.text = context.getString(R.string.release_year, item.year)
                favourite.isSelected = item.isFavourite
                Glide.with(root).load(item.posterUrl).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(image)

                favourite.setOnClickListener {
                    onFavouriteClicked(item.imdbID, !favourite.isSelected)
                }

                cardViewContainer.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }
}