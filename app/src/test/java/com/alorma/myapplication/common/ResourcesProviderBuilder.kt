package com.alorma.myapplication.common

import com.alorma.presentation.common.ResourcesProvider
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.mockito.ArgumentMatchers

fun getResourcesProvider(): ResourcesProvider = mock<ResourcesProvider>().apply {
    given(getString(ArgumentMatchers.anyInt())).willReturn("")
}