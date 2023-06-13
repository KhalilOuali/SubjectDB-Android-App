package my.subjectdb;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import my.subjectdb.databinding.ActivityAddBinding;
import my.subjectdb.databinding.ActivityModifyBinding;
import my.subjectdb.db.Subject;
import my.subjectdb.db.DBHelper;

public class AddActivity extends AppCompatActivity {
	private ActivityAddBinding binding;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAddBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dbHelper = new DBHelper(this);
	}

	public void onAdd(View v) {
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
		int inputType = radioGroup.indexOfChild(checkedRadioButton); // the index is equivalent to the type 1:Cours 2:Cours+TD 3:Cours+TD+TP

		boolean inputFinal = binding.finalCheckBox.isChecked();

		Subject subject = dbHelper.addSubject(inputName, inputProf, inputClassCode, inputType, inputFinal);
		if (subject == null) {
			Toast.makeText(this, "Error while adding subject. Make sure name is unique.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, subject + " added.", Toast.LENGTH_SHORT).show();
			onClear(null);
		}
	}

	public void onClear(View v) {
		binding.nameInput.setText(null);
		binding.profInput.setText(null);
		binding.classCodeInput.setText(null);
	}

	@Override
	protected void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}
}