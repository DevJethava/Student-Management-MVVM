package in.dev.android.studentmanagement.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import in.dev.android.studentmanagement.R;
import in.dev.android.studentmanagement.adapter.StudentAdapter;
import in.dev.android.studentmanagement.databinding.ActivityStudentListBinding;
import in.dev.android.studentmanagement.helper.Provider;
import in.dev.android.studentmanagement.helper.RecyclerTouchListener;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;
import in.dev.android.studentmanagement.view_model.StudentViewModel;
import in.dev.android.studentmanagement.view_model.TeacherViewModel;

public class StudentListActivity extends AppCompatActivity {

    ActivityStudentListBinding binding;
    StudentViewModel viewModel;
    StudentAdapter adapter;
    private RecyclerTouchListener touchListener;
    List<StudentModel> modelList = new ArrayList<>();

    private static final int ACTION_INSERT = 1;
    private static final int ACTION_EDIT = 2;

    private static int teacherId = -1;

    private List<Integer> teacherIdList = new ArrayList<>();
    private List<String> teacherNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.headerLayout.toolbar);
        binding.headerLayout.toolbar.setTitle("Student List");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = ViewModelProviders.of(StudentListActivity.this).get(StudentViewModel.class);

        Intent intent = getIntent();
        teacherId = intent.getIntExtra(Provider.TEACHER_ID, -1);
        if (teacherId == -1) {
            Toast.makeText(this, "Something wrong..", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.rvStudent.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudent.setHasFixedSize(true);

        touchListener = new RecyclerTouchListener(this, binding.rvStudent);
        touchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {

            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        })
                .setSwipeOptionViews(R.id.layoutEdit, R.id.layoutDelete)
                .setSwipeable(R.id.view_foreground, R.id.view_background, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.layoutDelete:
                                deleteStudent(modelList.get(position));
                                break;

                            case R.id.layoutEdit:
                                studentActionDialog(modelList.get(position), ACTION_EDIT);
                                break;
                        }
                    }
                });
        binding.rvStudent.addOnItemTouchListener(touchListener);

        viewModel.getAllTeacher().observe(this, new Observer<List<TeacherModel>>() {
            @Override
            public void onChanged(List<TeacherModel> teacherModels) {
                for (TeacherModel model : teacherModels) {
                    teacherIdList.add(model.getId());
                    teacherNameList.add(model.getName());
                }
            }
        });

        binding.fbAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentActionDialog(null, ACTION_INSERT);
            }
        });

        getStudentData(teacherId);
    }

    private void deleteStudent(StudentModel studentModel) {
        viewModel.deleteStudent(studentModel);
        viewModel.getStudentByTeacher(teacherId).observe(StudentListActivity.this, new Observer<List<StudentModel>>() {
            @Override
            public void onChanged(List<StudentModel> studentModels) {
                modelList = studentModels;
                adapter.updateList(studentModels);
            }
        });
    }

    private void studentActionDialog(StudentModel studentModel, int action) {
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

        Spinner spTeacher = dialog.findViewById(R.id.spTeacher);
        spTeacher.setVisibility(View.VISIBLE);

        Button btnAction = dialog.findViewById(R.id.btnAction);
        Button btnDismiss = dialog.findViewById(R.id.btnDismiss);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StudentListActivity.this, android.R.layout.simple_spinner_dropdown_item, teacherNameList);
        spTeacher.setAdapter(arrayAdapter);

        int position = teacherIdList.indexOf(teacherId);
        spTeacher.setSelection(position);

        if (action == ACTION_EDIT && studentModel != null) {
            tvDialogTitle.setText("Edit Student");
            btnAction.setText("EDIT");

            etName.setText(studentModel.getName());
            etPhoneNumber.setText(studentModel.getPhoneNumber());

        } else {
            tvDialogTitle.setText("Add Student");
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
                } else if (spTeacher.getSelectedItemPosition() <= -1) {
                    Toast.makeText(StudentListActivity.this, "Select corresponding Teacher.", Toast.LENGTH_SHORT).show();
                } else {
                    StudentModel student = new StudentModel();
                    student.setName(etName.getText().toString());
                    student.setPhoneNumber(etPhoneNumber.getText().toString());
                    student.setTeacherId(teacherIdList.get(spTeacher.getSelectedItemPosition()));

                    if (action == ACTION_EDIT && studentModel != null) {
                        student.setId(studentModel.getId());
                        viewModel.updateStudent(student);
                    } else if (action == ACTION_INSERT)
                        viewModel.insertStudent(student);

                    viewModel.getStudentByTeacher(teacherId).observe(StudentListActivity.this, new Observer<List<StudentModel>>() {
                        @Override
                        public void onChanged(List<StudentModel> studentModels) {
                            modelList = studentModels;
                            adapter.updateList(studentModels);
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

    private void getStudentData(int teacherId) {
        viewModel.getStudentByTeacher(teacherId).observe(StudentListActivity.this, new Observer<List<StudentModel>>() {
            @Override
            public void onChanged(List<StudentModel> studentModels) {
                modelList = studentModels;
                adapter = new StudentAdapter(StudentListActivity.this, studentModels);
                binding.rvStudent.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudentData(teacherId);
        binding.rvStudent.addOnItemTouchListener(touchListener);
    }
}