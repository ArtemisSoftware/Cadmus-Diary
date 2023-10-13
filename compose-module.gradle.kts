//apply('com.android.library')
//apply('kotlin-android')
import java.util.Properties
println(
    """
    I am defined at the top level of the script and
    executed at the configuration phase of build process
    """
)


fun getApiKey(): String {
    val props = Properties()
    val secretsFile = File("secrets.properties")

    if (secretsFile.exists()) {
        secretsFile.inputStream().use { props.load(it) }
        return props.getProperty("CLIENT_ID") ?: ""
    } else {
        return ""
    }
}


//val props = java.util.Properties()
//file("$rootDir/secret.properties").inputStream().use { props.load(it) }
//
//println("APP_ID ${props.getProperty("APP_ID")}")
//println("CLIENT_ID ${props.getProperty("CLIENT_ID")}")

//defaultConfig {
//
//    buildConfigField("String", "CLIENT_ID", props.getProperty("CLIENT_ID"))
//}

//
//android {
//    namespace = "com.home.presentation"
//    compileSdk = ProjectConfig.compileSdk
//
//    defaultConfig {
//        minSdk = ProjectConfig.minSdk
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro",
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = Compose.kotlinCompilerExtensionVersion
//    }
//}
//
//dependencies {
//    implementation(libs.activity.compose)
//    implementation(platform(libs.compose.bom))
//    implementation(libs.ui)
//    implementation(libs.ui.graphics)
//    implementation(libs.ui.tooling)
//    implementation(libs.ui.tooling.preview)
//    implementation(libs.material3)
//}