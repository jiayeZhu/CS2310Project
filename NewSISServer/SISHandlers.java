import java.io.IOException;
import java.util.stream.Collectors;

public class SISHandlers {
	// broadcast (up/down) and receiver
	// all "RECEIVER"s in the broadcast hierarchy can receive (scope startsWith)
	// ("A" in 1.1.1, "A" in 1.1, "A" in 1)

	// not broadcast and receiver
	// Only "RECEIVER" in specified SCOPE can receive (scope equals)
	// "A" in SCOPE

	// broadcast (up/down) and not receiver
	// all components in the hierarchy (up/down) (scope startsWith)
	// COMP1...COMPN in 1.1.1, COMP1...COMPN in 1.1, COMP1...COMPN in 1

	// not broadcast and not receiver
	// All components in specified SCOPE can receive (scope equals)
	// COMP1...COMPN in SCOPE

	static void ReadingHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		if (sender != null && !sender.equals("")) {
			// a sender is required

			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Monitor
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}

	static void AlertHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {
		System.out.println("===========Handling Alert================");
		if (sender != null && !sender.equals("")) {
			// a sender is required
			System.out.println("Sender: "+sender);
			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				System.out.println("No Receiver");
				if (broadcast != null && broadcast.equals("True")) {
					System.out.println("Broadcast!");
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					 System.out.println("Direction: "+direction);
					if (direction != null && direction.equals("Up")) {
						System.out.println("Direction: Up");
						System.out.println(scope);
						System.out.println(scope.startsWith("SIS.Tien"));
						SISServer.mapping.entrySet().stream().forEach(x->{
							System.out.println(x.getKey().scope+"\t"+(x.getKey().componentType==ComponentType.Controller)+"\t"+scope.startsWith(x.getKey().scope));
						});
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Monitor
												|| x.getKey().componentType == ComponentType.Controller
												|| x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Monitor
											|| x.getKey().componentType == ComponentType.Controller
											|| x.getKey().componentType == ComponentType.Advertiser
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}

	static void EmergencyHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		if (sender != null && !sender.equals("")) {
			// a sender is required

			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Advertiser
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Advertiser
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}

	static void SettingHandler(String scope, String sender, String receiver,
			String direction, String broadcast, KeyValueList kvList) {

		if (sender != null && !sender.equals("")) {
			// a sender is required

			if (receiver != null) {
				if (broadcast != null && broadcast.equals("True")) {

					/*
					 * all "RECEIVER" in the broadcast hierarchy can receive
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});

					} else if (direction != null && direction.equals("Down")) {

						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().name.equals(receiver)
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}

				} else {
					/*
					 * Only "RECEIVER" in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().name.equals(receiver)
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}

			} else {
				if (broadcast != null && broadcast.equals("True")) {
					/*
					 * all components in the hierarchy (up/down)
					 * (scope startsWith)
					 */
					if (direction != null && direction.equals("Up")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (scope.startsWith(x.getKey().scope)
										&& (x.getKey().componentType == ComponentType.Basic
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					} else if (direction != null && direction.equals("Down")) {
						SISServer.mapping.entrySet().stream()
								.filter(x -> (x.getKey().scope.startsWith(scope)
										&& (x.getKey().componentType == ComponentType.Basic
												|| x.getKey().componentType == ComponentType.Debugger)
								&& x.getValue().encoder != null)).forEach(x -> {
									try {
										// re-route this message to each
										// qualified
										// component
										x.getValue().encoder.sendMsg(kvList);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										System.out.println(
												"ERROR: Fail to send " + kvList
														+ ", abort subtask");
									}
								});
					}
				} else {
					/*
					 * All components in specified scope can receive
					 * (scope equals)
					 */
					SISServer.mapping.entrySet().stream()
							.filter(x -> (x.getKey().scope.equals(scope)
									&& (x.getKey().componentType == ComponentType.Basic
											|| x.getKey().componentType == ComponentType.Debugger)
							&& x.getValue().encoder != null)).forEach(x -> {
								try {
									// re-route this message to each
									// qualified
									// component
									x.getValue().encoder.sendMsg(kvList);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									System.out.println("ERROR: Fail to send "
											+ kvList + ", abort subtask");
								}
							});
				}
			}
		}

		// no sender no distribution
	}
	
	static void RegisterHandler(String scope, ComponentType type, String name, KeyValueList kvList, MQConnection mq) {
		if (type == null || name == null || name.equals("")) return;
		if (type == ComponentType.Controller) {
			String in = kvList.getValue("InputPath");
			String out = kvList.getValue("OutputPath");
//			new NewTranslator(name, in, out);
		}
		ComponentInfo info = new ComponentInfo(scope, type, name);
		SISServer.mapping.put(info, new ComponentConnection(mq));
		System.out.println(info);
	}

	static void ConnectHandler(String scope, ComponentType type, String name, KeyValueList kvList) throws IOException {
		ComponentInfo keyInfo = new ComponentInfo(scope, type, name);
		ComponentConnection connection = SISServer.mapping.get(keyInfo);
		if (connection != null){
			connection.encoder = new MsgEncoder(connection.getMq().getConnection().createChannel(), scope+"/"+name);
			KeyValueList confirm = new KeyValueList();
			confirm.putPair("Scope", scope);//SISServer.getTopScope());
			confirm.putPair("MessageType", "Confirm");
			confirm.putPair("Sender", "SISServer");
			confirm.putPair("Receiver", name);
			connection.encoder.sendMsg(confirm);

			String in = kvList.getValue("IncomingMessages");
			String out = kvList.getValue("OutgoingMessages");
			ComponentInfo target = SISServer.mapping.keySet().stream().filter(x -> x.equals(keyInfo)).findFirst().orElse(null);
			if(target != null){
				target.setIncomingMessages(in);
				target.setOutgoingMessages(out);
			}

			if (type != ComponentType.Monitor) {
				SISServer.reRoute(scope, kvList);
			}
		} else {
			System.out.println("=========================================");
			System.out.println("Not registered: "+scope+"|"+type+"|"+name);
			System.out.println("=========================================");
		}
	}

	static void ListHandler(String scope, ComponentType type, String name, KeyValueList kvList) throws IOException {
		ComponentInfo keyInfo = new ComponentInfo(scope, type, name);
		ComponentConnection connection = SISServer.mapping.get(keyInfo);
		KeyValueList list = new KeyValueList();
		list.putPair("Scope", scope);
		list.putPair("MessageType", "Confirm");
		list.putPair("Sender", "SISServer");
		// list.putPair("Receiver", name);
		list.putPair("MessageLists",
				SISServer.mapping.keySet().stream().map(x -> {
					return "\n\t\t\t\t" + x.name + "\n\t\t\t\t\t"
							+ x.incomingMessages + "\n\t\t\t\t\t"
							+ x.outgoingMessages;
				}).collect(Collectors.joining("\n")));
		connection.encoder.sendMsg(list);
		/*
		 * Only "RECEIVER" in specified scope can receive
		 * (scope equals)
		 */
		SISServer.reRoute(scope, list);
	}
}
