package com.ordering.po;

import java.util.Date;

public class Orders {
    private Integer orderid;

    private Date createtime;//����ʱ��

    private Integer number;//��Ʒ����

    private String discuss;//��������

    private Integer pay;//�Ƿ�֧��

    private Integer statu;//�Ƿ��������

    private Integer total;//�ܼ�

    private String phone;//��ϵ��ʽ

    private String address;//�ջ���ַ

    private String uname;//�ջ�������

    private Integer userId;//�û�id

    private Integer shoreId;//�̼�id

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss == null ? null : discuss.trim();
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getShoreId() {
        return shoreId;
    }

    public void setShoreId(Integer shoreId) {
        this.shoreId = shoreId;
    }
}