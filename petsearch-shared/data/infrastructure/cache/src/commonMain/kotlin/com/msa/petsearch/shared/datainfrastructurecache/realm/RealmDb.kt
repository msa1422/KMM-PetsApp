package com.msa.petsearch.shared.datainfrastructurecache.realm

import com.msa.petsearch.shared.datainfrastructurecache.entity.PetInfoCacheEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

private const val REALM_DB_NAME = "THE_REALM_OF_PETS"

internal val RealmDb: Realm
    get() {
        return Realm.open(
            RealmConfiguration
                .Builder(setOf(PetInfoCacheEntity::class))
                .name(REALM_DB_NAME)
                .schemaVersion(0)
                .build()
        )
    }
