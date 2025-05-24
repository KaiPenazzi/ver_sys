package com.node;

import com.common.JsonUtil;
import com.common.messages.InfoMessage;
import com.common.messages.StartMessage;

class Main {
    public static void main(String[] args) {
        System.out.println("hello from node");
        var start_msg = new StartMessage();
        var info_msg = new InfoMessage("idk where im from");

        try {
            String json = JsonUtil.toJson(start_msg);
            System.out.println(json);

            String json2 = JsonUtil.toJson(info_msg);
            System.out.println(json2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
