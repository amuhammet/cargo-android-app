# Cargo Android Application 🚚

Bu proje, kuryeler ve müşteriler için geliştirilmiş, gerçek zamanlı yük takip ve lojistik yönetimini sağlayan modern bir Android uygulamasıdır. MVVM mimari deseni, Jetpack bileşenleri ve kararlı arka plan konum servisleri kullanılarak inşa edilmiştir.

## 🚀 Öne Çıkan Özellikler

### Müşteri Paneli (Customer Dashboard)
* **Rota Fiyatı Sorgulama:** Başlangıç ve varış noktalarına göre tahmini taşıma bedellerini anlık olarak hesaplar ve Room veritabanında önbelleğe alır.
* **Canlı Kargo Takibi (Safe Args):** Kargo takip numarası girildiğinde, kuryenin harita üzerindeki hareketlerini 5 saniyede bir senkronize ederek akıcı bir şekilde gösterir.

### Kurye Paneli (Courier Dashboard)
* **Gelişmiş İzin Yönetimi:** Android 13+ (Bildirim İzni) ve Android 10+ (Arka Plan Konum İzni) standartlarına tam uyumlu dinamik runtime izin kontrolü.
* **Arka Plan Konum Servisi (Foreground Service):** Kurye uygulamayı kapatsa veya ekranı kilitlese dahi, `FOREGROUND_SERVICE_LOCATION` tipiyle entegre çalışarak sunucuya koordinat basmaya devam eder.

---

## 🛠️ Kullanılan Teknolojiler ve Mimari Yapı

Application mimarisi **Clean Architecture** prensiplerine ve **Single Source of Truth (Tek Doğru Kaynak)** yaklaşımına dayanmaktadır.

* **Kotlin Coroutines & Flow:** Asenkron veri akışları ve arka plan iş parçacığı yönetimi.
* **Jetpack Navigation & Safe Args:** Fragment'lar arası güvenli veri transferi ve tip güvenli rota yönetimi.
* **Retrofit & OkHttp:** Sunucu (Backend) entegrasyonu ve ağ hatalarını izlemek için `Logging Interceptor`.
* **Room Database:** Çevrimdışı (Offline-first) çalışma desteği için yerel veri tabanı önbellekleme katmanı.
* **Google Play Services (Maps & Location):** `FusedLocationProviderClient` ile batarya dostu, yüksek hassasiyetli canlı konum tespiti.

---

## 📁 Proje Klasör Yapısı

```text
app/src/main/java/com/example/cargo/
│
├── data/
│   ├── local/          # AppDatabase, CargoDao (Room konfigürasyonu)
│   ├── model/          # CourierLocation, Load, RoutePrice, User (Veri Modelleri)
│   └── repository/     # CargoRepository (Ağ ve yerel veritabanı yönetim merkezi)
│
├── network/            # ApiService (Retrofit Uç Noktaları), RetrofitClient
│
├── service/            # LocationService (Arka Plan Foreground Konum Takip Servisi)
│
├── ui/
│   ├── dashboard/      # CustomerDashboardFragment, CourierDashboardFragment
│   ├── login/          # CustomerLoginActivity, CourierLoginActivity
│   └── tracking/       # LoadTrackingFragment (Google Maps Entegrasyonu)
│
└── util/               # Uzantı fonksiyonları (showToast vb.)
```

---

## ⚙️ Kurulum ve Çalıştırma

### 1. Google Maps API Anahtarını Ekleme
Projenin harita fonksiyonlarının çalışabilmesi için geçerli bir Google Maps API anahtarına ihtiyacı vardır. `app/src/main/AndroidManifest.xml` dosyasındaki aşağıdaki alanı güncelleyin:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="BURAYA_API_ANAHTARINIZI_YAZIN" />
```

### 2. CI/CD İş Akışı (GitHub Actions)
Projede otomatik derleme ve test süreçlerini yöneten bir CI/CD hattı (`.github/workflows/gradle.yml`) tanımlıdır. Ana (`main`) dala gönderilen her kod veya açılan her Pull Request otomatik olarak:
1. Java 17 ortamında ilklendirilir.
2. Birim (Unit) testlerinden geçer (`./gradlew testDebugUnitTest`).
3. Başarıyla derlenerek test edilebilir bir `app-debug.apk` çıktısı üretir.

---

## 🔒 Güvenlik ve İzin Standartları
Uygulama, Google Play Store'un en güncel konum politikalarına uygundur. Konum takibi esnasında bildirim çubuğunda kuryeye açıkça bilgi gösterilir ve kurye dilediği an **"Canlı Konum Paylaşımını Durdur"** butonuyla servisi tamamen sonlandırabilir.
