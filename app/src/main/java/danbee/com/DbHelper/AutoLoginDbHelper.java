package danbee.com.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import danbee.com.MainActivity;
import danbee.com.UserInfo;

public class AutoLoginDbHelper {
    private static SQLiteDatabase database;
    private static String createTableAutoLoginSql = "create table if not exists auto" +
            "(" +
            "    _id integer primary key autoincrement, " +
            "    state int, " +
            "    userid string, " +
            "    phone string, " +
            "    name string, " +
            "    gender int, " +
            "    birth string" +
            ")";

    //db이름 autologin
    public static void openDatabase(Context context, String databaseName) {
        Log.d("test","openDatabase호출됨");

        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
                Log.d("test","데이터베이스 " + databaseName + " 오픈됨.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //테이블생성
    public static void createAutoTable() {
        Log.d("test","createCommentTable 호출됨");

        if (database != null) {
            database.execSQL(createTableAutoLoginSql);  //table 생성
            //comment 테이블 생성요청됨
        } else {
            Log.d("test", "db오픈안됨");
        }
    }


    //insert
    public static void insertData(int state, String userid, String phone, String name, int gender, String birth ) {
        Log.d("test","insertData()호출됨");

        if(database != null){
            String sql ="insert into auto( state, userid, phone, name, gender, birth) values(?, ?, ?, ?, ?, ?)";
            Object[] params = { state, userid, phone, name, gender, birth };
            database.execSQL(sql, params);   //2번째인자로 ?를 대체함
            Log.d("test","데이터삽입성공");
        }else
        {
            Log.d("test","db오픈안됨");
        }
    }

    public static void selectData() {
        Log.d("test","selectdata호출");

        if(database != null){
            String sql="select * from auto";
            Cursor cursor = database.rawQuery(sql, null);//결과값이존재하는 쿼리문
            Log.d("test","조회된 데이터 개수 : "+ cursor.getCount());
            if(cursor.getCount() == 0)
                return;
            int i = 0;
            cursor.moveToFirst();
            int state = cursor.getInt(1);

            //자동로그인상태면
            if(state == 1){
                UserInfo.info.setUserid(cursor.getString(2));
                UserInfo.info.setPhone(cursor.getString(3));
                UserInfo.info.setName(cursor.getString(4));
                UserInfo.info.setGender(cursor.getInt(5));
                UserInfo.info.setBirth(cursor.getString(6));
                UserInfo.info.setLoginState(true);
                Log.d("test", "자동로그인 유저정보저장");

            }

            cursor.close();
        }
    }

    public static void deleteLog(){
        database.execSQL("delete from auto");  //기존table삭제
        Log.d("test", "테이블 삭제");

    }

}
