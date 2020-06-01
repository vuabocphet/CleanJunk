-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

-keep public class ** {
    public protected *;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class **.R$* {
  public static <fields>;
}

-keepattributes MethodParameters
