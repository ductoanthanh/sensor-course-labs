package com.example.mibeo.w3_bluetoothscanner

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), BleWrapper.BleCallback {
    private var scanResults: HashMap<String, ScanResult>? = null

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var isScanning: Boolean = false
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null
//    private var bluetoothGatt: BluetoothGatt? = null

    private var resultListAdapter: ResultListAdapter? = null

    private var heartRateResults = ArrayList<Int>()

//    val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
//    val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
//    val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)

    companion object {
        const val SCAN_INTERVAL: Long = 3000
        const val EXTRA_MESSAGE = "HEART_RATE_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothManager=  getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        btn_startScan.setOnClickListener {
            Log.d("BluetoothTest", "button 'start scan' pressed")
            if (hasPermission()) startBluetoothScan()
            if (resultListAdapter == null) {
                resultListAdapter = ResultListAdapter(this,scanResults!!)
                lv_scan_result.adapter = resultListAdapter
            }
        }

        lv_scan_result.setOnItemClickListener { parent, _, position, _ ->
            val selectedResult: ScanResult = parent.getItemAtPosition(position) as ScanResult
            val bluetoothDevice = selectedResult.device
//            val gattCallback = BluetoothGattCallback()
//            bluetoothGatt = bluetoothDevice.connectGatt(this, false, gattCallback)
            val mBluetoothWrapper = BleWrapper(this, bluetoothDevice.address)
            mBluetoothWrapper.addListener(this)
            mBluetoothWrapper.connect(false)

        }

        tv_heartRate.setOnClickListener {
            if (heartRateResults.size < 2) {
                Toast.makeText(this, "Not enough data! Waiting for more results...", Toast.LENGTH_SHORT).show()
            } else {
                //create the intent and save data in it
                val graphIntent = Intent(this, GraphActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, heartRateResults)
                }
                startActivity(graphIntent)

            }
        }
    }

    private fun startBluetoothScan() {
        Log.d("BluetoothTest", "Scan started")

        //Initiate scan result hash map
        scanResults = HashMap()

        //Initiate scan callback and bluetooth scanner
        scanCallback = BleScanCallBack()
        bluetoothLeScanner = bluetoothAdapter!!.bluetoothLeScanner

        //Define settings and filter for scanner
        val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build()
        val scanFilter: List<ScanFilter>? = null

        //Stops scanning after the scan interval
        val handler = Handler()
        handler.postDelayed({stopBluetoothScan()}, SCAN_INTERVAL)

        isScanning = true
        bluetoothLeScanner!!.startScan(scanFilter, scanSettings, scanCallback)

    }

    private fun stopBluetoothScan() {
        bluetoothLeScanner!!.stopScan(scanCallback)
        isScanning = false
        Log.d("BluetoothTest", "Scan stopped")
    }

    private fun hasPermission(): Boolean {
        if(bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            Log.d("BluetoothTest", "No Bluetooth LE capability")
            return false
        } else if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("BluetoothTest", "No coarse location access")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return true
        }
        return true
    }

    inner class BleScanCallBack: ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Log.d("BluetoothTest", "Scan failed. Error code: $errorCode")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            if(results != null) {
                for (result in results) {
                    addScanResult(result)
                }
            }
            else Log.d("BluetoothTest", "Batch results returned NULL")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if(result != null) {
                addScanResult(result)
            }
            else Log.d("BluetoothTest", "Result returned NULL")
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceAddress = device.address
            scanResults!![deviceAddress] = result
            Log.d("BluetoothTest", result.device.address)
            if(resultListAdapter != null) {
                resultListAdapter!!.updateResults(scanResults!!)
                resultListAdapter!!.notifyDataSetChanged()
            }
        }
    }

    //Implements BleCallback methods
    override fun onDeviceReady(gatt: BluetoothGatt) {
        Toast.makeText(this, "Device ready.", Toast.LENGTH_SHORT).show()
    }

    override fun onDeviceDisconnected() {
        Toast.makeText(this, "Device disconnected.", Toast.LENGTH_SHORT).show()
    }

    override fun onNotify(characteristic: BluetoothGattCharacteristic) {
        val heartRate: Int = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)
        heartRateResults.add(heartRate)
        tv_heartRate.text = "$heartRate bpm"

    }

}
