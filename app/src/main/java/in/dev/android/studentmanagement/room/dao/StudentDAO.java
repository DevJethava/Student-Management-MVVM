package in.dev.android.studentmanagement.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import in.dev.android.studentmanagement.room.model.StudentModel;

@Dao
public interface StudentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StudentModel model);

    @Update
    void update(StudentModel model);

    @Delete
    void delete(StudentModel model);

    @Query("SELECT * FROM tbl_student WHERE teacher_id=:teacherId")
    List<StudentModel> getStudentOfTeacher(int teacherId);
}
