package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;

    public UserAdapter(@NonNull Context context, List<User> users) {
        super(context, R.layout.user_item, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        }

        User currentUser = users.get(position);

        TextView nameTextView = convertView.findViewById(R.id.user_name);
        TextView ageTextView = convertView.findViewById(R.id.user_age);

        nameTextView.setText(currentUser.getName());
        ageTextView.setText(String.valueOf(currentUser.getAge()));

        return convertView;
    }
}
