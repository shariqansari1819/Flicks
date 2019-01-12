package com.codebosses.flicks.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class FontUtils {

    private static Typeface typefaceBold, typefaceRegular;
    private static FontUtils fontUtils;

    public static FontUtils getFontUtils(Context context) {
        if (fontUtils == null) {
            fontUtils = new FontUtils();
            typefaceBold = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand_bold.otf");
            typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/quicksa d_regular.otf");
        }
        return fontUtils;
    }

    public void setTextViewRegularFont(TextView textView) {
        textView.setTypeface(typefaceRegular);
    }

    public void setTextViewBoldFont(TextView textView) {
        textView.setTypeface(typefaceBold);
    }


    public void setButtonRegularFont(Button button) {
        button.setTypeface(typefaceRegular);
    }

    public void setButtonBoldFont(Button button) {
        button.setTypeface(typefaceBold);
    }

    public void setEditTextRegularFont(EditText editText) {
        editText.setTypeface(typefaceRegular);
    }

    public void setEditTextBoldFont(EditText editText) {
        editText.setTypeface(typefaceBold);
    }

    public Typeface getTypefaceRegular() {
        return typefaceRegular;
    }

    public Typeface getTypefaceBold() {
        return typefaceBold;
    }

    public void setRadioButtonRegularFont(RadioButton radioButton) {
        radioButton.setTypeface(typefaceRegular);
    }
}
