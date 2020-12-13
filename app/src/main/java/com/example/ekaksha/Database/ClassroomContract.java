package com.example.ekaksha.Database;

import android.provider.BaseColumns;

public final class ClassroomContract {
    private ClassroomContract(){}
    public   final static class joined implements BaseColumns{

        public final static String JOINED_TABLE_NAME="joinedClassroomTable";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_DESCRIPTION="description";
        public final static String COLUMN_CLASSROOM_ID="classroomId";
        public final static String COLUMN_TEACHER="teacher";

    }
    public   final static class AssignmentList implements BaseColumns{

        public final static String JOINED_TABLE_NAME="assignmentsListTable";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_DESCRIPTION="description";
        public final static String COLUMN_CLASSROOM_ID="classroomId";
        public final static String COLUMN_ASSIGNMENT_ID="assignmnetId";
        public final static String COLUMN_CLASSROOM_NAME="classroomName";
        public final static String COLUMN_URL="assignmentUrl";
        public final static String COLUMN_DEADLINE="deadline";



    }
    public   final static class ExaminationList implements BaseColumns{

        public final static String JOINED_TABLE_NAME="examinationListTable";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_DESCRIPTION="description";
        public final static String COLUMN_CLASSROOM_ID="classroomId";
        public final static String COLUMN_CLASSROOM_NAME="classroomName";
        public final static String COLUMN_URL="examinationUrl";
        public final static String COLUMN_START="start";
        public final static String COLUMN_END="end";
        public final static String COLUMN_MAXIMUM_MARKS="maximumMarks";



    }
    public   final static class MessageList implements BaseColumns{

        public final static String TABLE_NAME="MessageListTable";
        public final static String _ID=BaseColumns._ID;
        public final static String COLUMN_MESSAGE_ID="messageId";
        public final static String COLUMN_NAME="name";
        public final static String COLUMN_CLASSROOM_ID="classroomId";
        public final static String COLUMN_URL="meaasgeUrl";
        public final static String COLUMN_message="message";
        public final static String COLUMN_TIME="time";



    }
}

