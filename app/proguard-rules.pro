#-dontshrink
#-dontobfuscate
#-dontoptimize
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

-dontwarn java.awt.Component
-dontwarn java.awt.GraphicsEnvironment
-dontwarn java.awt.HeadlessException
-dontwarn java.awt.Window