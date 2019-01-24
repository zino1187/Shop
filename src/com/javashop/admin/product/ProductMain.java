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
	JPanel p_west; //����� ����
	JPanel p_east;//�󼼺��� ����
	Choice ch_top;//���� ī�װ� 
	Choice ch_sub;//���� ī�װ� 
	JTextField t_name;
	JTextField t_price;
	Canvas can_regist;	
	JButton bt_find;//���� Ž���� ����
	JButton bt_regist;//��� ��ư
	
	ArrayList top_list=new ArrayList();//����ī�װ��� pk ���� �迭 !!!
	ArrayList sub_list=new ArrayList();//����ī�װ��� pk ���� �迭 !!!
	JFileChooser chooser;
	Image regist_img,regist_img2;
	String regist_path; //��Ͻ� ����� �̹��� ���
	String imgName;
	
	//jtable ���� 
	ProductTableModel model;
	JTable table;
	JScrollPane scroll;
	
	//�󼼺��� ���� 
	Choice ch_top2;//���� ī�װ� 
	Choice ch_sub2;//���� ī�װ� 
	JTextField t_name2;
	JTextField t_price2;
	Canvas can_regist2;	
	JButton bt_find2;//���� Ž���� ����
	JButton bt_edit;//���� ��ư
	JButton bt_del;//���� ��ư
	URL url;
	int product_id; //���� ���õ� ��ǰ�� pk
	String img;//���� ���õ� ��ǰ�� �̹���
	
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
				//�׸� �׸���!!!
				//g.setColor(Color.RED);
				//g.fillRect(0, 0,145, 145);
				g.drawImage(regist_img, 0, 0, 145, 145, null);
			}
		};
		bt_find = new JButton("����ã��");
		bt_regist = new JButton("����ϱ�");
		chooser = new JFileChooser("D:/java_developer/javascript/res");
		table = new JTable();
		scroll = new JScrollPane(table);
		
		//���̺��� row ���� Ű���!!!
		table.setRowHeight(65);
		
		//�󼼺��� ���� 
		ch_top2 = new Choice();
		ch_sub2 = new Choice();
		t_name2 = new JTextField();
		t_price2 = new JTextField();
		can_regist2 = new Canvas() {
			public void paint(Graphics g) {
				//�׸� �׸���!!!
				//g.setColor(Color.RED);
				//g.fillRect(0, 0,145, 145);
				g.drawImage(regist_img2, 0, 0, 145, 145, null);
			}
		};
		bt_find2 = new JButton("����ã��");
		bt_edit = new JButton("�����ϱ�");		
		bt_del = new JButton("�����ϱ�");	
		
		
		//����!!!
		Dimension d = new Dimension(145,25);
		ch_top.setPreferredSize(d);
		ch_sub.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_price.setPreferredSize(d);
		can_regist.setPreferredSize(new Dimension(145, 145));
		bt_find.setPreferredSize(d);
		bt_regist.setPreferredSize(d);
		
		//�󼼺������
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
		
		//�󼼺��� ����
		p_east.add(ch_top2);
		p_east.add(ch_sub2);
		p_east.add(t_name2);
		p_east.add(t_price2);
		p_east.add(can_regist2);
		p_east.add(bt_find2);
		p_east.add(bt_edit);
		p_east.add(bt_del);
		p_east.setPreferredSize(new Dimension(170, 600));
		
		//���� �г��� BorderLayout ���� ��ȯ 
		this.setLayout(new BorderLayout());
		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);
		add(scroll);//table�� ���Ϳ�!!
		
		this.setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1200, 600));
		
		//ch_top ���̽� ������Ʈ�� ������ ���� 
		ch_top.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int index=ch_top.getSelectedIndex();//������ ������ ���̽��� ��°
				Integer obj=(Integer)top_list.get(index);
				getSubList(obj,ch_sub);
			}
		});
		
		//ch_top ���̽� ������Ʈ�� ������ ���� 
		ch_top2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int index=ch_top2.getSelectedIndex();//������ ������ ���̽��� ��°
				Integer obj=(Integer)top_list.get(index);
				getSubList(obj,ch_sub2);
			}
		});
		
		//��ư�� ������ ����
		bt_find.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		
		//��ư�� ������ ����
		bt_regist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upload();
				regist();
			}
		});
		
		//���� ��ư�� ������ ����
		bt_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(product_id==0) {
					JOptionPane.showMessageDialog(main, "�����Ͻ� ��ǰ�� �����ϼ���");
					return;
				}
				
				if(JOptionPane.showConfirmDialog(main, "�����Ͻðڽ��ϱ�?")==JOptionPane.OK_OPTION) {
					deleteFile();//���ϸ��� �����
					//delete();//db �����
				}
			}
		});
		
		//���̺�� ������ ���� 
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
		
		//���̽� ������Ʈ�� �� ä��� 
		getTopList();
		
		//table�� �� ����!!
		table.setModel(model=new ProductTableModel());
		selectAll();
	}
	
	//�ֻ��� ī�װ� ���ϱ�
	public void getTopList() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			String sql="select * from topcategory";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			//rs�� ������ choice�� �ű��!!
			
			while(rs.next()) {
				ch_top.add(rs.getString("name"));
				ch_top2.add(rs.getString("name"));
				//pk�� ��������!!
				//getInt �� int ������ȯ�ϰ�, add�޼���� Object���� �μ���
				//�־�� �ϹǷ�, ���� ���� ���� �ʾ� ������ ����� �ϴµ�, �ڹ�
				//������ �⺻�ڷ����� ����Ŭ������ �ڵ� ����ȯ�� �����Ѵ�..
				//�̷� ������������ boxing!!!  int --> Integer(autoboxing)
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
	
	//���� ī�װ� ���ϱ�
	public void getSubList(int topcategory_id, Choice choice) {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select * from subcategory where topcategory_id="+topcategory_id;
		System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			//���� ������ ��!!!! ����� 
			choice.removeAll();
			sub_list.removeAll(sub_list);
			
			while(rs.next()) {
				//ch_sub�� �� ä���!!
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
	
	//���� ã�� �޼��� ���� 
	public void openFile() {
		int result=chooser.showOpenDialog(this);
		if(result ==JFileChooser.APPROVE_OPTION) {
			//������ ������ ��ȯ �޾Ƽ�!!
			File file=chooser.getSelectedFile();
			regist_path=file.getAbsolutePath();
			System.out.println(file.getAbsolutePath());
			ImageIcon icon=new ImageIcon(file.getAbsolutePath());
			regist_img=icon.getImage();
			can_regist.repaint();
		}
	}
	
	//�̹��� Ư�� ��ġ�� ���� ( ���̾��ٸ� ���ε� �Ҹ���)
	public void upload() {
		//data ������ �����ϱ�!!!
		FileInputStream fis=null;
		FileOutputStream fos=null;
		imgName=System.currentTimeMillis()+"."+StringUtil.getExt(regist_path);
		
		try {
			fis=new FileInputStream(regist_path);
			fos=new FileOutputStream("D:/java_developer/javaSE/Shop/data/"+imgName);
			
			byte[] b=new byte[1024];
			int data=-1;
			while(true) {
				data=fis.read(b);//�Է�
				if(data==-1)break;
				fos.write(b);//���
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
		//����~~~~
		
		String sql="insert into product(product_id,subcategory_id,product_name,price,img)";
		sql+=" values(seq_product.nextval,"+subcategory_id+" , '"+name+"', "+price+",'"+imgName+"')"; 
		System.out.println(sql);
		
		try {
			pstmt=con.prepareStatement(sql);
			int result=pstmt.executeUpdate();
			if(result ==0) {
				JOptionPane.showMessageDialog(this, "��Ͻ���");
			}else {
				JOptionPane.showMessageDialog(this, "��ϼ���");
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
	
	//��� ��ǰ ��������!! (ī�װ��� ��ǰ���̺��� ����!!)
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
			//��ũ�� ������ rs �� �����ϱ� !!!
			pstmt=con.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			rs.last();//������ ���ڵ�� ������!!
			int total=rs.getRow();//���� rs�� ��ġ�� ���ڵ� ��ȣ
			
			//rs�� �̿��Ͽ� �������迭�� ���� 
			Object[][] data=new Object[total][model.columnTitle.length];
			rs.beforeFirst();//����ġ
			
			for(int i=0;i<total;i++) {
				rs.next();
				data[i][1]=rs.getInt("product_id");//�����ڵ� product_id
				data[i][2]=rs.getString("img");
				data[i][3]=rs.getString("top_name");
				data[i][4]=rs.getString("sub_name");
				data[i][5]=rs.getString("product_name");
				data[i][6]=rs.getString("price");
			}
			//model�� �������迭�� ��� ������� �迭�� ��ü!!
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
		System.out.println("����� ���Ե� ��ǰ�� id="+product_id+",���� ī�װ�����"+top_name);
		
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
			
			if(rs.next()) {//�Ѱ��̱�� ������, Ŀ���� ������ �Ѵ�..
				//���� ī�װ� ���õǰ� ó��!!!
				for(int i=0;i<top_list.size();i++) {
					if(ch_top2.getItem(i).equals(top_name)) {
						System.out.println(i+"��°���� ��ġ�մϴ�");
						ch_top2.select(i);
						getSubList((int)top_list.get(i),ch_sub2);
					}
				}
				//���� ī�װ��� ���õǾ� �ְ� ó��...
				for(int i=0;i<sub_list.size();i++) {
					if(ch_sub2.getItem(i).equals(sub_name)) {
						ch_sub2.select(i);
					}
				}
				
				t_name2.setText(rs.getString("product_name"));//��ǰ��
				t_price2.setText(rs.getString("price"));
				url = this.getClass().getClassLoader().getResource(rs.getString("img"));
				ImageIcon icon=new ImageIcon(url);
				regist_img2=icon.getImage();
				can_regist2.repaint();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//�̹��� ���� �޼���
	public void deleteFile(){
		try {
			File file=new File(url.toURI());
			System.out.println(file.getAbsolutePath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		System.out.println(img+" �� �����ҷ���?");
	}
	
	//���õ� , ������ �Ѱ� ����
	public void delete() {
		Connection con=main.getCon();
		PreparedStatement pstmt=null;
		
		String sql="delete from product where product_id="+product_id;
		System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql);
			
			//DML ���࿡ ���� ������ ���� ���ڵ� ���� ��ȯ
			int result=pstmt.executeUpdate();
			if(result==0) {
				JOptionPane.showMessageDialog(this,"��������");
			}else{
				JOptionPane.showMessageDialog(this,"��������");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}






























