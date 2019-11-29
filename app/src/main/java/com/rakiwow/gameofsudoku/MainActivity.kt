package com.rakiwow.gameofsudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_drawer.*
import kotlinx.android.synthetic.main.number_picker_layout.*


class MainActivity : AppCompatActivity() {

    lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.main_activity_drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open,
                    R.string.drawer_close)
        toggle.syncState()

        drawer_button_continue.setOnClickListener {
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START)
            }
        }

        val navController = nav_host_fragment.findNavController()

        val args = Bundle()
        drawer_diff_1.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 0)
            navController.navigate(R.id.newGameFragment, args) }
        drawer_diff_2.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 1)
            navController.navigate(R.id.newGameFragment, args) }
        drawer_diff_3.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 2)
            navController.navigate(R.id.newGameFragment, args) }
        drawer_diff_4.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 3)
            navController.navigate(R.id.newGameFragment, args) }
        drawer_diff_5.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 4)
            navController.navigate(R.id.newGameFragment, args)}
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

}