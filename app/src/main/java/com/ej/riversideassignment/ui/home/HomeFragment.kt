package com.ej.riversideassignment.ui.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.ej.riversideassignment.R
import com.ej.riversideassignment.databinding.FragmentHomeBinding
import com.ej.riversideassignment.model.TitleDetails
import com.ej.riversideassignment.ui.base.BaseFragment
import com.ej.riversideassignment.ui.home.adapter.TitleAdapter
import com.ej.riversideassignment.utils.ZoomLinearLayoutManager
import com.ej.riversideassignment.utils.hideKeyboard
import com.ej.riversideassignment.utils.launchOnStart
import com.ej.riversideassignment.utils.toGone
import com.ej.riversideassignment.utils.toVisible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel by viewModels<HomeViewModel>()

    private var titleAdapter: TitleAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchOnStart {
            viewModel.uiState.collect {
                when (it) {
                    UiState.Initial -> {
                        binding.progress.toGone()
                    }
                    is UiState.Empty -> {
                        binding.progress.toGone()
                        titleAdapter?.submitList(emptyList())
                        Snackbar.make(binding.root,
                            getString(R.string.no_titles_error),
                            Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Failure -> {
                        binding.progress.toGone()
                        Snackbar.make(binding.root, it.stringRes, Toast.LENGTH_SHORT).show()
                    }

                    UiState.Loading -> binding.progress.toVisible()
                    is UiState.Success -> {
                        binding.progress.toGone()
                        titleAdapter?.submitList(it.data)
                    }
                }
            }
        }

        initViews()
        initAdapter()
    }

    private fun initViews() {
        binding.movieTitleET.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onTriggerEvent(HomeIntent.SearchByTitle(binding.movieTitleET.text.toString()))
                binding.movieTitleET.hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun initAdapter() {
        titleAdapter = TitleAdapter(onItemClicked = {
            navigateToTitleDetails(it)
        }, onFavouriteClicked = { titleId, isFavourite ->
            viewModel.onTriggerEvent(HomeIntent.FavouriteTitle(titleId, isFavourite))
        })
        val snapHelper = LinearSnapHelper()
        binding.titlesRV.apply {
            layoutManager = ZoomLinearLayoutManager(requireContext())
            itemAnimator = null
            adapter = titleAdapter
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun navigateToTitleDetails(selectedTitle: TitleDetails) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(
            selectedTitle.imdbID))
    }
}