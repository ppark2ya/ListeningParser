package com.myapp.senier;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ListeningParserApplicationTests {

	@Test
	public void contextLoads() {
	}

//	public static void main(String[] args) {
//		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
//        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
//        pbeEnc.setPassword("SENIER_PROJECT");
//
//        String url = "jdbc:log4jdbc:mariadb://104.198.86.245:3306/senier_project?useSSL=false&allowMultiQueries=true&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
//        String username = "admin";
//        String password = "1234";
//
//        System.out.println("변경 URL :: " + pbeEnc.encrypt(url));
//        System.out.println("변경 username :: " + pbeEnc.encrypt(username));
//        System.out.println("변경 password :: " + pbeEnc.encrypt(password));
//	}

}

