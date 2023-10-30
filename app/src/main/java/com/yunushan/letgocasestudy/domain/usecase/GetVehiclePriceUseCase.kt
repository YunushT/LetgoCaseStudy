package com.yunushan.letgocasestudy.domain.usecase

import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.response.VehiclePricingResponse
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleEngineUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehiclePricingUIModel
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


class GetVehiclePriceUseCase @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getVehiclePrice(
        pricingBody: VehiclePricingBody,
        onSuccess: (VehiclePricingUIModel) -> Unit,
        onFailure: (Resource.Failure) -> Unit
    ) {
        return withContext(dispatcher) {
            repository.getPricingList(pricingBody)
                .enqueue(object : Callback<VehiclePricingResponse> {
                    override fun onFailure(
                        call: Call<VehiclePricingResponse>,
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
                        call: Call<VehiclePricingResponse>,
                        response: Response<VehiclePricingResponse>
                    ) {
                        if (response.isSuccessful) {
                            Resource.Success(response.body()).data?.let { vehiclePricingResponse ->
                                onSuccess.invoke(
                                    VehiclePricingUIModel(
                                        id = vehiclePricingResponse.id,
                                        code = vehiclePricingResponse.code,
                                        price = vehiclePricingResponse.price,
                                        transmission = VehicleTransmissionUIModel(
                                            vehiclePricingResponse.transmission?.id,
                                            vehiclePricingResponse.transmission?.id.toString()
                                        ),
                                        engine = VehicleEngineUIModel(
                                            vehiclePricingResponse.engine?.id,
                                            vehiclePricingResponse.engine?.name
                                        ),
                                        randomId = vehiclePricingResponse.randomId
                                    )
                                )
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