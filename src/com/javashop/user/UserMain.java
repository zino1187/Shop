package com.javashop.user;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class UserMain extends JFrame{
	JPanel container;//ȭ�� ��ü�� �����̳ʰ� �� �г�!!
	JMenuBar bar;
	String[] menuTitle= {"��ǰ����","�������","ȸ������","ä����Ȳ"};
	JMenu[] menu=new JMenu[4];
	
	public UserMain() {
		bar = new JMenuBar();
		bar.setBackground(new Color(33,16,16));
		
		//�޴�����
		for(int i=0;i<menuTitle.length;i++) {
			menu[i]=new JMenu(menuTitle[i]);
			bar.add(menu[i]);
			menu[i].setFont(new Font("����",Font.BOLD,20));
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


