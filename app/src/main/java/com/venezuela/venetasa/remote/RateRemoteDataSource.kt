package com.venezuela.venetasa.remote

import com.venezuela.venetasa.utils.RemoteDataSource
import com.venezuela.venetasa.utils.Resource
import org.jsoup.Connection
import org.jsoup.nodes.Document
import javax.inject.Inject

class RateRemoteDataSource @Inject constructor(private val ratesConnection: Connection): RemoteDataSource(){

    suspend fun requestRates(): Resource<Document> = getResult { ratesConnection.get() }

}