package com.mayizaixian.myzx.bean;

/**
 * Created by Administrator on 2016/7/13.
 */
public class UserInfoBean {
    /**
     * status : 200
     * message : success
     * data : {"total_interest_month_q":"8","total_interest_month_h":"88","avatar":"http://t.mayionline.cn/resources/images/user_1.png?v=5c01693fd9","haitao_amount_right":"00","haitao_amount_left":"12","total_count":4,"today_interrest":"0.02","total_interest_show":"5.75"}
     */

    private int status;
    private String message;
    /**
     * total_interest_month_q : 8
     * total_interest_month_h : 88
     * avatar : http://t.mayionline.cn/resources/images/user_1.png?v=5c01693fd9
     * haitao_amount_right : 00
     * haitao_amount_left : 12
     * total_count : 4
     * today_interrest : 0.02
     * total_interest_show : 5.75
     */

    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String total_interest_month_q;
        private String total_interest_month_h;
        private String avatar;
        private String haitao_amount_right;
        private String haitao_amount_left;
        private int total_count;
        private String today_interrest;
        private String total_interest_show;
        private String capital_sum_user_interest;

        public String getCapital_sum_user_interest(){
            return capital_sum_user_interest;
        }

        public void setCapital_sum_user_interest(String capital_sum_user_interest) {
            this.capital_sum_user_interest = capital_sum_user_interest;
        }

        public String getTotal_interest_month_q() {
            return total_interest_month_q;
        }

        public void setTotal_interest_month_q(String total_interest_month_q) {
            this.total_interest_month_q = total_interest_month_q;
        }

        public String getTotal_interest_month_h() {
            return total_interest_month_h;
        }

        public void setTotal_interest_month_h(String total_interest_month_h) {
            this.total_interest_month_h = total_interest_month_h;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getHaitao_amount_right() {
            return haitao_amount_right;
        }

        public void setHaitao_amount_right(String haitao_amount_right) {
            this.haitao_amount_right = haitao_amount_right;
        }

        public String getHaitao_amount_left() {
            return haitao_amount_left;
        }

        public void setHaitao_amount_left(String haitao_amount_left) {
            this.haitao_amount_left = haitao_amount_left;
        }

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public String getToday_interrest() {
            return today_interrest;
        }

        public void setToday_interrest(String today_interrest) {
            this.today_interrest = today_interrest;
        }

        public String getTotal_interest_show() {
            return total_interest_show;
        }

        public void setTotal_interest_show(String total_interest_show) {
            this.total_interest_show = total_interest_show;
        }
    }
}
