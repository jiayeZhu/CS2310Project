import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class XMLUtil {

	public static KeyValueList extractToKV(File file) {
		KeyValueList kvList = new KeyValueList();
		try {
			JAXBContext context = JAXBContext.newInstance(Msg.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			Msg msg = (Msg) unmarshaller.unmarshal(file);

			kvList = generateKV(msg);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kvList;
	}

	public static KeyValueList generateKV(Msg msg) {
		KeyValueList kvList = new KeyValueList();
		// Head head = msg.getHead();
		// Body body = msg.getBody();
		// kvList.addPair("MsgID", head.getMsgId());
		// kvList.addPair("Description", head.getDescription());
		List<Item> items = msg.getItems();
		for (Item i : items) {
			kvList.putPair(i.getKey(), i.getValue());
		}
		return kvList;
	}
}

@XmlRootElement(name = "pnml")
@XmlAccessorType(XmlAccessType.FIELD)
class Pnml {
	@XmlElement(name = "place")
	private List<Place> places;
	@XmlElement(name = "transition")
	private List<Transition> transitions;

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

}

@XmlRootElement(name = "place")
@XmlAccessorType(XmlAccessType.FIELD)
class Place {
	@XmlElement(name = "name")
	private Name name;
	@XmlElement(name = "initialCode")
	private InitialCode initialCode;
	@XmlElement(name = "scope")
	private Scope scope;
	@XmlElement(name = "helperCode")
	private HelperCode helperCode;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public InitialCode getInitialCode() {
		return initialCode;
	}

	public void setInitialCode(InitialCode initialCode) {
		this.initialCode = initialCode;
	}

	public void setHelperCode(HelperCode helperCode) {
		this.helperCode = helperCode;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public HelperCode getHelperCode() {
		return helperCode;
	}

}

@XmlRootElement(name = "transition")
@XmlAccessorType(XmlAccessType.FIELD)
class Transition {

	@XmlElement(name = "name")
	private Name name;
	@XmlElement(name = "code")
	private Code code;
	@XmlElement(name = "source")
	private Source source;
	@XmlElement(name = "purpose")
	private Purpose purpose;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Purpose getPurpose() {
		return purpose;
	}

	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}

}

@XmlRootElement(name = "source")
@XmlAccessorType(XmlAccessType.FIELD)
class Source {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

@XmlRootElement(name = "purpose")
@XmlAccessorType(XmlAccessType.FIELD)
class Purpose {
	@XmlElement(name = "value")
	private String value;

	public Purpose() {

	}

	public Purpose(String v) {
		value = v;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

@XmlRootElement(name = "scope")
@XmlAccessorType(XmlAccessType.FIELD)
class Scope {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

@XmlRootElement(name = "name")
@XmlAccessorType(XmlAccessType.FIELD)
class Name {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

@XmlRootElement(name = "initialCode")
@XmlAccessorType(XmlAccessType.FIELD)
class InitialCode {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

@XmlRootElement(name = "helperCode")
@XmlAccessorType(XmlAccessType.FIELD)
class HelperCode {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
class Code {
	@XmlElement(name = "value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

@XmlRootElement(name = "Msg")
@XmlAccessorType(XmlAccessType.FIELD)
class Msg {

	@XmlElement(name = "Item")
	private List<Item> items;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}


@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
class Item {
	@XmlElement(name = "Key")
	String key;
	@XmlElement(name = "Value")
	String value;

	public Item() {

	}

	public Item(String k, String v) {
		// TODO Auto-generated constructor stub
		this.key = k;
		this.value = v;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
