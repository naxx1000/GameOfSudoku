package com.rakiwow.gameofsudoku.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BugFormsApi {

    @GET("formResponse")
    fun fetchContent(
        @Query(value = "usp") usp: String,
        @Query(value = "entry.724685163") report: String,
        @Query(value = "submit") submit: String
    ): Call<String>

}