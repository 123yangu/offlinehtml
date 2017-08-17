package test.ccy.com.offlinehtml;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        AndroidWebClient client = new AndroidWebClient();
        webView.setWebViewClient(client);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        webView.loadUrl("http://192.168.8.244:8087/rmc/rest/api/exam/examList?sessionid=kJfBw5KQlsrexpGRw97HxpCS3pHGxMTeysWRlpbLwcCRl8aWwMvJwpfJl8rJkMLJw5HJw8I=&macid=38:1d:d9:c1:0b:01&patientId=3163832&vid=1&id=&height=663&width=1024&mecdevice=1");
        loadArchive();
    }


    private class AndroidWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
        }
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.saveWebArchive(Environment.getExternalStorageDirectory()
                    + File.separator +"myArchive"+".mht");
            // our webarchive wull be available now at the above provided location with name "myArchive"+".mht"

        }
        public void onLoadResource(WebView view, String url) {

        }
    }

    private void loadArchive(){
        String rawData = null;
        try {
            rawData =   getStringFromFile(Environment.getExternalStorageDirectory()
                    + File.separator+"myArchive"+".mht");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            webView.loadUrl("file:///"+Environment.getExternalStorageDirectory()
                    + File.separator+"myArchive"+".mht");
        }else {
            webView.loadDataWithBaseURL(null, rawData, "application/x-webarchive-xml", "UTF-8", null);
        }
    }

    public String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public  String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
