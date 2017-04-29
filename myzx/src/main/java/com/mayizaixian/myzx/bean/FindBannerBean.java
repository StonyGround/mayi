package com.mayizaixian.myzx.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class FindBannerBean {
    /**
     * activity_id : 2
     * activity_pic : http://o7rjr97uo.qnssl.com/o_1ak44hfa910pj12ku11uq15j81j6m9.jpg?imageView2/1/w/480/h/200
     * activity_url : http://t.mayionline.cn/activity/online
     */

    private List<BannerBean> banner;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        private String activity_id;
        private String activity_pic;
        private String activity_url;

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getActivity_pic() {
            return activity_pic;
        }

        public void setActivity_pic(String activity_pic) {
            this.activity_pic = activity_pic;
        }

        public String getActivity_url() {
            return activity_url;
        }

        public void setActivity_url(String activity_url) {
            this.activity_url = activity_url;
        }
    }
}
