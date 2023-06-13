package my.subjectdb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import my.subjectdb.databinding.ActivityModifyBinding;
import my.subjectdb.db.Subject;
import my.subjectdb.db.DBHelper;

public class ModifyActivity extends AppCompatActivity {
	private ActivityModifyBinding binding;
	private DBHelper dbHelper;
	private Subject subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityModifyBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dbHelper = new DBHelper(this);

		long subjectID = getIntent().getLongExtra("id", -1);

		subject = dbHelper.getSubject(subjectID);
		if (subject == null) {
			Toast.makeText(this, "Error fetching subject!", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		binding.idText.setText("ID: " + subject.getId());
		binding.nameInput.setText(subject.getName());
		binding.profInput.setText(subject.getProf());
		binding.classCodeInput.setText(subject.getClassCode());

		RadioButton checkButton = subject.getType() == Subject.TYPE_C ? binding.radioButton0 :
				subject.getType() == Subject.TYPE_C_TD ? binding.radioButton1 : binding.radioButton2;
		binding.typeRadioGroup.check(checkButton.getId());

		binding.finalCheckBox.setChecked(subject.getHasFinal());
	}

	public void onSave(View v) {
		String inputName = binding.nameInput.getText().toString();

		if (inputName.isEmpty()) {
			Toast.makeText(this, "Please specify a subject name.", Toast.LENGTH_SHORT).show();
			return;
		}

		String inputProf = binding.profInput.getText().toString();
		String inputClassCode = binding.classCodeInput.getText().toString();

		RadioGroup radioGroup = binding.typeRadioGroup;
		int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
		View checkedRadioButton = radioGroup.findViewById(checkedRadioButtonId);
		int type = radioGroup.indexOfChild(checkedRadioButton); // the index is equivalent to the type 1:Cours 2:Cours+TD 3:Cours+TD+TP

		boolean inputFinal = binding.finalCheckBox.isChecked();

		if (dbHelper.updateSubject(subject.getId(), inputName, inputProf, inputClassCode, type, inputFinal)) {
			Toast.makeText(this, subject + " saved successfully.", Toast.LENGTH_SHORT).show();
			subject.setName(inputName);
			subject.setProf(inputProf);
			subject.setClassCode(inputClassCode);
			subject.setType(type);
			finish();
		} else {
			Toast.makeText(this, "Error while saving subject. Make sure name is unique.", Toast.LENGTH_SHORT).show();
		}
	}

	public void onRemove(View v) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Confirm");
		alertDialog.setMessage("Remove " + subject + "?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
			dbHelper.removeSubject(subject);
			Toast.makeText(this, subject + " deleted.", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
			setResult(RESULT_OK);
			finish();
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> {
			dialog.dismiss();
		});
		alertDialog.show();
	}

	@Override
	protected void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}
}