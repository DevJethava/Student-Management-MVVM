package in.dev.android.studentmanagement.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import in.dev.android.studentmanagement.room.database.RoomDB;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;

public class TeacherRepository {

    MutableLiveData<List<TeacherModel>> data;

    public static final String TAG = TeacherRepository.class.getSimpleName();
    RoomDB roomDB;
    Context context;

    public TeacherRepository(Context context) {
        this.context = context;
        roomDB = RoomDB.getInstance(context);
    }

    public void insertTeacher(TeacherModel teacher) {
        roomDB.teacherDAO().insert(teacher);
    }

    public void updateTeacher(TeacherModel teacher) {
        roomDB.teacherDAO().update(teacher);
    }

    public void deleteTeacher(TeacherModel teacher) {
        roomDB.teacherDAO().delete(teacher);
    }

    public MutableLiveData<List<TeacherModel>> getTeacherList() {
        MutableLiveData<List<TeacherModel>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(roomDB.teacherDAO().getList());
        return mutableLiveData;
    }

}
