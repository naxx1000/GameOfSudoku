package com.rakiwow.gameofsudoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rakiwow.gameofsudoku.viewmodel.MainSharedViewModel
import com.rakiwow.koalacolorpicker.ColorToHarmonyColors
import com.rakiwow.koalacolorpicker.KoalaColorPicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_drawer.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    val colorToHarmonyColors = ColorToHarmonyColors()
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawer: DrawerLayout
    private var cursorPos: Int? = null
    private lateinit var sharedViewModel: MainSharedViewModel
    private lateinit var colorThemeArray: IntArray

    companion object {
        private var mIsDarkMode: Boolean? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO save the colorThemeArray into sharedpreferences

        setSupportActionBar(main_toolbar)
        drawer = findViewById(R.id.main_activity_drawer_layout)
        toggle = ActionBarDrawerToggle(
            this, drawer, main_toolbar, R.string.drawer_open,
            R.string.drawer_close
        )
        toggle.syncState()

        drawer_button_continue.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            }
        }

        sharedViewModel = run{
            ViewModelProvider(this)[MainSharedViewModel::class.java]
        }

        val navController = nav_host_fragment.findNavController()

        //Instead of onClick which causes stuttering. Do these onDrawerClosed
        val args = Bundle()
        drawer_diff_1.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 0)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_2.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 1)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_3.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 2)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_4.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 3)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_diff_5.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            args.putInt("difficulty", 4)
            navController.navigate(R.id.newGameFragment, args)
        }
        drawer_button_history.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            navController.popBackStack()
            navController.navigate(R.id.toHistoryFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId

        if (itemId == R.id.theme_button) {
            val colorPicker = KoalaColorPicker()
                .setTitle("Select a theme color")
                .setHasDarkModeOption(true)
                .setAcceptButtonColor(resources.getColor(R.color.colorAccent))
                .setDialogBackgroundColor(resources.getColor(R.color.colorPrimary))
                .setTextColor(resources.getColor(R.color.colorPrimaryDark))
                .setSwitchDefaultValue(mIsDarkMode)
                .setCursorPosition(cursorPos)
                .setDarkModeIntensity(180)
                .setLightModeIntensity(195)

            colorPicker.setOnSwitchClickListener(object : KoalaColorPicker.OnSwitchClickListener {
                override fun onSwitchClick(isDarkMode: Boolean?) {
                    mIsDarkMode = isDarkMode
                }
            })

            colorPicker.setOnAcceptColorListener(object : KoalaColorPicker.OnAcceptColorListener {
                override fun onAcceptColor(colorInt: Int, cursorPosition: Int?) {

                    cursorPos = cursorPosition
                    colorThemeArray = colorToHarmonyColors.complementary(colorInt, mIsDarkMode)
                    sharedViewModel.setColorsArray(colorThemeArray)

                    paintMainActivityViews()
                }
            })
            colorPicker.show(supportFragmentManager, "ColorPickerDialogFragment")
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun paintMainActivityViews(){
        val unwrappedDrawable = AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_color_lens_white_24dp)
        if(unwrappedDrawable != null){
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, colorThemeArray[4])
            invalidateOptionsMenu()
        }
        main_toolbar.setTitleTextColor(colorThemeArray[4])
        toggle.drawerArrowDrawable.color = colorThemeArray[4]
        motion_drawer_menu.setBackgroundColor(colorThemeArray[0])
        main_toolbar.setBackgroundColor(colorThemeArray[2])
        drawer_button_continue.setBackgroundColor(colorThemeArray[4])
        drawer_button_newgame.setBackgroundColor(colorThemeArray[4])
        drawer_button_records.setBackgroundColor(colorThemeArray[4])
        drawer_button_history.setBackgroundColor(colorThemeArray[4])
        drawer_button_about.setBackgroundColor(colorThemeArray[4])
        drawer_diff_1.setBackgroundColor(colorThemeArray[4])
        drawer_diff_2.setBackgroundColor(colorThemeArray[4])
        drawer_diff_3.setBackgroundColor(colorThemeArray[4])
        drawer_diff_4.setBackgroundColor(colorThemeArray[4])
        drawer_diff_5.setBackgroundColor(colorThemeArray[4])
    }
}