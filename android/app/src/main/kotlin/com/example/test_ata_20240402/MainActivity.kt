package com.example.test_ata_20240402

// MethodChannelのため
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodCall

// バッテリー取得のため
import android.content.Context
import android.os.BatteryManager

class MainActivity : FlutterActivity() {

    /*===== ここから =====*/
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        /* @POINT1 チャンネルとハンドラコールバック登録 */
        val channel =
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "platform_method/battery")
        channel.setMethodCallHandler { methodCall: MethodCall, result: MethodChannel.Result ->

            if (methodCall.method == "getBatteryInfo") {

                /* @POINT2 : Flutterから渡されたデータを解析取得 */
                val text = methodCall.argument<String>("text") ?: "Not Message..."
                val num = methodCall.argument<Int>("num") ?: 0
                val message = makeMessage(text, num)

                // バッテリー残量を取得
                val level = getBatteryLevel()

                /* @POINT3 : Flutterへ返す情報を作成 */
                val res = mapOf(
                    "device" to "Android",
                    "level" to level,
                    "message" to message,
                )

                result.success(res)

            } else {
                result.notImplemented()
            }
        }
    }
    /*===== ここまで =====*/

    /* メッセージ作成 */
    // Androidからはtextの前後に「-」をnum回数分付与
    private fun makeMessage(text: String, num: Int): String {
        val mark = "-"
        var message = ""
        for (i in 1..num) {
            message += mark
        }
        message += text
        for (i in 1..num) {
            message += mark
        }
        return message
    }

    /* バッテリー残量を取得 */
    private fun getBatteryLevel(): Int {
        val manager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel: Int = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return batteryLevel
    }
}
