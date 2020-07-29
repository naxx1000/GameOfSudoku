package com.rakiwow.gameofsudoku

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rakiwow.gameofsudoku.utils.MainDrawerContent
import com.rakiwow.gameofsudoku.viewmodel.MainSharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_drawer.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawer: DrawerLayout
    private lateinit var sharedViewModel: MainSharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawer = findViewById(R.id.main_activity_drawer_layout)

        val sharedPref = getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        if (sharedPref.getBoolean("user_first_time", true)) {
            drawer.openDrawer(GravityCompat.START)
            sharedPref.edit().putBoolean("user_first_time", false).apply()
        }

        setSupportActionBar(main_toolbar)
        toggle = ActionBarDrawerToggle(
            this, drawer, main_toolbar, R.string.drawer_open,
            R.string.drawer_close
        )
        toggle.syncState()

        sharedViewModel = run{
            ViewModelProvider(this)[MainSharedViewModel::class.java]
        }

        val navController = nav_host_fragment.findNavController()

        //TODO Bug reporting button
        //Instead of onClick which causes stuttering. Do these onDrawerClosed
        val args = Bundle()
        drawer_button_continue.setOnClickListener {
            if(sharedViewModel.currentFragment == 1){
                drawer.closeDrawer(GravityCompat.START)
            }else{
                drawer.closeDrawer(GravityCompat.START)
                navController.popBackStack()
                args.putBoolean("isGameContinued", true)
                navController.navigate(R.id.newGameFragment, args)
            }
        }
        drawer_diff_1.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putBoolean("isGameContinued", false)
            args.putInt("difficulty", 0)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_2.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putBoolean("isGameContinued", false)
            args.putInt("difficulty", 1)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_3.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putBoolean("isGameContinued", false)
            args.putInt("difficulty", 2)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_4.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putBoolean("isGameContinued", false)
            args.putInt("difficulty", 3)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_5.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putBoolean("isGameContinued", false)
            args.putInt("difficulty", 4)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_button_records.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            navController.navigate(R.id.toRecordsFragment)
        }
        drawer_button_history.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            navController.navigate(R.id.toHistoryFragment)
        }
        drawer_button_about.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            navController.navigate(R.id.toAboutFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}