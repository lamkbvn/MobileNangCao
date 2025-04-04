package com.example.mobilenangcao;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.media.AudioManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class CommandHandler {
    private Context context;
    private AudioManager audioManager;
    private TextToSpeech textToSpeech;

    public CommandHandler(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.textToSpeech = new TextToSpeech(context, status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });
    }

    public void handleCommand(String intentName) {
        Intent intent = null;
        String spokenResponse = "Xin lỗi, tôi chưa hiểu lệnh này.";

        switch (intentName) {

            case "play_music":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.spotify.music");
                if (intent == null) {
                    intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                }
                spokenResponse = "Đang mở ứng dụng nghe nhạc.";
                break;

            case "what_song":
                spokenResponse = "Xin lỗi, tôi không thể xác định bài hát đang phát hiện tại.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "next_song":
                intent = new Intent("com.android.music.musicservicecommand");
                intent.putExtra("command", "next");
                spokenResponse = "Chuyển sang bài hát tiếp theo.";
                break;

            case "fun_fact":
                String[] facts = {
                        "Mỗi ngày, trung bình người trưởng thành hít thở khoảng 20.000 lần.",
                        "Mạng Internet ban đầu được gọi là ARPANET.",
                        "Một con bạch tuộc có ba trái tim!"
                };
                String randomFact = facts[new Random().nextInt(facts.length)];
                spokenResponse = "Bạn có biết? " + randomFact;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;


            case "book_flight":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.vietnamairlines.booking");
                spokenResponse = "Đang mở ứng dụng đặt vé máy bay.";
                break;

            case "weather":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.apps.weather");
                spokenResponse = "Đang mở ứng dụng thời tiết.";
                break;

            case "make_call":
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789")); // Số mẫu
                spokenResponse = "Đang mở trình quay số.";
                break;
            case "what_is_your_name":
                spokenResponse = "Tôi là trợ lý ảo do bạn lập trình. Rất vui được phục vụ!";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "what_can_i_ask_you":
                spokenResponse = "Bạn có thể hỏi tôi về thời tiết, lời nhắc, lịch trình, trò chơi, và nhiều hơn nữa!";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "what_are_your_hobbies":
                spokenResponse = "Tôi thích được nói chuyện với bạn và giúp bạn mỗi ngày!";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "calendar":
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_CALENDAR);
                spokenResponse = "Đang mở lịch của bạn.";
                break;

            case "alarm":
                intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                intent.putExtra(AlarmClock.EXTRA_MINUTES, Calendar.getInstance().get(Calendar.MINUTE) + 1);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Báo thức từ trợ lý ảo");
                spokenResponse = "Đã đặt báo thức sau một phút.";
                break;

            case "flip_coin":
                String coin = Math.random() > 0.5 ? "mặt ngửa" : "mặt sấp";
                spokenResponse = "Bạn tung được " + coin + ".";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "roll_dice":
                int dice = (int)(Math.random() * 6) + 1;
                spokenResponse = "Bạn đã tung xúc xắc ra số " + dice + ".";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "greeting":
                spokenResponse = "Xin chào! Tôi là trợ lý ảo của bạn.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "goodbye":
                spokenResponse = "Tạm biệt! Hẹn gặp lại bạn sau.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "thank_you":
                spokenResponse = "Không có chi. Rất hân hạnh được giúp bạn.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "time":
                Calendar now = Calendar.getInstance();
                String time = String.format(Locale.getDefault(), "%02d:%02d",
                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
                spokenResponse = "Bây giờ là " + time;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "date":
                Calendar today = Calendar.getInstance();
                String date = String.format(Locale.getDefault(), "%02d/%02d/%d",
                        today.get(Calendar.DAY_OF_MONTH),
                        today.get(Calendar.MONTH) + 1,
                        today.get(Calendar.YEAR));
                spokenResponse = "Hôm nay là ngày " + date;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "change_volume":
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                    spokenResponse = "Âm lượng đã được tăng tối đa.";
                } else {
                    spokenResponse = "Không thể thay đổi âm lượng.";
                }
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "translate":
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://translate.google.com/"));
                spokenResponse = "Đang mở Google Dịch.";
                break;

            case "traffic":
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/@?api=1&map_action=map&layer=traffic"));
                spokenResponse = "Đang mở bản đồ giao thông.";
                break;

            case "book_hotel":
                // Ưu tiên app Agoda nếu có
                intent = context.getPackageManager().getLaunchIntentForPackage("com.agoda.mobile.consumer");
                if (intent == null) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.booking.com"));
                    spokenResponse = "Đang mở Booking.com để đặt phòng khách sạn.";
                } else {
                    spokenResponse = "Đang mở Agoda để đặt phòng.";
                }
                break;

            case "todo_list_update":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.keep"); // Google Keep
                if (intent == null) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://keep.google.com"));
                    spokenResponse = "Mở Google Keep trên trình duyệt để cập nhật danh sách công việc.";
                } else {
                    spokenResponse = "Đang mở ứng dụng ghi chú để cập nhật danh sách công việc.";
                }
                break;
            case "reminder":
                intent = new Intent(AlarmClock.ACTION_SET_TIMER);
                intent.putExtra(AlarmClock.EXTRA_LENGTH, 120); // 2 phút
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Lời nhắc từ trợ lý ảo");
                spokenResponse = "Đã tạo lời nhắc sau 2 phút.";
                break;

            case "reminder_update":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.keep"); // Google Keep
                if (intent == null) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keep.google.com"));
                    spokenResponse = "Mở Google Keep trên trình duyệt để cập nhật lời nhắc.";
                } else {
                    spokenResponse = "Đang mở Google Keep để cập nhật lời nhắc.";
                }
                break;

            case "schedule_meeting":
                intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "Cuộc họp mới");
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Lên lịch họp từ trợ lý ảo");
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Văn phòng");
                Calendar beginTime = Calendar.getInstance();
                beginTime.add(Calendar.HOUR, 1);
                Calendar endTime = (Calendar) beginTime.clone();
                endTime.add(Calendar.MINUTE, 30);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                spokenResponse = "Đang lên lịch một cuộc họp mới.";
                break;

            case "calendar_update":
                intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
                if (intent == null) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://calendar.google.com"));
                    spokenResponse = "Mở lịch Google trong trình duyệt để cập nhật.";
                } else {
                    spokenResponse = "Đang mở ứng dụng Lịch để cập nhật sự kiện.";
                }
                break;

            case "timer":
                intent = new Intent(AlarmClock.ACTION_SET_TIMER);
                intent.putExtra(AlarmClock.EXTRA_LENGTH, 30); // 30 giây
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Hẹn giờ từ trợ lý");
                spokenResponse = "Đã hẹn giờ 30 giây.";
                break;

            case "next_holiday":
                spokenResponse = "Ngày nghỉ tiếp theo là ngày 30 tháng 4 - Giải phóng miền Nam.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "timezone":
                String timezone = TimeZone.getDefault().getDisplayName();
                spokenResponse = "Múi giờ hiện tại của bạn là " + timezone;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "meeting_schedule":
                spokenResponse = "Bạn có một cuộc họp lúc 10 giờ sáng mai.";
                showToast(spokenResponse);
                speak(spokenResponse);
                return;

            case "tell_joke":
                String[] jokes = {
                        "Vì sao máy tính luôn lạnh? Vì nó luôn mở cửa sổ!",
                        "Cậu biết vì sao lập trình viên ghét ra ngoài không? Vì có quá nhiều bugs!",
                        "Trợ lý ảo cũng cần nghỉ ngơi, nhưng đừng lo, tôi luôn sẵn sàng giúp bạn."
                };
                String randomJoke = jokes[new Random().nextInt(jokes.length)];
                spokenResponse = randomJoke;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;


            default:
                spokenResponse = "Tôi chưa xử lý được lệnh: " + intentName;
                showToast(spokenResponse);
                speak(spokenResponse);
                return;
        }

        // Nếu có intent để mở app hay thực hiện hành động
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        showToast(spokenResponse);
        speak(spokenResponse);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void speak(String message) {
        if (textToSpeech != null) {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}