package com.example.nu_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nu_app.models.Member;

public class EditActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextOccupation;
    private Button buttonEdit;
    private Button buttonCancel;
    private Member member;
    private int index = -1;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupViews();
        initPerson();
        setButtonCancelListener();
        setButtonEditListener();
    }


    private void setupViews(){
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonCancel = findViewById(R.id.buttonCancel);
    }

    private void initPerson() {
        Intent intent = getIntent();
        if(intent != null){
            isEdit = intent.getBooleanExtra(Constants.PERSON_INTENT_EDIT, false);
            if(isEdit){
                member = intent.getParcelableExtra(Constants.PERSON_INTENT_OBJECT);
                index = intent.getIntExtra(Constants.PERSON_INTENT_INDEX, -1);
                if(index == -1){
                    setResult(RESULT_CANCELED);
                    finish();
                }
                editTextFirstName.setText(member.getFirstName());
                editTextLastName.setText(member.getLastName());
                editTextOccupation.setText(member.getOccupation());
                buttonEdit.setText(getString(R.string.button_edit));
            }else{
                member = new Member();
                buttonEdit.setText(getString(R.string.button_add));
            }
        }
    }

    private void setButtonCancelListener(){
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                EditActivity.this.setResult(RESULT_CANCELED);
                EditActivity.this.finish();
            }
        });
    }

    private void setButtonEditListener(){
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String occupation = editTextOccupation.getText().toString().trim();
                member.setFirstName(firstName);
                member.setLastName(lastName);
                member.setOccupation(occupation);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PERSON_INTENT_OBJECT, member);
                bundle.putBoolean(Constants.PERSON_INTENT_EDIT, isEdit);
                bundle.putInt(Constants.PERSON_INTENT_INDEX, index);
                intent.putExtras(bundle);

                EditActivity.this.setResult(RESULT_OK, intent);
                EditActivity.this.finish();
            }
        });
    }
}