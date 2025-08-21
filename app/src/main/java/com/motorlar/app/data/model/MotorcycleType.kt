package com.motorlar.app.data.model

enum class MotorcycleType(val displayName: String, val description: String) {
    SPORT("Sport", "Yüksek performanslı, hızlı sürüş için tasarlanmış"),
    TOURING("Touring", "Uzun yolculuklar için konforlu"),
    CRUISER("Cruiser", "Rahat sürüş ve stil odaklı"),
    ADVENTURE("Adventure", "Her türlü yol koşulunda kullanılabilir"),
    NAKED("Naked", "Çıplak tasarım, şehir içi kullanım"),
    SCOOTER("Scooter", "Pratik şehir içi ulaşım"),
    DUAL_SPORT("Dual Sport", "Hem asfalt hem arazi için"),
    CUSTOM("Custom", "Özelleştirilmiş tasarım")
}
