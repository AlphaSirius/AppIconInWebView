package com.example.sunilsahu.myapplication;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = ((WebView) findViewById(R.id.webView));
        String html = "<html><head></head><body> <img src=\"" + getIconPath("com.whatsapp") + "\"> </body></html>";
        webView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

    }

    private String getIconPath(String packageName) {

        try {
            createIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return "file://" + getApplicationContext().getFilesDir().getAbsolutePath().toString() + "/" + getIconFileName(packageName);

    }

    private void createIcon(String packageName) throws PackageManager.NameNotFoundException, IOException {


        File file = new File(getApplicationContext().getFilesDir(), getIconFileName(packageName));
        if (file.exists() && file.isFile()) {

            return;
        }

        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, 0);
        Drawable drawable = getPackageManager().getApplicationIcon(appInfo);
        Bitmap bm = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        OutputStream outStream = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        outStream.flush();
        outStream.close();
    }

    private String getIconFileName(String packageName) {

        return packageName.replace(".", "_").concat(".PNG");
    }
}
