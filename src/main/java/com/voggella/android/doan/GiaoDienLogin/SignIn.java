package com.voggella.android.doan.GiaoDienLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;

public class SignIn extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        Button backHome = findViewById(R.id.backLogin);
        backHome.setOnClickListener(view -> {
            Intent intentHome = new Intent(SignIn.this, Login.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
            finish();
        });

        Button changePass = findViewById(R.id.changepass);
        changePass.setOnClickListener(view -> {
            Intent intentChangePass = new Intent(SignIn.this, ChangePass.class);
            intentChangePass.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentChangePass);
            finish();
        });

        EditText edtPhone = findViewById(R.id.user_Sdt);
        EditText edtPass = findViewById(R.id.pass_SignIn);
        Button signIn = findViewById(R.id.user_SignIn);

        signIn.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(SignIn.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteHelper dbHelper = new SQLiteHelper(SignIn.this);

            try {
                boolean isValid = dbHelper.validateUser(phone, pass);
                if (isValid) {
                    Intent intent = new Intent(SignIn.this, ScreenWait.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignIn.this, "Sai số điện thoại hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(SignIn.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
