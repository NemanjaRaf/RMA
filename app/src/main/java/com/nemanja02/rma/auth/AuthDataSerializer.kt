package com.nemanja02.rma.auth

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


class AuthDataSerializer : Serializer<UserStore> {

    override val defaultValue: UserStore = UserStore("", "", "", "")

    override suspend fun readFrom(input: InputStream): UserStore {
        return withContext(Dispatchers.IO) {
            try {
                val json = input.readBytes().decodeToString()
                Json.decodeFromString<UserStore>(json)
            } catch (e: Exception) {
                e.printStackTrace()
                defaultValue
            }
        }
    }

    override suspend fun writeTo(t: UserStore, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }
    }
}