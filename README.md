RoonroomLib
===========
RoonroomLib(るんるんりぶ)はJavaでルンバを制御するためのライブラリです。  
現在はUSBケーブルを用いた通常のシリアル通信と、Android端末を用いたシリアル通信に対応しています。


##利用例
###ルンバに掃除を開始させる
		StandardSerialAdapter adapter = new StandardSerialAdapter("/dev/tty.usbserial-A501KPF7", "Sample", 5000);
		controller = new Controller(adapter);
		controller.start();
		controller.clean();

###ルンバのバッテリー温度を取得する
		StandardSerialAdapter adapter = new StandardSerialAdapter("/dev/tty.usbserial-A501KPF7", "Sample", 5000);
		controller = new Controller(adapter);
		controller.start();
		controller.listen(new TemperatureListener() {
			@Override
			public void onReceive(int value) {
				System.out.println("Temperature: "+value);
			}
		});
