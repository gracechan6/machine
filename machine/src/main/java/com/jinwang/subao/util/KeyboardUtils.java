package com.jinwang.subao.util;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.jinwang.subao.R;

/**
 * Created by Chenss on 2015/9/22.
 */
public class KeyboardUtils implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    boolean isShow=false;
    private EditText editText;


    public KeyboardUtils(Activity activity, Context context, EditText editText) {
        this.editText=editText;
        keyboard=new Keyboard(context, R.xml.ninekey);
        keyboardView= (KeyboardView) activity.findViewById(R.id.keyboard_view_ninekey);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setEnabled(true);

        keyboardView.setOnKeyboardActionListener(this);
    }



    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable=editText.getText();
        int start=editText.getSelectionStart();
        switch (primaryCode){
            case Keyboard.KEYCODE_CANCEL:break;
            case Keyboard.KEYCODE_DELETE:
                if (editable!=null && editable.length()>0 &&start>0)
                    editable.delete(start-1,start);
                break;
            case 57419:
                if (start>0) editText.setSelection(start-1);
                break;
            case 57421:
                if (start<editText.length()) editText.setSelection(start+1);
                break;
            default:editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    public void turnEdit(EditText editText){
        this.editText=editText;
    }

    public void hideKeyboard(){
        if(keyboardView.getVisibility()== View.VISIBLE){
            keyboardView.setVisibility(View.INVISIBLE);
            isShow=false;
        }
    }

    public void showKeyboard(){
        if(keyboardView.getVisibility()== View.INVISIBLE || keyboardView.getVisibility()== View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
            isShow=true;
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
