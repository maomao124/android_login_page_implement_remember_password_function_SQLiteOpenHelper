package mao.android_login_page_implement_remember_password_function_sqliteopenhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener
{

    private String phone;
    private String verifyCode;
    private EditText et_password_first;
    private EditText et_password_second;
    private EditText et_verifyCode;

    public static final String TAG = "forget_password_page";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        et_password_first = findViewById(R.id.et_password_first);
        et_password_second = findViewById(R.id.et_password_second);
        et_verifyCode = findViewById(R.id.et_verifycode);
        // 从上一个页面获取要修改密码的手机号码
        phone = getIntent().getStringExtra("phone");

        findViewById(R.id.btn_verifycode).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        et_password_first.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() == 6)
                {
                    // 隐藏输入法软键盘
                    closeInput(MainActivity2.this, et_password_first);
                }
            }
        });

        et_password_second.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() == 6)
                {
                    // 隐藏输入法软键盘
                    closeInput(MainActivity2.this, et_password_second);
                }
            }
        });

        et_verifyCode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() == 6)
                {
                    // 隐藏输入法软键盘
                    closeInput(MainActivity2.this, et_verifyCode);
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btn_verifycode)
        {
            // 点击了“获取验证码”按钮
            clickVerifyCode();
        }
        else if (v.getId() == R.id.btn_confirm)
        {
            // 点击了“确定”按钮
            clickConfirm();
        }
        else
        {
            Log.w(TAG, "onClick: error");
        }

    }

    /**
     * 处理点击确认按钮
     */
    private void clickConfirm()
    {
        String password_first = et_password_first.getText().toString();
        String password_second = et_password_second.getText().toString();
        if (password_first.length() < 6)
        {
            Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password_first.equals(password_second))
        {
            Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if (verifyCode == null)
        {
            Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!verifyCode.equals(et_verifyCode.getText().toString()))
        {
            Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
        // 以下把修改好的新密码返回给上一个页面
        Intent intent = new Intent();
        intent.putExtra("new_password", password_first);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * 处理点击验证码按钮
     */
    @SuppressLint("DefaultLocale")
    private void clickVerifyCode()
    {
        // 生成六位随机数字的验证码
        verifyCode = String.format("%06d", new Random().nextInt(999999));
        // 以下弹出提醒对话框，提示用户记住六位验证码数字
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请记住验证码");
        builder.setMessage("手机号" + phone + ",本次验证码是" + verifyCode + ",请输入验证码");
        builder.setPositiveButton("好的", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * 关闭(隐藏)输入法
     *
     * @param activity 活动
     * @param view     视图
     */
    public void closeInput(Activity activity, View view)
    {
        //从系统服务中获取输入法管理器
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //关闭屏幕上的输入法软键盘
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}