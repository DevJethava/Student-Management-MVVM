package in.dev.android.studentmanagement.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import in.dev.android.studentmanagement.room.model.TeacherModel;

@Dao
public interface TeacherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TeacherModel model);

    @Update
    void update(TeacherModel model);

    @Delete
    void delete(TeacherModel model);

    @Query("SELECT * FROM tbl_teacher")
    List<TeacherModel> getList();
}
