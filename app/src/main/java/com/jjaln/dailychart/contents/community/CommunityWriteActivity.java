package com.jjaln.dailychart.contents.community;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.jjaln.dailychart.R;
import com.jjaln.dailychart.feature.Category;
import com.jjaln.dailychart.feature.Question;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_community_write);
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
                    question.setContent(etContents.getText().toString());
                    question.setToken(token);
                    question.setCategoryName(autoCompleteTextView.getText().toString());
                    question.setCategoryId(category.indexOf(question.getCategoryName()));
                    question.setUsername(username);
                    question.setDBKey(mDatabase.child(autoCompleteTextView.getText().toString()).push().getKey());
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