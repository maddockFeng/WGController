/**
* $Id: 2015-08-24 15:49:39 Karl CSN 陈绍宁 $
*
* 门禁控制器 短报文协议 测试案例(只适用于V6.56或以上版本)
* V1.0 版本  2015-08-24 15:50:17
*            基本功能:     远程开门
*            系统要求: android版本为2.3.1或以上
*            权限要求: 要在 AndroidMainfest.xml中增加如下权限
*                <uses-permission android:name="android.permission.INTERNET"></uses-permission> 
*            案例控制器: 驱动V6.56或以上; IP设为 192.168.168.123; 电脑IP在同一网段.
*
*            
*/


package com.coolvisit.wgcontroller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coolvisit.wgcontroller.controller.IAccessController;
import com.coolvisit.wgcontroller.controller.UserDB;
import com.coolvisit.wgcontroller.controller.WGController;
import com.coolvisit.wgcontroller.scanqr.ui.CaptureActivity;

//提交
public class MainActivity extends Activity {

	private IAccessController mAC;
	private EditText mEditText_cardid,mEditText_devid,mEditText_doorno,mEditText_ip,mEditText_port;
	private EditText mEditText_server_api,mEditText_ReaderNum;
	public static TextView mPrintView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
							
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//must manually  add StrictMode
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog() 
				.build());
		(findViewById(R.id.button1)).setOnClickListener(listener);
		(findViewById(R.id.button_add)).setOnClickListener(listener);
		(findViewById(R.id.button_del)).setOnClickListener(listener);
		(findViewById(R.id.button_check)).setOnClickListener(listener);
		(findViewById(R.id.button_scan)).setOnClickListener(listener);
		(findViewById(R.id.button_record)).setOnClickListener(listener);
		(findViewById(R.id.button_clear)).setOnClickListener(listener);

		mEditText_cardid = (EditText) findViewById(R.id.edit_cardid);
		mEditText_devid= (EditText) findViewById(R.id.edit_devid);
		mEditText_doorno = (EditText) findViewById(R.id.edit_doorno);
		mEditText_ip = (EditText) findViewById(R.id.edit_ip);
		mEditText_port = (EditText) findViewById(R.id.edit_port);
		mEditText_server_api = (EditText) findViewById(R.id.server_open_api);
		mEditText_ReaderNum = (EditText) findViewById(R.id.reader_number);
		mPrintView = (TextView) findViewById(R.id.print);

		mEditText_cardid.setText(UserDB.getValue(UserDB.KEY_CARD_ID,""));
		mEditText_devid.setText(UserDB.getValue(UserDB.KEY_DEV_ID,""));
		mEditText_doorno.setText(UserDB.getValue(UserDB.KEY_DOOR,""));
		mEditText_ip.setText(UserDB.getValue(UserDB.KEY_IP,""));
		mEditText_port.setText(UserDB.getValue(UserDB.KEY_PORT,""));
		mEditText_server_api.setText(UserDB.getValue(UserDB.KEY_OPEN_API,"http://www.coolvisit.top/qcvisit/uploadQrcode"));
		mEditText_ReaderNum.setText(UserDB.getValue(UserDB.KEY_READER_NUM,""));

		mAC = new WGController();
//		mAC.searchDevices("255.255.255.255",60000);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View btn = v;
			String cardId = mEditText_cardid.getEditableText().toString();
			final String devid = mEditText_devid.getEditableText().toString();
			final String doorNo = mEditText_doorno.getEditableText().toString();
			final String ip = mEditText_ip.getEditableText().toString();
			String strPort = mEditText_port.getEditableText().toString();
			UserDB.setValue(UserDB.KEY_CARD_ID,cardId);
			UserDB.setValue(UserDB.KEY_DEV_ID,devid);
			UserDB.setValue(UserDB.KEY_DOOR,doorNo);
			UserDB.setValue(UserDB.KEY_IP,ip);
			UserDB.setValue(UserDB.KEY_PORT,strPort);
			if (cardId == null
					||devid == null
					||doorNo == null
					|| ip == null
					|| strPort == null){
				Toast.makeText(MainActivity.this, "信息不完整", Toast.LENGTH_SHORT).show();
				return;
			}
			final int port = Integer.parseInt(strPort);

			switch (btn.getId()) {
			case R.id.button1:
				//Controller IP:192.168.168.123, Mask:255.255.255.0; zzzzz
				//PC IP: 192.168.168.120 or IP in the same local net
				try {
					int ret = ((WGController) mAC).open(devid, Integer.parseInt(doorNo), ip, port);
					if (ret > 0)
//			    if (RemoteOpenDoorIP(1,"172.16.109.56") >0)
					{
						setText("Successful!", 1);
					} else {
						setText("Failed." + ret, -1);
					}
				}catch (Exception e){

				}
				break;
				case R.id.button_add:
					int  ret = mAC.modifyCard(cardId,"20170101","20290101",devid,doorNo,ip,port);
					if ( ret >0)
					{
						setText("add Successful!",1);
					}
					else
					{
						setText("add Failed."+ret,-1);
					}

					for(int i=1000;i<3000;i++){
						mAC.addByOrder(cardId,"20170101","20290101",devid,doorNo,2000,i-1000+1,ip,port);
					}
					break;
				case R.id.button_del:
					ret = mAC.deleteCard(cardId,devid,ip,port);
					if (ret >0)
					{
						setText("delete Successful!",1);
					}
					else
					{
						setText("delete Failed."+ret,-1);
					}
					break;
				case R.id.button_clear:
					ret = mAC.clearCard(devid,ip,port);
					if (ret >0)
					{
						setText("clear Successful!",1);
					}
					else
					{
						setText("clear Failed."+ret,-1);
					}
					break;
				case R.id.button_check:
					ret = mAC.checkCard(cardId,devid,doorNo,ip,port);
					if (ret >0)
					{
						setText("check Successful!",1);
					}
					else
					{
						setText("check Failed."+ret,-1);
					}
					break;
				case R.id.button_record:
					ret = WGController.getUnReadRecord(devid,ip,port);
					if (ret >0)
					{
						setText("check Successful!",1);
					}
					else
					{
						setText("check Failed."+ret,-1);
					}
					break;
				case R.id.button_scan:
					String api = mEditText_server_api.getEditableText().toString();
					String reader = mEditText_ReaderNum.getEditableText().toString();
					UserDB.setValue(UserDB.KEY_OPEN_API,api);
					UserDB.setValue(UserDB.KEY_READER_NUM,reader);
					if(api.isEmpty() || reader.isEmpty()){
						Toast.makeText(MainActivity.this, "读头信息不完整！", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
					intent.putExtra("api",api);
					intent.putExtra("reader",reader);
					startActivity(intent);
					break;
			default:
				break;
			}
		}
	};
	
	private static int tag = 1;


	private int RemoteOpenDoorIP(int doorNO, String IP) {
		int ret = -13; 
		byte content[] = null;
		    DatagramPacket snddataPacket;
		try {
			int controllerPort = 60000;
			byte[] byteCmd = new byte[] { (byte) 0x17, (byte) 0x40,
					(byte) 0x00, (byte) 0x00, (byte) 0xBE, (byte) 0x2A,
					(byte) 0x59, (byte) 0x07, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
					(byte) 0x00, (byte) 0x00 };

			
			byteCmd[8] = (byte) doorNO;

			byteCmd[40] = (byte) (tag & 0xff);
			byteCmd[41] = (byte) ((tag >> 8) & 0xff);
			byteCmd[42] = (byte) ((tag >> 16) & 0xff);
			byteCmd[43] = (byte) ((tag >> 24) & 0xff); 

			content = null;
			DatagramSocket dataSocket = new DatagramSocket();
			dataSocket.setSoTimeout(1000); 

			snddataPacket = new DatagramPacket((byteCmd), byteCmd.length,
					InetAddress.getByName(IP), controllerPort);
			dataSocket.send(snddataPacket);

			byte recvDataByte[] = new byte[64];

			Thread.sleep(200); 
			DatagramPacket dataPacket = new DatagramPacket(recvDataByte,
					recvDataByte.length);
			dataSocket.receive(dataPacket);

			content = dataPacket.getData();
			dataSocket.close();
		} catch (NumberFormatException exNum) {
			exNum.printStackTrace();
		}

		catch (UnknownHostException e1) {
			e1.printStackTrace();
			return ret; 
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {

		}

		if ((content != null) && (content.length == 64)) {
			ret = content[8];
		} else {
			ret = -13; 
		}
		tag++;

		return ret; // null;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setText(String msg,int status){
		if(status >0){
			((TextView) findViewById(R.id.textView1)).setTextColor(Color.BLACK);
		}else{
			((TextView) findViewById(R.id.textView1)).setTextColor(Color.RED);
		}
		((TextView) findViewById(R.id.textView1)).setText(msg);
	}

	int p = 5;
	int g = 5;
	int count = 5;
	private void he(){

		int j = p/2;
		count = count + j;
		p = p-j;
		g = g+j;

		j = g/4;
		count = count+j;
		p = p+j;
		g = g-j*3;

		Log.d("main", "喝: "+p+"瓶子 "+g+"盖子 酒:"+count);

		if (p>= 2 || g >=4){
			he();
		}

	}

}
