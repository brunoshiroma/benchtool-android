[![Build Status](https://app.bitrise.io/app/d23bee8cbb51aa0a/status.svg?token=09WGNXyDkXF-NIG5aGBRew)](https://app.bitrise.io/app/d23bee8cbb51aa0a)
# Simple benchmarktool
It's run Java benchmark [benchtool-java](https://github.com/brunoshiroma/benchtool-java)

And also native Golang benchrmark [benchtool-go](https://github.com/brunoshiroma/benchtool-go) 
The native binaries are on app/src/main/jniLibs/[armbi]/*.so (it's a *.so because of android 10 security)

# lib*.so name
https://developer.android.com/ndk/guides/abis#aen


[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.brunoshiroma.benchtool_android&utm_source=github&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1)

# Play Feature
## Testing
https://developer.android.com/guide/app-bundle/test/testing-fakesplitinstallmanager

# JNA - Native lib calls
https://github.com/java-native-access/jna/blob/master/www/FrequentlyAskedQuestions.md#jna-on-android

### Tests of app bundle, dynamic feature download
```
java -jar bundletool-all-1.1.0.jar build-apks --local-testing --bundle app/build/outputs/bundle/debug/app-debug.aab --output bench.apks
java -jar bundletool-all-1.1.0.jar install-apks --apks bench.apks
```
