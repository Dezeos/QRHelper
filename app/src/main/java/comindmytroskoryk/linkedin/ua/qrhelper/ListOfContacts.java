package comindmytroskoryk.linkedin.ua.qrhelper;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.support.v7.widget.RecyclerView;

public class ListOfContacts extends AppCompatActivity {

    Button cansel3;
    Button generate2;

    ContentResolver contResv ;

    ArrayList <ContactDO> contactDOs;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_contacts);


        generate2 = (Button) findViewById(R.id.Generate2);
        cansel3 = (Button) findViewById(R.id.Cansel3);

        contResv = getContentResolver();

        contactDOs = getContacts();

        makingList(contactDOs);


        generate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cansel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void makingList( ArrayList <ContactDO> contactDOs){

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerAdapter = new RecyclerAdapter(contactDOs,  ListOfContacts.this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

    }



    public ArrayList<ContactDO> getContacts()
    {

        ArrayList<ContactDO> alContacts = null;
        Cursor cursor = contResv.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            alContacts = new ArrayList<ContactDO>();
            do
            {
                //Create a plain class with following variables - id, name, contactNumber, email
                ContactDO objContactDO = new ContactDO();

                objContactDO.name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                objContactDO.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor emails = contResv.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + objContactDO.id, null, null);
                while (emails.moveToNext())
                {
                    objContactDO.email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    break;
                }
                emails.close();

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = contResv.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ objContactDO.id }, null);
                    while (pCur.moveToNext())
                    {
                        objContactDO.contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        break;
                    }
                    pCur.close();
                }

                alContacts.add(objContactDO);

            } while (cursor.moveToNext()) ;
        }

        cursor.close();
        return alContacts;
    }

}
