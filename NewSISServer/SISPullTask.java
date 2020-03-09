import java.util.List;
import java.util.stream.Collectors;

/**
 * SISTask is used for handling one connection to a specific component
 */

public class SISPullTask implements Runnable {

	private static final String URL = "http://ksiresearchorg.ipage.com/chronobot/getMDB.php?uid=376896";

	// // message writer for a component
	// private MsgEncoder encoder;
	//
	// // message reader for a component
	// private MsgDecoder decoder;

	/*
	 * Constructor (socket for a connection to that component)
	 */
	public SISPullTask() {
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			List<KeyValueList> kvLists;
			while (true) {
				// attempt to read and decode a message, see MsgDecoder for
				// details
				System.out.println("Fetch messages from WebGUI");
				kvLists = XMLUtil.extractToKV(URL);

				for (KeyValueList kvList : kvLists) {
					ProcessMsg(kvList);
				}
				// process that message

				System.out.println("Messages fetched (if any)");

				Thread.sleep(5000);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(
					"ERROR: Fail to keep process message for a certain component, abort subtask");
		}
	}

	private ComponentType getComponentType(String role) {
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

	private static void execute(String query) throws Exception {
		String url = "http://ksiresearch.org/chronobot/PHP_Post.php";
		PostQuery.PostToPHP(url, query);
	}

	/*
	 * process a certain message, execute corresponding actions
	 */
	void ProcessMsg(KeyValueList kvList) throws Exception {
		if(kvList.size()>0){
			System.out.println("====================");
			System.out.println(kvList);
			System.out.println("====================");
		}else{
			System.out.println("Invalid");
		}
		String scope = kvList.getValue("Scope");

		if (scope == null || scope.equals("")) {
			return;
		}

		String messageType = kvList.getValue("MessageType");
		String sender = kvList.getValue("Sender");
		String receiver = kvList.getValue("Receiver");
		String name = kvList.getValue("Name");
		ComponentType type = getComponentType(kvList.getValue("Role"));

		String broadcast = kvList.getValue("Broadcast");
		String direction = kvList.getValue("Direction");

		switch (messageType) {
		case "Setting":
			SISHandlers.SettingHandler(scope, sender, receiver, direction,
					broadcast, kvList);
			break;
		case "Register":
			if (type != null && name != null && !name.equals("")) {

				if (type == ComponentType.Controller) {
					String in = kvList.getValue("InputPath");
					String out = kvList.getValue("OutputPath");
					new NewTranslator(name, in, out);
				}

				ComponentInfo info = new ComponentInfo(scope, type, name);
				SISServer.mapping.put(info, new ComponentConnection());
				System.out.println(info);
			}
			break;
		case "List":
			KeyValueList list = new KeyValueList();
			list.putPair("Scope", scope);
			list.putPair("MessageType", "Confirm");
			list.putPair("Sender", "SISServer");
			list.putPair("MessageLists",
					SISServer.mapping.keySet().stream().map(x -> {
						return "\n\t\t\t\t" + x.name + "\n\t\t\t\t\t"
								+ x.incomingMessages + "\n\t\t\t\t\t"
								+ x.outgoingMessages;
					}).collect(Collectors.joining("\n")));
			/*
			 * Only "RECEIVER" in specified scope can receive
			 * (scope equals)
			 */
			SISServer.mapping.entrySet().stream()
					.filter(x -> (x.getKey().scope.equals(scope)
							&& x.getKey().componentType == ComponentType.Debugger
							&& x.getValue().encoder != null))
					.forEach(x -> {
						try {
							// re-route this message to each
							// qualified
							// component
							x.getValue().encoder.sendMsg(list);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("ERROR: Fail to send " + kvList
									+ ", abort subtask");
						}
					});
		default:
			break;
		}
	}

}
