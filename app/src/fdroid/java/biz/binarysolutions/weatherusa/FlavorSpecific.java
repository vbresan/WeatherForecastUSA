package biz.binarysolutions.weatherusa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

class FlavorSpecific {

    private static void showSupportDialog(Activity activity) {

        AlertDialog.Builder builder =
            new MaterialAlertDialogBuilder(activity, R.style.AlertDialog);

        builder.setTitle(R.string.SupportDialogTitle);
        builder.setMessage(R.string.SupportDialogMessage);

        builder.setPositiveButton(R.string.Continue, (dialog, which) -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(activity.getString(R.string.donation_url)));
            activity.startActivity(intent);

            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void initialize(Activity activity) {

        Button button = activity.findViewById(R.id.buttonSupport);
        if (button != null) {
            button.setOnClickListener(view -> showSupportDialog(activity));
        }
    }
}
