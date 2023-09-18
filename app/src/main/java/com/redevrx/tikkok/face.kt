package com.redevrx.tikkok

import retrofit2.http.GET

interface Face {
   @GET("/posts")
   fun new()
}