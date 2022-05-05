package com.filundmoshpit.mymovies.presentation.watch_later

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.databinding.FragmentWatchLaterBinding
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import com.filundmoshpit.mymovies.presentation.contextComponent
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class WatchLaterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: WatchLaterViewModelFactory

    private lateinit var viewModel: WatchLaterViewModel

    private var _binding: FragmentWatchLaterBinding? = null
    private val binding: FragmentWatchLaterBinding
        get() = _binding!!

    private var listAdapter = WatchLaterListAdapter()

    override fun onAttach(context: Context) {
        context.contextComponent.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(WatchLaterViewModel::class.java)

        //ViewModel observers
        lifecycleScope.launchWhenCreated {
            viewModel.movies.collect {
                listAdapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.status.collect {
                onStatusChange(it)
            }
        }

        //Load data
        //viewModel.load()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchLaterBinding.inflate(inflater, container, false)

        binding.list.setHasFixedSize(true)
        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun onStatusChange(status: LoadingStatuses) {
        binding.errorLabel.visibility = View.GONE
        binding.loadingSpinner.visibility = View.GONE
        binding.list.visibility = View.GONE

        when (status) {
            LoadingStatuses.EMPTY -> {
                binding.errorLabel.visibility = View.VISIBLE
            }
            LoadingStatuses.LOADING -> {
                binding.loadingSpinner.visibility = View.VISIBLE
            }
            LoadingStatuses.LOADED -> {
                binding.list.visibility = View.VISIBLE
            }
        }
    }
}