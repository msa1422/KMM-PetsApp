package com.petsapp.petfinder.shared.datainfrastructurenetwork.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TokenInfoDTO(
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("access_token") val accessToken: String
)