package net.campbelldev.tuhawtweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by aaroncampbell on 6/6/17.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(R.string.oops)
                        .setMessage(R.string.error_notification)
                        .setPositiveButton(R.string.okay, null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
