package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

//	private static final String URL="jdbc:mysql://10.37.34.3:3306/wdbuyer_qa?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
//	private static final String NAME="remote";
//	private static final String PASSWORD="WDremote.123456";

	private static final String URL="jdbc:mysql://81.68.92.238:3306/bugly?useUnicode=true&characterEncoding=UTF-8";
	private static final String URL_mitm="jdbc:mysql://81.68.92.238:3306/mitm?useUnicode=true&characterEncoding=UTF-8";

	private static final String NAME="root";
	private static final String PASSWORD="test";

	@RequestMapping("/")
	public String index() throws ClassNotFoundException, SQLException {
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
//		Statement stmt = conn.createStatement();
//		ResultSet rs = stmt.executeQuery("select * from bugly_data;");//选择import java.sql.ResultSet;
//
//		while(rs.next()){//如果对象中有数据，就会循环打印出来
//			//System.out.println(rs.getString("user_name")+","+rs.getInt("age"));
//			System.out.println(rs);
//		}

		return "Hello Spring Boot";
	}

	@RequestMapping(value="/report/mitm_list/1.0", method=RequestMethod.POST)
	public void mitm_list(@RequestBody JSONObject jsonParam) throws SQLException, ClassNotFoundException {
		logger.info("调用接口：/report/mitm_list/1.0");

		// 直接将json信息打印出来
		logger.info("将JSON信息输出：");
		logger.info(jsonParam.toJSONString());

		String mark = jsonParam.getString("mark");

		logger.info("mark: " + mark);

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL_mitm, NAME, PASSWORD);
		Statement stmt = conn.createStatement();
		String exe = "insert into mitm_list(`mark`) " +
				"values(\'" + mark + "\');";
		logger.info(exe);
		int rs = stmt.executeUpdate(exe);//选择import java.sql.ResultSet;
		logger.info("受影响的行数: " + rs);
	}

	@RequestMapping(value="/report/mitm/1.0", method=RequestMethod.POST)
	public void create_mitm(@RequestBody JSONObject jsonParam) throws IOException, SQLException, ClassNotFoundException {
		logger.info("调用接口：/report/mitm/1.0");

		SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
		String created_at = sdf.format(new Date());// 获取当前时间

		// 直接将json信息打印出来
		logger.info("将JSON信息输出：");
		logger.info(jsonParam.toJSONString());

		String mark = jsonParam.getString("mark");
		String level = jsonParam.getString("level");
		String info = jsonParam.getString("info");
		int result = jsonParam.getIntValue("result");

		logger.info("mark: " + mark);
		logger.info("level: " + level);
		logger.info("info: " + info);
		logger.info("result: " + result);
		logger.info("created_at: " + created_at);

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL_mitm, NAME, PASSWORD);
		Statement stmt = conn.createStatement();
		String exe = "insert into mitm(`mark`, `level`, `info`, `created_at`, `result`) " +
				"values(\'" + mark + "\',\'" + level + "\',\'" + info + "\',\'" + created_at+ "\',\'" + result+ "\');";
		logger.info(exe);
		int rs = stmt.executeUpdate(exe);//选择import java.sql.ResultSet;
		logger.info("受影响的行数: " + rs);
	}

	@RequestMapping(value="/bugly", method=RequestMethod.POST)
	public void bugly(@RequestBody JSONObject jsonParam) throws IOException, SQLException, ClassNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
		String created_at = sdf.format(new Date());// 获取当前时间

		// 直接将json信息打印出来
		logger.info("将JSON信息输出：");
		logger.info(jsonParam.toJSONString());

		FileOutputStream fos = new FileOutputStream("./log.txt",true);
		//true表示在文件末尾追加
		String content = sdf.format(new Date()) + " " + jsonParam.toJSONString() + "\n";
		fos.write(content.getBytes());
		fos.close();

		String eventType = (String) jsonParam.get("eventType");
		String timestamp = jsonParam.getString("timestamp");
		JSONObject eventContent = jsonParam.getJSONObject("eventContent");
		String date = eventContent.getString("date");
		String appName = eventContent.getString("appName");
		String appId = eventContent.getString("appId");
		String appUrl = eventContent.getString("appUrl");
		int platformId = eventContent.getInteger("platformId");
		String datas = eventContent.getString("datas");

		logger.info("eventType: " + eventType);
		logger.info("timestamp: " + timestamp);
		logger.info("date: " + date);
		logger.info("appName: " + appName);
		logger.info("appId: " + appId);
		logger.info("appUrl: " + appUrl);
		logger.info("platformId: " + platformId);
		logger.info("datas: " + datas);
		logger.info("created_at: " + created_at);

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
		Statement stmt = conn.createStatement();
		String exe = "insert into bugly_data(`eventType`, `timestamp`, `date`, `appName`, `appId`, `appUrl`, `platformId`, `datas`, `created_at`) " +
				"values(\'" + eventType + "\',\'" + timestamp + "\',\'" + date+ "\',\'" + appName+ "\',\'" + appId+ "\',\'" + appUrl+ "\',\'" + platformId + "\',\'" + datas+ "\',\'" + created_at + "\');";
		logger.info(exe);
		int rs = stmt.executeUpdate(exe);//选择import java.sql.ResultSet;
		logger.info("受影响的行数: " + rs);
	}

	// /pac?ip=172.19.40.110:8082
	@RequestMapping("/pac")
	public String pac(@RequestParam(value = "ip", required=false) String ip, @RequestParam(value = "effect", required=false) boolean effect){
		if(effect == true){
			return "function FindProxyForURL(url, host) {\n" +
						"return \"DIRECT\";\n" +
					"}";
		}

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
