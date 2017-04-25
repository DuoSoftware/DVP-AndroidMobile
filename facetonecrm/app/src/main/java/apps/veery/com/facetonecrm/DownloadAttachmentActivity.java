package apps.veery.com.facetonecrm;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class DownloadAttachmentActivity extends AppCompatActivity {

    ImageView preview;
    String downloadLink,fileName;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(DownloadAttachmentActivity.this,"File downloaded to Download/FacetoneCRM.",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_attachment);

        downloadLink = getIntent().getStringExtra(TicketDetailActivity.DOWNLOAD_URL);
        fileName = getIntent().getStringExtra(TicketDetailActivity.FILE_NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ticketdeatials);
        toolbar.setTitle(fileName);

        setSupportActionBar(toolbar);

        this.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        preview = (ImageView) findViewById(R.id.imagePreview);
        Picasso.with(this).load(downloadLink)
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.error_loading)
                .into(preview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_download_attachment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        downloadFile(downloadLink);
        return super.onOptionsItemSelected(item);
    }

    private void downloadFile(String uRl) {
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/FacetoneCRM");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setDescription("Downloading file...")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS+ "/FacetoneCRM", fileName);

        mgr.enqueue(request);
//        mgr.addCompletedDownload("Download completed","File downloaded", true, "image/png"
//                ,Environment.DIRECTORY_DOWNLOADS+ "/FacetoneCRM",200,true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }
}
