package com.example.bluethoothble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var bAdapter: BluetoothAdapter
    private val REQUEST_ENABLE_BT = 102
    private val SCAN_PERIOD:Long = 10000
    private val handler = Handler()
    private lateinit var receiver : BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initilize the Adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bAdapter == null){
            Toast.makeText(this,"Device not Supported",Toast.LENGTH_SHORT).show()
        }

        btn_on.setOnClickListener {
            if (bAdapter?.isEnabled == false){
                var enableintent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableintent,REQUEST_ENABLE_BT)
            }else{
                Toast.makeText(this,"Bluethooth is already On.",Toast.LENGTH_SHORT).show()
            }
        }
        btn_off.setOnClickListener {
            if(bAdapter.isEnabled == true){
                bAdapter.disable()
            }else{
                Toast.makeText(this,"Bluetooth is already Off.",Toast.LENGTH_SHORT).show()
            }

        }
        btn_find.setOnClickListener {



            receiver = object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {
                    val action:String? = intent.action
                    when(action){
                        BluetoothDevice.ACTION_FOUND -> {
                            val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                            val devicename = device?.name
                            val deviceAdress = device?.address
                        }
                    }
                }
            }
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(receiver, filter)
        }
        btn_pair.setOnClickListener {
            //pair devices
            val pairDevice = bAdapter.bondedDevices
            if (pairDevice.size > 0){
                for(device in pairDevice){
                    val deviceName = device.name
                    val deviceAdress = device.address
                    txt_pairdevice.append("$deviceName - " + "$deviceAdress\n")
                }
            }else{
                Toast.makeText(this,"There is no Pair device",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun scanLeDevice() {
        val leScanCallback : ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult){
                super.onScanResult(callbackType, result)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}


