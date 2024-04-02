package com.example.rescuesupport;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ShelterArrayAdapter extends ArrayAdapter<String> {

    public ShelterArrayAdapter(Context context, List<String> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        String text = getItem(position);

        if (text != null && text.contains("\n")) {
            String[] splitText = text.split("\n", 2);
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new RelativeSizeSpan(0.8f), splitText[0].length() + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            textView.setText(text);
        }

        return textView;
    }

    // 데이터 세트 변경을 처리하는 메서드
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

