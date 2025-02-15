package com.ej.riversideassignment.repositories

import android.util.Log
import com.ej.riversideassignment.api.RetrofitService
import com.ej.riversideassignment.db.TitleDetailsDao
import com.ej.riversideassignment.model.TitleDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

interface TitleRepository {
    fun searchByTitle(title: String): Flow<List<TitleDetails>>
    fun getTitleDetailsById(titleId: String): Flow<TitleDetails?>
    suspend fun updateFavouriteTitle(titleId: String, isFavourite: Boolean)
}

class TitleRepositoryImpl @Inject constructor(
    private val retrofitService: RetrofitService,
    private val titleDetailsDao: TitleDetailsDao,
) : TitleRepository {

    private var currentJob: Job? = null

    override fun searchByTitle(title: String): Flow<List<TitleDetails>> = channelFlow {
        currentJob?.cancel()
        currentJob = launch {
            val localFlow = titleDetailsDao.getTitlesByName(title)
            var fetchedFromRemote = false
            localFlow.collectLatest { localData ->
                if (localData.isEmpty() && !fetchedFromRemote) {
                    fetchedFromRemote = true
                    try {
                        val response = retrofitService.searchByTitle(title)
                        val titles = response.list ?: emptyList()

                        if (titles.isNotEmpty()) {
                            titleDetailsDao.insertAll(*titles.toTypedArray())
                        } else {
                            send(titles)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching titles: ${e.message}")
                        throw e
                    }
                } else {
                    send(localData)
                }
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun getTitleDetailsById(titleId: String): Flow<TitleDetails?> = channelFlow {
        val localFlow = titleDetailsDao.getTitleById(titleId)
        localFlow.collectLatest { localData ->
            if (localData != null && localData.plot.isNullOrEmpty()) {
                try {
                    val response = retrofitService.getTitleDetailsById(titleId)
                    val finalResponse = response.copy(isFavourite = localData.isFavourite)
                    titleDetailsDao.insertAll(finalResponse)

                    send(finalResponse)
                } catch (e: Exception) {
                    Log.d(TAG, "Error getting title details from server")
                    send(localData)
                }
            } else {
                send(localData)
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateFavouriteTitle(titleId: String, isFavourite: Boolean) {
        titleDetailsDao.updateFavourite(titleId, isFavourite)
    }


    companion object {
        const val TAG = "TitleRepositoryTAG"
    }
}