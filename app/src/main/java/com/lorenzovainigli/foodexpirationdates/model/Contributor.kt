package com.lorenzovainigli.foodexpirationdates.model

class Contributor(
    val name: String,
    val username: String,
    val platform: Platform = Platform.GITHUB
) {
    constructor(username: String) : this(name = username, username = username)
    constructor(username: String, platform: Platform) : this(
        name = username,
        username = username,
        platform = platform
    )
}

enum class Platform(val url: String) {
    GITHUB("github.com"),
    WEBLATE("weblate.org")
}

val contributors = listOf(
    Contributor(name = "Lorenzo Vainigli", username = "lorenzovngl"),
    Contributor(name = "Abdul Muizz", username = "abdulmuizz0903"),
    Contributor(name = "Steve", username = "uDEV2019"),
    Contributor(name = "Devpreyo Roy", username = "devedroy"),
    Contributor(name = "Quadropo", username = "Quadropo"),
    Contributor(name = "Bhavesh Kumawat", username = "bhavesh100"),
    Contributor(name = "Richard", username = "Rick-AB"),
    Contributor(name = "Dmitriy", username = "DeKaN"),
    Contributor(name = "Aditya Kumdale", username = "AdityaKumdale"),
    Contributor(name = "Aaryan", username = "An-Array"),
    Contributor(name = "Yusril A", username = "rasvanjaya21"),
    Contributor(name = "Максим", username = "gerasimov-mv"),
    Contributor(name = "WINZORT", username = "mikropsoft"),
    Contributor(username = "3limssmile"),
    Contributor(username = "ngocanhtve"),
    Contributor(name = "kuragehime", username = "kuragehimekurara1"),
    Contributor(name = "gallegonovato", username = "gallegonovato", platform = Platform.WEBLATE),
    Contributor(name = "Eryk Michalak", username = "gnu-ewm", platform = Platform.WEBLATE),
    Contributor(name = "Oğuz Ersen", username = "oersen"),
    Contributor(username = "hugoalh", platform = Platform.WEBLATE),
    Contributor(name = "Ettore Atalan", username = "Atalanttore"),
    Contributor(name = "Maha Rajan", username = "Maha-Rajan"),
    Contributor(name = "Anurag Kanojiya", username = "anuragkanojiya1"),
    Contributor(name = "Prakash Irom", username = "PrakashIrom"),
)
