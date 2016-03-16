package com.zxa.app.iBike;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxa on 16/3/16.
 */
public class DatabaseOperator {
    private static final String TABLE_NAME = "bikeRecord";//要操作的数据表的名称
    private SQLiteDatabase db=null; //数据库操作

    //构造函数
    public DatabaseOperator(SQLiteDatabase db)
    {
        this.db=db;
    }

    //  //插入操作
//  public void insert(int id,String nid,String sid,int type,
//          String stime,String etime,String desc,String locate_main,String locate_detail,int state)
//  {
//      String sql = "INSERT INTO " + TABLE_NAME + " (id,nid,sid,type,stime,etime,desc,locate_main,locate_detail,state)"
//              + " VALUES(?,?,?,?,?,?,?,?,?,?)";
//      Object args[]=new Object[]{id,nid,sid,type,stime,etime,desc,locate_main,locate_detail,state};
//      this.db.execSQL(sql, args);
//      this.db.close();
//  }
    //插入重载操作
    public void insert(int id,int state)
    {
        String sql = "INSERT INTO " + TABLE_NAME + " (id,state)" +" VALUES(?,?)";
        Object args[]=new Object[]{id,state};
        this.db.execSQL(sql, args);
        this.db.close();
    }


    //更新操作
    public void update(int id,int state)
    {
        String sql = "UPDATE " + TABLE_NAME + " SET state=? WHERE id=?";
        Object args[]=new Object[]{state,id};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    //删除操作,删除
    public void delete(int id)
    {
        String sql = "DELETE FROM " + TABLE_NAME +" WHERE id=?";
        Object args[]=new Object[]{id};
        this.db.execSQL(sql, args);
        this.db.close();
    }

    //查询操作,查询表中所有的记录返回列表
    public List<String> find()
    {
        List<String> all = new ArrayList<String>(); //此时只是String
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor result = this.db.rawQuery(sql, null);    //执行查询语句
        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )   //采用循环的方式查询数据
        {
            all.add(result.getInt(0)+","+result.getString(1)+","+result.getString(2)+","+result.getInt(3)+","
                    +result.getString(4)+","+result.getString(5)+","+result.getString(6)+","+result.getString(7)+","
                    +result.getString(8));
        }
        this.db.close();
        return all;
    }

    //查询操作虫重载函数，返回指定ID的列表
    public int getstatebyID(int id)
    {
        int num=-1;//错误状态-1
        List<String> all = new ArrayList<String>(); //此时只是String
        String sql = "SELECT state FROM " + TABLE_NAME + " where id=?" ;
        String args[] = new String[]{String.valueOf(id)};
        Cursor result = this.db.rawQuery(sql, args);
        for(result.moveToFirst();!result.isAfterLast();result.moveToNext()  )
        {
            num=result.getInt(0);
        }

        Log.e("database", "图片状态state"+ String.valueOf(num));
        this.db.close();
        return num;
    }

    //判断插入数据的ID是否已经存在数据库中。
    public boolean check_same(int id)
    {
        String sql="SELECT id from " + TABLE_NAME + " where id = ?";
        String args[] =new String[]{String.valueOf(id)};
        Cursor result=this.db.rawQuery(sql,args);
        Log.e("database", "the sql has been excuate");

        Log.e("database","the hang count" + String.valueOf(result.getCount()));

        if(result.getCount()==0)//判断得到的返回数据是否为空
        {
            Log.e("database", "return false and not exist the same result" + String.valueOf(result.getCount()));
            this.db.close();
            return false;
        }
        else
        {
            Log.e("database", "return true and exist the same result"+ String.valueOf(result.getCount()));
            this.db.close();
            return true;
        }
    }
}
