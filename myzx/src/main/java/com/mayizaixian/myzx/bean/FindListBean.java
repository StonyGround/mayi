package com.mayizaixian.myzx.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class FindListBean {
    /**
     * activity_name : 加息赚不停
     * url : /activity/raise
     * count : 5009
     * days : 1424
     * over :
     * image : http://o7rjr97uo.qnssl.com/o_1akafeoo7d951hec12ag10hc55h9.png?imageView2/1/w/600/h/250
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String activity_name;
        private String url;
        private String count;
        private int days;
        private String over;
        private String image;

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public String getOver() {
            return over;
        }

        public void setOver(String over) {
            this.over = over;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
