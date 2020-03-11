import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class NewInitializer {
    private final static String SIS_QUEUE_NAME = "SISMQ";
    private final static String MQ_HOST = "192.168.30.133";
    private final static int MAX_RETRY = 3;
    static KeyValueList parseFile(File file){
        try {
            JAXBContext context = JAXBContext.newInstance(Msg.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Msg msg = (Msg) unmarshaller.unmarshal(file);

            return XMLUtil.generateKV(msg);
        } catch (JAXBException e) {
            System.out.println("Parsing Init XML Fail: " + file.getName());
            return null;
        }
    }

    public static void main(String[] args) {
        Path path = FileSystems.getDefault().getPath("../xml/InitXML");
        for (int retryAttempt = 0; retryAttempt <= MAX_RETRY; retryAttempt++) {
            try {
                MQConnection mq = new MQConnection(MQ_HOST);
                MsgEncoder encoder = new MsgEncoder(mq.getConnection().createChannel(),SIS_QUEUE_NAME);
                Files.list(path).forEach(x -> {
                    KeyValueList kv = parseFile(x.toFile());
                    if (kv != null){
                        System.out.print("Try to init: ");
                        System.out.println(x);
                        try {
                            encoder.sendMsgToMQ(kv);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                encoder.close();
                mq.close();
                break;
            } catch (TimeoutException | IOException e) {
                System.err.println("Failed to connect to the MQ with error: "+e.getMessage());
                if (retryAttempt != MAX_RETRY) System.out.println("Going to retry: "+retryAttempt+1+"/"+MAX_RETRY+" attempt.");
            }
        }
    }
}
