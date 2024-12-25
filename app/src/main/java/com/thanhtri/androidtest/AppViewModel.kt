package com.thanhtri.androidtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
    // Backing property for the item list, exposed as LiveData
    private val _itemList = MutableLiveData<List<Item>>(emptyList())
    val itemList: LiveData<List<Item>> get() = _itemList

    // Backing property for the selected item, exposed as LiveData
    private val _itemSelected = MutableLiveData<Item?>()
    val itemSelected: LiveData<Item?> get() = _itemSelected

    // Update the list of items
    fun setItemList(items: List<Item>) {
        _itemList.value = items
    }

    /**
     * Removes the specified item from the list and updates the LiveData.
     *
     * @param item The item to be removed from the list.
     */
    fun removeItem(item: Item) {
        _itemList.value = _itemList.value?.filter { it != item }
    }

    // Set the selected item
    fun setItemSelected(item: Item) {
        _itemSelected.value = item
    }

    // Sort the item list based on criteria
    fun sortData(type: TypeSort) {
        val sortedList = _itemList.value?.let { currentList ->
            when (type) {
                TypeSort.INDEX -> currentList.sortedByDescending { it.index }
                TypeSort.TITLE -> currentList.sortedByDescending { it.title }
                TypeSort.DATE -> currentList.sortedByDescending { it.date }
            }
        } ?: emptyList()

        // Update the sorted list
        _itemList.value = sortedList
    }
}