package com.filundmoshpit.mymovies.presentation.watch_later

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.filundmoshpit.mymovies.BusEvents
import com.filundmoshpit.mymovies.presentation.contextComponent
import com.filundmoshpit.mymovies.databinding.FragmentWatchLaterBinding
import com.filundmoshpit.mymovies.domain.MovieEntity
import com.filundmoshpit.mymovies.presentation.LoadingStatuses
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class WatchLaterFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: WatchLaterViewModelFactory

    private lateinit var viewModel: WatchLaterViewModel

    private lateinit var binding: FragmentWatchLaterBinding

    private var listAdapter = WatchLaterListAdapter()

    override fun onAttach(context: Context) {
        context.contextComponent.inject(this)

        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(WatchLaterViewModel::class.java)

        //ViewModel observers
        lifecycleScope.launchWhenCreated { viewModel.movies.collect { listAdapter.submitList(it as MutableList<MovieEntity>) } }
        lifecycleScope.launchWhenStarted { viewModel.status.collect { onStatusChange(it) } }

        //Load data
        viewModel.load()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchLaterBinding.inflate(inflater, container, false)

        binding.list.setHasFixedSize(true)
        binding.list.itemAnimator = null
        binding.list.adapter = listAdapter

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
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

    @Subscribe
    fun onWatchLaterChanged(event: BusEvents.WatchLaterChanged) {
        viewModel.load()
    }
}