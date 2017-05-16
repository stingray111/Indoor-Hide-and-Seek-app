package com.csci3310.indoorhns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Created by Edmund on 5/16/2017.
 */

public class NativeInputBox implements TextField.OnscreenKeyboard {

    private TextField textField;
    private String title, hint;

    public NativeInputBox(TextField textField, String title, String hint){
        this.textField = textField;
        this.title = title;
        this.hint = hint;
    }
    @Override
    public void show(boolean visible) {
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                textField.setText(text);
            }

            @Override
            public void canceled() {

            }
        }, title, textField.getText(), hint);
    }
}
