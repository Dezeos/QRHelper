package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class QreateQRActivity extends AppCompatActivity {

    Button canselCreate;
    Button createQR;
    Button createFromContacts;
    EditText enterText;
    String textToGenerate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qreate_qr);

        canselCreate = (Button) findViewById(R.id.Cansel2);
        createQR = (Button) findViewById(R.id.Generate);
        createFromContacts = (Button) findViewById(R.id.Contacts);
        enterText = (EditText) findViewById(R.id.EnterText);

        canselCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        createQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!enterText.getText().toString().isEmpty()) {
                    //textToGenerate = enterText.getText().toString();
                    Intent imageIntent = new Intent(QreateQRActivity.this,ImageQR.class);
                    imageIntent.putExtra("ImageInfo", enterText.getText().toString());
                    startActivityForResult(imageIntent,1);
                }
                else {
                    Toast emptyField= Toast.makeText(QreateQRActivity.this, "Поле для введенния данных пустое!",Toast.LENGTH_LONG);
                    emptyField.setGravity(Gravity.CENTER,0,0);
                    emptyField.show();
                }
            }
        });

        createFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(QreateQRActivity.this,ListOfContacts.class);
                startActivity(imageIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            Toast resultOfEdit = Toast.makeText(QreateQRActivity.this, data.getStringExtra("OK"),Toast.LENGTH_LONG);
            resultOfEdit.setGravity(Gravity.CENTER,0,0);
            resultOfEdit.show();

        }

        else {

            Toast resultOfEdit = Toast.makeText(QreateQRActivity.this, "Данные не были сохранены",Toast.LENGTH_LONG);
            resultOfEdit.setGravity(Gravity.CENTER,0,0);
            resultOfEdit.show();

        }
    }
}