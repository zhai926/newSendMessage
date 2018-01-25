package com.zhaihuilin;

import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.zhaihuilin.util.MD5Encrypt;
import com.zhaihuilin.util.SmsTools;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j
public class SendmessageApplicationTests {

	@Test
	public void contextLoads() {
	}


	@Test
	public  void  TestMD5(){
		String key=UUID.randomUUID().toString().replaceAll("-","");
		log.info("生成的key的值:"+key);
		log.info("key的长度:"+key.length());
		Map<String, String> map = new HashMap<String, String>();
		map.put("name:","张三丰");
		map.put("age:","20");
		map.put("sex:","男");
		map=MD5Encrypt.paraFilter(map);//除去数组中的空值和签名参数
		String prestr =MD5Encrypt.createLinkString(map,true);//把数组所有元素排序
		String mysign=MD5Encrypt.buildRequestMysign(map,"","utf-8");

		log.info("-------要生成签名的字符串："+prestr);
		log.info("-------------生成签名结果:"+mysign);
		log.info("-------------生成签名的长度:"+mysign.length());
	}


	@Test
	public  void  Test() throws ClientException, InterruptedException{

		SendSmsResponse response=SmsTools.sendSms();//发送短信
		System.out.println("短信接口返回的数据----------------");
		System.out.println("状态码Code=" + response.getCode());//返回OK代表请求成功,其他错误码详见错误码列表
		System.out.println("状态码的描述Message=" + response.getMessage());//状态码的描述  请求是否成功 还是权限...
		System.out.println("请求IDRequestId=" + response.getRequestId());//请求ID
		System.out.println("发送回执IDBizId=" + response.getBizId());//发送回执ID,可根据该ID查询具体的发送状态

		Thread.sleep(3000L);

		//查明细
		if(response.getCode() != null && response.getCode().equals("OK")) {
			QuerySendDetailsResponse querySendDetailsResponse =SmsTools.querySendDetails(response.getBizId());
			System.out.println("短信明细查询接口返回数据----------------");
			System.out.println("Code=" + querySendDetailsResponse.getCode());  //状态码-返回OK代表请求成功,其他错误码详见错误码列表
			System.out.println("Message=" + querySendDetailsResponse.getMessage());//状态码的描述
			int i = 0;
			for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO :
					 querySendDetailsResponse.getSmsSendDetailDTOs()){   //发送明细结构体
				System.out.println("SmsSendDetailDTO["+i+"]:");
				System.out.println("Content=" + smsSendDetailDTO.getContent());//短信内容
				System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());//运营商短信错误码
				System.out.println("OutId=" + smsSendDetailDTO.getOutId());//外部流水扩展字段
				System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());//手机号码
				System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());//接收时间
				System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());//发送时间
				System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());//发送状态 1：等待回执，2：发送失败，3：发送成功
				System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());//模板ID
			}
			System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());//发送总条数
			System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());//请求ID
		}
	}





}
