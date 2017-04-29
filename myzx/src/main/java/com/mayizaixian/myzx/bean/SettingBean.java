package com.mayizaixian.myzx.bean;

/**
 * Created by Administrator on 2016/7/15.
 */
public class SettingBean {
    /**
     * status : 200
     * message : success
     * data : {"invite":"18308469","card_id":"410423********2519","user_img":"http://t.mayionline.cn/resources/images/user_1.png?v=5c01693fd9","bank":"62122********587"}
     */

    private int status;
    private String message;
    /**
     * invite : 18308469
     * card_id : 410423********2519
     * user_img : http://t.mayionline.cn/resources/images/user_1.png?v=5c01693fd9
     * bank : 62122********587
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
        private String invite;
        private String card_id;
        private String user_img;
        private String bank;

        public String getInvite() {
            return invite;
        }

        public void setInvite(String invite) {
            this.invite = invite;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getUser_img() {
            return user_img;
        }

        public void setUser_img(String user_img) {
            this.user_img = user_img;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }
    }
}
