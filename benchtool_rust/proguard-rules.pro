-keepclassmembers class com.brunoshiroma.benchtool_android.benchtool_rust.Binder {
   public *;
}

-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile