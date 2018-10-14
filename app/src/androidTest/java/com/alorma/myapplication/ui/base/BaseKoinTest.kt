package com.alorma.myapplication.ui.base

import org.junit.Before
import org.koin.dsl.context.ModuleDefinition
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

abstract class BaseKoinTest {

    @Before
    fun setup() {
        val mockModule = module {
            mockDependencies()
        }
        loadKoinModules(mockModule)
        configureMocks()
    }

    protected abstract fun ModuleDefinition.mockDependencies()

    protected open fun configureMocks() {

    }
}
