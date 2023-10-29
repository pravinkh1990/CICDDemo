apply plugin : 'jacoco'

tasks.withType(Test) {
    jacoco.includeNoLocationClasses = true
    jacoco.excludes = ['jdk.internal.*']
}

tasks.withType(Test) { jacoco.includeNoLocationClasses = true }

project.afterEvaluate {

    (android.hasProperty('applicationVariants')
    ? android.'applicationVariants'
    : android.'libraryVariants').all { variant ->
    def variantName = variant . name
            def unitTestTask = "test${variantName.capitalize()}UnitTest"
    def uiTestCoverageTask = "create${variantName.capitalize()}CoverageReport"

    tasks.create(name: "${unitTestTask}Coverage", type: JacocoReport, dependsOn: [
    "$unitTestTask",
    "$uiTestCoverageTask"
    ]) {
    group = "Reporting"
    description = "Generate Jacoco coverage reports for the ${variantName.capitalize()} build"

    reports {
        html.enabled = true
        xml.enabled = true
        csv.enabled = false
    }

    def fileFilter =[
        '**/R.class',
        '**/R$*.class',
        '**/BuildConfig.*',
        '**/Manifest*.*',
        '**/*Test*.*',
        '**/com/example/databinding/*',
        '**/com/example/generated/callback/*',
        '**/android/databinding/*',
        '**/androidx/databinding/*',
        '**/di/module/*',
        '**/*MapperImpl*.*',
        '**/*$ViewInjector*.*',
        '**/*$ViewBinder*.*',
        '**/BuildConfig.*',
        '**/*Component*.*',
        '**/*BR*.*',
        '**/Manifest*.*',
        '**/*$Lambda$*.*',
        '**/*Companion*.*',
        '**/*Module.*', /* filtering Dagger modules classes */
        '**/*Dagger*.*',/* filtering Dagger-generated classes */
        '**/*MembersInjector*.*',
        '**/*_Factory*.*',
        '**/*_Provide*Factory*.*',
        '**/*Extensions*.*',
        '**/*$Result.*', /* filtering `sealed` and `data` classes */
        '**/*$Result$*.*',/* filtering `sealed` and `data` classes */
        '**/*Args*.*', /* filtering Navigation Component generated classes */
        '**/*Directions*.*' /* filtering Navigation Component generated classes */
    ]

    classDirectories.setFrom(
        files(
            [
                fileTree(dir: "${buildDir}/tmp/kotlin-classes/${variantName}", excludes: fileFilter
        ),
    ]))

    def coverageSourceDirs =[
        "$project.rootDir/app/src/main/java",
        "$project.projectDir/src/${variantName}/java",
    ]
    additionalSourceDirs.setFrom(files(coverageSourceDirs))
    sourceDirectories.setFrom(files(coverageSourceDirs))

    def uiTestsData = fileTree (dir: "${buildDir}/outputs/code_coverage/${variantName}AndroidTest/connected/", includes: ["**/*.ec"])

    executionData(
        files(
            [
                "$project.buildDir/jacoco/${unitTestTask}.exec",
                uiTestsData
            ]
        )
    )
}
}
}