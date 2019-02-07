package com.myapp.senier.common.utils;

import java.util.HashMap;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.model.DataModel;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.nurigo.java_sdk.api.GroupMessage;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

public class PushMessage {
    private final Logger logger = LoggerFactory.getLogger(PushMessage.class);
    private final String api_key = CommonConstant.SMS_API_KEY;
    private final String api_secret = CommonConstant.SMS_API_SECRET;
    private Message coolsms;
    private GroupMessage groupCoolsms;

    public PushMessage() {
        coolsms = new Message(this.api_key, this.api_secret);    
    }

    public PushMessage(int members) {
        groupCoolsms = new GroupMessage(this.api_key, this.api_secret);    
    }
    /**
     * SMS(단문) 발송
     * @param serverType
     * @return 
     */
    public DataModel sendSMS(String telNum, String message) {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("to", telNum);
            params.put("from", "01027253120");
            params.put("type", "SMS");
            params.put("text", message);
    
            JSONObject obj = (JSONObject) coolsms.send(params);

            resMap.putJson(obj);
            logger.info("SMS 문자 발송 결과 - {}", resMap);
        } catch(CoolsmsException  e) {
            logger.error("SMS 발송 에러 코드 - {}", e.getCode());
            logger.error("SMS 발송 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * LMS(장문) 발송
     * @param serverType
     * @return
     */
    public DataModel sendLMS(String telNum, String message) {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("to", telNum);
            params.put("from", "01027253120");
            params.put("type", "LMS");
            params.put("text", message);
    
            JSONObject obj = (JSONObject) coolsms.send(params);

            resMap.putJson(obj);
            logger.info("LMS 문자 발송 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("LMS 발송 에러 코드 - {}", e.getCode());
            logger.error("LMS 발송 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * MMS(포토문자) 발송
     * @param serverType
     * @return
     */
    public DataModel sendMMS(String telNum, String message) {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("to", telNum);
            params.put("from", "");
            params.put("type", "MMS");
            params.put("text", "문자보내기 테스트");
            params.put("image", message);
    
            JSONObject obj = (JSONObject) coolsms.send(params);

            resMap.putJson(obj);
            logger.info("MMS 문자 발송 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("MMS 발송 에러 코드 - {}", e.getCode());
            logger.error("MMS 발송 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 예약문자 발송
     * @param serverType
     * @return
     */
    public DataModel sendReservationSMS(String telNum, String message) {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("to", telNum);
            params.put("from", "");
            params.put("type", "SMS");
            params.put("text", "문자보내기 테스트");
            params.put("datetime", "20190301121000"); // YYYYMMDDHHMISS
    
            JSONObject obj = (JSONObject) coolsms.send(params);

            resMap.putJson(obj);
            logger.info("예약 문자 발송 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("예약문자 발송 에러 코드 - {}", e.getCode());
            logger.error("예약문자 발송 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 예약문자 취소
     * @return
     */
    public DataModel cancelReservationSMS() {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<>();
            // 해당 문자의 mid나 gid를 넣어서 취소한다.
            params.put("message_id", "");
            params.put("group_id", "");
    
            JSONObject obj = (JSONObject) coolsms.cancel(params);

            resMap.putJson(obj);
            logger.info("예약 문자 취소 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("예약문자 취소 에러 코드 - {}", e.getCode());
            logger.error("예약문자 취소 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * Coolsms 잔액 조회
     * @return
     */
    public DataModel getBalance() {
        DataModel resMap = new DataModel();
        try {
            JSONObject obj = (JSONObject) coolsms.balance();

            resMap.putJson(obj);
            logger.info("Coolsms 잔액조회 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("Coolsms 잔액조회 에러 코드 - {}", e.getCode());
            logger.error("Coolsms 잔액조회 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 메세지내역 조회
     * @return
     */
    public DataModel getMessageHistory() {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("message_id", "M52CB443257C61"); 
            params.put("group_id", "G52CB4432576C8"); 
            params.put("offset", "0"); // default 0
            params.put("limit", "20"); // default 20
            params.put("rcpt", "01000000000"); // 문자 수신인의 전화 번호
            params.put("start", "201601070915"); // 문자 발송일 
            params.put("end", "201601071230"); // 문자 수신일
            JSONObject obj = (JSONObject) coolsms.sent(params);

            resMap.putJson(obj);
            logger.info("메세지 내역조회 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("메세지 내역조회 에러 코드 - {}", e.getCode());
            logger.error("메세지 내역조회 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 메세지를 담아둘 그룹 생성
     * @return
     */
    public DataModel createGroup() {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("charset", "utf8"); // utf8, euckr default value is utf8
            params.put("srk", "293DIWNEK103"); // Solution key
            params.put("mode", "test"); // If 'test' value, refund cash to point
            params.put("delay", "10"); // '0~20' delay messages
            params.put("force_sms", "true"); // true is always send sms ( default true )
            params.put("app_version", ""); 	// App version

            JSONObject obj = (JSONObject) groupCoolsms.createGroup(params);

            resMap.putJson(obj);
            logger.info("그룹 생성 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("그룹 생성 에러 코드 - {}", e.getCode());
            logger.error("그룹 생성 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 그룹 리스트 조회
     * @return
     */
    public DataModel getGroupList() {
        DataModel resMap = new DataModel();
        try {
            JSONObject obj = (JSONObject) groupCoolsms.getGroupList();

            resMap.putJson(obj);
            logger.info("그룹 리스트 조회 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("그룹 리스트 조회 에러 코드 - {}", e.getCode());
            logger.error("그룹 리스트 조회 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 그룹 리스트 삭제
     * @return
     */
    public DataModel deleteGroupList() {
        DataModel resMap = new DataModel();
        try {
            String groupIds = "GID56FCE501593C8,GID56FB7C8A9A135"; // Group IDs
            JSONObject obj = (JSONObject) groupCoolsms.deleteGroups(groupIds);

            resMap.putJson(obj);
            logger.info("그룹 리스트 삭제 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("그룹 리스트 삭제 에러 코드 - {}", e.getCode());
            logger.error("그룹 리스트 삭제 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 그룹 정보 보기
     * @return
     */
    public DataModel getGroupInfo() {
        DataModel resMap = new DataModel();
        try {
            String groupId = "GID56FCE501593C8"; // Group IDs
            JSONObject obj = (JSONObject) groupCoolsms.getGroupInfo(groupId);

            resMap.putJson(obj);
            logger.info("그룹 정보 보기 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("그룹 정보 보기 에러 코드 - {}", e.getCode());
            logger.error("그룹 정보 보기 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 메세지 리스트 보기
     * @return
     */
    public DataModel getMessageList() {
        DataModel resMap = new DataModel();
        try {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("group_id", "GID56FA3B1BF0826"); // Group ID
            params.put("offset", "0"); // default 0
            params.put("limit", "20"); // default 20
            JSONObject obj = (JSONObject) groupCoolsms.getMessageList(params);

            resMap.putJson(obj);
            logger.info("메세지 리스트 보기 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("메세지 리스트 보기 에러 코드 - {}", e.getCode());
            logger.error("메세지 리스트 보기 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 메세지 삭제
     * @return
     */
    public DataModel deleteMessages() {
        DataModel resMap = new DataModel();
        try {
            String groupId = "GID56FA3B1BF0826"; // Group ID
            String messageIds = "MID56FA3B405A847, MIDFFFA3B405A847"; // Message Ids
            JSONObject obj = (JSONObject) groupCoolsms.deleteMessages(groupId, messageIds);

            resMap.putJson(obj);
            logger.info("메세지 삭제 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("메세지 삭제 에러 코드 - {}", e.getCode());
            logger.error("메세지 삭제 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }

    /**
     * 그룹 메세지 전송
     * @return
     */
    public DataModel groupMessageSend() {
        DataModel resMap = new DataModel();
        try {
            String groupId = "GID56FA3B1BF0826"; // Group ID
            JSONObject obj = (JSONObject) groupCoolsms.sendGroupMessage(groupId);

            resMap.putJson(obj);
            logger.info("그룹 메세지 전송 결과 - {}", resMap);
        } catch(CoolsmsException e) {
            logger.error("그룹 메세지 전송 에러 코드 - {}", e.getCode());
            logger.error("그룹 메세지 전송 에러 내용 - {}", e.getMessage());
        }
        
        return resMap;
    }
}