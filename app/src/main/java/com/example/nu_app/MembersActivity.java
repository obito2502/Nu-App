package com.example.nu_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nu_app.adapters.MemberAdapter;
import com.example.nu_app.models.Member;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MembersActivity extends AppCompatActivity implements OnEditListener, OnDeleteListener {


    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    private MemberAdapter memberAdapter;
    private List<Member> listPerson;

    private final int REQUEST_CODE_EDIT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        listPerson = new ArrayList<>();
        for(int i=0;i<3;i++){
            listPerson.add(new Member("F" + (i+1), "L" + (i+1), "O"+(i+1)));
        }

        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);

        memberAdapter = new MemberAdapter(listPerson, this, this);
        recyclerView.setAdapter(memberAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));

        setFloatingActionButtonListener();

    }

    private void setFloatingActionButtonListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                Intent intent = new Intent(MembersActivity.this, EditActivity.class);
                intent.putExtra(Constants.PERSON_INTENT_EDIT, false);
                MembersActivity.this.startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
    }


    @Override
    public void deleteItem(Member person) {
        listPerson.remove(person);
        memberAdapter.notifyDataSetChanged();
    }

    @Override
    public void editItem(Member person, int index) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Constants.PERSON_INTENT_EDIT, true);
        intent.putExtra(Constants.PERSON_INTENT_INDEX, index);
        intent.putExtra(Constants.PERSON_INTENT_OBJECT, person);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_EDIT){
            if(resultCode == RESULT_OK){
                if(data == null){
                    return;
                }
                boolean isEdit = data.getBooleanExtra(Constants.PERSON_INTENT_EDIT, false);
                Member person = data.getParcelableExtra(Constants.PERSON_INTENT_OBJECT);
                if(isEdit){
                    int index = data.getIntExtra(Constants.PERSON_INTENT_INDEX, -1);
                    if(index == -1){
                        return;
                    }
                    listPerson.set(index, person);
                }else{
                    listPerson.add(person);
                }
                memberAdapter.notifyDataSetChanged();
            }
        }
    }


}