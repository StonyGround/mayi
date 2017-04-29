package com.mayizaixian.myzx.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class HomeBean {

    /**
     * status : 200
     * message : success
     * data : [{"borrow_id":"80","borrow_coupon_id":"69","status":"2","cat":"家居","cats":"家居","time_start":"1467612124","time_limit":"30","borrow_title":"唯品会5号","apr":10,"discount":8.5,"total_discount":28,"is_repay":null,"is_full":false,"is_time":true,"is_can_tender":true,"degree":null,"is_chain":true,"chain_type":"GVP","title_pic":"http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png","pic":"http://o82gnovb0.qnssl.com/o_1amq82bdah3bm7ea8rtdc1g6mm.jpg?imageView2/1/w/684/h/253","time_now":1468487835,"url":"http://t.mayionline.cn/invest/detail/69"},{"borrow_id":"79","borrow_coupon_id":"65","status":"2","cat":"母婴供应链","cats":"母婴专区","time_start":"1464710400","time_limit":"90","borrow_title":"唯品会票据1号1期","apr":9,"discount":8.5,"total_discount":15,"is_repay":null,"is_full":false,"is_time":true,"is_can_tender":true,"degree":null,"is_chain":true,"chain_type":"GVP","title_pic":"http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png","pic":"http://o82gnovb0.qnssl.com/o_1ak3qjk1j12ihgv4cnuon8t7em.jpg?imageView2/1/w/684/h/253","time_now":1468487835,"url":"http://t.mayionline.cn/invest/detail/65"},{"borrow_id":"79","borrow_coupon_id":"66","status":"2","cat":"美妆专区","cats":"美妆专区","time_start":"1464710400","time_limit":"90","borrow_title":"唯品会票据1号1期","apr":9,"discount":8.5,"total_discount":15,"is_repay":null,"is_full":false,"is_time":true,"is_can_tender":true,"degree":null,"is_chain":true,"chain_type":"GVP","title_pic":"http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png","pic":"http://o82gnovb0.qnssl.com/o_1ak3qk6bl17b01jn51kic1ji41v1513.jpg?imageView2/1/w/684/h/253","time_now":1468487835,"url":"http://t.mayionline.cn/invest/detail/66"},{"borrow_id":"79","borrow_coupon_id":"67","status":"2","cat":"日用专区","cats":"日用专区","time_start":"1464710400","time_limit":"90","borrow_title":"唯品会票据1号1期","apr":9,"discount":8.5,"total_discount":15,"is_repay":null,"is_full":false,"is_time":true,"is_can_tender":true,"degree":null,"is_chain":true,"chain_type":"GVP","title_pic":"http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png","pic":"http://o82gnovb0.qnssl.com/o_1ak3qklbv174a1a879il1j1515bo1g.jpg?imageView2/1/w/684/h/253","time_now":1468487835,"url":"http://t.mayionline.cn/invest/detail/67"},{"borrow_id":"79","borrow_coupon_id":"68","status":"2","cat":"营养品专区","cats":"营养品专区","time_start":"1464710400","time_limit":"90","borrow_title":"唯品会票据1号1期","apr":9,"discount":8.5,"total_discount":15,"is_repay":null,"is_full":false,"is_time":true,"is_can_tender":true,"degree":null,"is_chain":true,"chain_type":"GVP","title_pic":"http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png","pic":"http://o82gnovb0.qnssl.com/o_1ak3ql3pp1nsa1glb1h5huvk16bg1t.jpg?imageView2/1/w/684/h/253","time_now":1468487835,"url":"http://t.mayionline.cn/invest/detail/68"}]
     * count : {"count":"62"}
     */

    private int status;
    private String message;
    /**
     * count : 62
     */

    private CountBean count;
    /**
     * borrow_id : 80
     * borrow_coupon_id : 69
     * status : 2
     * cat : 家居
     * cats : 家居
     * time_start : 1467612124
     * time_limit : 30
     * borrow_title : 唯品会5号
     * apr : 10
     * discount : 8.5
     * total_discount : 28
     * is_repay : null
     * is_full : false
     * is_time : true
     * is_can_tender : true
     * degree : null
     * is_chain : true
     * chain_type : GVP
     * title_pic : http://t.mayionline.cn/resources/images/ic_qiye/ic_qiye_GVP.png
     * pic : http://o82gnovb0.qnssl.com/o_1amq82bdah3bm7ea8rtdc1g6mm.jpg?imageView2/1/w/684/h/253
     * time_now : 1468487835
     * url : http://t.mayionline.cn/invest/detail/69
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

    public CountBean getCount() {
        return count;
    }

    public void setCount(CountBean count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class CountBean {
        private String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }

    public static class DataBean {
        private String borrow_id;
        private String borrow_coupon_id;
        private String status;
        private String cat;
        private String cats;
        private String time_start;
        private String time_limit;
        private String borrow_title;
        private double apr;
        private double discount;
        private double total_discount;
        private Object is_repay;
        private boolean is_full;
        private boolean is_time;
        private boolean is_can_tender;
        private Object degree;
        private boolean is_chain;
        private String chain_type;
        private String title_pic;
        private String pic;
        private int time_now;
        private String url;

        public String getBorrow_id() {
            return borrow_id;
        }

        public void setBorrow_id(String borrow_id) {
            this.borrow_id = borrow_id;
        }

        public String getBorrow_coupon_id() {
            return borrow_coupon_id;
        }

        public void setBorrow_coupon_id(String borrow_coupon_id) {
            this.borrow_coupon_id = borrow_coupon_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public String getCats() {
            return cats;
        }

        public void setCats(String cats) {
            this.cats = cats;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_limit() {
            return time_limit;
        }

        public void setTime_limit(String time_limit) {
            this.time_limit = time_limit;
        }

        public String getBorrow_title() {
            return borrow_title;
        }

        public void setBorrow_title(String borrow_title) {
            this.borrow_title = borrow_title;
        }

        public double getApr() {
            return apr;
        }

        public void setApr(int apr) {
            this.apr = apr;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getTotal_discount() {
            return total_discount;
        }

        public void setTotal_discount(double total_discount) {
            this.total_discount = total_discount;
        }

        public Object getIs_repay() {
            return is_repay;
        }

        public void setIs_repay(Object is_repay) {
            this.is_repay = is_repay;
        }

        public boolean getIs_full() {
            return is_full;
        }

        public void setIs_full(boolean is_full) {
            this.is_full = is_full;
        }

        public boolean getIs_time() {
            return is_time;
        }

        public void setIs_time(boolean is_time) {
            this.is_time = is_time;
        }

        public boolean getIs_can_tender() {
            return is_can_tender;
        }

        public void setIs_can_tender(boolean is_can_tender) {
            this.is_can_tender = is_can_tender;
        }

        public Object getDegree() {
            return degree;
        }

        public void setDegree(Object degree) {
            this.degree = degree;
        }

        public boolean getIs_chain() {
            return is_chain;
        }

        public void setIs_chain(boolean is_chain) {
            this.is_chain = is_chain;
        }

        public String getChain_type() {
            return chain_type;
        }

        public void setChain_type(String chain_type) {
            this.chain_type = chain_type;
        }

        public String getTitle_pic() {
            return title_pic;
        }

        public void setTitle_pic(String title_pic) {
            this.title_pic = title_pic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getTime_now() {
            return time_now;
        }

        public void setTime_now(int time_now) {
            this.time_now = time_now;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
