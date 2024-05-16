package com.mashibing.tx.xml.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class BookDao {

    JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 减去某个用户的余额
     * @param userName
     * @param price
     */
    public void updateBalance(String userName,int price){
        String sql = "UPDATE `mysql-learn`.`SUser` SET `email` = 'qxlxi@qq.com', `email_2` = 'qxlxi@qq.com' WHERE `ID` = 1;";
        jdbcTemplate.update(sql);
    }

    /**
     * 按照图书的id来获取图书的价格
     * @param id
     * @return
     */
    public int getPrice(int id){
        String sql = "select id SUser where id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id);
    }

    /**
     * 减库存，减去某本书的库存
     * @param id
     */
    public void updateStock(int id){
        String sql = "UPDATE `mysql-learn`.`SUser` SET `email` = 'qxlxi@qq.com', `email_2` = 'qxlxi@qq.com' WHERE `ID` = 1;";
        jdbcTemplate.update(sql);
//        for (int i = 1 ;i>=0 ;i--)
//            System.out.println(10/i);
    }
}