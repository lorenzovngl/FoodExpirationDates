<div align="center">

<img src="app/src/main/res/drawable/fed_icon.png" width="100px">

# Food Expiration Dates

[![Author](https://img.shields.io/badge/Author-lorenzovngl-white?style=flat-square)](https://github.com/lorenzovngl)
![GitHub Repo stars](https://img.shields.io/github/stars/lorenzovngl/FoodExpirationDates?style=flat-square&label=%E2%AD%90&&color=white)
[![Discord](https://img.shields.io/discord/1208027149299224606?style=flat-square&logo=discord&logoColor=white)](https://discord.gg/UVpzQqzg5c)

[![](https://img.shields.io/badge/Featured%20on-Open%20Sustainable%20Technology-009485?style=flat-square)](https://github.com/protontypes/open-sustainable-technology)

![GitHub Workflow (Android CI)](https://img.shields.io/github/actions/workflow/status/lorenzovngl/FoodExpirationDates/.github%2Fworkflows%2Fandroid.yml?style=flat-square&label=Build)
![GitHub last commit](https://img.shields.io/github/last-commit/lorenzovngl/FoodExpirationDates?label=Last%20commit&style=flat-square)
![GitHub](https://img.shields.io/github/license/lorenzovngl/FoodExpirationDates?style=flat-square&label=License)
![Languages](https://img.shields.io/badge/Languages-15-orange?style=flat-square)
![GitHub repo size](https://img.shields.io/github/repo-size/lorenzovngl/FoodExpirationDates?style=flat-square&label=Size)
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

- Display a list of food with their expiration dates in ascending order of time
- Add, edit and delete items
- Product retrieval using barcode scanner and [Open Food Facts](https://world.openfoodfacts.org/) API
- Opening date
- Notifications

## 📱 Screenshots

<div align="center">

| | | | |
|-|-|-|-|
| <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen1MainActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen2InsertActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screenBarcodeScanner.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen3SettingsActivity.png"> | 

| | | | |
|-|-|-|-|
| <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen4InfoActivity.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen5DarkMode.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screen6DynamicColors.png"> | <img width="150px" src="screenshots/screenshot_ScreenshotEnglish_screenMadeWithHeart.png"> |

</div>

## 🌐 Languages

<div align="center">

[![Translation status](https://hosted.weblate.org/widget/food-expiration-dates/287x66-black.png)](https://hosted.weblate.org/engage/food-expiration-dates/)

[![Translation status](https://hosted.weblate.org/widget/food-expiration-dates/horizontal-auto.svg)](https://hosted.weblate.org/engage/food-expiration-dates/)

</div>

> You can help translating this project on [Hosted Weblate](https://hosted.weblate.org/engage/food-expiration-dates/).

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

## 👥 Contributors

<a href="https://github.com/lorenzovngl"><img src="https://avatars.githubusercontent.com/lorenzovngl" alt="@lorenzovngl" height="32" width="32"></a>
<a href="https://github.com/abdulmuizz0903"><img src="https://avatars.githubusercontent.com/abdulmuizz0903" alt="@abdulmuizz0903" height="32" width="32"></a>
<a href="https://github.com/uDEV2019"><img src="https://avatars.githubusercontent.com/uDEV2019" alt="@uDEV2019" height="32" width="32"></a>
<a href="https://github.com/devedroy"><img src="https://avatars.githubusercontent.com/devedroy" alt="@devedroy" height="32" width="32"></a>
<a href="https://github.com/Quadropo"><img src="https://avatars.githubusercontent.com/Quadropo" alt="@Quadropo" height="32" width="32"></a>
<a href="https://github.com/bhavesh100"><img src="https://avatars.githubusercontent.com/bhavesh100" alt="@bhavesh100" height="32" width="32"></a>
<a href="https://github.com/Rick-AB"><img src="https://avatars.githubusercontent.com/Rick-AB" alt="@Rick-AB" height="32" width="32"></a>
<a href="https://github.com/DeKaN"><img src="https://avatars.githubusercontent.com/DeKaN" alt="@DeKaN" height="32" width="32"></a>
<a href="https://github.com/AdityaKumdale"><img src="https://avatars.githubusercontent.com/AdityaKumdale" alt="@AdityaKumdale" height="32" width="32"></a>
<a href="https://github.com/An-Array"><img src="https://avatars.githubusercontent.com/An-Array" alt="@An-Array" height="32" width="32"></a>
<a href="https://github.com/rasvanjaya21"><img src="https://avatars.githubusercontent.com/rasvanjaya21" alt="@rasvanjaya21" height="32" width="32"></a>
<a href="https://github.com/gerasimov-mv"><img src="https://avatars.githubusercontent.com/gerasimov-mv" alt="@gerasimov-mv" height="32" width="32"></a>
<a href="https://github.com/mikropsoft"><img src="https://avatars.githubusercontent.com/mikropsoft" alt="@mikropsoft" height="32" width="32"></a>
<a href="https://github.com/3limssmile"><img src="https://avatars.githubusercontent.com/3limssmile" alt="@3limssmile" height="32" width="32"></a>
<a href="https://github.com/ngocanhtve"><img src="https://avatars.githubusercontent.com/ngocanhtve" alt="@ngocanhtve" height="32" width="32"></a>
<a href="https://github.com/kuragehimekurara1"><img src="https://avatars.githubusercontent.com/kuragehimekurara1" alt="@kuragehimekurara1" height="32" width="32"></a>
<a href="https://hosted.weblate.org/user/gallegonovato/"><img src="https://hosted.weblate.org/avatar/128/gallegonovato.png" alt="@gallegonovato" height="32" width="32"></a>
<a href="https://hosted.weblate.org/user/gnu-ewm/"><img src="https://hosted.weblate.org/avatar/128/gnu-ewm.png" alt="@gnu-ewm" height="32" width="32"></a>
<a href="https://github.com/oersen"><img src="https://avatars.githubusercontent.com/oersen" alt="@oersen" height="32" width="32"></a>
<a href="https://hosted.weblate.org/user/hugoalh/"><img src="https://hosted.weblate.org/avatar/128/hugoalh.png" alt="@hugoalh" height="32" width="32"></a>
<a href="https://github.com/Atalanttore"><img src="https://avatars.githubusercontent.com/Atalanttore" alt="@Atalanttore" height="32" width="32"></a>
<a href="https://github.com/Maha-Rajan"><img src="https://avatars.githubusercontent.com/Maha-Rajan" alt="@Maha-Rajan" height="32" width="32"></a>
<a href="https://github.com/anuragkanojiya1"><img src="https://avatars.githubusercontent.com/anuragkanojiya1" alt="@anuragkanojiya1" height="32" width="32"></a>
<a href="https://github.com/PrakashIrom"><img src="https://avatars.githubusercontent.com/PrakashIrom" alt="@PrakashIrom" height="32" width="32"></a>
<a href="https://github.com/serAKL16lysA"><img src="https://avatars.githubusercontent.com/serAKL16lysA" alt="@serAKL16lysA" height="32" width="32"></a>

## ❤️ Support

*Building software is awesome, making it open source is even more so. However, this requires dedication, efforts, and time. If you use this software or find it valuable, please support my commitment in developing and maintaining this project through one or more of the following methods:*

- *Follow me on one of these platforms*

[![Twitter](https://img.shields.io/badge/twitter/x-%23000000.svg?&style=for-the-badge&logo=x&logoColor=white)](https://twitter.com/lorenzovngl_dev)
[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?&style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/lorenzovainigli)
[![Threads](https://img.shields.io/badge/threads-%23FFFFFF.svg?&style=for-the-badge&logo=threads&logoColor=black)](https://www.threads.net/@lorenzovngl)

- *Star the project*
- *[Make a donation](https://www.paypal.com/donate/?hosted_button_id=LX8P6X75XF65A)*

*Your support would be very precious for me.*

*Thank you,*

*Lorenzo*

## ✨ Star History

[![Star History Chart](https://api.star-history.com/svg?repos=lorenzovngl/FoodExpirationDates&type=Date)](https://star-history.com/#lorenzovngl/FoodExpirationDates&Date)