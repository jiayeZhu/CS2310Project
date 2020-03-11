import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class NewCreateGesture {
    // scope of this component
    private static final String SCOPE = "SIS.Scope1";
    // name of this component
    private static final String NAME = "Gesture";
    // messages types that can be handled by this component
    private static List<String> TYPES = new ArrayList<>(Arrays.asList("Setting", "Confirm"));

    private static final String MQ_HOST = "192.168.30.133";
    private static final String COMPONENT_QUEUE_NAME = SCOPE+"/"+NAME;
    private static final String SIS_QUEUE_NAME = "SISMQ";

    public static void main(String[] args) {
        try {
            GestureComponent gestureComponent = new GestureComponent(MQ_HOST, COMPONENT_QUEUE_NAME, SIS_QUEUE_NAME, SCOPE, NAME, TYPES);
            DeliverCallback cb = (consumerTag, delivery)->{
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
                if (message.length() > 2){ gestureComponent.handleMessage(message); }
            };
            gestureComponent.getDecoder().setMQConsumer(cb);
            gestureComponent.connect();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
