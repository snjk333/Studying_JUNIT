package com.oleksandr.junit.dao;

import java.util.HashMap;
import java.util.Map;

public class UserDaoSpy extends UserDao {

    private UserDao userDao;
    private Map<Integer, Boolean> answers = new HashMap<>();

    public UserDaoSpy(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean delete(int id) {
        return answers.getOrDefault(id,userDao.delete(id));
    }
}
