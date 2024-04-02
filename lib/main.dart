import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

///
void main() => runApp(const MyApp());

///
class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  ///
  @override
  Widget build(BuildContext context) => const MaterialApp(home: MyHomePage());
}

///
class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  ///
  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

///
class _MyHomePageState extends State<MyHomePage> {
  /* @POINT1 : メソッドチャンネルを作成 */
  static const batteryChannel = MethodChannel('platform_method/battery');

  // バッテリーの残量
  String batteryLevel = 'Waiting...';

  String message = "";

  ///
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(child: Column(mainAxisSize: MainAxisSize.min, children: <Widget>[Text(batteryLevel), Text(message)])),
      floatingActionButton: FloatingActionButton(
        onPressed: () async => await getBatteryInfo(),
        tooltip: 'Get Battery Level',
        child: const Icon(Icons.battery_full),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }

  ///
  Future getBatteryInfo() async {
    String devName = "Not...";
    int level = 0;
    String devMessage = "ERROR";

    try {
      /* @POINT2 : パラメータを作成して、プラットフォームの関数呼び出し */
      final arguments = {'text': "Flutter", 'num': 10};

      final res = await batteryChannel.invokeMethod('getBatteryInfo', arguments);

      /* @POINT3 : プラットフォームからの結果を解析取得 */
      devName = res["device"];
      level = res["level"];
      devMessage = res["message"];
    } on PlatformException catch (e) {
      batteryLevel = "Failed to get battery level: '${e.message}'.";
    }

    // 画面を再描画する
    setState(() {
      batteryLevel = '$devName:$level%';
      message = '$devMessage';
    });
  }
}
