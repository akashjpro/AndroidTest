package com.thanhtri.androidtest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thanhtri.androidtest.databinding.FragmentDetailBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    // Binding for the fragment (only valid between onCreateView and onDestroyView)
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // Shared ViewModel obtained from the Activity
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = viewModel.itemSelected.value

        binding.title.text = item?.title
        binding.desc.text = item?.description
        binding.date.text = item?.date

        item?.let {
            binding.deleteButton.setOnClickListener {
                showDeleteConfirmationDialog(item) {
                    viewModel.removeItem(item)
                    findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
                }
            }
        }

    }

    /**
     * Displays a confirmation dialog to delete the specified item.
     *
     * @param item The item to be deleted.
     * @param onConfirm Callback executed if the user confirms the deletion.
     */
    private fun showDeleteConfirmationDialog(item: Item, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.title} ?")
            .setPositiveButton("Delete") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}