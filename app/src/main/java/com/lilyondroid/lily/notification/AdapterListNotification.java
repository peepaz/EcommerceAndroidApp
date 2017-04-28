package com.lilyondroid.lily.notification;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lilyondroid.lily.R;
import com.lilyondroid.lily.customviews.LilyTextView;
import com.lilyondroid.lily.customviews.LilyTextViewBold;

import java.util.ArrayList;

public class AdapterListNotification extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> titleId;
    private final ArrayList<String> content;
    private final ArrayList<String> recvDate;
    private final ArrayList<String> expDate;

    public AdapterListNotification(Activity context, ArrayList<String> titleId, ArrayList<String> contentId,
                                   ArrayList<String>expDate, ArrayList<String> recvDate) {
        super(context, R.layout.about_item, titleId);
        this.context = context;
        this.titleId = titleId;
        this.content = contentId;
        this.recvDate = recvDate;
        this.expDate = expDate;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.notification_item, null, true);

        LilyTextViewBold title = (LilyTextViewBold) rowView.findViewById(R.id.notification_title);
        LilyTextView content = (LilyTextView) rowView.findViewById(R.id.content);
        LilyTextView expDate = (LilyTextView) rowView.findViewById(R.id.exp_date);
        LilyTextView recvDate = (LilyTextView) rowView.findViewById(R.id.recv_date);

        title.setText(titleId.get(position));
        content.setText(this.content.get(position));
        expDate.setText(this.expDate.get(position));
        recvDate.setText(this.recvDate.get(position));

        return rowView;
    }
}