package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;

public class GuestBookDao {
	
	//필드
	private Connection conn = null;
	private	PreparedStatement pstmt = null;
	private	ResultSet rs = null;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	public void close () {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	public void  getConnection() {
		
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

		// 2. Connection 얻어오기
		conn = DriverManager.getConnection(url, id, pw);

		}catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}
	public List<GuestBookVo> guestbookList() {
	
		
		List<GuestBookVo> list = new ArrayList<GuestBookVo>();

		try {
			
			this.getConnection();
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query +=" select no, ";
			query +="        name, ";
			query +="        password, ";
			query +="        content, ";
			query +="        reg_date ";
			query +=" from guestbook ";
			
			pstmt = conn.prepareStatement(query);
			System.out.println("=====");
			System.out.println(query);
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				GuestBookVo vo = new GuestBookVo(no, name, password, content, regDate);
				list.add(vo);
			}

		} 
		 catch (SQLException e) {
			System.out.println("error:" + e);
		} 
			this.close();
		

		return list;
	}

	public int insert(GuestBookVo vo) {
		int count = 0;
		this.getConnection();
		
		try {
			

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " INSERT INTO guestbook ";
			query += " values(SEQ_GUESTBOOK_no.nextval, ?, ?, ?, sysdate) ";
			
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

		}    catch (SQLException e) {
				System.out.println("error:" + e);
			}

		
		this.close();
		return count;
	}

	public int delete(GuestBookVo vo) {
		this.getConnection();
		int count = 0;

		try {
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " delete from guestbook " + 
					       " where no= ? " + 
				           " and password= ? " ;
			
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		}  catch (SQLException e) {
				System.out.println("error:" + e);
			}

		this.close();

		return count;
	}
	
	
	public GuestBookVo getGuest(int no) {
		GuestBookVo guestBookVo = null;
		this.getConnection();
		
		List<GuestBookVo> list = new ArrayList<GuestBookVo>();

		try {
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " select no, " + 
						   "        name, " + 
					       "        password, " + 
					       "        content, " + 
					       "        reg_date " + 
			               " from guestbook " +
					       " where no = ? ";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			System.out.println("=====");
			System.out.println(query);
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String regDate = rs.getString("reg_date");

				guestBookVo = new GuestBookVo(no, name, password, content, regDate);
			
			}

		}  catch (SQLException e) {
				System.out.println("error:" + e);
			}

			this.close();
			return guestBookVo;
	}
	

}