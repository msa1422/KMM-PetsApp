package com.petsapp.petfinder.shared.data_infrastructure_cache.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

internal class PetInfoCacheEntity(
    @PrimaryKey
    var id: Long? = null,
    var name: String? = null,
    var type: String? = null,
    var description: String? = null,
    var shortDescription: String? = null

    // TODO: Add Other fields and respective Entities and Mappers

) : RealmObject {
    @Suppress("UNUSED")
    constructor() : this(null)
}