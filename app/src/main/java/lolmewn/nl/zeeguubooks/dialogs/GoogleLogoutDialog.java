package lolmewn.nl.zeeguubooks.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import lolmewn.nl.zeeguubooks.R;
import lolmewn.nl.zeeguubooks.settings.SettingsCallbacks;

/**
 * Creates the dialog to confirm the logout task
 */
public class GoogleLogoutDialog extends DialogFragment {
    private SettingsCallbacks callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(getActivity(), ch.unibe.R.style.AlertDialogCustom);
        else
            builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_google_logout);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.dialog_google_logout_confirm, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   //callback.googleLogout();
                   // Google log-out is having issues concerning the API still thinking it is logged in; and therefore failing in the main menu.
                   Toast.makeText(getActivity(), "Sorry, this function is currently not working. Please check back in the next version.", Toast.LENGTH_LONG);}
               }
        );

        builder.setNegativeButton(ch.unibe.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int which) {dlg.cancel();
                    }
                } );

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the interface is implemented in the container activity
        try {
            callback = (SettingsCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SettingsCallbacks");
        }
    }

}