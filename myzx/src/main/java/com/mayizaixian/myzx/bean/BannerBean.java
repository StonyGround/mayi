package com.mayizaixian.myzx.bean;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class BannerBean {

    /**
     * status : 200
     * message : success
     * data : [{"id":"21","name":"榜单争夺战","location":"1","url":"https://m.mayizaixian.cn/article/index/27","pic":"http://t.mayionline.cn/data/upfiles/banner/146434572778.png"},{"id":"12","name":"一分钟读懂\u201c供应链金融\u201d","location":"1","url":"http://www.rabbitpre.com/m/uyzraMuq5?sui=YmVr9Kfb&lc=8#from=share","pic":"http://t.mayionline.cn/data/upfiles/banner/145871462342.png"},{"id":"2","name":"供应链","location":"1","url":"#","pic":"http://t.mayionline.cn/data/upfiles/banner/144965884812.jpg"},{"id":"3","name":"注册送3000元","location":"1","url":"/passport/register","pic":"http://t.mayionline.cn/data/upfiles/banner/145827938476.png"}]
     */

    private int status;
    private String message;
    /**
     * id : 21
     * name : 榜单争夺战
     * location : 1
     * url : https://m.mayizaixian.cn/article/index/27
     * pic : http://t.mayionline.cn/data/upfiles/banner/146434572778.png
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String name;
        private String location;
        private String url;
        private String pic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
