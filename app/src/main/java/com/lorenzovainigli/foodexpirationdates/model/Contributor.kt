package com.lorenzovainigli.foodexpirationdates.model

class Contributor(
    val name: String,
    val username: String
) {
    constructor(username: String) : this(name = username, username = username)
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
    Contributor(name = "ＷＩＮＺＯＲＴ", username = "mikropsoft"),
    Contributor(username = "3limssmile"),
    Contributor(username = "ngocanhtve"),
    Contributor(name = "kuragehime", username = "kuragehimekurara1")
)
