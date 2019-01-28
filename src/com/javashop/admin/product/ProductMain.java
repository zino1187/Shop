package com.javashop.admin.product;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.javashop.admin.Main;
import com.javashop.util.StringUtil;

public class ProductMain extends JPanel{
	Main main;
	JPanel p_west; //등록폼 영역
	JPanel p_east;//상세보기 영역
	Choice ch_top;//상위 카테고리 
	Choice ch_sub;//하위 카테고리 
	JTextField t_name;
	JTextField t_price;
	Canvas can_regist;	
	JButton bt_find;//파일 탐색기 띄우기
	JButton bt_regist;//등록 버튼
	
	ArrayList top_list=new ArrayList();//상위카테고리의 pk 담을 배열 !!!
	ArrayList sub_list=new ArrayList();//하위카테고리의 pk 담을 배열 !!!
	JFileChooser chooser;
	Image regist_img,regist_img2;
	String regist_path; //등록시 사용한 이미지 경로
	String imgName;
	
	//jtable 관련 
	ProductTableModel model;
	JTable table;
	JScrollPane scroll;
	
	//상세보기 관련 
	Choice ch_top2;//상위 카테고리 
	Choice ch_sub2;//하위 카테고리 
	JTextField t_name2;
	JTextField t_price2;
	Canvas can_regist2;	
	JButton bt_find2;//파일 탐색기 띄우기
	JButton bt_edit;//수정 버튼
	JButton bt_del;//삭제 버튼
	URL url;
	int product_id; //현재 선택된 상품의 pk
	String img;//현재 선택된 상품의 이미지
	String userData="C:/Users/itbank510/java_developer/data";
	File file;
	
	public ProductMain(Main main) {
		this.main=main;
		p_west  = new JPanel();
		p_east  = new JPanel();
		ch_top = new Choice();
		ch_sub = new Choice();
		t_name = new JTextField();
		t_price = new JTextField();
		can_regist = new Canvas() {
			public void paint(Graphics g) {
				//그림 그리기!!!
				//g.setColor(Color.RED);
				//g.fillRect(0, 0,145, 145);
				g.drawImage(regist_img, 0, 0, 145, 145, null);
			}
		};
		bt_find = new JButton("파일찾기");
		bt_regist = new JButton("등록하기");
		chooser = new JFileChooser("D:/java_developer/javascript/res");
		table = new JTable();
		scroll = new JScrollPane(table);
		
		//테이블의 row 높이 키우기!!!
		table.setRowHeight(65);
		
		//상세보기 관련 
		ch_top2 = new Choice();
		ch_sub2 = new Choice();
		t_name2 = new JTextField();
		t_price2 = new JTextField();
		can_regist2 = new Canvas() {
			public void paint(Graphics g) {
				//그림 그리기!!!
				//g.setColor(Color.RED);
				//g.fillRect(0, 0,145, 145);
				g.drawImage(regist_img2, 0, 0, 145, 145, null);
			}
		};
		bt_find2 = new JButton("파일찾기");
		bt_edit = new JButton("수정하기");		
		bt_del = new JButton("삭제하기");	
		
		
		//부착!!!
		Dimension d = new Dimension(145,25);
		ch_top.setPreferredSize(d);
		ch_sub.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_price.setPreferredSize(d);
		can_regist.setPreferredSize(new Dimension(145, 145));
		bt_find.setPreferredSize(d);
		bt_regist.setPreferredSize(d);
		
		//상세보기관련
		ch_top2.setPreferredSize(d);
		ch_sub2.setPreferredSize(d);
		t_name2.setPreferredSize(d);
		t_price2.setPreferredSize(d);
		can_regist2.setPreferredSize(new Dimension(145, 145));
		bt_find2.setPreferredSize(d);
		bt_edit.setPreferredSize(d);
		bt_del.setPreferredSize(d);
		
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can_regist);
		p_west.add(bt_find);
		p_west.add(bt_regist);
		p_west.setPreferredSize(new Dimension(170, 600));
		
		//상세보기 관련
		p_east.add(ch_top2);
		p_east.add(ch_sub2);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(can_regist2);
		p_east.add(bt_find2);
		p_east.add(bt_edit);
		p_east.add(bt_del);
		p_east.setPreferredSize(new Dimension(170, 600));
		
		//현재 패널을 BorderLayout 으로 전환 
		this.setLayout(new BorderLayout());
		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);
		add(scroll);//table을 센터에!!
		
		this.setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1200, 600));
		
		//ch_top 초이스 컴포넌트에 리스너 연결 
		ch_top.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int index=ch_top.getSelectedIndex();//유저가 선택한 초이스의 번째
				Integer obj=(Integer)top_list.get(index);
				getSubList(obj,ch_sub);
			}
		});
		
		//ch_top 초이스 컴포넌트에 리스너 연결 
		ch_top2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int index=ch_top2.getSelectedIndex();//유저가 선택한 초이스의 번째
				Integer obj=(Integer)top_list.get(index);
				getSubList(obj,ch_sub2);
			}
		});
		
		//버튼과 리스너 연결
		bt_find.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(can_regist);
			}
		});
		//버튼과 리스너 연결
		bt_find2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(can_regist2);
			}
		});
		
		//버튼과 리스너 연결
		bt_regist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upload();
				regist();
			}
		});
		
		//삭제 버튼과 리스너 연결
		bt_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(product_id==0) {
					JOptionPane.showMessageDialog(main, "삭제하실 상품을 선택하세요");
					return;
				}
				
				if(JOptionPane.showConfirmDialog(main, "삭제하시겠습니까?")==JOptionPane.OK_OPTION) {
					if(deleteFile()) {//파일먼저 지우고
						delete();//db 지우기
						selectAll();
						table.updateUI();
						//켄버스 업데이트!!
						regist_img2=null;
						can_regist2.repaint();
					}
				}
			}
		});
		
		//수정 버튼과 리스너 연결
		bt_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(main, "수정하시겠습니까?")==JOptionPane.OK_OPTION) {
					//상세보기의 이미지명과, 우측영역의 파일찾기에 의해
					//선택된 이미지명이 다르다면, 이 사람은 사진교체를 희망..
					if(regist_img2 !=null){
						System.out.println("저 파일교체 원해요!!");
						upload();//원할때만...
					}
					edit();
					selectAll();
					table.updateUI();
				}
			}
		});
		
		//테이블과 리스너 연결 
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row=table.getSelectedRow();
				int col=1;
				//System.out.println(table.getValueAt(row, col));
				product_id=(Integer)table.getValueAt(row, col);
				img=(String)table.getValueAt(row, 2);
				String top_name=(String)table.getValueAt(row, 3);
				String sub_name=(String)table.getValueAt(row, 4);
				getDetail(product_id, top_name, sub_name);
			}
		});
		
		//초이스 컴포넌트에 값 채우기 
		getTopList();
		
		//table과 모델 연결!!
		table.setModel(model=new ProductTableModel());
		selectAll();
	}
	
	//최상위 카테고리 구하기
	public void getTopList() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			String sql="select * from topcategory";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			//rs의 값에서 choice로 옮기기!!
			
			while(rs.next()) {
				ch_top.add(rs.getString("name"));
				ch_top2.add(rs.getString("name"));
				//pk도 보관하자!!
				//getInt 는 int 형을반환하고, add메서드는 Object형을 인수로
				//넣어야 하므로, 원래 형이 맞지 않아 에러가 났어야 하는데, 자바
				//에서는 기본자료형과 레퍼클래스가 자동 형변환을 지원한다..
				//이런 현상을가리켜 boxing!!!  int --> Integer(autoboxing)
				//Integer --> int (unboxing)
				top_list.add(rs.getInt("topcategory_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//하위 카테고리 구하기
	public void getSubList(int topcategory_id, Choice choice) {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from subcategory where topcategory_id="+topcategory_id;
		System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			//기존 아이템 싹!!!! 지우기 
			choice.removeAll();
			sub_list.removeAll(sub_list);
			
			while(rs.next()) {
				//ch_sub에 값 채우기!!
				choice.add(rs.getString("name"));
				sub_list.add(rs.getInt("subcategory_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//파일 찾기 메서드 정의 
	public void openFile(Canvas can) {
		int result=chooser.showOpenDialog(this);
		if(result ==JFileChooser.APPROVE_OPTION) {
			//선택한 파일을 반환 받아서!!
			file=chooser.getSelectedFile();
			regist_path=file.getAbsolutePath();
			System.out.println(file.getAbsolutePath());
			ImageIcon icon=new ImageIcon(file.getAbsolutePath());
			regist_img=icon.getImage();
			System.out.println("regist_img2"+regist_img2);
			regist_img2=regist_img;
			can.repaint();//누가 다시 그려질지 변수로 설정!!
			img=System.currentTimeMillis()+"."+StringUtil.getExt(regist_path);
			JOptionPane.showMessageDialog(main,"당신이 수정할 이미지명은"+img);
		}
	}
	
	//이미지 특정 위치로 복사 ( 웹이었다면 업로드 불린다)
	public void upload() {
		//data 폴더로 복사하기!!!
		FileInputStream fis=null;
		FileOutputStream fos=null;
		//imgName=System.currentTimeMillis()+"."+StringUtil.getExt(regist_path);
		
		try {
			fis=new FileInputStream(regist_path);
			fos=new FileOutputStream(userData+File.separator+img);
			System.out.println("C:/Users/itbank510/java_developer/data"+File.separator+img);
			
			byte[] b=new byte[1024];
			int data=-1;
			while(true) {
				data=fis.read(b);//입력
				if(data==-1)break;
				fos.write(b);//출력
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fos !=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis !=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void regist() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		int index=ch_sub.getSelectedIndex();
		int subcategory_id=(Integer)sub_list.get(index);
		String name=t_name.getText();
		String price=t_price.getText();
		//로직~~~~
		
		String sql="insert into product(product_id,subcategory_id,product_name,price,img)";
		sql+=" values(seq_product.nextval,"+subcategory_id+" , '"+name+"', "+price+",'"+img+"')"; 
		System.out.println(sql);
		
		try {
			pstmt=con.prepareStatement(sql);
			int result=pstmt.executeUpdate();
			if(result ==0) {
				JOptionPane.showMessageDialog(this, "등록실패");
			}else {
				JOptionPane.showMessageDialog(this, "등록성공");
				selectAll();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//모든 상품 가져오기!! (카테고리와 상품테이블의 조인!!)
	public void selectAll() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		StringBuffer sb=new StringBuffer();
		sb.append("select t.topcategory_id,t.name as top_name");
		sb.append(", s.subcategory_id, s.name as sub_name");
		sb.append(", product_id, product_name, price ,img ");
		sb.append(" from topcategory t, subcategory s, product p");
		sb.append(" where t.topcategory_id=s.topcategory_id");
		sb.append(" and s.subcategory_id=p.subcategory_id");
		
		System.out.println(sb.toString());
		
		try {
			//스크롤 가능한 rs 로 지원하기 !!!
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			rs.last();//마지막 레코드로 보내기!!
			int total=rs.getRow();//현재 rs가 위치한 레코드 번호
			
			//rs를 이용하여 이차원배열을 생성 
			Object[][] data=new Object[total][model.columnTitle.length];
			rs.beforeFirst();//원위치
			
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][1]=rs.getInt("product_id");//고유코드 product_id
				data[i][2]=rs.getString("img");
				data[i][3]=rs.getString("top_name");
				data[i][4]=rs.getString("sub_name");
				data[i][5]=rs.getString("product_name");
				data[i][6]=rs.getString("price");
			}
			//model의 이차원배열을 방금 만들어진 배열로 대체!!
			model.data=data;
			table.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void getDetail(int product_id, String top_name, String sub_name) {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		System.out.println("당신이 보게될 상품의 id="+product_id+",상위 카테고리명은"+top_name);
		
		StringBuffer sb = new StringBuffer();
		sb.append("select s.subcategory_id as subcategory_id, s.topcategory_id as topcategory_id");
		sb.append(", product_id, product_name, price");
		sb.append(",img from subcategory s, product p");
		sb.append(" where s.subcategory_id=p.subcategory_id");
		sb.append(" and product_id="+product_id);
		
		System.out.println(sb.toString());
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			if(rs.next()) {//한건이기는 하지만, 커서를 내려야 한다..
				//상위 카테고리 선택되게 처리!!!
				for(int i=0;i<top_list.size();i++) {
					if(ch_top2.getItem(i).equals(top_name)) {
						System.out.println(i+"번째에서 일치합니다");
						ch_top2.select(i);
						getSubList((int)top_list.get(i),ch_sub2);
					}
				}
				//하위 카테고리가 선택되어 있게 처리...
				for(int i=0;i<sub_list.size();i++) {
					if(ch_sub2.getItem(i).equals(sub_name)) {
						ch_sub2.select(i);
					}
				}
				
				t_name2.setText(rs.getString("product_name"));//상품명
				t_price2.setText(rs.getString("price"));
				
				ImageIcon icon=new ImageIcon(userData+File.separator+rs.getString("img"));
				regist_img2=icon.getImage();
				can_regist2.repaint();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//이미지 삭제 메서드
	public boolean deleteFile(){
		boolean result=false;//성공여부를 담는 변수
		System.out.println(img+" 를 삭제할래요?");
		File file=new File(userData+File.separator+img);
		result=file.delete();
		System.out.println(file.getAbsolutePath());
		return result;
	}
	
	//선택된 , 데이터 한건 삭제
	public void delete() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		
		String sql="delete from product where product_id="+product_id;
		System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			
			//DML 수행에 의해 영향을 받은 레코드 수를 반환
			int result=pstmt.executeUpdate();
			if(result==0) {
				JOptionPane.showMessageDialog(this,"삭제실패");
			}else{
				JOptionPane.showMessageDialog(this,"삭제성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//수정 메서드 정의 
	public void edit() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		
		//쿼리문 수행시, 바인드 변수를 사용하면 성능 향상된다!!
		//바인드 변수는 쿼리문의 컴파일과 관련하여 성능을향상시키기
		//위한 용도였다..바인드 변수를 지원하는 쿼리문 수행객체가
		//바로 PreparedStatement 이다!!
		StringBuffer sb = new StringBuffer();
		sb.append("update product set subcategory_id=?");
		sb.append(", product_name=?");
		sb.append(", price=?");
		sb.append(", img=?");
		sb.append(" where product_id=?");
		
		try {
			pstmt=con.prepareStatement(sb.toString());
			//물음표로 대체했던 바인드 변수값을 결정지어야 한다!!
			
			  pstmt.setInt(1, (int)sub_list.get(ch_sub2.getSelectedIndex()));//subcategory_id
			  pstmt.setString(2, t_name2.getText());
			  pstmt.setInt(3, Integer.parseInt(t_price2.getText()));
			  pstmt.setString(4, img); 
			  pstmt.setInt(5, product_id);
			  
			  int result=pstmt.executeUpdate();//쿼리실행!!
			  if(result >0) {
				  JOptionPane.showMessageDialog(this, "수정성공");
			  }else {
				  JOptionPane.showMessageDialog(this, "수정실패");
			  }
			  
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}






























