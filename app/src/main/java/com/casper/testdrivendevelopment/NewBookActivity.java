package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewBookActivity extends AppCompatActivity {
     private Button buttonOk,buttonCancel;
    private EditText editTextBookTitle,editTextBookPrice;
     int insertPosition;
        @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);
            buttonCancel=(Button)findViewById(R.id.button_cancel);
            buttonOk=(Button)findViewById(R.id.button_ok);
            editTextBookPrice=(EditText)findViewById(R.id.editText_book_price);
            editTextBookTitle=(EditText)findViewById(R.id.editText_book_title);

            editTextBookPrice.setText(getIntent().getDoubleExtra("book_price",0)+"");
            editTextBookTitle.setText(getIntent().getStringExtra("book_title"));
            insertPosition=getIntent().getIntExtra("edit_position",0);

            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("edit_position",insertPosition);
                    intent.putExtra("book_title",editTextBookTitle.getText().toString().trim());
                    intent.putExtra("book_price",Double.parseDouble(editTextBookPrice.getText().toString()));
                    setResult(RESULT_OK,intent);
                    NewBookActivity.this.finish();
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewBookActivity.this.finish();
                }
            });
    }
}
