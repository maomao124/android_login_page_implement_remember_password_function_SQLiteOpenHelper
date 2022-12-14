package mao.android_login_page_implement_remember_password_function_sqliteopenhelper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

import mao.android_login_page_implement_remember_password_function_sqliteopenhelper.dao.UserDao;
import mao.android_login_page_implement_remember_password_function_sqliteopenhelper.entity.User;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener
{
    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private CheckBox ck_remember;
    private EditText et_phone;
    private RadioButton rb_password;
    private RadioButton rb_verifyCode;
    private ActivityResultLauncher<Intent> register;
    @SuppressWarnings("all")
    private Button btn_login;
    private String password = "123456";
    private String verifyCode;

    private static final String TAG = "loginPage";
    private RadioGroup rb_login;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rb_login = findViewById(R.id.rg_login);
        tv_password = findViewById(R.id.tv_password);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        btn_forget = findViewById(R.id.btn_forget);
        ck_remember = findViewById(R.id.ck_remember);
        rb_password = findViewById(R.id.rb_password);
        rb_verifyCode = findViewById(R.id.rb_verifycode);
        btn_login = findViewById(R.id.btn_login);
        // ???rg_login?????????????????????
        rb_login.setOnCheckedChangeListener(this);
        // ???et_phone???????????????????????????
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone, 11));
        // ???et_password???????????????????????????
        et_password.addTextChangedListener(new HideTextWatcher(et_password, 6));
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>()
        {
            @Override
            public void onActivityResult(ActivityResult result)
            {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK)
                {
                    // ??????????????????????????????????????????????????????
                    password = intent.getStringExtra("new_password");
                }
            }
        });

        loadData();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_password)
        {
            // ?????????????????????
            changeRadioGroupPassword();
        }
        else if (checkedId == R.id.rb_verifycode)
        {
            // ????????????????????????
            changeRadioGroupVerifycode();
        }
        else
        {
            //??????
            Log.w(TAG, "onCheckedChanged: error");
        }
       /* switch (checkedId)
        {
            case R.id.rb_password:
                changeRadioGroupPassword();
                break;
            case R.id.rb_verifycode:
                changeRadioGroupVerifycode();
                break;
        }*/
    }

    private void changeRadioGroupVerifycode()
    {
        tv_password.setText(getString(R.string.verifycode));
        et_password.setHint(getString(R.string.input_verifycode));
        btn_forget.setText(getString(R.string.get_verifycode));
        ck_remember.setVisibility(View.GONE);
    }

    private void changeRadioGroupPassword()
    {
        tv_password.setText(getString(R.string.login_password));
        et_password.setHint(getString(R.string.input_password));
        btn_forget.setText(getString(R.string.forget_password));
        ck_remember.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v)
    {
        //??????????????????
        String phone = et_phone.getText().toString();
        if (phone.length() < 11)
        {
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (v.getId() == R.id.btn_forget)
        {
            // ???????????????????????????????????????????????????????????????
            clickForget(phone);
        }
        else if (v.getId() == R.id.btn_login)
        {
            // ??????????????????
            clickLogin();
        }
        else
        {
            Log.w(TAG, "onClick: error");
        }

        /*switch (v.getId())
        {
            case R.id.btn_forget:
                // ???????????????????????????????????????????????????????????????
                clickForget(phone);
                break;
            case R.id.btn_login:
                // ??????????????????
                clickLogin();
                break;
        }*/
    }

    private void clickLogin()
    {
        if (rb_password.isChecked())
        {
//            if (!password.equals(et_password.getText().toString()))
//            {
//                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
//                return;
//            }

            User user = userDao.queryById(et_phone.getText().toString());
            if (user == null)
            {
                if (!password.equals(et_password.getText().toString()))
                {
                    Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginSuccess();
                return;
            }
            if (!(et_password.getText().toString().equals(user.getPassword())))
            {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            loginSuccess();
        }
        else if (rb_verifyCode.isChecked())
        {
            if (verifyCode == null)
            {
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            // ?????????????????????
            if (!verifyCode.equals(et_password.getText().toString()))
            {
                Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                return;
            }
            // ????????????????????????
            loginSuccess();
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param phone ??????
     */
    @SuppressLint("DefaultLocale")
    private void clickForget(String phone)
    {
        if (rb_password.isChecked())
        {
            // ???????????????????????????????????????????????????
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("phone", phone);
            register.launch(intent);
        }
        else if (rb_verifyCode.isChecked())
        {
            // ????????????????????????????????????
            verifyCode = String.format("%06d", new Random().nextInt(999999));
            // ?????????????????????????????????????????????????????????????????????
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("??????????????????");
            builder.setMessage("?????????" + phone + ",??????????????????" + verifyCode + ",??????????????????");
            builder.setPositiveButton("??????", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * ????????????
     */
    private void loginSuccess()
    {
        saveData();
        String desc = String.format("?????????????????????%s???????????????????????????????????????????????????????????????????????????",
                et_phone.getText().toString());
        // ??????????????????????????????????????????????????????
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????????");
        builder.setMessage(desc);
        builder.setPositiveButton("????????????", (dialog, which) ->
        {
            // ???????????????????????????
            finish();
        });
        builder.setNegativeButton("????????????", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    class HideTextWatcher implements TextWatcher
    {
        private final EditText editText;
        private final int maxLength;

        public HideTextWatcher(EditText v, int maxLength)
        {
            this.editText = v;
            this.maxLength = maxLength;
        }

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
            if (s.toString().length() == maxLength)
            {
                // ????????????????????????
                closeInput(MainActivity.this, editText);
            }
        }
    }

    /**
     * ??????(??????)?????????
     *
     * @param activity ??????
     * @param view     ??????
     */
    public void closeInput(Activity activity, View view)
    {
        //??????????????????????????????????????????
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //????????????????????????????????????
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * ????????????
     */
    private void loadData()
    {
        Log.d(TAG, "loadData: start");
//        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//
//        String phone = sharedPreferences.getString("phone", "");
//        String password = sharedPreferences.getString("password", "");

        UserDao instance = UserDao.getInstance(this);
        instance.openReadConnection();
        User user = instance.queryLastTime();
        if (user == null)
        {
            return;
        }
        String phone = user.getPhone();
        String password = user.getPassword();

        et_phone.setText(phone);
        et_password.setText(password);
        ck_remember.setChecked(true);
        instance.closeConnection();
    }

    /**
     * ????????????
     */
    private void saveData()
    {
        Log.d(TAG, "saveData: startSave");
        if (rb_login.getCheckedRadioButtonId() != R.id.rb_password)
        {
            return;
        }
        if (ck_remember.isChecked())
        {
//            Log.d(TAG, "saveData: save...");
//            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//
//            String phone = et_phone.getText().toString();
//            String password = et_password.getText().toString();
//
//            editor.putString("phone", phone);
//            editor.putString("password", password);
//
//            editor.apply();

            String phone = et_phone.getText().toString();
            String password = et_password.getText().toString();

            User user = new User(phone, password, new Date().getTime());
            boolean b = userDao.insertOrUpdate(user);
            if (!b)
            {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();

        }
        else
        {
            String phone = et_phone.getText().toString();
            User user = userDao.queryById(phone);
            if (user != null)
            {
                userDao.delete(phone);
            }

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        userDao = UserDao.getInstance(this);
        userDao.openReadConnection();
        userDao.openWriteConnection();
        Log.d(TAG, "onResume: queryAll:\n" + userDao.queryAll());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        userDao.closeConnection();
    }
}