package com.example.mibeo.w2d2bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var scanResults: HashMap<String, ScanResult>? = null

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var isScanning: Boolean = false
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanCallback: ScanCallback? = null
    private var bluetoothGatt: BluetoothGatt? = null

    private var resultListAdapter: ResultListAdapter? = null

    val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)

    companion object {
        const val SCAN_INTERVAL: Long = 3000
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
            val gattCallback = BluetoothGattCallback()
            bluetoothGatt = bluetoothDevice.connectGatt(this, false, gattCallback)

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

    /*Generates 128-bit UUID from the Protocol Indentifier (16-bit number) and
        the BASE_UUID (00000000-0000-1000-8000-00805F9B34FB) */

    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    inner class BluetoothGattCallback : android.bluetooth.BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status == BluetoothGatt.GATT_FAILURE) {
                Log.d("GattTest", "GATT connection failed")
                return
            } else if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d("GattTest", "GATT connection succeeded")
                return
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("GattTest", "Connected to GATT service")
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("GattTest", "Disconnected to GATT service")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                return
            }

            Log.d("GattTest", "GATT services discovered")
            for (gattService in gatt.services) {
                Log.d("GattTest", "Service: ${gattService.uuid}")

                if (gattService.uuid == HEART_RATE_SERVICE_UUID) {
                    for (gattCharacteristic in gattService.characteristics) {
                        Log.d("GattTest", "Characteristic: ${gattCharacteristic.uuid}")

                        /* setup the system for the notification messages */
                        val characteristic = gatt.getService(HEART_RATE_SERVICE_UUID)
                                .getCharacteristic(HEART_RATE_MEASUREMENT_CHAR_UUID)
                        gatt.setCharacteristicNotification(characteristic, true)
                        val descriptor: BluetoothGattDescriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID)
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(descriptor)
                    }
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            val heartRate: Int = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)
            runOnUiThread {
                tv_heartRate.text = "${heartRate.toString()} bpm"
            }
        }
    }


}