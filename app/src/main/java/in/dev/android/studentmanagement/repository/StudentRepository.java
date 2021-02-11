package in.dev.android.studentmanagement.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.dev.android.studentmanagement.room.database.RoomDB;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class StudentRepository {

    public static final String TAG = StudentRepository.class.getSimpleName();
    RoomDB roomDB;
    Context context;

    public StudentRepository(Context context) {
        this.context = context;
        roomDB = RoomDB.getInstance(context);
    }

    public void insertStudent(StudentModel student) {
        roomDB.studentDAO().insert(student);
    }

    public void updateStudent(StudentModel student) {
        roomDB.studentDAO().update(student);
    }

    public void deleteStudent(StudentModel student) {
        roomDB.studentDAO().delete(student);
    }

    public LiveData<List<StudentModel>> getStudentByTeacher(int teacherId) {
        MutableLiveData<List<StudentModel>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(roomDB.studentDAO().getStudentOfTeacher(teacherId));
        return mutableLiveData;
    }

    public LiveData<List<TeacherModel>> getAllTeacher() {
        MutableLiveData<List<TeacherModel>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(roomDB.teacherDAO().getList());
        return mutableLiveData;
    }
}
