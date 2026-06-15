# SeçAl — release shrink/obfuscate kuralları.
# Supabase / ktor / kotlinx-serialization reflection korumaları.

# kotlinx-serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }

# @Serializable veri sınıfları
-keep,includedescriptorclasses class com.secal.**$$serializer { *; }
-keepclassmembers class com.secal.** { *** Companion; }
-keepclasseswithmembers class com.secal.** { kotlinx.serialization.KSerializer serializer(...); }

# ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Coroutines
-dontwarn kotlinx.coroutines.**
