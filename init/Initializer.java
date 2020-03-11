
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Initializer {

    static Socket universal;

    static MsgEncoder encoder;

    static int port = 53217;

    static void processFile(File file) {
        KeyValueList kvList = new KeyValueList();
        try {
            JAXBContext context = JAXBContext.newInstance(Msg.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Msg msg = (Msg) unmarshaller.unmarshal(file);

            kvList = XMLUtil.generateKV(msg);

            System.out.println("Registration Attempt: "
                               + kvList.getValue("Name"));

            encoder.sendMsg(kvList);

            System.out.println("Registration Success: "
                               + kvList.getValue("Name"));

        } catch (JAXBException | IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Registration Fail: " + kvList.getValue("Name"));
        }
    }

    static Socket connect() throws IOException {
        Socket socket = new Socket("127.0.0.1", port);
        return socket;
    }

    public static void main(String[] args) {

        Path path = FileSystems.getDefault().getPath("xml/InitXML");

        while (true) {

            try {

                universal = connect();
                encoder = new MsgEncoder(universal.getOutputStream());

                Files.list(path).forEach(x -> {
                    processFile(x.toFile());
                });
                universal.close();
                break;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                 e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    // TODO Auto-generated catch block
                    // e2.printStackTrace();
                }
                System.out.println("Try to reconnect");
                try {
                    universal = connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    // e1.printStackTrace();
                }
            }
        }
    }
}
