package com.chillin.type

import com.fasterxml.jackson.annotation.JsonValue

enum class StorageType(@get:JsonValue val value: String) {
    EXTERNAL("external"),
    AZURE("azure"),
    DROPBOX("dropbox"),
    ADOBE("adobe");
}
