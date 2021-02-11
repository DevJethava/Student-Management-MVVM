package in.dev.android.studentmanagement.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.dev.android.studentmanagement.repository.StudentRepository;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class StudentViewModel extends AndroidViewModel {

    private StudentRepository repository;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository = new StudentRepository(application);
    }

    public void insertStudent(StudentModel student) {
        repository.insertStudent(student);
    }

    public void updateStudent(StudentModel student) {
        repository.updateStudent(student);
    }

    public void deleteStudent(StudentModel student) {
        repository.deleteStudent(student);
    }

    public LiveData<List<StudentModel>> getStudentByTeacher(int teacherId) {
        return repository.getStudentByTeacher(teacherId);
    }

    public LiveData<List<TeacherModel>> getAllTeacher() {
        return repository.getAllTeacher();
    }
}
