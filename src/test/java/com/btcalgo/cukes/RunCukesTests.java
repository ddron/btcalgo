package com.btcalgo.cukes;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(tags = {"~@wip", "~@dev"}, format = {"pretty", "html:/target/cucumber"}, strict = true,
        glue = "com.btcalgo.cukes")
@Ignore
public class RunCukesTests {

}
