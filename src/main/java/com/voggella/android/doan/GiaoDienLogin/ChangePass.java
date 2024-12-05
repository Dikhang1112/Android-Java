package com.voggella.android.doan.GiaoDienLogin;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;

public class ChangePass extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText newPass, confirmNewPass;
    private Button changeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        Button backSignIn = findViewById(R.id.btnSign);
        backSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            // Quay lại đăng nhập
            public void onClick(View view) {
                Intent intentBackSign = new Intent(ChangePass.this, SignIn.class);
                intentBackSign.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentBackSign);
                finish();
            }
        });

        newPass = findViewById(R.id.pass1_change);
        confirmNewPass = findViewById(R.id.pass2_change);
        changeBtn = findViewById(R.id.btnChange);

        // Lấy số điện thoại từ Intent hoặc SharedPreferences
        String sdt = getIntent().getStringExtra("user_sdt");  // Giả sử bạn đã truyền số điện thoại qua Intent

        SQLiteHelper dbHelper = new SQLiteHelper(this);
        db = dbHelper.getWritableDatabase();

        changeBtn.setOnClickListener(v -> {
            String newPassword = newPass.getText().toString();
            String confirmNewPassword = confirmNewPass.getText().toString();

            // Kiểm tra các trường dữ liệu
            if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp.", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật mật khẩu mới vào cơ sở dữ liệu trực tiếp
                ContentValues values = new ContentValues();
                values.put("Users_Password", newPassword);  // Cập nhật mật khẩu mới

                if (db != null) {
                    int rowsAffected = db.update("Users", values, "Users_SDT=?", new String[]{sdt});
                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Mật khẩu đã được thay đổi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Có lỗi xảy ra, không thể thay đổi mật khẩu.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();  // Đảm bảo đóng kết nối cơ sở dữ liệu khi không sử dụng
        }
    }
}
