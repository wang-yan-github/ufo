package work.framework.modules.message.handle.impl;

import work.framework.common.util.SpringContextUtils;
import work.framework.modules.message.handle.ISendMsgHandle;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSendMsgHandle implements ISendMsgHandle {

	@Override
	public void SendMsg(String es_receiver, String es_title, String es_content) {
		JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
		SimpleMailMessage message = new SimpleMailMessage();
		// 设置发送方邮箱地址
		message.setFrom("2897976540@qq.com");
		message.setTo(es_receiver);
		message.setSubject(es_title);
		message.setText(es_content);
		mailSender.send(message);

	}
}
