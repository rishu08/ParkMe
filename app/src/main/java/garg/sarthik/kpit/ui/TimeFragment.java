package garg.sarthik.kpit.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import garg.sarthik.kpit.Statics.Functions;

public class TimeFragment extends DialogFragment {

    private final String TAG = "Time";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        int currentTime = Functions.getCurrentTime();
        int hour = currentTime / 100;
        int minute = currentTime % 100;

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);
    }
}

