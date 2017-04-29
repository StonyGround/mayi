package com.mayizaixian.myzx.bean;

/**
 * Created by kiddo on 16-9-13.
 */
public class IndexDevBean {
    /**
     * status : 200
     * message : success
     * data : {"name":"蚂蚁合伙人 不投资也赚","summary":"/invite","is_display":"1","adv_pic":"/resources/images/ht_areaimg.png","adv_url":"/activity/index_adv/advert"}
     */

    private int status;
    private String message;
    /**
     * name : 蚂蚁合伙人 不投资也赚
     * summary : /invite
     * is_display : 1
     * adv_pic : /resources/images/ht_areaimg.png
     * adv_url : /activity/index_adv/advert
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
        private String name;
        private String summary;
        private String is_display;
        private String adv_pic;
        private String adv_url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getIs_display() {
            return is_display;
        }

        public void setIs_display(String is_display) {
            this.is_display = is_display;
        }

        public String getAdv_pic() {
            return adv_pic;
        }

        public void setAdv_pic(String adv_pic) {
            this.adv_pic = adv_pic;
        }

        public String getAdv_url() {
            return adv_url;
        }

        public void setAdv_url(String adv_url) {
            this.adv_url = adv_url;
        }
    }
}
