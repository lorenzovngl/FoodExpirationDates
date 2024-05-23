import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.stdlib)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

val locales = mapOf(
    "ar" to "Arabic",
    "de" to "German",
    "en" to "English",
    "es" to "Spanish",
    "fr" to "French",
    "hi" to "Hindi",
    "in" to "Indonesian",
    "it" to "Italian",
    "ja" to "Japanese",
    "ru" to "Russian",
    "tr" to "Turkish",
    "pl" to "Polish",
    "vi" to "Vietnamese"
)

var results: List<Pair<String, Int>> = mutableListOf()

data class JsonEntry(
    val schemaVersion: Int = 1,
    val label: String,
    val message: String,
    val color: String,
    val style: String = "flat-square"
) {
    fun toJson() = "{\n" +
            "  \"schemaVersion\": $schemaVersion,\n" +
            "  \"label\": \"$label\",\n" +
            "  \"message\": \"$message\",\n" +
            "  \"color\": \"$color\",\n" +
            "  \"style\": \"$style\"\n" +
            "}"
}

fun path(vararg elements: String) = elements.joinToString(separator = File.separator)

fun analyzeTranslations(androidProjectPath: String) {
    val androidProject = File(androidProjectPath)
    val defaultXML = androidProject.walkTopDown()
        .filter {
            it.absolutePath.endsWith(path("values", "strings.xml")) &&
                    it.absolutePath.contains(path("app", "src", "main"))
        }.first()
    androidProject.walkTopDown()
        .filter {
            it.name.endsWith("strings.xml") && it.absolutePath.contains("main")
        }.forEach {
            parseXML(defaultXML, it)
        }
}

fun parseXML(default: File, translation: File) {
    val folderName = translation.absolutePath.substring(
        translation.absolutePath.indexOf("values"),
        translation.absolutePath.lastIndexOf(File.separator)
    )
    val localeKey = folderName.let {
        it.substring(startIndex = it.indexOf("-") + 1)
    }
    val locale = locales[localeKey]
    val percent = (countStrings(translation).toDouble() / countStrings(default) * 100).toInt()
    val color = percent.let {
        when {
            it < 50 -> "red"
            it < 80 -> "orange"
            it < 90 -> "yellow"
            it < 95 -> "yellowgreen"
            it < 100 -> "green"
            else -> "brightgreen"
        }
    }
    if (locale != null) {
        results = results.plus(locale to percent)
        File(path("shields", "translations", "${localeKey}.json"))
            .writeText(
                JsonEntry(
                    label = locale,
                    message = "$percent%",
                    color = color
                ).toJson()
            )
    }
}

fun countStrings(file: File): Int {
    val doc = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(file)
    doc.documentElement.normalize()
    return doc.getElementsByTagName("string").length +
            doc.getElementsByTagName("item").length
}

tasks.register("analyzeTranslations") {
    doLast {
        println("Computing translation statistics...")
        analyzeTranslations(System.getProperty("user.dir"))
        println(
            "Translation statistics: ${
                results.sortedBy { 
                    it.first
                }.joinToString(separator = ", ") {
                    "${it.first}: ${it.second}%"
                }
            }"
        )
    }
}

tasks.named("build") { finalizedBy("analyzeTranslations") }

tasks.named("assemble") { finalizedBy("analyzeTranslations") }