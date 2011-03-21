/*
 * 卓竞劲创建
 * 2010-05-13
 * 
 */
package nc.ui.ncblk.yjcb.yjcbjs.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextField;

public class InputJEDialog extends UIDialog {
   private UILabel toplabel = new UILabel("请输入金额： ");
   private UITextField input = new UITextField("在这里输入");
   private UIButton ok = new UIButton("确定");
   private UIButton cancel = new UIButton("取消");
   
   private GridLayout btnsLayout = new GridLayout(1,2,15,0);
   private BorderLayout botmLayout = new BorderLayout();
   private BorderLayout totalLayout = new BorderLayout();
   private BorderLayout topLayout = new BorderLayout();
   
   private UIPanel top = new UIPanel();
   private UIPanel botm = new UIPanel();
   private UIPanel btns = new UIPanel();
  
   public static String str = null;
   public static boolean isTz = false;
   
   public InputJEDialog() 
   {
	   this.setLayout(this.totalLayout);
	   this.top.setLayout(this.topLayout);
	   this.botm.setLayout(this.botmLayout);
	   this.btns.setLayout(this.btnsLayout);
	   isTz = true;
	   this.btns.add(this.ok);
	   this.ok.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent actionevent) {
			// TODO Auto-generated method stub
			 if(check())
			 {
				 str = input.getText().trim();
				 dispose();
			 }
			 else 
			 {
				 JOptionPane.showMessageDialog(null, "输入的不是数字", "错误", JOptionPane.ERROR_MESSAGE);
			 }
		}
		   
	   });
	   
	   this.btns.add(this.cancel);
	   this.cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				isTz = false;
				dispose();
			}
			});
	   this.top.add(this.toplabel,BorderLayout.WEST);
	   this.top.add(this.input,BorderLayout.CENTER);
	   this.botm.add(this.btns,BorderLayout.EAST);
	   
	   
	   this.add(this.top,BorderLayout.CENTER);
	   this.add(this.botm,BorderLayout.SOUTH);
	   
	   this.setDefaultCloseOperation(UIDialog.DISPOSE_ON_CLOSE);
	   this.setSize(300,75);
	   Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	   this.setLocation(size.width/3,size.height/3);
	   this.setTitle("输入");
	   this.setVisible(true);
   }
   
   public boolean check()
   {
	   String temp = this.input.getText().trim();
	   try
	   {
		   Double.parseDouble(temp);
		   
	   }
	   catch(Exception e)
	   {
		   return false;
	   }
	   return true;
   }
   
   
   
}
