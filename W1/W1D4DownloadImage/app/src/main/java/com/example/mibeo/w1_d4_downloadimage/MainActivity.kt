package com.example.mibeo.w1_d4_downloadimage

import android.graphics.Bitmap
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.graphics.BitmapFactory
import kotlinx.android.synthetic.main.activity_main.image
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DownloadImagesTask{
            image.setImageBitmap(it)
        }.execute(URL("https://scontent-arn2-1.xx.fbcdn.net/v/t1.0-9/34140934_2085175195027566_1227945486527234048_n.jpg?_nc_cat=0&oh=c95862d60f4694170a2dadb8c8b9b931&oe=5C63003B"))
    }

    class DownloadImagesTask(private val callback: (Bitmap) -> Unit) : AsyncTask<URL, Void, Bitmap>() {
        override fun onPostExecute(result: Bitmap?) {
            result?.let {
                callback(it)
            }
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg params: URL): Bitmap? {
            return downloadImage(params[0])
        }

        private fun downloadImage(url: URL): Bitmap? {
            return try {
                (url.openConnection() as? HttpURLConnection)
                        ?.inputStream
                        ?.let {
                            BitmapFactory.decodeStream(it)
                        }

            } catch (e: Throwable) {
                null
            }
        }
    }
}
