---

# 📱 MobileNangCao

Ứng dụng Android sử dụng **Machine Learning** để phân loại ý định người dùng từ text và giọng nói, hỗ trợ điều khiển thiết bị thông minh.

---

## 📋 Mục đích

MobileNangCao là một ứng dụng di động được xây dựng để:

* 🔍 **Phân loại ý định**: Nhận diện ý định từ văn bản hoặc giọng nói
* ⚙️ **Xử lý lệnh**: Chuyển đổi ý định thành hành động cụ thể
* 📲 **Điều khiển thiết bị**: Tích hợp với hệ thống Android
* 🎯 **Cá nhân hóa**: Tùy chỉnh hành vi theo người dùng

---

## 🏗️ Kiến trúc Project

```
MobileNangCao/
├── myproject/
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/example/mobilenangcao/
│   │   │   │   ├── MainActivity.java        # Giao diện chính
│   │   │   │   ├── IntentClassifier.java   # Phân loại ý định (ML)
│   │   │   │   ├── CommandHandler.java     # Xử lý lệnh
│   │   │   │   └── VoiceService.java       # Dịch vụ giọng nói
│   │   │   ├── assets/
│   │   │   │   ├── behavior_model.tflite
│   │   │   │   ├── label_classes.json
│   │   │   │   └── tokenizer_word_index.json
│   │   │   └── res/                        # UI resources
│   │   ├── build.gradle.kts
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── gradle.properties
```

---

## 🔧 Công nghệ sử dụng

| Thành phần       | Công nghệ              |
| ---------------- | ---------------------- |
| Ngôn ngữ         | Java                   |
| Android SDK      | Min 24, Target 35      |
| Machine Learning | TensorFlow Lite 2.14.0 |
| JSON             | Gson 2.10.1            |
| UI               | Material Design        |
| Build Tool       | Gradle (Kotlin DSL)    |
| JVM              | Java 11                |

---

## 📦 Thành phần chính

### 1. IntentClassifier.java

* Nhận text đầu vào
* Tokenize dữ liệu
* Chạy inference với TFLite
* Trả về intent có xác suất cao nhất

---

### 2. CommandHandler.java

* Xử lý logic theo intent
* Gọi API hệ thống Android
* Quản lý quyền thực thi

---

### 3. VoiceService.java

* Speech-to-Text
* Tích hợp nhận diện giọng nói Android

---

### 4. MainActivity.java

* UI chính
* Nhập text / voice
* Hiển thị lịch sử

---

### 5. SettingsActivity.java

* Cấu hình ứng dụng
* Quản lý permissions

---

## 🤖 Mô hình Machine Learning

**Assets:**

* `behavior_model.tflite` (~3MB): Model phân loại ý định
* `label_classes.json`: Mapping output → label
* `tokenizer_word_index.json`: Từ điển NLP

---

## 📋 Quyền hệ thống

```xml
<!-- Đặt báo thức -->
<uses-permission android:name="com.android.alarm.permission.SET_ALARM"/>

<!-- Device Admin -->
<uses-permission android:name="android.permission.BIND_DEVICE_ADMIN"/>

<!-- Wake Lock -->
<uses-permission android:name="android.permission.WAKE_LOCK"/>

<!-- Query apps -->
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"/>
```

---

## 🚀 Cách sử dụng

### Cài đặt

```bash
git clone https://github.com/lamkbvn/MobileNangCao.git
cd MobileNangCao/myproject
./gradlew build
./gradlew installDebug
```

---

### Sử dụng

1. Mở app
2. Nhập text hoặc dùng giọng nói
3. Ứng dụng sẽ:

   * Chuyển voice → text
   * Phân loại intent
   * Thực thi hành động
4. Xem kết quả trên UI

---

## 📚 Luồng xử lý

```
User Input (Text/Voice)
        ↓
VoiceService (Speech-to-Text)
        ↓
IntentClassifier (ML)
        ↓
CommandHandler (Execute)
        ↓
System Action
```

---

## 🔐 Yêu cầu hệ thống

* Android 7.0+ (API 24+)
* RAM ≥ 2GB
* Storage ~50MB
* Permissions: Microphone, Alarm, Device Admin

---

## 📝 Ghi chú phát triển

* Sử dụng Gradle Kotlin DSL
* Model tối ưu cho mobile (TFLite)
* Hỗ trợ Java 11+
* Tương thích Android cũ

---
