package work.framework.modules.message.handle.impl;

import work.framework.modules.message.handle.ISendMsgHandle;

public class SmsSendMsgHandle implements ISendMsgHandle {

	@Override
	public void SendMsg(String es_receiver, String es_title, String es_content) {
		// TODO Auto-generated method stub
		System.out.println("发短信");
	}

}
