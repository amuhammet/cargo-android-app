# ProGuard rules for the cargo Android application

# Keep the application class
-keep class com.example.cargo.** { *; }

# Keep all activities
-keep public class * extends android.app.Activity
-keep public class * extends androidx.fragment.app.Fragment

# Keep all model classes
-keep class com.example.cargo.data.model.** { *; }

# Keep all repository classes
-keep class com.example.cargo.data.repository.** { *; }

# Keep all database classes
-keep class com.example.cargo.data.local.** { *; }

# Keep all network service classes
-keep class com.example.cargo.network.** { *; }

# Keep all utility classes
-keep class com.example.cargo.util.** { *; }

# Keep all string resources
-keep class * {
    public static <fields>;
}

# Keep annotations
-keepattributes *Annotation*

# Keep the names of classes and methods
-keepnames class * { *; }