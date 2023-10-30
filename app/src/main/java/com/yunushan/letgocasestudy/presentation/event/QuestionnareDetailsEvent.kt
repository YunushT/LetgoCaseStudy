package com.yunushan.letgocasestudy.presentation.event

import com.yunushan.letgocasestudy.data.model.uimodel.QuestionnareListItem
import com.yunushan.letgocasestudy.data.model.uimodel.VehiclePricingUIModel
import com.yunushan.letgocasestudy.domain.util.NetworkError

sealed class QuestionnareDetailsEvent {
    object Idle : QuestionnareDetailsEvent()
    data class FragmentListLoaded(val itemDetail: QuestionnareListItem) : QuestionnareDetailsEvent()
    object NavigateMileageFragment : QuestionnareDetailsEvent()
    data class PricingResult(val priceUIDetail: VehiclePricingUIModel): QuestionnareDetailsEvent()
    data class Error(val error: NetworkError) : QuestionnareDetailsEvent()
}
