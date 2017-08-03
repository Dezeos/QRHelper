package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.auron.library.mecard.MeCard;
import it.auron.library.vcard.VCard;

public class ImageQR extends AppCompatActivity {


    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;
    final String DIR_SD2 = "QR helper";

    ImageView imageView;
    Button cansSave;
    Button saveQqImg;
    String str = "";

    String nameContact = "" ;
    String numberContact = "" ;
    String emailContact = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_qr);

        cansSave = (Button)findViewById(R.id.CanselImage) ;
        saveQqImg = (Button)findViewById(R.id.SaveImage) ;

        Intent intent = getIntent();

        if (intent.hasExtra("ImageInfo")){

            str =  intent.getStringExtra("ImageInfo");

        }

        else {

            nameContact = intent.getStringExtra("name");
            numberContact = intent.getStringExtra("number");
            emailContact = intent.getStringExtra("email");

            VCard vCard = new VCard();

            vCard.setName(intent.getStringExtra("name"));
            vCard.addTelephone(intent.getStringExtra("number"));
            vCard.addEmail(intent.getStringExtra("email"));

            str = vCard.buildString();

            Log.d("228", str);

        }


        //str =  intent.getStringExtra("contact");
        imageView = (ImageView) findViewById(R.id.EnterText);

        try {
            Bitmap bitmap = encodeAsBitmap(str);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        cansSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveQqImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                saveImageFile(bm);
                Intent resultOfedit = new Intent();
                resultOfedit.putExtra("OK","Данные сохранены");
                setResult(RESULT_OK,resultOfedit);
                finish();
            }
        });

    }


    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }


    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File sdPath2 = Environment.getExternalStorageDirectory();
        sdPath2 = new File(sdPath2.getAbsolutePath() + "/" + DIR_SD2 + "/" + "QR`s image");
        if (!sdPath2.exists()) {
            sdPath2.mkdirs();
        }
        String uriSting = (sdPath2.getAbsolutePath() + "/"
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + ".png");
        return uriSting;
    }
}
