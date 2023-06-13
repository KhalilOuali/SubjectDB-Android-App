package my.subjectdb.db;

import android.provider.BaseColumns;

import androidx.annotation.NonNull;

public class Subject {
    public static final int TYPE_C = 0;
    public static final int TYPE_C_TD = 1;
    public static final int TYPE_C_TD_TP = 2;

    private long id;
    private String name;
    private String prof;
    private String classCode;
    private boolean hasFinal;
    private int type;

    public Subject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(long id, String name, String prof, String classCode, int type, boolean hasFinal) {
        this.id = id;
        this.name = name;
        this.prof = prof;
        this.classCode = classCode;
        this.type = type;
        this.hasFinal = hasFinal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getHasFinal() {
        return hasFinal;
    }

    public void setHasFinal(boolean hasFinal) {
        this.hasFinal = hasFinal;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s [%d]", name, id);
    }

    public static class DBEntry implements BaseColumns {
        public static final String TABLE_NAME = "subjects";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PROF = "prof";
        public static final String COLUMN_CLASSCODE = "classcode";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_HASFINAL = "hasfinal";
    }
}
