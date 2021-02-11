package in.dev.android.studentmanagement.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import in.dev.android.studentmanagement.R;
import in.dev.android.studentmanagement.adapter.TeacherAdapter;
import in.dev.android.studentmanagement.databinding.ActivityTeacherListBinding;
import in.dev.android.studentmanagement.helper.Provider;
import in.dev.android.studentmanagement.helper.RecyclerTouchListener;
import in.dev.android.studentmanagement.room.model.TeacherModel;
import in.dev.android.studentmanagement.view_model.TeacherViewModel;

public class TeacherListActivity extends AppCompatActivity {

    ActivityTeacherListBinding binding;
    TeacherViewModel viewModel;
    TeacherAdapter adapter;
    private RecyclerTouchListener touchListener;
    List<TeacherModel> modelList = new ArrayList<>();

    private static final int ACTION_INSERT = 1;
    private static final int ACTION_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(TeacherListActivity.this).get(TeacherViewModel.class);

        binding.headerLayout.toolbar.setTitle("Teacher List");

        binding.rvTeacher.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTeacher.setHasFixedSize(true);
        touchListener = new RecyclerTouchListener(this, binding.rvTeacher);
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                Intent intent = new Intent(TeacherListActivity.this, StudentListActivity.class);
                intent.putExtra(Provider.TEACHER_ID, modelList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        })
                .setSwipeOptionViews(R.id.layoutEdit, R.id.layoutDelete)
                .setSwipeable(R.id.view_foreground, R.id.view_background, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.layoutDelete:
                                deleteTeacher(modelList.get(position));
                                break;

                            case R.id.layoutEdit:
                                teacherActionDialog(modelList.get(position), ACTION_EDIT);
                                break;
                        }
                    }
                });

        binding.rvTeacher.addOnItemTouchListener(touchListener);

        binding.fbAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teacherActionDialog(null, ACTION_INSERT);
            }
        });

        getData();

    }

    private void getData() {
        viewModel.getTeacherList().observe(this, new Observer<List<TeacherModel>>() {
            @Override
            public void onChanged(List<TeacherModel> teacherModels) {
                modelList = teacherModels;
                adapter = new TeacherAdapter(TeacherListActivity.this, teacherModels);
                binding.rvTeacher.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void teacherActionDialog(TeacherModel model, int actionId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_data);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView tvDialogTitle = dialog.findViewById(R.id.tvDialogTitle);

        TextInputLayout tilName = dialog.findViewById(R.id.tilName);
        EditText etName = dialog.findViewById(R.id.etName);

        TextInputLayout tilPhoneNumber = dialog.findViewById(R.id.tilPhoneNumber);
        EditText etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);

        Button btnAction = dialog.findViewById(R.id.btnAction);
        Button btnDismiss = dialog.findViewById(R.id.btnDismiss);

        if (actionId == ACTION_EDIT && model != null) {
            tvDialogTitle.setText("Edit Teacher");
            btnAction.setText("EDIT");

            etName.setText(model.getName());
            etPhoneNumber.setText(model.getPhoneNumber());
        } else {
            tvDialogTitle.setText("Add Teacher");
            btnAction.setText("Add");
        }

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilName.setError("");
                tilPhoneNumber.setError("");

                if (TextUtils.isEmpty(etName.getText().toString())) {
                    tilName.setError("Invalid Name");
                } else if (TextUtils.isEmpty(etPhoneNumber.getText().toString()) || etPhoneNumber.getText().length() < 10) {
                    tilPhoneNumber.setError("Invalid Phone Number");
                } else {
                    TeacherModel teacherModel = new TeacherModel();
                    teacherModel.setName(etName.getText().toString());
                    teacherModel.setPhoneNumber(etPhoneNumber.getText().toString());

                    if (actionId == ACTION_INSERT)
                        viewModel.insertTeacher(teacherModel);
                    else if (actionId == ACTION_EDIT) {
                        teacherModel.setId(model.getId());
                        viewModel.updateTeacher(teacherModel);
                    }

                    viewModel.getTeacherList().observe(TeacherListActivity.this, new Observer<List<TeacherModel>>() {
                        @Override
                        public void onChanged(List<TeacherModel> teacherModels) {
                            modelList = teacherModels;
                            adapter.updateList(teacherModels);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    dialog.dismiss();
                }
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void deleteTeacher(TeacherModel model) {
        viewModel.deleteTeacher(model);
        viewModel.getTeacherList().observe(TeacherListActivity.this, new Observer<List<TeacherModel>>() {
            @Override
            public void onChanged(List<TeacherModel> teacherModels) {
                modelList = teacherModels;
                adapter.updateList(teacherModels);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
        binding.rvTeacher.addOnItemTouchListener(touchListener);
    }
}