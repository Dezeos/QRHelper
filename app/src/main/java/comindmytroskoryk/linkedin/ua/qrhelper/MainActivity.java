package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button scannerQR;
    Button qreateQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerQR = (Button) findViewById(R.id.scannerQR);
        scannerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleScannerActivity.class);
                startActivity(intent);
            }
        });

        qreateQR = (Button) findViewById(R.id.createQR);
        qreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QreateQRActivity.class);
                startActivity(intent);
            }
        });
    }
}
