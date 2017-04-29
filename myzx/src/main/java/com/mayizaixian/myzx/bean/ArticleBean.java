package com.mayizaixian.myzx.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class ArticleBean {
    /**
     * data : [{"id":"27","name":"有奖活动丨榜星争夺战","type_id":"1","url":"http://t.mayionline.cn/article/index/27"},{"id":"17","name":"蚂蚁在线iOS App上线啦！","type_id":"1","url":"http://t.mayionline.cn/article/index/17"},{"id":"11","name":"关于现金劵使用规则的修订","type_id":"1","url":"http://t.mayionline.cn/article/index/11"},{"id":"6","name":"满标补贴活动，温馨上线","type_id":"1","url":"http://t.mayionline.cn/article/index/6"}]
     * message : success
     * status : 200
     */

    private String message;
    private int status;
    /**
     * id : 27
     * name : 有奖活动丨榜星争夺战
     * type_id : 1
     * url : http://t.mayionline.cn/article/index/27
     */

    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        private String type_id;
        private String url;

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

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
