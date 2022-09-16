package com.msa.petsearch.shared.coreentity

import com.msa.petsearch.shared.coreentity.petinfo.enum.*

data class PetSearchParams(
    val breed: List<String>? = null,
    val size: List<PetSize>? = null,
    val gender: List<PetGender>? = null,
    val age: List<PetAge>? = null,
    val color: List<String>? = null,
    val coat: List<PetCoat>? = null,
    val status: List<PetStatus>? = null,
    val goodWithChildren: Boolean? = null,
    val goodWithDogs: Boolean? = null,
    val goodWithCats: Boolean? = null,
    val houseTrained: Boolean? = null,
    val declawed: Boolean? = null,
    val specialNeeds: Boolean? = null,
    val sort: String? = null
)
