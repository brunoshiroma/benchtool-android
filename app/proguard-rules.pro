#-dontshrink
#-dontobfuscate
#-dontoptimize
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }