package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.model.response.VehicleItemDetailTrimsResponse
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleBodyConfigUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleEngineUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleItemDetailTrimsUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleTransmissionUIModel
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


class GetInfoByModelMakeIdAndTrimUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehicleTrimsInfo(
        vehicleModelYear: String,
        vehicleBrandId: String,
        vehicleModelId: String,
        onSuccess: (List<VehicleItemDetailTrimsUIModel>) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getVehicleByModelMakesAndTrim(
                vehicleModelYear,
                vehicleBrandId,
                vehicleModelId
            )
                .enqueue(object : Callback<List<VehicleItemDetailTrimsResponse>> {

                    override fun onFailure(
                        call: Call<List<VehicleItemDetailTrimsResponse>>,
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
                        call: Call<List<VehicleItemDetailTrimsResponse>>,
                        response: Response<List<VehicleItemDetailTrimsResponse>>
                    ) {
                        if (response.isSuccessful) {
                            Resource.Success(response.body()).data?.let {
                                onSuccess.invoke(it.map { item ->
                                    VehicleItemDetailTrimsUIModel(
                                        id = item.id,
                                        name = item.name,
                                        code = item.code,
                                        displacement = item.displacement,
                                        bodyConfig = VehicleBodyConfigUIModel(
                                            item.bodyConfig?.id,
                                            item.bodyConfig?.name
                                        ),
                                        transmission = VehicleTransmissionUIModel(
                                            item.transmission?.id,
                                            item.transmission?.name
                                        ),
                                        engine = VehicleEngineUIModel(
                                            item.engine?.id,
                                            item.engine?.name
                                        )
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