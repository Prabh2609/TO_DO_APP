package com.saviour.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DialogNewTask extends DialogFragment {

    public static final String TAG = "Create new task";
    private MaterialToolbar toolbar;
    private MaterialButton btnSave;
    final Calendar myCalendar = Calendar.getInstance();
    private String tempDate, tempTime;
    private List<EditText> editTextList;
    private TextInputEditText titleEditText, dueByEditText, notifyEditTxt;

    public static DialogNewTask display(FragmentManager fragmentManager){
        DialogNewTask dialogNewTask = new DialogNewTask();
        dialogNewTask.show(fragmentManager,TAG);
        return  dialogNewTask;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_Dialog);

        editTextList = new ArrayList<EditText>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog,container,false);

        toolbar = view.findViewById(R.id.toolbar);
        btnSave = view.findViewById(R.id.save);
        titleEditText = view.findViewById(R.id.title);
        dueByEditText = view.findViewById(R.id.due);
        notifyEditTxt = view.findViewById(R.id.notify);

        editTextList.add(titleEditText);
        editTextList.add(dueByEditText);
        editTextList.add(notifyEditTxt);

        dueByEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(dueByEditText);
            }
        });

        notifyEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker(notifyEditTxt);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields(editTextList)) {
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
         return view;

    }

    private boolean validateTextFields(List<EditText> editTextList) {
        boolean isEmpty = false;
        for (EditText editText : editTextList) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError("Please fill out this field");
                isEmpty = true;
            }
        }
        if (isEmpty) return false;

        return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Create a new task");
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if(dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private void showDateTimePicker(final EditText editText) {
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String DateFormat = "dd/MM/YYYY";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormat);

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                tempDate = simpleDateFormat.format(myCalendar.getTime());

                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String TimeFormat = "hh:mm a";
                        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(TimeFormat);

                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        tempTime = simpleTimeFormat.format(myCalendar.getTime());


                        editText.setText(tempDate + ", " + tempTime);
                    }
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
