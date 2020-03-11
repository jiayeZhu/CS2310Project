import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class BasicComponent {
    protected String SCOPE;
    protected String MQ_HOST;
    protected String COMPONENT_QUEUE_NAME;
    protected String SIS_QUEUE_NAME;
    protected String NAME;
    protected List<String> TYPES;
    protected MQConnection mq;
    protected MsgEncoder encoder;
    protected MsgDecoder decoder;
    protected DeliverCallback cb;

    public BasicComponent(String MQ_HOST, String COMPONENT_QUEUE_NAME, String SIS_QUEUE_NAME, String SCOPE, String NAME, List<String> TYPES) throws IOException, TimeoutException {
        this.MQ_HOST = MQ_HOST;
        this.COMPONENT_QUEUE_NAME = COMPONENT_QUEUE_NAME;
        this.SIS_QUEUE_NAME = SIS_QUEUE_NAME;
        this.SCOPE = SCOPE;
        this.NAME = NAME;
        this.TYPES = TYPES;
        this.mq = new MQConnection(this.MQ_HOST);
        this.encoder = new MsgEncoder(this.mq.getConnection().createChannel(),this.SIS_QUEUE_NAME);
        this.decoder = new MsgDecoder(this.mq.getConnection().createChannel(),this.COMPONENT_QUEUE_NAME);
    }

    public MsgEncoder getEncoder() {
        return encoder;
    }

    public MsgDecoder getDecoder() {
        return decoder;
    }
    public void closeAll() throws IOException, TimeoutException {
        this.decoder.close();
        this.encoder.close();
        this.mq.close();
    }
    public void connect() throws IOException {
        KeyValueList conn = new KeyValueList();
        conn.putPair("Scope", this.SCOPE);
        conn.putPair("MessageType", "Connect");
        conn.putPair("Role", "Basic");
        conn.putPair("Name", this.NAME);
        this.encoder.sendMsgToMQ(conn);
    }

    public void handleMessage(String message) {
        KeyValueList kvList = KeyValueList.decodedKV(message.substring(1, message.length() - 1));
        try {
            this.ProcessMsg(kvList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ProcessMsg(KeyValueList kvList) throws Exception {}
}
