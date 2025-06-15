package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;

public class LanguageAdapter extends ArrayAdapter<String> {
    Context context;
    String[] languageNames;
    int[] flags;
    private int selectedPosition = -1;

    public LanguageAdapter(Context context, String[] languageNames, int[] flags) {
        super(context, R.layout.language_item, languageNames);
        this.context = context;
        this.languageNames = languageNames;
        this.flags = flags;
    }

    // Setter để cập nhật vị trí được chọn
    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.language_item, parent, false);

        TextView name = row.findViewById(R.id.languageName);
        ImageView flag = row.findViewById(R.id.flag);
        LinearLayout container = row.findViewById(R.id.languageContainer); // layout cha

        name.setText(languageNames[position]);
        flag.setImageResource(flags[position]);

        // Nếu là item được chọn thì gán viền xanh, ngược lại viền mặc định
        if (position == selectedPosition) {
            container.setBackground(ContextCompat.getDrawable(context, R.drawable.language_item_bg));
        }
       else {
           container.setBackground(ContextCompat.getDrawable(context, R.drawable.language_default_border));
        }

        return row;
    }
}
