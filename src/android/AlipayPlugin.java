package com.wicture.cordova.alipay;

/**
 * 这个插件是基于支付宝实现的的phonegap插件.
 * 该插件封装了支付宝的快捷支付模块.
 * 其中的一些业务数据还需要再次根据需求来处理.
 *
 * Copyright (c) Ruijin Xia
 *
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

public class AlipayPlugin extends CordovaPlugin  {

    public static final String TAG = "alipay-sdk";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {

        if (action.equals("alipay")) {
            try{

                JSONObject cityad = args.optJSONObject(0);
                String out_trade_no = cityad.optString("out_trade_no");
                String subject = cityad.optString("subject");
                String body = cityad.optString("bodtxt");
                String total_fee = cityad.optString("total_fee");
                String url = cityad.optString("callbackUrl");

                //服务器异步通知页面路径,需要自己定义  参数 notify_url，如果商户没设定，则不会进行该操作
                //String url = "http://host:port/path/Alipay/notify_url.aspx";

                final String orderInfo = getOrderInfo(out_trade_no,subject, body,total_fee,url);

                String sign = sign(orderInfo);
                try {
                    // 仅需对sign 做URL编码
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                        + getSignType();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(cordova.getActivity());
                        // 调用支付接口
                        String result = alipay.pay(payInfo);

                        //Log.i(TAG, "result = " + result);
                        callbackContext.success(result); // Thread-safe.

//                        Message msg = new Message();
//                        msg.what = SDK_PAY_FLAG;
//                        msg.obj = result;
//                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();

                return true;

            }catch(Exception ex){
                callbackContext.error("支付不成功！");
                return false;
            }

        }else {

            callbackContext.error("Invalid Action");
            return false;
        }

    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    Result resultObj = new Result((String) msg.obj);
                    String resultStatus = resultObj.resultStatus;

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(cordova.getActivity(), "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(cordova.getActivity(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(cordova.getActivity(), "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(cordova.getActivity(), "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfo( String out_trade_no,String subject,String body, String price,String url) {
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.DEFAULT_PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.DEFAULT_SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径 //服务器异步通知页面路径  参数 notify_url，如果商户没设定，则不会进行该操作
        orderInfo += "&notify_url=" + "\"" + url + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Keys.PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}