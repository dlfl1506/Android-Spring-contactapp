package com.cos.androidtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.cos.androidtest.adapter.ContactAdapter;
import com.cos.androidtest.model.Contact;
import com.cos.androidtest.model.CMRespDto;
import com.cos.androidtest.service.ContactService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView rvContactList;
    private ContactAdapter contactAdapter;
    private FloatingActionButton addContact;
    private ContactService contactService = ContactService.retrofit.create(ContactService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        contactsListload();
        addContact();
    }


    public void init(){
        addContact = findViewById(R.id.btn_ContactAdd);
        rvContactList = findViewById(R.id.rv_Contactlist);
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvContactList.setLayoutManager(manager);

        contactAdapter = new ContactAdapter(this);
        rvContactList.setAdapter(contactAdapter);
    }
    public void contactsListload(){
        Call<CMRespDto<List<Contact>>> call = contactService.findAll();
        call.enqueue(new Callback<CMRespDto<List<Contact>>>() {
            @Override
            public void onResponse(Call<CMRespDto<List<Contact>>> call, Response<CMRespDto<List<Contact>>> response) {
                CMRespDto<List<Contact>> cmRespDto = response.body();
                List<Contact> contacts = cmRespDto.getData();
                // 어댑터에게 넘기기
                Log.d(TAG, "onResponse: " + contacts);
                contactAdapter.setContacts(contacts);
            }
            @Override
            public void onFailure(Call<CMRespDto<List<Contact>>> call, Throwable t) {
                Log.d(TAG, "onFailure: findAll() 실패");
            }
        });
    }

    public void addContact(){
        addContact.setOnClickListener(v -> {
            View dialog = v.inflate(v.getContext(), R.layout.dialog_item, null);
            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());

            TextInputEditText tiname = dialog.findViewById(R.id.ti_name);
            TextInputEditText tiPhone = dialog.findViewById(R.id.ti_phone);

            dlg.setTitle("연락처 등록");
            dlg.setView(dialog);
            dlg.setPositiveButton("등록", (dialog1, which) -> {
                    saveContact(new Contact(null,tiname.getText().toString(),tiPhone.getText().toString()));
                });
            dlg.setNegativeButton("닫기", null);
            dlg.show();
        });
    }

    public void saveContact(Contact contact){
        Call<CMRespDto<Contact>> call = contactService.save(contact);
        call.enqueue(new Callback<CMRespDto<Contact>>() {
            @Override
            public void onResponse(Call<CMRespDto<Contact>> call, Response<CMRespDto<Contact>> response) {
                CMRespDto<Contact> cmRespDto = response.body();
                if(cmRespDto.getCode()==1){
                    contactAdapter.addItem(cmRespDto.getData());
                }else{
                    Log.d(TAG, "onResponse: save 실패");
                }
            }
            @Override
            public void onFailure(Call<CMRespDto<Contact>> call, Throwable t) {
                Log.d(TAG, "onFailure: 실패");
            }
        });
    }

    public void updateContact(Long id, Contact contact, int position){
        Call<CMRespDto<Contact>> call =contactService.update(id,contact);
        call.enqueue(new Callback<CMRespDto<Contact>>() {
            @Override
            public void onResponse(Call<CMRespDto<Contact>> call, Response<CMRespDto<Contact>> response) {
                CMRespDto<Contact> cmRespDto = response.body();
                if(cmRespDto.getCode() == 1){
                    contactAdapter.updateItem(position,cmRespDto.getData());
                }else{
                    Log.d(TAG, "onResponse: update 실패");
                }
            }
            @Override
            public void onFailure(Call<CMRespDto<Contact>> call, Throwable t) {
                Log.d(TAG, "onFailure: update 실패");
            }
        });
    }

    public void deleteContact(Long id, int position){
        Call<CMRespDto> call = contactService.delete(id);
        call.enqueue(new Callback<CMRespDto>() {
            @Override
            public void onResponse(Call<CMRespDto> call, Response<CMRespDto> response) {
                CMRespDto<Contact> cmRespDto= response.body();
                if(cmRespDto.getCode() == 1){
                    contactAdapter.removeItem(position);
                }else{
                    Log.d(TAG, "onResponse: delete 실패");
                }
            }
            @Override
            public void onFailure(Call<CMRespDto> call, Throwable t) {
                Log.d(TAG, "onFailure: delete 실패");
            }
        });
    }
}