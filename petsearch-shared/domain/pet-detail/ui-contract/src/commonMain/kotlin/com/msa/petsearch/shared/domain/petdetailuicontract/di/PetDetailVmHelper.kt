package com.msa.petsearch.shared.domain.petdetailuicontract.di

import com.msa.petsearch.shared.domain.petdetailuicontract.PetDetailViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PetDetailVmHelper : KoinComponent {
    private val vm by inject<PetDetailViewModel>()
    fun provide() = vm
}
