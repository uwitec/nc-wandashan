package nc.ui.zb.pub;

import java.util.Vector;

import javax.swing.DefaultListModel;

import nc.vo.pub.BusinessException;
import nc.vo.zb.pub.ZbPubTool;
/**
 * ʵ�����ݴ�һ�������Ƶ���һ����Panel
 * @author��LIUDC
 */
public abstract class UIListToListPanel2 extends AbstractListToListUI implements javax.swing.event.ListSelectionListener {
	//�������ģ��
	private DefaultListModel m_leftModel = null;
	//�Ҳ�����ģ��
	private DefaultListModel m_rightModel = null;

	private nc.ui.pub.beans.UIList ivjUIListLeft = null;
	private nc.ui.pub.beans.UIList ivjUIListRight = null;
public UIListToListPanel2() {
	super();
	initialize();
}
/**
 *  ��������б�ģ��
 * @return javax.swing.DefaultListModel
 */
protected javax.swing.DefaultListModel getLeftListModel() {
	if(m_leftModel == null)
		m_leftModel = new DefaultListModel();
	return m_leftModel;
}
/**
 * �����Ҳ��б�ģ��
 * @return javax.swing.DefaultListModel
 */
protected javax.swing.DefaultListModel getRightListModel() {
	if(m_rightModel == null)
		m_rightModel = new DefaultListModel();
	return m_rightModel;
}

//protected void setLeftListModel(DefaultListModel leftmodel){
//	m_leftModel = leftmodel;
//}
//
//protected void setRightListModel(DefaultListModel leftmodel){
//	m_rightModel = leftmodel;
//}
/**
 * ���� UIListLeft ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIList getUIListLeft() {
	if (ivjUIListLeft == null) {
		try {
			ivjUIListLeft = new nc.ui.pub.beans.UIList();
			ivjUIListLeft.setName("UIListLeft");
			ivjUIListLeft.setBounds(0, 0, 160, 120);
			// user code begin {1}
			ivjUIListLeft.addListSelectionListener(this);
//			ivjUIListLeft.setModel(getLeftListModel());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIListLeft;
}
/**
 * ���� UIListRight ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIList getUIListRight() {
	if (ivjUIListRight == null) {
		try {
			ivjUIListRight = new nc.ui.pub.beans.UIList();
			ivjUIListRight.setName("UIListRight");
			ivjUIListRight.setBounds(0, 0, 160, 120);
			// user code begin {1}
			ivjUIListRight.addListSelectionListener(this);
//			ivjUIListRight.setModel(getRightListModel());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIListRight;
}
/**
 * ��ʼ���ࡣ
 */

private void initialize() {
	getUIScrollPLeft().setViewportView(getUIListLeft());
	getUIScrollPRight().setViewportView(getUIListRight());

}
///**
// * 1:�������ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
// * 2:����������ݵı���
// * 3:�������������ƶ�ʱ�Ƿ���ԭʼ������
// * @param leftDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
// * @param String leftTitle ����
// * @param boolean ifHoldDataLeft �ƶ�ʱ�Ƿ���ԭʼ����
// */
//protected void initLeftListDatas() 
//{
//	//��ȡ���ݲ������б�ģ�� 
//	 if (getLeftData() != null)
//	  {
////		m_leftNewItems = new Vector(1,1);
////		m_leftNewItemsIndex = new Vector(1,1);
//		m_leftModel = new DefaultListModel();  
////		m_leftVOs = getLeftData().getTreeVO();
////
////		if(m_leftVOs == null || m_leftVOs.length == 0)
////			return;
////		StringBuffer showName = null;
////		for (int i=0;i<m_leftVOs.length;i++)
////		 {
////		   showName = new StringBuffer();
////		   m_leftNewItems.addElement(m_leftVOs[i]);
////		   //for (int j=0;j<leftDataInterface.getShowFieldName().length-1;j++)
////			 //showName.append(m_leftVOs[i].getAttributeValue(leftDataInterface.getShowFieldName()[j])+"-");
////			 showName.append(m_leftVOs[i].getAttributeValue(getLeftData().getShowFieldName()));
////			 
////		   //showName.append(m_leftVOs[i].getAttributeValue(leftDataInterface.getShowFieldName()[leftDataInterface.getShowFieldName().length-1]));
////		   if (m_leftNewItemsIndex.indexOf(showName) < 0 )
////			{
////			  m_leftModel.insertElementAt(showName, m_leftModel.getSize());
////			  m_leftNewItemsIndex.addElement(showName);
////			}
////		 }
//	  }
//} 
///**
// * 1:�����Ҳ�ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
// * 2:�����Ҳ����ݵı���
// * 3:�����Ҳ�������ƶ�ʱ�Ƿ���ԭʼ������
// * @param rightDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
// * @param String rightTitle ����
// * @param boolean ifHoldDataRight �ƶ�ʱ�Ƿ���ԭʼ����
// */
//protected void initRightListDatas() 
//{
//	 if (getRightData() != null)
//	  {
////		m_rightNewItems = new Vector(1,1);
////		m_rightNewItemsIndex = new Vector(1,1);
//
//		//��ȡ���ݲ������б�ģ�� 
//		m_rightModel = new DefaultListModel();  
//		//m_rightVOs = rightDataInterface.getListVO();
////		m_rightVOs = getRightData().getTreeVO();
////		
////		StringBuffer showName = null;
////		if(m_rightVOs != null){
////			for (int i=0;i<m_rightVOs.length;i++)
////			 {
////			   showName = new StringBuffer();
////			   m_rightNewItems.addElement(m_rightVOs[i]);
////				showName.append(m_rightVOs[i].getAttributeValue(getRightData().getShowFieldName()));
////			   if (m_rightNewItemsIndex.indexOf(showName) <0 )
////				{
////				  m_rightModel.insertElementAt(showName, m_rightModel.getSize());
////				  m_rightNewItemsIndex.addElement(showName);
////				}
////			 }
////		}
//	  }
//}


/**
 * 1:�������ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
 * 2:����������ݵı���
 * 3:�������������ƶ�ʱ�Ƿ���ԭʼ������
 * @param leftDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
 * @param String leftTitle ����
 * @param boolean ifHoldDataLeft �ƶ�ʱ�Ƿ���ԭʼ����
 */
protected void setLeftListDatas() 
{
	//��ȡ���ݲ������б�ģ�� 
	 if (getLeftData() != null)
	  {
		m_leftNewItems = new Vector(1,1);
		m_leftNewItemsIndex = new Vector(1,1);
		m_leftModel = new DefaultListModel(); 
//		setLeftListModel(m_leftModel);
		m_leftVOs = getLeftData().getTreeVO();

		if(m_leftVOs == null || m_leftVOs.length == 0){
			
			 getUIListLeft().setModel(m_leftModel);
			 return;
		}
			
		StringBuffer showName = null;
		for (int i=0;i<m_leftVOs.length;i++)
		 {
		   showName = new StringBuffer();
		   
		   m_leftNewItems.addElement(m_leftVOs[i]);
		   //for (int j=0;j<leftDataInterface.getShowFieldName().length-1;j++)
			 //showName.append(m_leftVOs[i].getAttributeValue(leftDataInterface.getShowFieldName()[j])+"-");
		   String[] names = null;
		try {
			names = ZbPubTool.splitCode(getLeftData().getShowFieldName());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(names!=null)
		   for(String name:names){
			   showName.append(ZbPubTool.getString_NullAsTrimZeroLen(m_leftVOs[i].getAttributeValue(name))+" ");
		   }
			 
			 
		   //showName.append(m_leftVOs[i].getAttributeValue(leftDataInterface.getShowFieldName()[leftDataInterface.getShowFieldName().length-1]));
		   if (m_leftNewItemsIndex.indexOf(showName) < 0 )
			{
			  m_leftModel.insertElementAt(showName, m_leftModel.getSize());
			  m_leftNewItemsIndex.addElement(showName);
			}
		 }
	  }
	 getUIListLeft().setModel(m_leftModel);
} 
/**
 * 1:�����Ҳ�ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
 * 2:�����Ҳ����ݵı���
 * 3:�����Ҳ�������ƶ�ʱ�Ƿ���ԭʼ������
 * @param rightDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
 * @param String rightTitle ����
 * @param boolean ifHoldDataRight �ƶ�ʱ�Ƿ���ԭʼ����
 */
protected void setRightListDatas() 
{
	 if (getRightData() != null)
	  {
		m_rightNewItems = new Vector(1,1);
		m_rightNewItemsIndex = new Vector(1,1);

		//��ȡ���ݲ������б�ģ�� 
		m_rightModel = new DefaultListModel();  
		//m_rightVOs = rightDataInterface.getListVO();
		m_rightVOs = getRightData().getTreeVO();
		
		StringBuffer showName = null;
		if(m_rightVOs != null){
			for (int i=0;i<m_rightVOs.length;i++)
			 {
			   showName = new StringBuffer();
			   m_rightNewItems.addElement(m_rightVOs[i]);
			   
			   String[] names = null;
				try {
					names = ZbPubTool.splitCode(getLeftData().getShowFieldName());
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(names!=null)
				   for(String name:names){
					   showName.append(ZbPubTool.getString_NullAsTrimZeroLen(m_rightVOs[i].getAttributeValue(name))+" ");
				   }
			   
//				showName.append(m_rightVOs[i].getAttributeValue(getRightData().getShowFieldName()));
			   if (m_rightNewItemsIndex.indexOf(showName) <0 )
				{
				  m_rightModel.insertElementAt(showName, m_rightModel.getSize());
				  m_rightNewItemsIndex.addElement(showName);
				}
			 }
		}
	  }
	 getUIListRight().setModel(m_rightModel);
}
/**
 * ���Ҳ��ƶ�ѡ������ݵ������¼�
 */
protected void onAddLeft()
{
//��ȡѡ��������	
	Object[] selectItems = getUIListRight().getSelectedValues();
	Object temp = null;
	if (selectItems != null)
	 for (int i=0;i<selectItems.length;i++)
	 {
	    m_leftModel.insertElementAt(selectItems[i],m_leftModel.getSize());
	    if (m_rightNewItemsIndex.indexOf(selectItems[i]) >=0 && m_rightNewItemsIndex.indexOf(selectItems[i]) < m_rightNewItems.size())
	     if (m_leftNewItemsIndex.indexOf(selectItems[i]) < 0 )
	      {
		     m_leftNewItemsIndex.insertElementAt(selectItems[i], m_leftNewItemsIndex.size()); 
		     temp = m_rightNewItems.elementAt(m_rightNewItemsIndex.indexOf(selectItems[i]));
		     m_leftNewItems.insertElementAt(temp, m_leftNewItems.size());
	      }
	    if (!getListController().isLeftHoldData())
	     {
		    m_rightModel.removeElement(selectItems[i]);
		    m_rightNewItems.removeElementAt(m_rightNewItemsIndex.indexOf(selectItems[i]));
		    m_rightNewItemsIndex.removeElement(selectItems[i]);
	     }
	 }

   if (m_rightModel.getSize() <=0 )
	 getUIBuAddLeft().setEnabled(false);
}
/**
 * ������ƶ�ѡ������ݵ��Ҳ��е��¼�
 */
protected void onAddRight() 
{
//��ȡѡ��������	
	Object[] selectItems = getUIListLeft().getSelectedValues();
	Object temp = null;
	if (selectItems != null)
	 for (int i=0;i<selectItems.length;i++)
	 {
	    m_rightModel.insertElementAt(selectItems[i],m_rightModel.getSize());
	    if (m_leftNewItemsIndex.indexOf(selectItems[i]) >=0 && m_leftNewItemsIndex.indexOf(selectItems[i]) < m_leftNewItems.size())
	      if (m_rightNewItemsIndex.indexOf(selectItems[i]) <0 )
	      {
		     m_rightNewItemsIndex.insertElementAt(selectItems[i], m_rightNewItemsIndex.size()); 
		     temp = m_leftNewItems.elementAt(m_leftNewItemsIndex.indexOf(selectItems[i]));
		     m_rightNewItems.insertElementAt(temp, m_rightNewItems.size());
	      }
	    if (!getListController().isRightHoldData())
	     {
		    m_leftModel.removeElement(selectItems[i]);
		    m_leftNewItems.removeElementAt(m_leftNewItemsIndex.indexOf(selectItems[i]));
		    m_leftNewItemsIndex.removeElement(selectItems[i]);
	     }
	 }

   if (m_leftModel.getSize() <=0 )
	 getUIBuAddRight().setEnabled(false);
}
 /** 
   * Called whenever the value of the selection changes.
   * @param e the event that characterizes the change.
   */
public void valueChanged(javax.swing.event.ListSelectionEvent e) 
{
   if (e.getSource() == getUIListLeft() || e.getSource() == getUIListRight())
	{
	   if (getUIListLeft().getSelectedIndex() >=0)
	      getUIBuAddRight().setEnabled(true);
	   else
	      getUIBuAddRight().setEnabled(false);
	   
	   if (getUIListRight().getSelectedIndex() >=0)
	      getUIBuAddLeft().setEnabled(true);
	   else
	      getUIBuAddLeft().setEnabled(false);    
	      
	}	
}
}
