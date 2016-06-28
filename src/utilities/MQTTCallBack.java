package utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by g2525_000 on 2016/4/20.
 */
public class MQTTCallBack implements MqttCallback {


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Disconnected");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Topic :" + topic);
        System.out.println("Qos : " + message.getQos());
        System.out.println("Message : " + new String(message.getPayload()));


        //json parse
        String jsonText = new String(message.getPayload());
        JSONParser parser = new JSONParser();
        JSONObject newJObject = null;
        try {
            newJObject = (JSONObject) parser.parse(jsonText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Convert UTC to Local Time
        Date cstTime = new Date(newJObject.get("date").toString());
        Timestamp time_st = new Timestamp(cstTime.getTime());

        MQTTSubscriber.fw.write("location:" + newJObject.get("location"));
        MQTTSubscriber.fw.write("\r\n");
        MQTTSubscriber.fw.write("date:" + time_st);
        MQTTSubscriber.fw.write("\r\n");
        MQTTSubscriber.fw.write("date_stamp:" + cstTime.getTime());
        MQTTSubscriber.fw.write("\r\n");
        MQTTSubscriber.fw.write("displayName:" + newJObject.get("displayName"));
        MQTTSubscriber.fw.write("\r\n");
        MQTTSubscriber.fw.write("value:" + newJObject.get("value"));
        MQTTSubscriber.fw.write("\r\n");
        MQTTSubscriber.fw.flush();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
