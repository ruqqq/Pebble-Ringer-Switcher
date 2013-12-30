package sg.ruqqq.pebbleprofileswitcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    Button installButton;
    Button installButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        installButton = (Button) findViewById(R.id.button);

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAsset(MainActivity.this, "phone_ringer_v1.pbw", getExternalCacheDir() + "/" + "phone_ringer_v1.pbw");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("com.getpebble.android");
                i.setDataAndType(Uri.fromFile(new File(getExternalCacheDir() + "/" + "phone_ringer_v1.pbw")), "application/octet-stream");
                //i.setData(Uri.parse("https://www.dropbox.com/s/ty9ordu47h2n12j/phone_ringer_v1.pbw?v=0mcn"));
                startActivity(i);
            }
        });

        installButton2 = (Button) findViewById(R.id.button2);

        installButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyAsset(MainActivity.this, "phone_ringer_v2.pbw", getExternalCacheDir() + "/" + "phone_ringer_v2.pbw");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setPackage("com.getpebble.android");
                i.setDataAndType(Uri.fromFile(new File(getExternalCacheDir() + "/" + "phone_ringer_v2.pbw")), "application/octet-stream");
                //i.setData(Uri.parse("https://www.dropbox.com/s/ty9ordu47h2n12j/phone_ringer_v1.pbw?v=0mcn"));
                startActivity(i);
            }
        });
    }

    public static void copyAsset(Context context, String filename, String dest) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            out = new FileOutputStream(dest);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
}
