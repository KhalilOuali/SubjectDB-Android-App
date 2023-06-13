package my.subjectdb;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import my.subjectdb.databinding.ActivityMainBinding;
import my.subjectdb.db.Subject;
import my.subjectdb.db.DBHelper;

public class MainActivity extends AppCompatActivity {
	private ActivityMainBinding binding;
	private SubjectListViewAdapter subjectListViewAdapter;
	private DBHelper dbHelper;
	private final List<Subject> subjectList = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dbHelper = new DBHelper(this);

		// Do only once
//		initDB();

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		subjectListViewAdapter = new SubjectListViewAdapter(subjectList);
		binding.subjectListView.setLayoutManager(linearLayoutManager);
		binding.subjectListView.setAdapter(subjectListViewAdapter);
		binding.subjectListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

		binding.searchInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				refreshSubjectList();
			}
		});
		binding.filterFinalCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				refreshSubjectList();
			}
		});

		refreshSubjectList();
	}

	private void initDB() {
		dbHelper.addSubject("Mobile",
				"Mme. Faten Abbassi",
				"https://classroom.google.com/c/NDY3ODE5OTUzMzQ3",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("Web ",
				"Mme. Imen Hajji",
				"https://classroom.google.com/c/NTQzMTg0ODQ2NDcx",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("VHDL",
				"Mr. Aymen Zermani, Mr. Ibrahim Ben Abdallah",
				"https://classroom.google.com/c/NTkyODM0OTI0NDkx",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("SoC",
				"Mr. Kamel Echaieb, Mme. Nada Jebali",
				"https://drive.google.com/drive/folders/1_b1U2Vgi-gAsHAb6FvyGDxNLF_iXvWno",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("Test logiciel",
				"Mme. Imen Khanchouch",
				"https://classroom.google.com/c/NTg4Nzg2NzczMzYx",
				Subject.TYPE_C_TD,
				true);
		dbHelper.addSubject("Sécurité",
				"Mr. Hamed Aouadi",
				"https://classroom.google.com/c/NTQzMjcxNjExNjgz",
				Subject.TYPE_C_TD,
				true);
		dbHelper.addSubject("Ingénierie BD",
				"Mme. Leila Hadded",
				"https://classroom.google.com/c/NTk2NzYxMjA4ODc1",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("Réseaux IP",
				"Mme. Sinda Boussen",
				"https://classroom.google.com/c/NTkyMTk2MDE1Njc0",
				Subject.TYPE_C_TD_TP,
				true);
		dbHelper.addSubject("Anglais",
				"Mme. Nawel Krichene",
				"https://classroom.google.com/c/NTU1MzkwMTY0NzEw",
				Subject.TYPE_C,
				false);
		dbHelper.addSubject("Droit Informatique",
				"Mme. Asma Gharbi",
				"https://classroom.google.com/c/NTU1MzkwMTY0NzEw",
				Subject.TYPE_C,
				false);
		dbHelper.addSubject("Projet fédéré",
				"",
				"",
				Subject.TYPE_C,
				false);
		dbHelper.addSubject("Irrelevant class A",
				"",
				"",
				Subject.TYPE_C_TD,
				true);
		dbHelper.addSubject("Irrelevant class B",
				"",
				"",
				Subject.TYPE_C_TD_TP,
				false);
		dbHelper.addSubject("Irrelevant class C",
				"",
				"",
				Subject.TYPE_C_TD_TP,
				false);
	}

	private void refreshSubjectList() {
		subjectList.clear();
		String searchFilter = binding.searchInput.getText().toString();
		Boolean finalFilter = binding.filterFinalCheckBox.isChecked();
		subjectList.addAll(dbHelper.getSubjectList(searchFilter, finalFilter));
		subjectListViewAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
		binding.searchInput.setText(null);
		refreshSubjectList();
	}

	public void onFABClick(View v) {
		Intent i = new Intent(this, AddActivity.class);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}
}