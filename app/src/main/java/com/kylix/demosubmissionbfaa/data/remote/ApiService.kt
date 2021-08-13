package com.kylix.demosubmissionbfaa.data.remote

import com.kylix.demosubmissionbfaa.model.SearchResponse
import com.kylix.demosubmissionbfaa.model.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //endpoint: BASE_URL + Value GET (https://api.github.com/search/users)
    @GET("search/users")
    suspend fun searchUsers (
        @Query("q")
        query: String
    ): SearchResponse

    //endpoint: BASE_URL + Value GET (https://api.github.com/users/{username})
    @GET("users/{username}")
    suspend fun getDetailUser (
        @Path("username")
        username: String
    ): User

    @GET("users/{username}/followers")
    suspend fun getUserFollowers (
        @Path("username")
        username: String
    ): List<User>

    @GET("users/{username}/following")
    suspend fun getUserFollowing (
        @Path("username")
        username: String
    ): List<User>
}