package com.sushamna.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private int position;
    private String itemValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        position = getIntent().getIntExtra("position", 0);
        itemValue = getIntent().getStringExtra("itemValue");

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemValue);
    }

    public void onEditItem(View view) {
        String fieldValue = etEditItem.getText().toString();

        Intent i = new Intent();
        i.putExtra("newFieldValue", fieldValue);
        i.putExtra("position", position);

        setResult(RESULT_OK, i);
        finish();
    }
}
