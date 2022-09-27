package mao.android_login_page_implement_remember_password_function_sqliteopenhelper.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mao.android_login_page_implement_remember_password_function_sqliteopenhelper.entity.User;

/**
 * Project name(项目名称)：android_login_page_implement_remember_password_function_SQLiteOpenHelper
 * Package(包名): mao.android_login_page_implement_remember_password_function_sqliteopenhelper.dao
 * Class(类名): UserDao
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/27
 * Time(创建时间)： 10:49
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class UserDao extends SQLiteOpenHelper
{
    /**
     * 数据库名字
     */
    private static final String DB_NAME = "user.db";

    /**
     * 表名
     */
    private static final String TABLE_NAME = "user_info";

    /**
     * 数据库版本
     */
    private static final int DB_VERSION = 1;

    /**
     * 实例，单例模式，懒汉式，双重检查锁方式
     */
    private static volatile UserDao userDao = null;

    /**
     * 读数据库
     */
    private SQLiteDatabase readDatabase;
    /**
     * 写数据库
     */
    private SQLiteDatabase writeDatabase;

    /**
     * 标签
     */
    private static final String TAG = "UserDao";


    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public UserDao(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 获得实例
     *
     * @param context 上下文
     * @return {@link UserDao}
     */
    public static UserDao getInstance(Context context)
    {
        if (userDao == null)
        {
            synchronized (UserDao.class)
            {
                if (userDao == null)
                {
                    userDao = new UserDao(context);
                }
            }
        }
        return userDao;
    }

    /**
     * 打开读连接
     *
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase openReadConnection()
    {
        if (readDatabase == null || !readDatabase.isOpen())
        {
            readDatabase = userDao.getReadableDatabase();
        }
        return readDatabase;
    }

    /**
     * 打开写连接
     *
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase openWriteConnection()
    {
        if (writeDatabase == null || !writeDatabase.isOpen())
        {
            writeDatabase = userDao.getWritableDatabase();
        }
        return readDatabase;
    }

    /**
     * 关闭数据库读连接和写连接
     */
    public void closeConnection()
    {
        if (readDatabase != null && readDatabase.isOpen())
        {
            readDatabase.close();
            readDatabase = null;
        }

        if (writeDatabase != null && writeDatabase.isOpen())
        {
            writeDatabase.close();
            writeDatabase = null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "phone VARCHAR PRIMARY KEY NOT NULL," +
                "password VARCHAR NOT NULL," +
                "lastUpdateTime LONG NOT NULL)";

        db.execSQL(sql);
    }

    /**
     * 数据库版本更新时触发回调
     *
     * @param db         SQLiteDatabase
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }


    /**
     * 查询所有
     *
     * @return {@link List}<{@link User}>
     */
    public List<User> queryAll()
    {
        List<User> list = new ArrayList<>();

        Cursor cursor = readDatabase.query(TABLE_NAME, null, "1=1", new String[]{}, null, null, null);

        while (cursor.moveToNext())
        {
            User user = new User();
            setUser(cursor, user);
            list.add(user);
        }

        cursor.close();
        return list;
    }

    /**
     * 通过id查询
     *
     * @param phone 电话
     * @return {@link User}
     */
    public User queryById(Serializable phone)
    {
        User user = null;
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "phone=?", new String[]{String.valueOf(phone)}, null, null, null);
        if (cursor.moveToNext())
        {
            user = new User();
            setUser(cursor, user);
        }
        cursor.close();
        return user;
    }

    /**
     * 插入一条数据
     *
     * @param user User对象
     * @return boolean
     */
    public boolean insert(User user)
    {
        ContentValues contentValues = new ContentValues();
        setContentValues(user, contentValues);
        long insert = writeDatabase.insert(TABLE_NAME, null, contentValues);
        return insert > 0;
    }

    /**
     * 插入多条数据
     *
     * @param list 列表
     * @return boolean
     */
    public boolean insert(List<User> list)
    {
        try
        {
            writeDatabase.beginTransaction();
            for (User user : list)
            {
                boolean insert = this.insert(user);
                if (!insert)
                {
                    throw new Exception();
                }
            }
            writeDatabase.setTransactionSuccessful();
            return true;
        }
        catch (Exception e)
        {
            writeDatabase.endTransaction();
            Log.e(TAG, "insert: ", e);
            return false;
        }
    }

    /**
     * 更新
     *
     * @param user User对象
     * @return boolean
     */
    public boolean update(User user)
    {
        ContentValues contentValues = new ContentValues();
        setContentValues(user, contentValues);
        int update = writeDatabase.update(TABLE_NAME, contentValues, "phone=?", new String[]{user.getPhone()});
        return update > 0;
    }

    /**
     * 插入或更新，先尝试插入，如果插入失败，更新
     *
     * @param user 用户
     * @return boolean
     */
    public boolean insertOrUpdate(User user)
    {
        boolean insert = insert(user);
        if (insert)
        {
            return true;
        }
        return update(user);
    }


    /**
     * 删除
     *
     * @param phone phone
     * @return boolean
     */
    public boolean delete(Serializable phone)
    {
        int delete = writeDatabase.delete(TABLE_NAME, "phone=?", new String[]{(String) phone});
        return delete > 0;
    }

    /**
     * 填充ContentValues
     *
     * @param user          User
     * @param contentValues ContentValues
     */
    private void setContentValues(User user, ContentValues contentValues)
    {
        contentValues.put("phone", user.getPhone());
        contentValues.put("password", user.getPassword());
        contentValues.put("lastUpdateTime", user.getLastUpdateTime());
    }

    /**
     * 填充User
     *
     * @param cursor 游标
     * @param user   用户
     */
    private User setUser(Cursor cursor, User user)
    {
        user.setPhone(cursor.getString(0));
        user.setPassword(cursor.getString(1));
        user.setLastUpdateTime(cursor.getLong(2));

        return user;
    }

    public User queryLastTime()
    {
        User user = null;
        Cursor cursor = readDatabase.query(TABLE_NAME, null, "1=1", new String[]{}, null, null, "lastUpdateTime desc");
        if (cursor.moveToNext())
        {
            user = setUser(cursor, new User());
        }
        cursor.close();
        return user;
    }

}
