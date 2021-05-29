package com.jjaln.dailychart.contents.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.jjaln.dailychart.R;
import com.jjaln.dailychart.feature.Category;
import com.jjaln.dailychart.feature.Question;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommunityWriteActivity extends AppCompatActivity {
    private ImageView ivBack;
    private AutoCompleteTextView autoCompleteTextView;
    private AppCompatButton btnSaveCommunity;
    public List<String> category;
    private List<Category> categories;
    private TextInputEditText etTitle;
    private TextInputEditText etContents;
    private DatabaseReference mDatabase;

    private RichWysiwyg wysiwyg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_community_write);

        wysiwyg = findViewById(R.id.richwysiwygeditor);

        wysiwyg.getContent()
                .setEditorFontSize(18)
                .setEditorPadding(4, 0, 4, 0);


    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            List<Image> images = ImagePicker.getImages(data);
            insertImages(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insertImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath()).append("\n");
            // Handle this
            wysiwyg.getContent().insertImage("file://" + images.get(i).getPath(), "alt");
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(v -> {
            finish();
        });

        autoCompleteTextView = findViewById(R.id.autoCompleteText);
        btnSaveCommunity = findViewById(R.id.btn_save_community);
        etTitle = findViewById(R.id.et_title);
        etContents = findViewById(R.id.et_content);
        category = CommunityActivity.category;
        categories = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.community_category_item, category);

        autoCompleteTextView.setText("카테고리 선택", true);
        autoCompleteTextView.setAdapter(arrayAdapter);

        btnSaveCommunity.setOnClickListener(v -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            String token = pref.getString("token", "");
            String username = pref.getString("username","");
            String flag = autoCompleteTextView.getText().toString();
            if (etTitle.getText().toString().equals("") | etContents.getText().toString().equals(""))
                Toast.makeText(this, "Check your Input", Toast.LENGTH_SHORT).show();
            else {
                if (category.indexOf(flag) != -1) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Question question = new Question();
                    question.setTitle(etTitle.getText().toString());
                    question.setContent(wysiwyg.getContent().getHtml());
                    question.setToken(token);
                    question.setCategoryName(autoCompleteTextView.getText().toString());
                    question.setCategoryId(category.indexOf(question.getCategoryName()));
                    question.setUsername(username);
                    question.setDBKey(mDatabase.child(autoCompleteTextView.getText().toString()).push().getKey());
                    question.setDate(new Date());
                    mDatabase.child("Post").
                            child(autoCompleteTextView.getText().toString()).
                            child(question.getDBKey()).setValue(question);
                    mDatabase.child("Search").child(question.getDBKey()).setValue(question);
                    finish();
                } else {
                    Toast toast = Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}