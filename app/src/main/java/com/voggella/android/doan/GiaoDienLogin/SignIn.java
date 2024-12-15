package com.voggella.android.doan.GiaoDienLogin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.voggella.android.doan.Database.SQLiteHelper;
import com.voggella.android.doan.R;
import com.voggella.android.doan.mainHome.mainScreen;

public class SignIn extends AppCompatActivity {
    private EditText edtPhone, edtPassword;
    private SQLiteHelper dbHelper;
    
    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        
        dbHelper = new SQLiteHelper(this);
        
        edtPhone = findViewById(R.id.user_Sdt);
        edtPassword = findViewById(R.id.pass_SignIn);
        
        Button backHome = findViewById(R.id.backLogin);
        backHome.setOnClickListener(view -> {
            Intent intentHome = new Intent(SignIn.this, Login.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentHome);
            finish();
        });

        Button signIn = findViewById(R.id.user_SignIn);
        signIn.setOnClickListener(v -> {
            checkLogin();
        });

        Button changePass = findViewById(R.id.changepass);
        changePass.setOnClickListener(view -> {
            String phone = edtPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(SignIn.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intentChangePass = new Intent(SignIn.this, ChangePass.class);
            intentChangePass.putExtra("USERS_SDT", phone);
            intentChangePass.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentChangePass);
        });
    }

    private void checkLogin() {
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Log.d("SignIn", "Attempting login with - Phone: " + phone + ", Password: " + password);

        if (phone.equals("admin") && password.equals("admin")) {
            Log.d("SignIn", "Admin login successful, launching AdminManagementActivity");
            try {
                Intent adminIntent = new Intent(SignIn.this, AdminManagementActivity.class);
                startActivity(adminIntent);
                finish();
            } catch (Exception e) {
                Log.e("SignIn", "Error launching admin activity: " + e.getMessage());
                Toast.makeText(this, "Lỗi khi mở trang Admin", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
            SQLiteHelper.TB_USERS,
            new String[]{SQLiteHelper.COLUMN_USER_SDT, SQLiteHelper.COLUMN_USER_PASSWORD},
            SQLiteHelper.COLUMN_USER_SDT + "=?",
            new String[]{phone},
            null, null, null
        );

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(
                cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_PASSWORD));
            
            if (password.equals(storedPassword)) {
                Intent intent = new Intent(SignIn.this, mainScreen.class);
                intent.putExtra("USERS_SDT", phone);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
