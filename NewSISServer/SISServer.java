import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * SISServer is used for re-routing messages from some components
 * (must be allowed in their definition) to other components
 * that need those messages (according to their definition)
 */
public class SISServer {
	private final static String SIS_QUEUE_NAME = "SISMQ";
	private final static String MQ_HOST = "192.168.30.133";
	// internal routing table for each component
	// key is the name of a component, see ComponentInfo for details

	static int port = 53217;

	static Map<ComponentInfo, ComponentConnection> mapping = new ConcurrentHashMap<>();

	static String getTopScope() {
		return "SIS";
		// return scope.current;
	}
	public static void reRoute(String scope, KeyValueList kvList){
		SISServer.mapping.entrySet().stream()
			.filter(x -> (x.getKey().scope.equals(scope)
					&& (x.getKey().componentType == ComponentType.Monitor || x.getKey().componentType == ComponentType.Debugger)
					&& x.getValue().encoder != null))
			.forEach(x -> {
				try {
					// re-route this message to each
					// qualified
					// component
					x.getValue().encoder.sendMsg(kvList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("ERROR: Fail to send " + kvList + ", abort subtask");
				}
			});
	}
	public static void main(String[] args) {

		// thread pool for handling connections to components
		ExecutorService service = Executors.newCachedThreadPool();
		// server socket that accepts new connections
		ServerSocket serverSocket = null;

		try {
			// scope.current = "Top";
			serverSocket = new ServerSocket(port);
			System.out.println("SISServer starts, waiting for new components");
			service.execute(new SISPullTask());
			// Setup MQ consumer
			MQConnection mq = new MQConnection(MQ_HOST);
			MsgDecoder decoder = new MsgDecoder(mq.getConnection().createChannel(),SIS_QUEUE_NAME);
			SISTaskManager taskManager = new SISTaskManager(mq);
			decoder.setMQConsumer((consumerTag, delivery)->{
				String message = new String(delivery.getBody(), "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				if (message.length() > 2){ taskManager.handleMessage(message); }
			});
			while (true) {
				// initialize a secondary task for each
				// new connection in the thread pool
				service.execute(new SISTask(serverSocket.accept()));
			}
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}

/**
 * ComponentInfo represents a unique composite key
 * that can identify a component
 * 
 * @author dexterchen
 *
 */
class ComponentInfo {
	String scope;
	ComponentType componentType;
	String name;
	String incomingMessages;
	String outgoingMessages;

	public ComponentInfo(String s, ComponentType t, String n) {
		// TODO Auto-generated constructor stub
		scope = s;
		componentType = t;
		name = n;
	}

	public String getIncomingMessages() {
		return incomingMessages;
	}

	public void setIncomingMessages(String incomingMessages) {
		this.incomingMessages = incomingMessages;
	}

	public String getOutgoingMessages() {
		return outgoingMessages;
	}

	public void setOutgoingMessages(String outgoingMessages) {
		this.outgoingMessages = outgoingMessages;
	}
	public static ComponentType getComponentType(String role) {
		ComponentType type = null;
		if (role == null) {
			return null;
		}
		switch (role) {
			case "Basic":
				type = ComponentType.Basic;
				break;
			case "Monitor":
				type = ComponentType.Monitor;
				break;
			case "Advertiser":
				type = ComponentType.Advertiser;
				break;
			case "Controller":
				type = ComponentType.Controller;
				break;
			case "Debugger":
				type = ComponentType.Debugger;
			default:
				break;
		}
		return type;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		ComponentInfo info = (ComponentInfo) obj;
		return info.name.equals(name) && info.scope.equals(scope);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = HashCodeUtil.hash(HashCodeUtil.SEED, scope);
		result = HashCodeUtil.hash(result, name);
		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append("\n===== Component Info Start =====\n");
		builder.append("Scope: " + scope + "\n");
		builder.append("Component Type: " + componentType + "\n");
		builder.append("Name: " + name + "\n");
		builder.append("===== Component Info End =====\n");
		return builder.toString();
	}
}

/**
 * ComponentType represents a possible type of a component
 * 
 * @author dexterchen
 *
 */
enum ComponentType {
	Basic, Controller, Monitor, Advertiser, Debugger
}


/**
 * 
 * ComponentConnection represents connections
 * that are related to a component
 * see MsgEncoder, MsgDecoder, KeyValueList for details
 * 
 * @author dexterchen
 *
 */
class ComponentConnection {
	// message writer for a component
	MsgEncoder encoder;
	// message reader for a component
	MsgDecoder decoder;

	private MQConnection mq;

	public MQConnection getMq() {
		return mq;
	}

	public ComponentConnection() {
		// TODO Auto-generated constructor stub
	}
	public ComponentConnection(MQConnection mq){
		this.mq = mq;
	}

	public ComponentConnection(MsgEncoder e, MsgDecoder d) {
		// TODO Auto-generated constructor stub
		encoder = e;
		decoder = d;
	}
}
