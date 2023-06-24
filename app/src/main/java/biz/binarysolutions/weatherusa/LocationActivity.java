package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import biz.binarysolutions.weatherusa.components.preferences.Preferences;
import biz.binarysolutions.weatherusa.util.DefaultTextWatcher;

/**
 *
 */
public class LocationActivity extends Activity {

    /**
     *
     */
    private void setListeners() {

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

        RadioButton radioButtonGPS = findViewById(R.id.radioButtonGPS);
        if (radioButtonGPS == null) {
            return;
        }

        radioButtonGPS.setOnCheckedChangeListener((cb, isChecked) -> {
            if (isChecked) {
                buttonDone.setEnabled(true);
            }
        });

        RadioButton radioButtonZIP = findViewById(R.id.radioButtonZIP);
        if (radioButtonZIP != null) {
            radioButtonZIP.setOnCheckedChangeListener((cb, isChecked) -> {
                editText.setEnabled(isChecked);
                if (isChecked) {
                    editText.requestFocus();
                    buttonDone.setEnabled(editText.length() == 5);
                }
            });
        }

        buttonDone.setOnClickListener(v -> {

            boolean isGPS = radioButtonGPS.isChecked();
            String  zip   = editText.getText().toString();

            Preferences.save(getPreferences(MODE_PRIVATE), isGPS, zip);
            finish();
        });
    }

    /**
     *
     */
    private void setDefaultValues() {

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String  zip   = Preferences.getZIP(preferences);
        boolean isGPS = Preferences.isGPS(preferences);

        /* do not change the ordering, as setting incomplete ZIP makes button
            Done disabled */
        EditText editText = findViewById(R.id.editTextZIPCode);
        if (editText != null) {
            editText.setText(zip);
        }

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        if (radioGroup != null) {
            radioGroup.check(isGPS? R.id.radioButtonGPS : R.id.radioButtonZIP);
        }
    }

    /**
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        setListeners();
        setDefaultValues();
    }
}
