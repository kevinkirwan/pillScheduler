package com.kevinkirwansoftware.capsule.popouts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.kevinkirwansoftware.capsule.R;


/*
    This is a very simple dialog box that takes in 3 user inputs and uses them to changed the main
    alarm's time if the user hit's "OK" The three inputs are as follows:
        Hour (Numeric keyboard input)
        Minute (Numeric keyboard input)
        AM or PM (Toggle Switch)
        It is expected that a 24-Hour version without an AM/PM switch will be added at a later date.
 */
public class NewReminderPopOut extends AppCompatDialogFragment {
    private EditText userInputHour;
    private EditText userInputMinute;
    private Switch userAmpm;
    private DialogBoxListener listener;
    private int dialogPosition;
    private SingleDateAndTimePicker sdtp;

    /*
        The three arguments passed to MainActivity from the DialogBox activity correlate to the
        three user inputs
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popout_new_reminder, null);

        builder.setView(view)
                .setTitle("Edit Alarm")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                /*
                    The .setPositiveButton method will send the three parameters to MainActivity if
                    the OK button is pressed AND the input is valid. A valid input is determined by
                    the validInput boolean variable which ensures that the default values have been
                    changed and the numbers input constitute a valid 12-hour formatted  time.
                 */
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userHour = userInputHour.getText().toString();
                        String userMinute = userInputMinute.getText().toString();
                        boolean userAmpmSend = userAmpm.isChecked();
                        boolean validInput = ((userInputHour.getText().toString().charAt(0) != '0') && (userInputHour.getText().toString().charAt(0) != 'H') && ((userInputMinute.getText().toString().length() == 2) && (userInputMinute.getText().toString().charAt(0) != 'M')) && ((Integer.parseInt(userInputHour.getText().toString()) < 13) && (Integer.parseInt(userInputMinute.getText().toString()) < 60)));
                    /*
                        As mentioned above, the validInput boolean variable determines whether or
                        not the input is OK to use, and sends it to MainActivity if the variables
                        make up a time that is usable by the system
                     */
                        if (validInput) {
                            listener.updateVariablesFromEditDialog(getDialogPosition(), userHour, userMinute, userAmpmSend);

                        } else {
                            Toast.makeText(getContext(), "Invalid input. Please enter a valid time in 12-Hour format.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogBoxListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement DialogBoxListener");
        }
    }

    public void setDialogPosition(int position){
        dialogPosition = position;
    }

    public int getDialogPosition(){
        return dialogPosition;
    }

    /*
        DialogBoxListener takes variables and sends them to MainActivity
     */
    public interface DialogBoxListener{
        void updateVariablesFromEditDialog(int position, String userHour, String userMinute, boolean userAmpmSend);
    }
}