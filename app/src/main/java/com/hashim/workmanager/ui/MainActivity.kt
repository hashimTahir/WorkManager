package com.hashim.workmanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hashim.workmanager.R
import com.hashim.workmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var hActivityMainBinding: ActivityMainBinding
    private lateinit var hNavHostFragments: NavHostFragment
    private lateinit var hNavController: NavController
    val hMainViewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(hActivityMainBinding.root)
        setSupportActionBar(hActivityMainBinding.toolbar)

        hInitNavView()

    }

    private fun hInitNavView() {


        hNavHostFragments = supportFragmentManager
            .findFragmentById(R.id.hMainFragmentContainer)
                as NavHostFragment

        hNavController = hNavHostFragments.navController

        hNavController.setGraph(R.navigation.nav_graph)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}