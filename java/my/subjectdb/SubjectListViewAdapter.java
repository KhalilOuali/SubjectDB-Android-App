package my.subjectdb;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.subjectdb.databinding.SubjectListItemBinding;
import my.subjectdb.db.Subject;

public class SubjectListViewAdapter extends RecyclerView.Adapter<SubjectListViewAdapter.ViewHolder> {
	private List<Subject> localDataSet;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private SubjectListItemBinding binding;
		private Subject subject;

		public ViewHolder(SubjectListItemBinding binding) {
			super(binding.getRoot());
			Context context = binding.getRoot().getContext();
			this.binding = binding;

			binding.listNameText.setOnClickListener(v -> {
				Intent i = new Intent(context, DetailsActivity.class);
				i.putExtra("id", subject.getId());
				context.startActivity(i);
			});
			binding.listEditButton.setOnClickListener(v -> {
				Intent i = new Intent(context, ModifyActivity.class);
				i.putExtra("id", subject.getId());
				context.startActivity(i);
			});
		}

		public void setSubject(Subject subject) {
			this.subject = subject;
			binding.listIdText.setText(String.valueOf(subject.getId()));
			binding.listNameText.setText(subject.getName());
		}
	}

	public SubjectListViewAdapter(List<Subject> dataSet) {
		localDataSet = dataSet;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		SubjectListItemBinding binding = SubjectListItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
		return new ViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		viewHolder.setSubject(localDataSet.get(position));
	}

	@Override
	public int getItemCount() {
		return localDataSet.size();
	}
}
