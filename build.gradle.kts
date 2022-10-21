plugins {
    java
    alias(libs.plugins.quarkus)
    alias(libs.plugins.lombok)
}

dependencies {
    implementation(platform(libs.quarkus.bom))
    implementation(platform(libs.quarkus.operatorsdk.bom))

    implementation(libs.quarkus.jaxrs)
    implementation(libs.quarkus.operatorsdk)

    runtimeOnly(libs.bouncycastle.pkix)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
