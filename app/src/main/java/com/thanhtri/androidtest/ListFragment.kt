package com.thanhtri.androidtest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanhtri.androidtest.databinding.FragmentListBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment() {

    // Binding for the fragment
    private var _binding: FragmentListBinding? = null

    // Only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    // ViewModel obtained from the Activity
    private val viewModel: AppViewModel by activityViewModels()

    // Adapter for the RecyclerView
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemList.observe(viewLifecycleOwner) { items ->
            adapter = ItemAdapter(items) { selectedItem ->
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                viewModel.setItemSelected(item = selectedItem)
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.recyclerView.adapter = adapter

            adapter.notifyDataSetChanged()
        }

        // Set up Spinner (sort options)
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sortList(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    // Sort the list based on selected option in Spinner
    @SuppressLint("NotifyDataSetChanged")
    private fun sortList(position: Int) {
        when (position) {
            0 -> viewModel.sortData(TypeSort.INDEX) // Sort by Index DESC
            1 -> viewModel.sortData(TypeSort.TITLE) // Sort by Title DESC
            2 -> viewModel.sortData(TypeSort.DATE)  // Sort by Date DESC
        }
        adapter.notifyDataSetChanged()


//        TypeSort.entries.find { it.index == position }?.let {
//            viewModel.sortData(it)
//        }
//
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}