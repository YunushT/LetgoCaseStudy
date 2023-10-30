package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.model.response.VehicleBrandsListResponse
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleBrandsListUIModel
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

class GetVehicleBrandsUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehicleBrands(
        vehicleModel: String,
        onSuccess: (List<VehicleBrandsListUIModel>) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getVehiclesBrandList(vehicleModel)
                .enqueue(object : Callback<List<VehicleBrandsListResponse>> {
                    override fun onFailure(
                        call: Call<List<VehicleBrandsListResponse>>,
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
                        call: Call<List<VehicleBrandsListResponse>>,
                        response: Response<List<VehicleBrandsListResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Resource.Success(response.body()).data?.let { onSuccess.invoke(it.map { item -> VehicleBrandsListUIModel(item.id,item.name,item.code) }) }

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