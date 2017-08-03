package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RedactResult extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    final String DIR_SD = "QR helper";
    final String FILENAME_SD = "fileSD";
    String fileName = "";

    EditText redactResult;
    Button cansel;
    Button save;
    String redact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_result);

        redactResult = (EditText) findViewById(R.id.RedactResult);
        cansel = (Button) findViewById(R.id.Cansel);
        save = (Button) findViewById(R.id.Save);

        Intent intent = getIntent();
        redactResult.setText(intent.getStringExtra("redact"));
        redact = intent.getStringExtra("redact");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFileSD();
                Intent resultOfedit = new Intent();
                resultOfedit.putExtra("OK","Данные сохранены");
                setResult(RESULT_OK,resultOfedit);
                finish();

            }
        });

        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }


    void writeFileSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        Log.d(LOG_TAG, "ПУть к Сд " + Environment.getExternalStorageDirectory());
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу

        //fileName = String.valueOf(System.currentTimeMillis());
        fileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Log.d(LOG_TAG, "File name " + fileName + ".txt");
        File sdFile = new File(sdPath, fileName + ".txt");

        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
