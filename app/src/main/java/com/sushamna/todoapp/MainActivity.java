package com.sushamna.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aTodoAdapter;
    private ListView lvItems;
    private EditText etAddItem;
    private String todoFilename = "todo.txt";
    private final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DeleteItem(position);

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SwitchToEditItemActivity(position);
            }
        });

        etAddItem = (EditText) findViewById(R.id.etAddItem);

        etAddItem.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (IsEnterKey(keyCode) && event.getAction() == KeyEvent.ACTION_UP) {
                    AddItem();
                    return true;
                } else {
                    return false;
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String newFieldValue = data.getExtras().getString("newFieldValue");
            int position = data.getExtras().getInt("position", 0);

            UpdateItem(position, newFieldValue);
        }
    }

    public void populateArrayItems() {
        readItems();

        aTodoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        todoItems = new ArrayList<>();

        File filesDir = getFilesDir();
        File file = new File(filesDir, todoFilename);
        try {
            todoItems = new ArrayList<>(FileUtils.readLines(file));
        } catch (IOException e) {

        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, todoFilename);
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
    }

    private void DeleteItem(int position) {
        todoItems.remove(position);
        aTodoAdapter.notifyDataSetChanged();

        writeItems();
    }

    private void UpdateItem(int position, String newFieldValue) {
        todoItems.set(position, newFieldValue);
        aTodoAdapter.notifyDataSetChanged();

        writeItems();

        Toast.makeText(MainActivity.this, "Todo list updated", Toast.LENGTH_SHORT).show();
    }

    private void AddItem() {
        String fieldValue = etAddItem.getText().toString();
        etAddItem.setText("");
        aTodoAdapter.add(fieldValue);

        writeItems();

        Toast.makeText(this, fieldValue + " added", Toast.LENGTH_SHORT).show();
    }

    public void onAddItem(View view) {
        AddItem();
    }


    private boolean IsEnterKey(int keyCode) {
        return (keyCode == KeyEvent.KEYCODE_ENTER);
    }

    private void SwitchToEditItemActivity(int position) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        i.putExtra("position", position);
        i.putExtra("itemValue", todoItems.get(position));

        startActivityForResult(i, REQUEST_CODE);
    }
}
