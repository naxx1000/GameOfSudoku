package com.rakiwow.gameofsudoku.views

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.api.BugFormsApi
import com.rakiwow.gameofsudoku.viewmodel.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.toast_layout.*
import kotlinx.android.synthetic.main.toast_layout.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception

private const val TAG = "AboutFragment"
private const val FRAGMENT_ID = 4

class AboutFragment : Fragment() {

    private lateinit var sharedViewModel: MainSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = activity?.run{
            ViewModelProvider(this).get(MainSharedViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        sharedViewModel.currentFragment = FRAGMENT_ID

        submit_bug_button.setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLSci1OcaM2mrXHP-H60Q6I2wVZsvMPeRyAUxAUtQZBf4k4oUMg/"
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            val bugFormsApi = retrofit.create(BugFormsApi::class.java)
            val bugFormsCall = bugFormsApi.fetchContent("pp_url", bug_edit_text.text.toString(), "Submit")

            bugFormsCall.enqueue(object : retrofit2.Callback<String> {

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e(TAG, "Failure to submit bug report.")
                    showCustomToast(R.string.form_submit_failed)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d(TAG, "Form submitted")
                    showCustomToast(R.string.form_submit_success)
                }
            })
        }
    }

    fun showCustomToast(textId: Int) {
        val layout = layoutInflater.inflate(R.layout.toast_layout, toast_custom_view)
        layout.toast_text_view.apply {
            setText(textId)
            setTextColor(Color.WHITE)
            textSize = 16f
        }

        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }
}