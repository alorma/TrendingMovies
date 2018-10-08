package com.alorma.myapplication.common

import com.alorma.domain.common.ResourceProvider
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.mockito.ArgumentMatchers

fun getResourcesProvider(): ResourceProvider = mock<ResourceProvider>().apply {
    given(getString(ArgumentMatchers.anyInt())).willReturn("")
}