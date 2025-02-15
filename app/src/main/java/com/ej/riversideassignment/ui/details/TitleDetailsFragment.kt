package com.ej.riversideassignment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ej.riversideassignment.utils.launchOnStart
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TitleDetailsFragment : Fragment() {
    private val viewModel by viewModels<TitleDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        val titleId = requireArguments().getString(TITLE_ID_KEY)
            ?: throw IllegalArgumentException("titleId argument is required")

        viewModel.onTriggerEvent(DetailsIntent.FetchMovieDetails(titleId))

        launchOnStart {
            viewModel.events.collect { event ->
                when (event) {
                    is Event.ShowSnackbar -> Snackbar.make(requireView(),
                        event.stringRes,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        val composeView = ComposeView(requireContext())

        composeView.setContent {
            val uiState by viewModel.uiState.collectAsState()
            TitleDetailsScreen(uiState = uiState, viewModel::onTriggerEvent)
        }
        return composeView
    }

    companion object {
        const val TITLE_ID_KEY = "titleId"
    }
}