package com.javashop.admin;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.javashop.admin.chat.ChatMain;
import com.javashop.admin.member.MemberMain;
import com.javashop.admin.product.ProductMain;
import com.javashop.admin.sales.SalesMain;
import com.javashop.db.ConnectionManager;
public class Main extends JFrame{
	JPanel container;//ȭ�� ��ü�� �����̳ʰ� �� �г�!!
	JMenuBar bar;
	String[] menuTitle= {"��ǰ����","�������","ȸ������","ä����Ȳ"};
	JMenu[] menu=new JMenu[menuTitle.length];
	
	//������ ���� 
	JPanel[] pages=new JPanel[menuTitle.length];
	private Connection con;
	ConnectionManager connectionManager;
	
	public Main() {
		container = new JPanel();
		bar = new JMenuBar();
		bar.setBackground(new Color(33,16,16));
		
		//�޴�����
		for(int i=0;i<menuTitle.length;i++) {
			menu[i]=new JMenu(menuTitle[i]);
			menu[i].setHorizontalAlignment(SwingConstants.CENTER);
			menu[i].setPreferredSize(new Dimension(180, 50));
			bar.add(menu[i]);
			menu[i].setFont(new Font("����",Font.BOLD,20));
			menu[i].setForeground(new Color(237,239,133));
		}
		setJMenuBar(bar);
		
		//create page
		connectionManager = new ConnectionManager();
		//�����ϱ�
		con=connectionManager.getConnection();
		
		pages[0]=new ProductMain(this);
		pages[1]=new SalesMain();
		pages[2]=new MemberMain();
		pages[3]=new ChatMain();
		 
		//add panel index 0 
		container.add(pages[0]);
		container.add(pages[1]);
		container.add(pages[2]);
		container.add(pages[3]);
		
		add(container);
		
		//�� �޴����� ������ �޼��� ȣ�� 
		menu[0].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showPage(0);
			}
		});
		
		menu[1].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showPage(1);
			}
		});
		menu[2].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showPage(2);
			}
		});
		menu[3].addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				showPage(3);
			}
		});
		
		
		//������ ������ �����ϱ�!!
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				connectionManager.disconnect(con);
				System.exit(0);
			}
		});
		
		
		setLocationRelativeTo(null);
		setSize(1300,650);
		setVisible(true);
	}
	
	//ȭ�� ��ȯ �޼��� ����
	public void showPage(int page){
		for(int i=0;i<pages.length;i++) {
			if(i==page) {
				pages[i].setVisible(true);
			}else {
				pages[i].setVisible(false);
			}
		}
	}
	
	//getter!!
	public Connection getCon() {
		return con;
	}
	
	public static void main(String[] args) {
		new Main();
	}

}



