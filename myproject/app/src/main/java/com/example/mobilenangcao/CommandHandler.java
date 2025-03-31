package com.example.mobilenangcao;
import android.content.Intent;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.SmsManager;
import java.util.Locale;

public class CommandHandler {
    private Context context;
    private AudioManager audioManager;
    private TextToSpeech tts;

    public CommandHandler(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.tts = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.getDefault());
            }
        });
    }

    public void handleCommand(String command) {
        Intent intent = null;

        // 📌 Mở ứng dụng phổ biến
        if (command.contains("mở YouTube")) {
            intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");

        } else if (command.contains("mở Facebook")) {
            intent = context.getPackageManager().getLaunchIntentForPackage("com.facebook.katana");

        } else if (command.contains("mở Messenger")) {
            intent = context.getPackageManager().getLaunchIntentForPackage("com.facebook.orca");

        } else if (command.contains("mở Zalo")) {
            intent = context.getPackageManager().getLaunchIntentForPackage("com.zing.zalo");

        } else if (command.contains("mở TikTok")) {
            intent = context.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");

        }

        // 🔊 Điều chỉnh âm lượng
        else if (command.contains("tăng âm lượng")) {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

        } else if (command.contains("giảm âm lượng")) {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

        }

        // 🎵 Phát nhạc
        else if (command.contains("phát nhạc")) {
            intent = new Intent("com.android.music.musicservicecommand");
            intent.putExtra("command", "play");

        } else if (command.contains("dừng nhạc")) {
            intent = new Intent("com.android.music.musicservicecommand");
            intent.putExtra("command", "pause");

        }

        // 🔦 Bật/Tắt đèn pin
        else if (command.contains("bật đèn pin")) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

        } else if (command.contains("tắt đèn pin")) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

        }

        // 📷 Camera
        else if (command.contains("mở camera")) {
            intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

        } else if (command.contains("quay video")) {
            intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        }

        // ⏰ Đặt báo thức
        else if (command.contains("đặt báo thức")) {
            intent = new Intent(AlarmClock.ACTION_SET_ALARM);
            intent.putExtra(AlarmClock.EXTRA_HOUR, 7);
            intent.putExtra(AlarmClock.EXTRA_MINUTES, 0);

        }

        // 📅 Thêm sự kiện vào lịch
        else if (command.contains("thêm sự kiện vào lịch")) {
            intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, "Cuộc họp");
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis() + 3600000);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis() + 7200000);

        }

        // 📞 Gọi điện thoại
        else if (command.contains("gọi")) {
            String phoneNumber = command.replaceAll("\\D+", ""); // Lấy số từ câu lệnh
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

        }

        // 📩 Gửi tin nhắn
        else if (command.contains("gửi tin nhắn")) {
            String phoneNumber = "0123456789"; // Thay bằng số điện thoại thật
            String message = "Xin chào!";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        }

        // 📶 Kết nối mạng
        else if (command.contains("bật Wi-Fi")) {
            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);

        } else if (command.contains("tắt Wi-Fi")) {
            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);

        }

        // 🔵 Bật/Tắt Bluetooth
        else if (command.contains("bật Bluetooth")) {
            intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        } else if (command.contains("tắt Bluetooth")) {
            intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        }

        // 🗺 Mở Google Maps
        else if (command.contains("mở Google Maps")) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=Nhà Thờ Đức Bà, Hồ Chí Minh"));

        }

        // ☁️ Xem thời tiết
        else if (command.contains("xem thời tiết")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=thời+tiết+hôm+nay"));

        }

        // 🔊 Đọc văn bản thành giọng nói (TTS)
        else if (command.contains("đọc văn bản")) {
            tts.speak("Xin chào, đây là văn bản được đọc lên!", TextToSpeech.QUEUE_FLUSH, null, null);

        }

        // 🛠 Hệ thống
        else if (command.contains("khởi động lại")) {
            intent = new Intent(Intent.ACTION_REBOOT);

        } else if (command.contains("khóa màn hình")) {
            intent = new Intent(Intent.ACTION_SCREEN_OFF);

        }

        // ❌ Lệnh không hợp lệ
        else {
            System.out.println("Không hiểu lệnh: " + command);
            return;
        }

        if (intent != null) {
            context.startActivity(intent);
        }
    }
}