package in.dev.android.studentmanagement.view_model;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.dev.android.studentmanagement.repository.TeacherRepository;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class TeacherViewModel extends AndroidViewModel {

    private TeacherRepository repository;

    public TeacherViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TeacherRepository(application);
    }

    public void insertTeacher(TeacherModel teacher) {
        repository.insertTeacher(teacher);
    }

    public void updateTeacher(TeacherModel teacher) {
        repository.updateTeacher(teacher);
    }

    public void deleteTeacher(TeacherModel teacher) {
        repository.deleteTeacher(teacher);
    }

    public LiveData<List<TeacherModel>> getTeacherList() {
        return repository.getTeacherList();
    }
}
