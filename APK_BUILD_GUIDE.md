# APK Oluşturma Rehberi

## 🚀 Android Studio ile APK Oluşturma

### Yöntem 1: Build > Build Bundle(s) / APK(s)
1. Android Studio'da projeyi açın
2. Üst menüden **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)** seçin
3. İşlem tamamlandığında **"locate"** linkine tıklayın
4. APK dosyası `app/build/outputs/apk/debug/` klasöründe olacak

### Yöntem 2: Terminal ile APK Oluşturma
```bash
# Proje klasöründe terminal açın
cd motorlar

# Debug APK oluşturma
./gradlew assembleDebug

# Release APK oluşturma (önerilen)
./gradlew assembleRelease
```

### Yöntem 3: Gradle Panel ile
1. Sağ tarafta **Gradle** panelini açın
2. **app** > **Tasks** > **build** > **assembleDebug** çift tıklayın
3. APK dosyası oluşturulacak

## 📁 APK Dosya Konumu

APK dosyaları şu konumlarda oluşturulur:
```
motorlar/app/build/outputs/apk/
├── debug/
│   └── app-debug.apk
└── release/
    └── app-release.apk
```

## 🔧 Release APK için Keystore Oluşturma

### 1. Keystore Oluşturma
```bash
keytool -genkey -v -keystore motorlar.keystore -alias motorlar -keyalg RSA -keysize 2048 -validity 10000
```

### 2. build.gradle'a Keystore Ekleme
`app/build.gradle` dosyasına ekleyin:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("motorlar.keystore")
            storePassword "your_store_password"
            keyAlias "motorlar"
            keyPassword "your_key_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 📱 APK Kurulumu

### Fiziksel Cihaza Kurulum
1. APK dosyasını cihaza kopyalayın
2. **"Bilinmeyen kaynaklar"** iznini verin
3. APK dosyasına tıklayın ve kurun

### ADB ile Kurulum
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## ⚠️ Önemli Notlar

### Debug vs Release APK
- **Debug APK**: Test için, büyük boyut
- **Release APK**: Yayın için, optimize edilmiş

### Güvenlik
- Keystore dosyasını güvenli tutun
- Şifreleri kaybetmeyin
- Release APK için mutlaka keystore kullanın

### Boyut Optimizasyonu
- Release APK daha küçük olur
- ProGuard ile kod küçültülür
- Gereksiz dosyalar kaldırılır

## 🐛 Sorun Giderme

### APK Oluşturma Hatası
```bash
# Gradle cache temizleme
./gradlew clean
./gradlew assembleDebug
```

### Keystore Hatası
- Keystore dosyasının doğru konumda olduğundan emin olun
- Şifrelerin doğru olduğunu kontrol edin

### Kurulum Hatası
- Cihazda "Bilinmeyen kaynaklar" iznini verin
- APK dosyasının bozuk olmadığını kontrol edin
