package com.cos.androidtest.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cos.androidtest.MainActivity;
import com.cos.androidtest.R;
import com.cos.androidtest.model.CMRespDto;
import com.cos.androidtest.model.Contact;
import com.cos.androidtest.service.ContactService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private static final String TAG = "ContactAdapter";
    private List<Contact> contacts;
    private MainActivity mContext;

    public ContactAdapter(MainActivity mContext) {
        this.mContext = mContext;
        this.contacts=new ArrayList<>();
    }

    public void setContacts(List<Contact> contacts){
        this.contacts =contacts;
        notifyDataSetChanged();
    }

    public void addItem(Contact contact){
        contacts.add(contact);
        notifyDataSetChanged();
    }

    public Contact getItem(int position){
        return contacts.get(position);
    }

    public void removeItem(int position) {
        contacts.remove(position);
        notifyDataSetChanged();
    }


    public void updateItem(int position, Contact contact){
        contacts.get(position).setId(contact.getId());
        contacts.get(position).setName(contact.getName());
        contacts.get(position).setTel(contact.getTel());
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);   // main엑티비티에 연결할 객체를 생성해주는 인플레이터
        View view = inflater.inflate(R.layout.contact_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setItem(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout contactList;
        private TextInputEditText tiName;
        private TextInputEditText tiPhone;
        private TextView tvname, tvphone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tv_name);
            tvphone = itemView.findViewById(R.id.tv_phone);
            contactList = itemView.findViewById(R.id.contact_list);

            contactList.setOnClickListener(v -> {
                View dialog = v.inflate(v.getContext(), R.layout.dialog_item, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
                int pos = getAdapterPosition();
                Long id = getItem(getAdapterPosition()).getId();

                        tiName = dialog.findViewById(R.id.ti_name);
                        tiPhone = dialog.findViewById(R.id.ti_phone);
                        tiName.setText(contacts.get(pos).getName());
                        tiPhone.setText(contacts.get(pos).getTel());

                        dlg.setTitle("연락처 수정");
                        dlg.setView(dialog);

                        dlg.setNegativeButton("수정", (dialog1,which)->{
                            mContext.updateContact(id,new Contact(null,tiName.getText().toString(),tiPhone.getText().toString()),pos);
                        });
                        dlg.setPositiveButton("삭제", (dialog1, which) -> {
                            mContext.deleteContact(id,pos);
                        });
                        dlg.show();
                    });
        }
        public void setItem(Contact contact) {
            tvname.setText(contact.getName());
            tvphone.setText(contact.getTel());
        }

    }
}
