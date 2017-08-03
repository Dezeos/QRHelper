package comindmytroskoryk.linkedin.ua.qrhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dem on 16.06.2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<ContactDO> infoAboutContacts;
    static Activity listActivity;

    String nameContact = "";
    String numberContact = "";
    String emailContact = "";


    public RecyclerAdapter(ArrayList<ContactDO> contactDOs, ListOfContacts listOfContacts) {

        infoAboutContacts = contactDOs;
        listActivity = listOfContacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameCont;
        public TextView numberCont;
        public TextView emailCont;
        public Button  btnContac;


        public ViewHolder(View v) {

            super(v);
            nameCont = (TextView) v.findViewById(R.id.nameCont);
            numberCont = (TextView) v.findViewById(R.id.numberCont);
            emailCont = (TextView) v.findViewById(R.id.emailCont);
            btnContac = (Button) v.findViewById(R.id.btnContac);

        }



    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout, parent, false);

        ViewHolder vh = new ViewHolder(item);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {

        ContactDO contactDO = infoAboutContacts.get(position);

        holder.nameCont.setText(contactDO.getName());
        nameContact = contactDO.getName();

        if (contactDO.getContactNumber().isEmpty()){
            holder.numberCont.setText("номер отсутствует");
            numberContact = "";
        }
        else {
            holder.numberCont.setText(contactDO.getContactNumber());
            numberContact = contactDO.getContactNumber();
        }

        if (contactDO.getEmail().isEmpty()){
            holder.emailCont.setText("email отсутствует");
            emailContact = "";
        }
        else {
            holder.emailCont.setText(contactDO.getEmail());
            emailContact = contactDO.getEmail();
        }
        holder.btnContac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img = new Intent(listActivity,ImageQR.class);
                img.putExtra("name", nameContact);
                img.putExtra("number", numberContact);
                img.putExtra("email", emailContact);
                listActivity.startActivity(img);

            }
        });



    }

    @Override
    public int getItemCount() {
        return infoAboutContacts == null ? 0 : infoAboutContacts.size();
    }
}
