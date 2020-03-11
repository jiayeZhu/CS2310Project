import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

class KeyValueList {
	// interal map for the message <property name, property value>, key and
	// value are both in String format
	private Map<String, String> map;

	// delimiter for encoding the message
	static final String delim = "$$$";

	// regex pattern for decoding the message
	static final String pattern = "\\$+";

	/*
	 * Constructor
	 */
	public KeyValueList() {
		map = new HashMap<>();
	}

	/*
	 * Add one property to the map
	 */
	public boolean putPair(String key, String value) {
		key = key.trim();
		value = value.trim();
		if (key == null || key.length() == 0 || value == null
				|| value.length() == 0) {
			return false;
		}
		map.put(key, value);
		return true;
	}

	/*
	 * encode the KeyValueList into a String
	 */
	public String encodedString() {

		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey() + delim + entry.getValue() + delim);
		}
		// X$$$Y$$$, minimum
		builder.append(")");
		return builder.toString();
	}

	/*
	 * decode a message in String format into a corresponding KeyValueList
	 */
	public static KeyValueList decodedKV(String message) {
		KeyValueList kvList = new KeyValueList();

		String[] parts = message.split(pattern);
		int validLen = parts.length;
		if (validLen % 2 != 0) {
			--validLen;
		}
		if (validLen < 1) {
			return kvList;
		}

		for (int i = 0; i < validLen; i += 2) {
			kvList.putPair(parts[i], parts[i + 1]);
		}
		return kvList;
	}

	/*
	 * get the property value based on property name
	 */
	public String getValue(String key) {
		return map.get(key);
	}

	/*
	 * get the number of properties
	 */
	public int size() {
		return map.size();
	}

	/*
	 * toString for printing
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		for (Entry<String, String> entry : map.entrySet()) {
			builder.append(entry.getKey() + " : " + entry.getValue() + "\n");
		}
		return builder.toString();
	}
}

/**************************************************
 * Class MsgEncoder:
 * Serialize the KeyValue List and Send it out to a Stream.
 ***************************************************/
class MsgEncoder {

	// used for writing Strings
	private PrintStream writer;

	private Channel channel;
	private String QUEUE_NAME;
	/*
	 * Constructor
	 */
	public MsgEncoder(OutputStream out) throws IOException {
		writer = new PrintStream(out);
	}
	public MsgEncoder(Channel channel, String QUEUE_NAME) throws IOException {
		this.channel = channel;
		this.QUEUE_NAME = QUEUE_NAME;
		this.channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	}

	/*
	 * encode the KeyValueList that represents a message into a String and send
	 */
	public void sendMsg(KeyValueList kvList) throws IOException {
		if (kvList == null || kvList.size() < 1) { return; }
		if (this.writer == null) { sendMsgToMQ(kvList); return;}
		this.writer.print(kvList.encodedString() + "\n");
		this.writer.flush();
	}
	public void sendMsgToMQ(KeyValueList kv) throws IOException {
		if (kv == null || kv.size() < 1) { return; }
		this.channel.basicPublish("", QUEUE_NAME, null, kv.encodedString().getBytes());
	}
	public void close() throws IOException, TimeoutException { this.channel.close(); }
}

/**************************************
 * Class MsgDecoder:
 * Get String from input Stream and reconstruct it to
 * a Key Value List.
 ***************************************/

class MsgDecoder {
	// used for reading Strings
	private BufferedReader reader;

	private Channel channel;
	private String QUEUE_NAME;
	/*
	 * Constructor
	 */
	public MsgDecoder(InputStream in) throws IOException {
		reader = new BufferedReader(new InputStreamReader(in));
	}
	public MsgDecoder(Channel channel, String QUEUE_NAME) throws IOException {
		this.channel = channel;
		this.QUEUE_NAME = QUEUE_NAME;
		this.channel.queueDeclare(this.QUEUE_NAME, false, false, false, null);
	}
	/*
	 * read and decode the message into KeyValueList
	 */
	public KeyValueList getMsg() throws Exception {
		KeyValueList kvList = new KeyValueList();
		StringBuilder builder = new StringBuilder();

		String message = reader.readLine();

		if (message != null && message.length() > 2) {

			builder.append(message);

			while (message != null && !message.endsWith(")")) {
				message = reader.readLine();
				builder.append("\n" + message);
			}

			kvList = KeyValueList
					.decodedKV(builder.substring(1, builder.length() - 1));
		}
		return kvList;
	}

	public void setMQConsumer(DeliverCallback cb) throws IOException {
		this.channel.basicConsume(this.QUEUE_NAME, true, cb, consumerTag -> { });
	}
	public void close() throws IOException, TimeoutException { this.channel.close(); }
}

class MQConnection {
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	public MQConnection(String MQ_HOST) throws IOException, TimeoutException {
		this.factory.setHost(MQ_HOST);
		this.connection = this.factory.newConnection();
	}
	public Connection getConnection() { return connection; }
	public ConnectionFactory getFactory() { return factory; }
	public void close() throws IOException { this.connection.close(); }
}
