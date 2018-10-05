package com.alorma.myapplication.config


import android.app.Activity
import android.content.Intent
import com.schibsted.spain.barista.rule.BaristaRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ProjectTestRule<T : Activity>(activityClass: Class<T>, private val target: Any) : TestRule {

    private val daggerRule = getDaggerRule()
    private val baristaRule = BaristaRule.create(activityClass)

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(baristaRule)
                .around { newBase, _ -> daggerRule.apply(newBase, null, target) }
                .apply(base, description)
    }

    fun run() = baristaRule.launchActivity()
    fun run(intent: Intent) = baristaRule.launchActivity(intent)
}