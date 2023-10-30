package com.yunushan.letgocasestudy.data.model.request

import com.google.gson.annotations.SerializedName

data class VehiclePricingBody(
    @SerializedName("trim_id")
    val trimId: String,
    val model: String,
    val kilometerage: String,
    @SerializedName("color_id")
    val colorId: String,
    val expertise: String? = " {\"ceiling\":\"original\",\"front_hood\":\"modified\",\"rear_hood\":\"original\",\"front_left_mudguard\":\"original\",\"front_right_mudguard\":\"original\",\"rear_left_mudguard\":\"original\",\"front_left_door\":\"original\",\"front_right_door\":\"painted\",\"rear_left_door\":\"original\",\"rear_right_door\":\"original\",\"rear_right_mudguard\":\"original\"}"
)