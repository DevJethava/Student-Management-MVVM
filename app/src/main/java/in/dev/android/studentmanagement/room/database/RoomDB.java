package in.dev.android.studentmanagement.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.dev.android.studentmanagement.room.dao.StudentDAO;
import in.dev.android.studentmanagement.room.dao.TeacherDAO;
import in.dev.android.studentmanagement.room.model.StudentModel;
import in.dev.android.studentmanagement.room.model.TeacherModel;

@Database(entities = {StudentModel.class, TeacherModel.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB instance;

    private static String DATABASE_NAME = RoomDB.class.getSimpleName();

    public synchronized static RoomDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract StudentDAO studentDAO();

    public abstract TeacherDAO teacherDAO();
}
