package com.mayizaixian.myzx.bean;

/**
 * Created by Administrator on 2016/7/13.
 */
public class LoginBean {
    /**
     * status : 200
     * message : success
     * data : {"secret":"6c66564ef7e06f0ef9e3984e6e17449d","uid":"96370","keep_code":"52a4dbd46b52fc86c1c003c8717f885b"}
     */

    private int status;
    private String message;
    /**
     * secret : 6c66564ef7e06f0ef9e3984e6e17449d
     * uid : 96370
     * keep_code : 52a4dbd46b52fc86c1c003c8717f885b
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
        private String secret;
        private String uid;
        private String keep_code;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getKeep_code() {
            return keep_code;
        }

        public void setKeep_code(String keep_code) {
            this.keep_code = keep_code;
        }
    }
}
