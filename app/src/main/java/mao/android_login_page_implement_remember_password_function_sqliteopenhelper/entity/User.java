package mao.android_login_page_implement_remember_password_function_sqliteopenhelper.entity;


import java.util.Date;

/**
 * Project name(项目名称)：android_login_page_implement_remember_password_function_SQLiteOpenHelper
 * Package(包名): mao.android_login_page_implement_remember_password_function_sqliteopenhelper.entity
 * Class(类名): User
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/27
 * Time(创建时间)： 10:47
 * Version(版本): 1.0
 * Description(描述)： 无
 */


public class User
{
    /**
     * 电话
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 最后更新时间，存放的是时间戳
     * 1970 年 1 月 1 日 00:00:00 GMT 以来的毫秒数
     */
    private Long lastUpdateTime;

    /**
     * Instantiates a new User.
     */
    public User()
    {

    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone()
    {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets last update time.
     *
     * @return the last update time
     */
    public Long getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    /**
     * Sets last update time.
     *
     * @param lastUpdateTime the last update time
     */
    public void setLastUpdateTime(Long lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 设置最后更新时间
     */
    public void setLastUpdateTime()
    {
        this.lastUpdateTime = new Date().getTime();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        User user = (User) o;

        if (getPhone() != null ? !getPhone().equals(user.getPhone()) : user.getPhone() != null)
        {
            return false;
        }
        if (getPassword() != null ? !getPassword().equals(user.getPassword()) : user.getPassword() != null)
        {
            return false;
        }
        return getLastUpdateTime() != null ? getLastUpdateTime().equals(user.getLastUpdateTime()) : user.getLastUpdateTime() == null;
    }

    @Override
    public int hashCode()
    {
        int result = getPhone() != null ? getPhone().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getLastUpdateTime() != null ? getLastUpdateTime().hashCode() : 0);
        return result;
    }

    @Override
    @SuppressWarnings("all")
    public String toString()
    {
        final StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("phone：").append(phone).append('\n');
        stringbuilder.append("password：").append(password).append('\n');
        stringbuilder.append("lastUpdateTime：").append(lastUpdateTime).append('\n');
        return stringbuilder.toString();
    }
}
