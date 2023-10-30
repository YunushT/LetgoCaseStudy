package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.repository.Repository
import com.yunushan.letgocasestudy.domain.util.NetworkError
import com.yunushan.letgocasestudy.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GetVehicleModelYearsUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehicleModelYears(
        onSuccess: (List<String>) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getVehicleModels().enqueue(object : Callback<List<Int>> {
                override fun onResponse(call: Call<List<Int>>, response: Response<List<Int>>) {
                    if (response.isSuccessful) {
                        Resource.Success(response.body()).data?.let { onSuccess.invoke(it.map { item -> item.toString() }) }

                    } else {
                        onFailure.invoke(
                            Resource.Failure(
                                NetworkError(
                                    response.code(),
                                    response.message()
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<List<Int>>, t: Throwable) {
                    onFailure.invoke(Resource.Failure(NetworkError(400, "Unknown Network Error")))
                }
            })
        }
    }
}