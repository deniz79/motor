# Google Maps API Key Alma Rehberi

## 1. Google Cloud Console'a Giriş
1. [Google Cloud Console](https://console.cloud.google.com/) adresine gidin
2. Google hesabınızla giriş yapın

## 2. Yeni Proje Oluşturma
1. Üst menüden "Proje Seç" > "Yeni Proje" tıklayın
2. Proje adı: "Motorlar" yazın
3. "Oluştur" butonuna tıklayın

## 3. Maps API'lerini Etkinleştirme
1. Sol menüden "API'ler ve Hizmetler" > "Kütüphane" seçin
2. Aşağıdaki API'leri arayın ve etkinleştirin:
   - **Maps SDK for Android**
   - **Places API**
   - **Directions API**
   - **Geocoding API**

## 4. API Key Oluşturma
1. "API'ler ve Hizmetler" > "Kimlik Bilgileri" seçin
2. "Kimlik Bilgisi Oluştur" > "API Anahtarı" tıklayın
3. Oluşturulan API key'i kopyalayın

## 5. API Key'i Kısıtlama (Önerilen)
1. Oluşturulan API key'e tıklayın
2. "Uygulama kısıtlamaları" bölümünde "Android uygulamaları" seçin
3. SHA-1 sertifika parmak izinizi ekleyin
4. Paket adı: `com.motorlar.app` ekleyin

## 6. Projeye API Key Ekleme
1. `app/src/main/AndroidManifest.xml` dosyasını açın
2. `YOUR_GOOGLE_MAPS_API_KEY` yerine kendi API key'inizi yazın:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyB..." />
```

## 7. local.properties Dosyası (Opsiyonel)
Proje kök dizininde `local.properties` dosyası oluşturun:

```properties
MAPS_API_KEY=AIzaSyB...
```

## Not
- API key'inizi güvenli tutun ve public repository'lere yüklemeyin
- Google Cloud Console'da kullanım limitlerini kontrol edin
- Ücretsiz kullanım için günlük limitler vardır
