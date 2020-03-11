import java.io.IOException;
import java.util.stream.Collectors;

public class SISTaskManager {
    private MQConnection mq;
    public SISTaskManager(MQConnection mq) {
        this.mq = mq;
    }
    public void handleMessage(String message) {
        KeyValueList kvList = KeyValueList.decodedKV(message.substring(1, message.length() - 1));
        try {
            this.ProcessMsg(kvList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ProcessMsg(KeyValueList kvList) throws IOException {
        if(kvList.size()>0){
            System.out.println("==========MSG Receive Start==========");
            System.out.println(kvList);
            System.out.println("==========MSG Receive Finish==========");
        }

        String scope = kvList.getValue("Scope");

        if (scope == null || scope.equals("")) {
            return;
        }

        String messageType = kvList.getValue("MessageType");
        String sender = kvList.getValue("Sender");
        String receiver = kvList.getValue("Receiver");
        String name = kvList.getValue("Name");
        ComponentType type = ComponentInfo.getComponentType(kvList.getValue("Role"));

        String broadcast = kvList.getValue("Broadcast");
        String direction = kvList.getValue("Direction");

        switch (messageType) {
            case "Reading":
                SISHandlers.ReadingHandler(scope, sender, receiver, direction,
                        broadcast, kvList);
                break;
            case "Emergency":
                SISHandlers.EmergencyHandler(scope, sender, receiver, direction,
                        broadcast, kvList);

                break;
            case "Alert":
                SISHandlers.AlertHandler(scope, sender, receiver, direction,
                        broadcast, kvList);

                break;
            case "Setting":
                SISHandlers.SettingHandler(scope, sender, receiver, direction,
                        broadcast, kvList);
                break;
            case "Register":
                SISHandlers.RegisterHandler(scope, type, name, kvList, mq);
                break;
            case "Connect":
                SISHandlers.ConnectHandler(scope, type, name, kvList);
                break;
            case "List":
                SISHandlers.ListHandler(scope, type, name, kvList);
                break;
            default:
                break;
        }
    }
}
