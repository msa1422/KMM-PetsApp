package com.msa.petsearch.shared.domain.homeuicontract.interactor

internal data class UseCaseWrapper(
    val getPetTypes: LoadPetTypesUseCase,
    val getPets: LoadPetsUseCase
)
