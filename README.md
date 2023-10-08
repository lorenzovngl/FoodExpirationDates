<div align="center">

<img src="fed-icon.png" width="100px">

# Food Expiration Dates

[![Author](https://img.shields.io/badge/Author-lorenzovngl-white?style=flat-square)](https://github.com/lorenzovngl)

![GitHub Workflow (Android CI)](https://img.shields.io/github/actions/workflow/status/lorenzovngl/FoodExpirationDates/.github%2Fworkflows%2Fandroid.yml?style=flat-square&label=Build)
![GitHub last commit](https://img.shields.io/github/last-commit/lorenzovngl/FoodExpirationDates?label=Last%20commit&style=flat-square)
![GitHub](https://img.shields.io/github/license/lorenzovngl/FoodExpirationDates?style=flat-square&label=License)
![Languages](https://img.shields.io/badge/Languages-7-orange?style=flat-square)
![Pull requests welcome](https://img.shields.io/badge/Pull%20requests-Welcome-ff69b4?style=flat-square)
<a href="https://gitmoji.dev">
  <img
    src="https://img.shields.io/badge/gitmoji-%20😜%20😍-FFDD67.svg?style=flat-square"
    alt="Gitmoji"
  />
</a>

</div>

Have you ever forgotten to eat a food item before its expiration date? Throwing away food because it has expired is a practice to avoid if you want to reduce food waste.

This simple app helps you avoid forgetting to consume foods that are about to expire. Using it is simple: just record each food item along with its expiration date and you will always have a table reminding you which foods are going to expire!

<div align="center">

![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/lorenzovngl/FoodExpirationDates?include_prereleases&label=Pre-release&style=flat-square)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/lorenzovngl/FoodExpirationDates?style=flat-square&label=Release)

[<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="80"/>](https://play.google.com/store/apps/details?id=com.lorenzovainigli.foodexpirationdates)
[<img alt="Get it on GitHub" src="https://raw.githubusercontent.com/NeoApplications/Neo-Backup/main/badge_github.png" height="80"/>](https://github.com/lorenzovngl/FoodExpirationDates/releases)
[<img alt="Get it on IzzyOnDroid" src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" height="80"/>](https://apt.izzysoft.de/fdroid/index/apk/com.lorenzovainigli.foodexpirationdates.foss/)


![Google Play downloads](https://img.shields.io/endpoint?style=flat-square&url=https%3A%2F%2Fraw.githubusercontent.com%2Florenzovngl%2FFoodExpirationDates%2Fmain%2Fshields%2Fdownloads-google-play.json)
![GitHub all downloads](https://img.shields.io/github/downloads/lorenzovngl/FoodExpirationDates/total?style=flat-square&label=Downloads&logo=github)

</div>

## ✨ Features

- Display a list of food with their expiration dates in ascending order of time.
- Add, edit and delete items.
- Notifications.

## 📱 Screenshots

<div align="center">

| | | | |
|-|-|-|-|
| <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen1MainActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen2InsertActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen3SettingsActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen4InfoActivity.png"> |
<img width="150px" src="screenshots/screenshot.night_ScreenshotEnglishNight_screen1MainActivity.png"> | <img width="150px" src="screenshots/screenshot.night_ScreenshotEnglishNight_screen2InsertActivity.png"> | <img width="150px" src="screenshots/screenshot.night_ScreenshotEnglishNight_screen3SettingsActivity.png"> | <img width="150px" src="screenshots/screenshot.night_ScreenshotEnglishNight_screen4InfoActivity.png"> |
</div>

## 🛠️ Technologies

<div align="center">

![Android 34](https://img.shields.io/badge/Android%20SDK-34-3DDC84?style=for-the-badge&logo=android)
![Kotlin 1.9.10](https://img.shields.io/badge/Kotlin-1.9.10-A97BFF?&style=for-the-badge&logo=kotlin&logoColor=A97BFF)
![Jetpack Compose 1.4.8](https://img.shields.io/badge/Jetpack%20Compose-1.5.3-4285F4?style=for-the-badge&logo=Jetpack+Compose&logoColor=4285F4)

</div>

## 🌐 Languages

<div align="center">

![English default](https://img.shields.io/badge/English-default-blue?style=flat-square)
![Arabic 84%](https://img.shields.io/badge/Arabic-84%25-yellow?style=flat-square)
![German 98%](https://img.shields.io/badge/German-98%25-green?style=flat-square)
![Hindi 73%](https://img.shields.io/badge/Hindi-73%25-orange?style=flat-square)
![Italian 100%](https://img.shields.io/badge/Italian-100%25-brightgreen?style=flat-square)
![Japanese 87%](https://img.shields.io/badge/Japanese-87%25-yellow?style=flat-square)
![Spanish 100%](https://img.shields.io/badge/Spanish-100%25-brightgreen?style=flat-square)


</div>

> New translations are welcome. If you want to add a new language to the app, or improve or review an existing one, please [open an issue](https://github.com/lorenzovngl/FoodExpirationDates/issues/new).

## 🏗️ Installation steps

1. Clone the repository

    ```bash
    git clone https://github.com/lorenzovngl/FoodExpirationDates.git
    ```

2. Setup your Firebase project as described below **or** set `buildFoss = true` in [build.gradle.kts](https://github.com/lorenzovngl/FoodExpirationDates/blob/main/app/build.gradle.kts#L12) and switch to the `foss` build variant to disable the Firebase SDK in the app.

   - Setup your Firebase project:

      1. Create a Firebase project in [Firebase console](https://console.firebase.google.com/);
      2. Get the file `google-services.json` as [explained here](https://support.google.com/firebase/answer/7015592#zippy=%2Cin-this-article:~:text=Get%20config%20file%20for%20your%20Android%20app) and put it in the project root.


3. Run the app!

## 📚 Third Party

- [Material Design Icons](https://pictogrammers.com/library/mdi/)
- [Icons8 Fluency Icons](https://icons8.it/icons/fluency)
- [Marquee by T8RIN](https://github.com/T8RIN/Marquee)

## 👥 Contributors

<a href="https://github.com/lorenzovngl"><img src="https://avatars.githubusercontent.com/lorenzovngl" alt="@lorenzovngl" height="32" width="32"></a>
<a href="https://github.com/abdulmuizz0903"><img src="https://avatars.githubusercontent.com/abdulmuizz0903" alt="@abdulmuizz0903" height="32" width="32"></a>
<a href="https://github.com/uDEV2019"><img src="https://avatars.githubusercontent.com/uDEV2019" alt="@uDEV2019" height="32" width="32"></a>
<a href="https://github.com/devedroy"><img src="https://avatars.githubusercontent.com/devedroy" alt="@devedroy" height="32" width="32"></a>
<a href="https://github.com/Quadropo"><img src="https://avatars.githubusercontent.com/Quadropo" alt="@Quadropo" height="32" width="32"></a>
<a href="https://github.com/bhavesh100"><img src="https://avatars.githubusercontent.com/bhavesh100" alt="@bhavesh100" height="32" width="32"></a>
<a href="https://github.com/Rick-AB"><img src="https://avatars.githubusercontent.com/Rick-AB" alt="@Rick-AB" height="32" width="32"></a>
<a href="https://github.com/DeKaN"><img src="https://avatars.githubusercontent.com/DeKaN" alt="@DeKaN" height="32" width="32"></a>

## ❤️ Support

*Building software is awesome, making it open source is even more so. However, this requires dedication, efforts, and time. If you use this software or find it valuable, please support my commitment in developing and maintaining this project through one or more of the following methods:*

- *Follow me on [GitHub](https://github.com/lorenzovngl), [Twitter](https://twitter.com/lorenzovngl_dev) or [LinkedIn](https://www.linkedin.com/in/lorenzovainigli/).*
- *Share or star the project.*
- *[Make a donation](https://www.paypal.com/donate/?hosted_button_id=LX8P6X75XF65A).*

*Your support would be very precious for me.*

*Thank you,*

*Lorenzo*
