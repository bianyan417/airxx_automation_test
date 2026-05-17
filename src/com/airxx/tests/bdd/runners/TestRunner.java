package com.airxx.tests.bdd.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner - Cucumber TestNG runner for BDD tests.
 * Configures feature files, step definitions, and reporting.
 * 
 * @author XAC Automation Team
 * @version 1.0
 */
@CucumberOptions(
    features = "src/com/airnz/tests/bdd/features",
    glue = {"com.airnz.tests.bdd.stepdefinitions"},
    plugin = {
        "pretty",
        "html:reports/cucumber-reports/cucumber-html-report.html",
        "json:reports/cucumber-reports/cucumber.json",
        "junit:reports/cucumber-reports/cucumber.xml",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "rerun:reports/cucumber-reports/rerun.txt"
    },
    monochrome = true,
    dryRun = false,
    tags = "not @ignore and not @wip"
)
public class TestRunner extends AbstractTestNGCucumberTests {
    
    /**
     * Enables parallel execution of scenarios
     * @return Object array with scenarios
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
