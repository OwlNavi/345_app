package com.example.cwagt.taskapp345;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class AvatarHome extends AppCompatActivity {
	ImageView avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_home);

		avatar = findViewById(R.id.avatar);
	}
}
