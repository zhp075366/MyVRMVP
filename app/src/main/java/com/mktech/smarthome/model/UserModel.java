package com.mktech.smarthome.model;

import com.mktech.smarthome.SmartHomeApplication;
import com.mktech.smarthome.greendao.UserBeanDao;
import com.mktech.smarthome.model.bean.UserBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 作者：Created by ZouHaiping on 2017/5/26
 * 邮箱：haiping.zou@gotechcn.cn
 * 公司：MKTech
 */
public class UserModel {

    private UserBeanDao mUserDao;

    public UserModel() {
        this.mUserDao = SmartHomeApplication.getApplication().getDaoSession().getUserBeanDao();
    }

    public void addUser(String name) {
        UserBean mUser = new UserBean();
        mUser.setUsername(name);
        mUserDao.insert(mUser);
    }

    public void deleteUser(String name) {
        List<UserBean> listUser = queryUserByName(name);
        for(int i = 0; i < listUser.size(); i++) {
            UserBean user = listUser.get(i);
            //根据Entity删除数据
            mUserDao.delete(user);
            //根据Id删除数据
            //mUserDao.deleteByKey(user.getId());
        }
    }

    private List<UserBean> queryUserByName(String name) {
        QueryBuilder<UserBean> qb = mUserDao.queryBuilder();
        return qb.where(UserBeanDao.Properties.Username.eq(name)).list();

        //查询姓名为小红且id为1的数据，并以id为升序排列
        //List<UserBean> users = qb.where(qb.and(UserDao.Properties.Name.eq("小红"),UserDao.Properties.Id.eq((long)1)))
        //.orderAsc(UserDao.Properties.Id).list();
    }

    public void updateUser(String updateId, String updatedName) {
        Long userId = Long.parseLong(updateId);
        UserBean findUser = mUserDao.queryBuilder().where(UserBeanDao.Properties.Id.eq(userId)).build().unique();
        if(findUser != null) {
            findUser.setUsername(updatedName);
            mUserDao.update(findUser);
        }
    }

    public List<UserBean> loadAllUser() {
        return mUserDao.loadAll();
    }

    public void deleteAllUser() {
        mUserDao.deleteAll();
    }

    private void executeSql() {
        try {
            String sql = "select ADD_TEST from USERS_TABLE where userName = '小明'";
            SmartHomeApplication.getApplication().getDaoSession().getDatabase().execSQL(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
