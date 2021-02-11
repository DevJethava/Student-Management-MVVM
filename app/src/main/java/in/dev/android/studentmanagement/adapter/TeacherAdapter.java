package in.dev.android.studentmanagement.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.dev.android.studentmanagement.databinding.RowTeacherBinding;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    Activity activity;
    List<TeacherModel> modelList = new ArrayList<>();

    public TeacherAdapter(Activity activity, List<TeacherModel> modelList) {
        this.activity = activity;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowTeacherBinding binding = RowTeacherBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TeacherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.tvName.setText(modelList.get(position).getName());
        holder.tvPhoneNumber.setText(modelList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        if (modelList != null)
            return modelList.size();
        return 0;
    }

    public void updateList(List<TeacherModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public class TeacherViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPhoneNumber;

        public TeacherViewHolder(@NonNull RowTeacherBinding binding) {
            super(binding.getRoot());

            tvName = binding.tvName;
            tvPhoneNumber = binding.tvPhoneNumber;
        }
    }
}
