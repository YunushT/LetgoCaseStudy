package com.yunushan.letgocasestudy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunushan.letgocasestudy.R
import com.yunushan.letgocasestudy.data.model.request.VehiclePricingBody
import com.yunushan.letgocasestudy.data.model.uimodel.QuestionnareListItem
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleBrandsListUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleColorsUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleInfo
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleItemDetailTrimsUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehiclePricingUIModel
import com.yunushan.letgocasestudy.data.model.uimodel.VehicleSeriesItemUIModel
import com.yunushan.letgocasestudy.domain.usecase.GetInfoByModelMakeIdAndTrimUseCase
import com.yunushan.letgocasestudy.domain.usecase.GetVehicleBrandsUseCase
import com.yunushan.letgocasestudy.domain.usecase.GetVehicleColorsUseCase
import com.yunushan.letgocasestudy.domain.usecase.GetVehicleModelYearsUseCase
import com.yunushan.letgocasestudy.domain.usecase.GetVehicleModelsUseCase
import com.yunushan.letgocasestudy.domain.usecase.GetVehiclePriceUseCase
import com.yunushan.letgocasestudy.presentation.event.PageTypes
import com.yunushan.letgocasestudy.presentation.event.PriceResultEvent
import com.yunushan.letgocasestudy.presentation.event.QuestionnareDetailsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVehicleModelYearsUseCase: GetVehicleModelYearsUseCase,
    private val getVehicleBrandsUseCase: GetVehicleBrandsUseCase,
    private val getVehicleModelsUseCase: GetVehicleModelsUseCase,
    private val getInfoByModelMakeIdAndTrimUseCase: GetInfoByModelMakeIdAndTrimUseCase,
    private val getVehicleColorsUseCase: GetVehicleColorsUseCase,
    private val getVehiclePriceUseCase: GetVehiclePriceUseCase
) : ViewModel() {

    private val _questionnareDetailFlow =
        MutableStateFlow<QuestionnareDetailsEvent>(QuestionnareDetailsEvent.Idle)
    val questionnareDetailFlow: StateFlow<QuestionnareDetailsEvent> =
        _questionnareDetailFlow.asStateFlow()

    private val _priceResultDetailFlow =
        MutableStateFlow<PriceResultEvent>(PriceResultEvent.Idle)
    val priceResultDetailFlow: StateFlow<PriceResultEvent> =
        _priceResultDetailFlow.asStateFlow()
    private lateinit var nextPageState: PageTypes
    private var vehicleInfo = VehicleInfo()
    private var vehicleBrandsListUIModel: List<VehicleBrandsListUIModel> = emptyList()
    private var vehicleModelsListUIModel: List<VehicleSeriesItemUIModel> = emptyList()
    private var vehicleItemDetailTrimsUIModel: List<VehicleItemDetailTrimsUIModel> = emptyList()
    private var vehicleColorsDetailColorsUIModel: List<VehicleColorsUIModel> = emptyList()
    private var vehiclePriceUIModel: VehiclePricingUIModel? = null

    init {
        getVehicleModelYears()
    }

    private fun getVehicleModelYears() {
        viewModelScope.launch() {
            getVehicleModelYearsUseCase.getVehicleModelYears(
                onSuccess = { itemList ->
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                        QuestionnareListItem(
                            R.string.model_year_title,
                            itemList
                        )
                    )
                    nextPageState = PageTypes.MAKE
                },
                onFailure = {
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.Error(
                        it.error
                    )
                })
        }
    }

    fun getNextPage(selectedText: String) {
        when (nextPageState) {
            PageTypes.MAKE -> getVehicleMake(selectedText)
            PageTypes.MODEL -> getVehicleModels(selectedText)
            PageTypes.BODY_TYPE -> getBodyTypes(selectedText)
            PageTypes.ENGINE_TYPE -> getEngineTypes(selectedText)
            PageTypes.TRANSMISSION_TYPE -> getTransmissionTypes(selectedText)
            PageTypes.TRIM -> getVehicleTrims(selectedText)
            PageTypes.COLOR -> getVehicleColors(selectedText)
            PageTypes.KM -> navigateMileageFragment(selectedText)
            else -> {}
        }
    }

    private fun navigateMileageFragment(selectedText: String) {
        vehicleInfo =
            vehicleInfo.copy(
                vehicleColor = vehicleColorsDetailColorsUIModel
                    .find { item -> item.name == selectedText }?.id.toString(),
                vehicleColorName = selectedText
            )
        _questionnareDetailFlow.value = QuestionnareDetailsEvent.NavigateMileageFragment
    }

    private fun getVehicleColors(selectedText: String) {
        viewModelScope.launch() {
            vehicleInfo =
                vehicleInfo.copy(
                    vehicleTrimType = vehicleItemDetailTrimsUIModel
                        .find { item -> item.name == selectedText }?.id.toString(),
                    vehicleTrimName = selectedText
                )
            getVehicleColorsUseCase.getVehicleColors(
                onSuccess = { itemList ->
                    vehicleColorsDetailColorsUIModel = itemList
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                        QuestionnareListItem(
                            R.string.color_title,
                            itemList.map { it.name.orEmpty() }
                        )
                    )
                    nextPageState = PageTypes.KM
                }, onFailure = {
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.Error(
                        it.error
                    )
                }
            )

            nextPageState = PageTypes.COLOR
        }
    }

    private fun getVehicleTrims(selectedText: String) {
        viewModelScope.launch {
            vehicleInfo =
                vehicleInfo.copy(
                    vehicleTransmissionType = vehicleItemDetailTrimsUIModel
                        .find { item -> item.transmission?.name == selectedText }?.transmission?.id.toString(),
                    vehicleGearTypeName = selectedText
                )

            _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                QuestionnareListItem(
                    R.string.trim_type_title,
                    vehicleItemDetailTrimsUIModel.map { it.name.orEmpty() }.distinct()
                )
            )
            nextPageState = PageTypes.COLOR
        }
    }

    private fun getTransmissionTypes(selectedText: String) {
        viewModelScope.launch {
            vehicleInfo =
                vehicleInfo.copy(
                    vehicleEngineType = vehicleItemDetailTrimsUIModel
                        .find { item -> item.engine?.name == selectedText }?.engine?.id.toString(),
                    vehicleEngineName = selectedText
                )

            _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                QuestionnareListItem(
                    R.string.transmission_type_title,
                    vehicleItemDetailTrimsUIModel.map { it.transmission?.name.orEmpty() }.distinct()
                )
            )
            nextPageState = PageTypes.TRIM
        }
    }

    private fun getEngineTypes(selectedText: String) {
        viewModelScope.launch {
            vehicleInfo =
                vehicleInfo.copy(
                    vehicleBodyType = vehicleItemDetailTrimsUIModel
                        .find { item -> item.bodyConfig?.name == selectedText }?.bodyConfig?.id.toString()
                )

            _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                QuestionnareListItem(
                    R.string.engine_type_title,
                    vehicleItemDetailTrimsUIModel.map { it.engine?.name.orEmpty() }.distinct()
                )
            )
            nextPageState = PageTypes.TRANSMISSION_TYPE
        }
    }

    private fun getVehicleModels(selectedText: String) {
        viewModelScope.launch() {
            vehicleInfo =
                vehicleInfo.copy(
                    vehicleBrandId = vehicleBrandsListUIModel
                        .find { item -> item.name == selectedText }?.id.toString()
                )
            getVehicleModelsUseCase.getVehicleModels(
                vehicleModel = vehicleInfo.vehicleModelYear.orEmpty(),
                vehicleBrandId = vehicleInfo.vehicleBrandId.orEmpty(),
                onSuccess = { itemList ->
                    vehicleModelsListUIModel = itemList
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                        QuestionnareListItem(
                            R.string.model_title,
                            itemList.map { it.name.orEmpty() }
                        )
                    )
                    nextPageState = PageTypes.BODY_TYPE
                },
                onFailure = {
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.Error(
                        it.error
                    )
                }
            )
        }
    }

    private fun getBodyTypes(selectedText: String) {
        vehicleInfo =
            vehicleInfo.copy(
                vehicleModelId = vehicleModelsListUIModel
                    .find { item -> item.name == selectedText }?.id.toString()
            )
        viewModelScope.launch() {
            getInfoByModelMakeIdAndTrimUseCase.getVehicleTrimsInfo(
                vehicleModelYear = vehicleInfo.vehicleModelYear.orEmpty(),
                vehicleBrandId = vehicleInfo.vehicleBrandId.orEmpty(),
                vehicleModelId = vehicleInfo.vehicleModelId.orEmpty(),
                onSuccess = { itemList ->
                    vehicleItemDetailTrimsUIModel = itemList
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                        QuestionnareListItem(
                            R.string.body_type_title,
                            itemList.map { it.bodyConfig?.name.orEmpty() }.distinct()
                        )
                    )
                    nextPageState = PageTypes.ENGINE_TYPE
                },
                onFailure = {
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.Error(
                        it.error
                    )
                }
            )
        }
    }

    private fun getVehicleMake(selectedText: String) {
        vehicleInfo = vehicleInfo.copy(vehicleModelYear = selectedText)
        viewModelScope.launch() {
            getVehicleBrandsUseCase.getVehicleBrands(
                vehicleModel = selectedText,
                onSuccess = { itemList ->
                    vehicleBrandsListUIModel = itemList
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.FragmentListLoaded(
                        QuestionnareListItem(
                            R.string.model_make_title,
                            itemList.map { it.name.orEmpty() }
                        )
                    )
                    nextPageState = PageTypes.MODEL
                },
                onFailure = {
                    _questionnareDetailFlow.value = QuestionnareDetailsEvent.Error(
                        it.error
                    )
                }
            )
        }
    }

    fun getVehiclePrice(vehicleMileage: String) {
        vehicleInfo = vehicleInfo.copy(vehicleMileage = vehicleMileage)
        viewModelScope.launch() {
            getVehiclePriceUseCase.getVehiclePrice(
                VehiclePricingBody(
                    vehicleInfo.vehicleTrimType.orEmpty(),
                    vehicleInfo.vehicleModelId.orEmpty(),
                    vehicleMileage,
                    vehicleInfo.vehicleColor.orEmpty(),
                ),
                onSuccess = { priceUIModel ->
                    vehiclePriceUIModel = priceUIModel
                    vehiclePriceUIModel?.let {
                        _priceResultDetailFlow.value = PriceResultEvent.PricingResult(
                            it.copy(pageTitle = R.string.pricing_result),
                            vehicleInfo
                        )
                        nextPageState = PageTypes.RESULT
                    }
                },
                onFailure = {
                    _priceResultDetailFlow.value = PriceResultEvent.Error(
                        it.error
                    )
                }
            )
        }
    }
}