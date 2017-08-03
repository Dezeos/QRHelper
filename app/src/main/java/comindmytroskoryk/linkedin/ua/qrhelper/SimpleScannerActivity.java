package comindmytroskoryk.linkedin.ua.qrhelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.auron.library.mecard.MeCard;
import it.auron.library.mecard.MeCardParser;
import it.auron.library.vcard.VCard;
import it.auron.library.vcard.VCardParser;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends Activity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    DB db;

    String unswer = "";
    String name = "";
    ArrayList<String> emails;
    ArrayList<String> numbers;
    String number = "";
    String email = "";



    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {

        Log.v("myLog", rawResult.getContents()); // Prints scan results
        Log.v("myLog", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)


        db = new DB(this);
        unswer = rawResult.getContents();

        Log.d("228", unswer);

        if (!(unswer.contains("VCARD")|unswer.contains("MECARD")) & (unswer.contains("http://") | unswer.contains("https://"))){
           // unswer = rawResult.getContents();
            Log.v("myLog", "FFFFFFFFFFFFF"); // Prints scan results

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Содержимое");
            builder.setMessage(rawResult.getContents());
            builder.setCancelable(true);
            builder.setPositiveButton("Открыть в браузере", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(unswer));
                    startActivity(browserIntent);
                    db.insertDATA(new History(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                            format(Calendar.getInstance()
                                    .getTime()),"считан QR содержащий ссылку - " + unswer + " и открыт в браузере"));
                    db.closeDB();
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                dialog.dismiss();
                    db.insertDATA(new History(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                            format(Calendar.getInstance()
                                    .getTime()),"считан QR содержащий ссылку - " + unswer + " но открытие отменено пользователем"));
                    db.closeDB();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if (unswer.contains("VCARD")){

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Содержимое");
                builder.setMessage(unswer = rawResult.getContents());
                builder.setPositiveButton("Добавить контакт", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Log.d("228", unswer);
                    String parseVcard = unswer;

                    VCard vCard = VCardParser.parse(parseVcard);

                    if (!vCard.getName().isEmpty()){

                        name = vCard.getName();
                    }


                    if (!vCard.getEmails().isEmpty()){

                        for (String email2 : vCard.getEmails()) {
                            emails = new ArrayList<String>();
                            emails.add(email2);
                        }

                    }

                    if (!vCard.getTelephones().isEmpty()){

                        numbers = new ArrayList<String>();

                        for (String number2 : vCard.getTelephones()) {

                            numbers.add(number2);
                        }

                    }

                    addContact(name,numbers,emails);

                    name = "";
                    numbers.clear();
                    emails.clear();

                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


        }
        else if (unswer.contains("MECARD")){


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Содержимое");
            builder.setMessage(unswer = rawResult.getContents());
            builder.setPositiveButton("Добавить контакт", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String parseMEcard = unswer;
                    MeCard meCard = MeCardParser.parse(parseMEcard);

                    Log.d("228", meCard.getName() + " "  + meCard.getEmail() + " " + meCard.getTelephones());


                    if (!meCard.getName().isEmpty()){

                        name = meCard.getName();
                    }


                    if (!meCard.getEmail().isEmpty()){

                        emails = new ArrayList<String>();
                        emails.add(meCard.getEmail());

                    }

                    if (!meCard.getTelephones().isEmpty()){

                        numbers = new ArrayList<String>();
                        for (String phoneNumber : meCard.getTelephones()) {
                            numbers.add(phoneNumber);
                        }

                    }

                    addContact(name,numbers,emails);

                    name = "";
                    numbers.clear();
                    emails.clear();
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }

        else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Содержимое");
            builder.setMessage(unswer = rawResult.getContents());
            builder.setPositiveButton("Редактировать", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(SimpleScannerActivity.this, RedactResult.class);
                    intent.putExtra("redact", unswer);
                    startActivityForResult(intent,1);
                }
            });
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }


        //AlertDialog alert1 = builder.create();
        //alert1.show();

        //If you would like to resume scanning, call this method below:

        //mScannerView.stopCameraPreview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            Log.v("myLog", rawResult.getContents());

           */

        Log.d("myLog", String.valueOf(requestCode));

        if (resultCode == RESULT_OK) {

            Toast resultOfEdit = Toast.makeText(SimpleScannerActivity.this, data.getStringExtra("OK"),Toast.LENGTH_LONG);
            resultOfEdit.setGravity(Gravity.CENTER,0,0);
            resultOfEdit.show();

            }

        else {

            Toast resultOfEdit = Toast.makeText(SimpleScannerActivity.this, "Данные не были сохранены",Toast.LENGTH_LONG);
            resultOfEdit.setGravity(Gravity.CENTER,0,0);
            resultOfEdit.show();

        }
    }

    public void addContact (String name, ArrayList<String> nums, ArrayList<String> ems){

        ContentResolver cr = this.getContentResolver();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (name != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            name).build());
        }


        //------------------------------------------------------ Mobile Number
        if (!nums.isEmpty()) {
            for (String oneNumber : nums) {
                ops.add(ContentProviderOperation.
                        newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, oneNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }

        }
        else {

        }


        //------------------------------------------------------ Email
        if (!ems.isEmpty()) {
            for (String oneEmail : ems) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, oneEmail)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

        }



        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
    }


}
