# Motorlar Uygulaması Kurulum Rehberi

## 🚀 Hızlı Başlangıç

### 1. Projeyi İndirme
```bash
# Git ile klonlama
git clone https://github.com/kullaniciadi/motorlar.git
cd motorlar

# Veya ZIP olarak indirme
# GitHub'dan ZIP dosyasını indirin ve açın
```

### 2. Android Studio'da Açma
1. **Android Studio**'yu açın
2. **"Open an Existing Project"** seçin
3. `motorlar` klasörünü seçin
4. Gradle sync'in tamamlanmasını bekleyin

### 3. Google Maps API Key Ekleme
1. `app/src/main/AndroidManifest.xml` dosyasını açın
2. `YOUR_GOOGLE_MAPS_API_KEY` yerine kendi API key'inizi yazın
3. [Google Maps API Key Alma Rehberi](GOOGLE_MAPS_SETUP.md) dosyasını takip edin

### 4. Uygulamayı Çalıştırma

#### Emülatör ile:
1. **AVD Manager**'ı açın
2. **"Create Virtual Device"** tıklayın
3. **Pixel 6** veya benzeri bir cihaz seçin
4. **API 30** veya üzeri bir Android sürümü seçin
5. Emülatörü başlatın
6. **Run** butonuna tıklayın

#### Fiziksel Cihaz ile:
1. Android cihazınızda **"Geliştirici Seçenekleri"**'ni açın
2. **"USB Hata Ayıklama"**'yı etkinleştirin
3. Cihazı USB ile bilgisayara bağlayın
4. **Run** butonuna tıklayın

## 🔧 Gereksinimler

### Sistem Gereksinimleri
- **Android Studio Arctic Fox** veya üzeri
- **JDK 11** veya üzeri
- **Android SDK API 24** (Android 7.0) veya üzeri
- **En az 8GB RAM** (önerilen 16GB)

### Cihaz Gereksinimleri
- **Android 7.0** (API 24) veya üzeri
- **GPS** sensörü
- **Jiroskop** sensörü
- **İnternet bağlantısı**

## 🐛 Sorun Giderme

### Gradle Sync Hatası
```bash
# Gradle cache'ini temizleme
./gradlew clean
./gradlew build
```

### API Key Hatası
- API key'in doğru eklendiğinden emin olun
- Google Cloud Console'da API'lerin etkinleştirildiğini kontrol edin
- API key kısıtlamalarını kontrol edin

### Konum İzni Hatası
- Uygulama ilk açıldığında konum izni isteyecektir
- **"İzin Ver"** seçeneğini seçin
- Ayarlar > Uygulamalar > Motorlar > İzinler'den kontrol edin

### Sensör Hatası
- Cihazınızda jiroskop sensörü olup olmadığını kontrol edin
- Emülatör kullanıyorsanız sensör verileri sınırlı olabilir

## 📱 Uygulama Özellikleri

### Ana Ekranlar
1. **Ana Sayfa**: Rota listesi ve filtreleme
2. **Harita**: Google Maps entegrasyonu
3. **Kayıt**: Rota kayıt ve sensör verileri
4. **Takım**: Sosyal özellikler
5. **Profil**: Kullanıcı ayarları

### Test Verileri
Uygulama şu anda örnek verilerle çalışmaktadır:
- Örnek rotalar
- Örnek kullanıcı profili
- Örnek takım verileri

## 🔄 Güncellemeler

### Yeni Özellikler Ekleme
1. Yeni ekranlar için `ui/screens/` klasörüne ekleyin
2. Yeni veri modelleri için `data/model/` klasörüne ekleyin
3. Navigation'a yeni rotalar ekleyin

### API Entegrasyonu
1. `data/remote/` klasörüne API servisleri ekleyin
2. Repository katmanını güncelleyin
3. ViewModel'leri güncelleyin

## 📞 Destek

Sorun yaşarsanız:
1. GitHub Issues bölümüne sorun bildirin
2. Detaylı hata mesajlarını paylaşın
3. Cihaz bilgilerinizi belirtin

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
