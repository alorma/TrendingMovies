package com.alorma.domain.common

interface ResourceProvider {
    fun getString(stringId: Int, vararg arg: Any): String
}