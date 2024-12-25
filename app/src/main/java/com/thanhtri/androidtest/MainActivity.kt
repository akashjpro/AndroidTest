package com.thanhtri.androidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.gson.Gson
import com.thanhtri.androidtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Binding for the activity's layout
    private lateinit var binding: ActivityMainBinding

    // Configuration for the app bar and navigation
    private lateinit var appBarConfiguration: AppBarConfiguration

    // ViewModel scoped to this Activity
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        loadSampleData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**
     * Loads sample data from a JSON file in the assets folder and updates the ViewModel's item list.
     *
     * - Reads the JSON file "sample_data_list.json" from the assets directory.
     * - Parses the JSON data into an array of `Item` objects using Gson.
     * - Converts the array to a list and sets it in the ViewModel.
     */
    private fun loadSampleData() {
        val json = assets.open("sample_data_list.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val items = gson.fromJson(json, Array<Item>::class.java)

        viewModel.setItemList(items.toList())
    }
}