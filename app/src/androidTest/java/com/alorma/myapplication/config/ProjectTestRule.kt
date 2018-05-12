package com.alorma.myapplication.config


import android.app.Activity
import android.content.Intent
import com.schibsted.spain.barista.rule.BaristaRule
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import com.schibsted.spain.barista.rule.cleardata.ClearFilesRule
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import com.schibsted.spain.barista.rule.flaky.FlakyTestRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ProjectTestRule<T : Activity>(activityClass: Class<T>, private val target: Any) : TestRule {

    private val daggerRule = getDaggerRule()
    private val clearPreferencesRule = ClearPreferencesRule()
    private val clearDatabaseRule = ClearDatabaseRule()
    private val clearFilesRule = ClearFilesRule()
    private val flakyTestRule = FlakyTestRule()
    private val baristaRule = BaristaRule.create(activityClass)

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(flakyTestRule)
                // ↓ All rules below flakyTestRule will be repeated
                .around(baristaRule)
                // ↓ All rules below baristaRule will execute before launching the activity
                .around { newBase, _ -> daggerRule.apply(newBase, null, target) }
                .around(clearPreferencesRule)
                .around(clearDatabaseRule)
                .around(clearFilesRule)
                .apply(base, description)
    }

    fun run() = baristaRule.launchActivity()
    fun run(intent: Intent) = baristaRule.launchActivity(intent)
}