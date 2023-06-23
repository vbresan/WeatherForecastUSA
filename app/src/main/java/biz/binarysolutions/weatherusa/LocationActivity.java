package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import biz.binarysolutions.weatherusa.util.DefaultTextWatcher;

/**
 *
 */
public class LocationActivity extends Activity {

    /**
     *
     */
    private void setButtonListeners() {

        Button buttonDone = findViewById(R.id.buttonDone);
        if (buttonDone == null) {
            return;
        }

        EditText editText = findViewById(R.id.editTextZIPCode);
        if (editText == null) {
            return;
        }

        editText.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                buttonDone.setEnabled(editable.length() == 5);
            }
        });

        RadioButton radioButton = findViewById(R.id.radioButtonGPS);
        if (radioButton != null) {
            radioButton.setOnCheckedChangeListener((cb, isChecked) -> {
                if (isChecked) {
                    buttonDone.setEnabled(true);
                }
            });
        }

        radioButton = findViewById(R.id.radioButtonZIPCode);
        if (radioButton != null) {
            radioButton.setOnCheckedChangeListener((cb, isChecked) -> {
                editText.setEnabled(isChecked);
                if (isChecked) {
                    editText.requestFocus();
                    buttonDone.setEnabled(editText.length() == 5);
                }
            });
        }
    }

    /**
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setButtonListeners();
    }
}
