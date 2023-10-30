package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.model.response.VehicleColorsResponse
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleColorsUIModel
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

class GetVehicleColorsUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehicleColors(
        onSuccess: (List<VehicleColorsUIModel>) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getVehicleColors()
                .enqueue(object : Callback<List<VehicleColorsResponse>> {
                    override fun onFailure(
                        call: Call<List<VehicleColorsResponse>>,
                        t: Throwable
                    ) {
                        onFailure.invoke(
                            Resource.Failure(
                                NetworkError(
                                    400,
                                    "Unknown Network Error"
                                )
                            )
                        )
                    }

                    override fun onResponse(
                        call: Call<List<VehicleColorsResponse>>,
                        response: Response<List<VehicleColorsResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Resource.Success(response.body()).data?.let {
                                onSuccess.invoke(it.map { item ->
                                    VehicleColorsUIModel(
                                        item.id,
                                        item.name,
                                        item.colorCode
                                    )
                                })
                            }

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
                })
        }
    }
}