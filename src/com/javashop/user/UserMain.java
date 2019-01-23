package com.javashop.user;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class UserMain extends JFrame{
	JPanel container;//화면 교체시 컨테이너가 될 패널!!
	JMenuBar bar;
	String[] menuTitle= {"상품관리","매출관리","회원관리","채팅현황"};
	JMenu[] menu=new JMenu[4];
	
	public UserMain() {
		bar = new JMenuBar();
		bar.setBackground(new Color(33,16,16));
		
		//메뉴생성
		for(int i=0;i<menuTitle.length;i++) {
			menu[i]=new JMenu(menuTitle[i]);
			bar.add(menu[i]);
			menu[i].setFont(new Font("돋움",Font.BOLD,20));
			menu[i].setForeground(new Color(237,239,133));
			
		}
		setJMenuBar(bar);
		setLocationRelativeTo(null);
		setSize(1200,650);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new UserMain();
	}

}


