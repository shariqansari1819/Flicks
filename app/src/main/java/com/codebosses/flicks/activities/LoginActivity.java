package com.codebosses.flicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codebosses.flicks.R;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.imageViewBackLogIn)
    AppCompatImageView imageViewBack;
    @BindView(R.id.textViewWelcomeLogIn)
    TextView textViewWelcome;
    @BindView(R.id.textViewLogInAccountLogIn)
    TextView textViewLoginAccount;
    @BindView(R.id.editTextEmailLogIn)
    EditText editTextEmail;
    @BindView(R.id.editTextPasswordLogIn)
    EditText editTextPassword;
    @BindView(R.id.textViewForgotPassword)
    TextView textViewForgotPassword;
    @BindView(R.id.buttonLogIn)
    Button buttonLogIn;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorWhite));

//        Setting custom font....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewBoldFont(textViewWelcome);
        fontUtils.setTextViewBoldFont(textViewLoginAccount);
        fontUtils.setEditTextRegularFont(editTextEmail);
        fontUtils.setEditTextRegularFont(editTextPassword);
        fontUtils.setTextViewRegularFont(textViewForgotPassword);
        fontUtils.setButtonRegularFont(buttonLogIn);
    }


}
