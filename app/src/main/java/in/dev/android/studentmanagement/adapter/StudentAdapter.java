package in.dev.android.studentmanagement.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.dev.android.studentmanagement.databinding.RowStudentBinding;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    Activity activity;
    List<StudentModel> modelList = new ArrayList<>();

    public StudentAdapter(Activity activity, List<StudentModel> modelList) {
        this.activity = activity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowStudentBinding binding = RowStudentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StudentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.tvName.setText(modelList.get(position).getName());
        holder.tvPhoneNumber.setText(modelList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        if (modelList != null)
            return modelList.size();
        return 0;
    }

    public void updateList(List<StudentModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPhoneNumber;

        public StudentViewHolder(@NonNull RowStudentBinding binding) {
            super(binding.getRoot());

            tvName = binding.tvName;
            tvPhoneNumber = binding.tvPhoneNumber;
        }
    }
}
