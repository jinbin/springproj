package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

//	private static final String URL="jdbc:mysql://10.37.34.3:3306/wdbuyer_qa?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
//	private static final String NAME="remote";
//	private static final String PASSWORD="WDremote.123456";

//	private static final String URL="jdbc:mysql://10.1.120.172:3306/member_club_test?useUnicode=true&characterEncoding=UTF-8";
//	private static final String NAME="root";
//	private static final String PASSWORD="wdbuyer.123456";

	@RequestMapping("/")
	public String index() throws ClassNotFoundException, SQLException {
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
//		Statement stmt = conn.createStatement();
//		ResultSet rs = stmt.executeQuery("select * from version;");//选择import java.sql.ResultSet;
//
//		while(rs.next()){//如果对象中有数据，就会循环打印出来
//			//System.out.println(rs.getString("user_name")+","+rs.getInt("age"));
//			System.out.println(rs);
//		}

		return "Hello Spring Boot";
	}

	@RequestMapping(value="/bugly", method=RequestMethod.POST)
	public void bugly(@RequestBody JSONObject jsonParam) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
		Date date = new Date();// 获取当前时间
		logger.info("进入bugly：" + sdf.format(date));

		// 直接将json信息打印出来
		System.out.println(jsonParam.toJSONString());

		FileOutputStream fos = new FileOutputStream("./log.txt",true);
		//true表示在文件末尾追加
		fos.write(jsonParam.toJSONString().getBytes());
		fos.close();
	}

	// /pac?ip=172.19.40.110:8082
	@RequestMapping("/pac")
	public String pac(@RequestParam(value = "ip", required=false) String ip){
		if(ip == null){
			ip = "172.19.40.110:8082";
		}
		return "function FindProxyForURL(url, host) {\n" +
				"  if(dnsDomainIs(host, \".weidian.com\") || dnsDomainIs(host, '.geilicdn.com')){\n" +
				"        return \"PROXY " + ip + "; DIRECT\";\n" +
				"  }else {\n" +
				"        return \"DIRECT\";\n" +
				"  }\n" +
				"}";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
