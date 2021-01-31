package ru.evbobrutskov.tinkoffandroiddeveloper2021.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://developerslife.ru/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface DevelopersLifeApiService {
    @GET("{category}/{page}?json=true")
    fun getPropertiesAsync(@Path("category") category: String, @Path("page") page: Int):
            Deferred<DevelopersLifePropertyContainer>
}

object DevelopersLifeApi {
    val retrofitService: DevelopersLifeApiService by lazy {
        retrofit.create(DevelopersLifeApiService::class.java)
    }
}