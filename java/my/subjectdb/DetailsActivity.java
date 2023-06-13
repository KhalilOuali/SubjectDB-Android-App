package my.subjectdb;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import my.subjectdb.databinding.ActivityDetailsBinding;
import my.subjectdb.db.DBHelper;
import my.subjectdb.db.Subject;

public class DetailsActivity extends AppCompatActivity {
	ActivityDetailsBinding binding;
	private DBHelper dbHelper;
	private Subject subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityDetailsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dbHelper = new DBHelper(this);
		refreshSubject();
	}

	ActivityResultLauncher<Intent> modifyActivityLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			result -> {
				if (result.getResultCode() == RESULT_OK) {
					finish();
				}
			});


	private void refreshSubject() {
		long subjectID = getIntent().getLongExtra("id", -1);

		subject = dbHelper.getSubject(subjectID);
		if (subject == null) {
			Toast.makeText(this, "Error fetching subject!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		binding.nameText.setText(subject.getName());

		String typeText = subject.getType() == Subject.TYPE_C ? "Cours" :
				subject.getType() == Subject.TYPE_C_TD ? "Cours + TD" : "Cours + TD + TP";
		typeText += subject.getHasFinal() ? "\nWith final exam" : "\nNo final exam";
		binding.typeText.setText(typeText);

		String profText = subject.getProf().isEmpty() ? "- Professor not set -" : subject.getProf().replace(", ", "\n");
		binding.profText.setText(profText);

		binding.classroomButton.setEnabled(!subject.getClassCode().isEmpty());
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshSubject();
	}

	public void onClassroom(View v) {
		Uri uri = Uri.parse(subject.getClassCode()); // "https://classroom.google.com/c/"
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Error opening link. Make sure link is valid.", Toast.LENGTH_SHORT).show();
		}
	}

	public void onEdit(View v) {
		Intent intent = new Intent(this, ModifyActivity.class);
		intent.putExtra("id", subject.getId());
		modifyActivityLauncher.launch(intent);
	}
}