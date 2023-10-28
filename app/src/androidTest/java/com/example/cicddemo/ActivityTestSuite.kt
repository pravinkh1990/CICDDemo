package com.example.cicddemo

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityTest::class,
    ExampleInstrumentedTest::class
)
class ActivityTestSuite