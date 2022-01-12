package com.venezuela.venetasa.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.venezuela.venetasa.model.Rate

@Dao
interface RateDao {

    @Query("SELECT * FROM rates")
    fun queryRates(): LiveData<List<Rate>>

    @Insert
    fun insertAllRates(rates: List<Rate>)

    @Query("DELETE FROM rates")
    fun deleteAllRates();
}