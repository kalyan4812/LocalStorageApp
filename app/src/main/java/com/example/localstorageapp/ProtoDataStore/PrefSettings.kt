package com.example.localstorageapp.ProtoDataStore

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@kotlinx.serialization.Serializable
data class PrefSettings(
    val language: LANGUAGE = LANGUAGE.ENGLISH,
    val lng:String="English",
    val list: List<UserName> = persistentListOf()
)

@kotlinx.serialization.Serializable
data class UserName(val firstName: String, val lastName: String)

enum class LANGUAGE {
    ENGLISH, TELUGU, HINDI, KANNADA, TAMIL
}