package nc.ui.zb.pub;

import java.util.Vector;
import javax.swing.JComponent;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.trade.listtolist.IListToListController;
import nc.ui.trade.pub.IVOTreeData;
import nc.vo.pub.SuperVO;

/**
 * ʵ�����ݴ�һ�������Ƶ���һ����Panel
 * @author��LIUDC
 */
public abstract class AbstractListToListUI extends nc.ui.pub.beans.UIPanel implements java.awt.event.ActionListener, javax.swing.event.ListSelectionListener {
	private UIButton ivjUIBuAddLeft = null;
	private UIButton ivjUIBuAddRight = null;
	private UILabel ivjUILLeftTitle = null;
	private UILabel ivjUILRightTitle = null;
	
	private JComponent m_leftRef = null;//zhf add
	private JComponent m_rightRef = null;//zhf add
	
	
	//���ԭʼ������
	protected SuperVO[] m_leftVOs = null;
	//�����������
	protected Vector m_leftNewItems = null;
	protected Vector m_leftNewItemsIndex = null;
	//�Ҳ�ԭʼ������
	protected nc.vo.pub.SuperVO[] m_rightVOs = null;
	//�Ҳ���������
	protected Vector m_rightNewItems = null;
	protected Vector m_rightNewItemsIndex = null;

	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPLeft = null;
	private nc.ui.pub.beans.UIScrollPane ivjUIScrollPRight = null;
	private nc.ui.pub.beans.UIButton ivjUIBuResume = null;
	private nc.ui.pub.beans.UIList ivjUIListLeft = null;
	private nc.ui.pub.beans.UIList ivjUIListRight = null;

	private nc.ui.trade.pub.IVOTreeData m_leftdata = null;
	private nc.ui.trade.pub.IVOTreeData m_rightdata = null;

	private IListToListController m_controller = null;
public AbstractListToListUI() {
	super();
	initialize();
}
public AbstractListToListUI(IVOTreeData left,IVOTreeData right) {
	super();
	m_leftdata = left;
	m_rightdata = right;
	initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent e)
{
	if (e.getSource() == getUIBuAddLeft())
		onAddLeft();
	else if (e.getSource() == getUIBuAddRight())
		onAddRight();
	else if (e.getSource() == getUIBuResume()){
		onResume();
	}
}
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected abstract nc.ui.trade.pub.IVOTreeData createLeftData();
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected abstract IListToListController createListController();
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected abstract nc.ui.trade.pub.IVOTreeData createRightData();
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected nc.ui.trade.pub.IVOTreeData getLeftData(){
	if(m_leftdata == null)
		m_leftdata = createLeftData();
	return m_leftdata;
}
/**
 * ��ȡ�������������
 * return CircularlyAccessibleValueObject[]
 */
public nc.vo.pub.SuperVO[] getLeftListItems()
{
   nc.vo.pub.SuperVO[] newItems = null;
   if (m_leftNewItems != null )
	{
	  newItems = new nc.vo.pub.SuperVO[m_leftNewItems.size()];
	  m_leftNewItems.copyInto(newItems);
	}

   return newItems;
}
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected final IListToListController getListController(){
	if(m_controller == null)
		m_controller = createListController();
	return m_controller;
}
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected nc.ui.trade.pub.IVOTreeData getRightData(){
	if(m_rightdata == null)
		m_rightdata = createRightData();
	return m_rightdata;
}
/**
 * ��ȡ�Ҳ�����������
 * return CircularlyAccessibleValueObject[]
 */
public nc.vo.pub.SuperVO[] getRightListItems()
{
   nc.vo.pub.SuperVO[] newItems = null;
   if (m_rightNewItems != null )
	{
	  newItems = new nc.vo.pub.SuperVO[m_rightNewItems.size()];
	  m_rightNewItems.copyInto(newItems);
	}

   return newItems;
}
/**
 * ���� UIBuAddLeft ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UIButton getUIBuAddLeft() {
	if (ivjUIBuAddLeft == null) {
		try {
			ivjUIBuAddLeft = new nc.ui.pub.beans.UIButton();
			ivjUIBuAddLeft.setName("UIBuAddLeft");
			ivjUIBuAddLeft.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000114")/*@res "�ƶ��Ҳ�����ѡ��Ľڵ㵽�������ѡ��Ľڵ���"*/);
			ivjUIBuAddLeft.setIButtonType(1);
			ivjUIBuAddLeft.setText("<");
			ivjUIBuAddLeft.setTranslate(true);
			ivjUIBuAddLeft.setMaximumSize(new java.awt.Dimension(40, 22));
			ivjUIBuAddLeft.setActionCommand("<");
			ivjUIBuAddLeft.setBounds(270, 180, 50, 22);
			ivjUIBuAddLeft.setMinimumSize(new java.awt.Dimension(40, 22));
			ivjUIBuAddLeft.setEnabled(false);
			// user code begin {1}
			ivjUIBuAddLeft.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBuAddLeft;
}
/**
 * ���� UIBuAddRight ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UIButton getUIBuAddRight() {
	if (ivjUIBuAddRight == null) {
		try {
			ivjUIBuAddRight = new nc.ui.pub.beans.UIButton();
			ivjUIBuAddRight.setName("UIBuAddRight");
			ivjUIBuAddRight.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000115")/*@res "�ƶ��������ѡ��Ľڵ㵽�Ҳ�����ѡ��Ľڵ���"*/);
			ivjUIBuAddRight.setIButtonType(1);
			ivjUIBuAddRight.setText(">");
			ivjUIBuAddRight.setTranslate(true);
			ivjUIBuAddRight.setMaximumSize(new java.awt.Dimension(40, 22));
			ivjUIBuAddRight.setActionCommand(">");
			ivjUIBuAddRight.setBounds(270, 118, 50, 22);
			ivjUIBuAddRight.setMinimumSize(new java.awt.Dimension(40, 22));
			ivjUIBuAddRight.setEnabled(false);
			// user code begin {1}
			ivjUIBuAddRight.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBuAddRight;
}
/**
 * ���� UIBuResumeLeft ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UIButton getUIBuResume() {
	if (ivjUIBuResume == null) {
		try {
			ivjUIBuResume = new nc.ui.pub.beans.UIButton();
			ivjUIBuResume.setName("UIBuResume");
			ivjUIBuResume.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000116")/*@res "�ָ�����ʼ���ṹ"*/);
			ivjUIBuResume.setFont(new java.awt.Font("dialog", 0, 12));
			ivjUIBuResume.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000117")/*@res "�ָ�"*/);
			ivjUIBuResume.setBounds(267, 50, 60, 22);
			ivjUIBuResume.setMargin(new java.awt.Insets(2, 2, 2, 2));
			// user code begin {1}
			ivjUIBuResume.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIBuResume;
}
/**
 * ���� UILLeftTitle ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UILabel getUILLeftTitle() {
	if (ivjUILLeftTitle == null) {
		try {
			ivjUILLeftTitle = new nc.ui.pub.beans.UILabel();
			ivjUILLeftTitle.setName("UILLeftTitle");
			ivjUILLeftTitle.setToolTipText("LeftTitle");
			ivjUILLeftTitle.setTranslate(true);
			ivjUILLeftTitle.setSize(50,22);
			ivjUILLeftTitle.setBounds(30, 20, 20, 30);
			ivjUILLeftTitle.setMinimumSize(new java.awt.Dimension(50, 22));
			ivjUILLeftTitle.setText("LeftTitle");
			ivjUILLeftTitle.setBackground(new java.awt.Color(255,255,255));
			ivjUILLeftTitle.setMaximumSize(new java.awt.Dimension(50, 22));
			ivjUILLeftTitle.setForeground(new java.awt.Color(0,0,255));
			ivjUILLeftTitle.setFont(new java.awt.Font("dialog", 1, 14));
			ivjUILLeftTitle.setILabelType(0);
			// user code begin {1}
			if(getListController().getLeftTitle() != null)
				ivjUILLeftTitle.setText(getListController().getLeftTitle());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILLeftTitle;
}
protected abstract JComponent createUILiftContrlComponent();
protected abstract JComponent createUIRithtContrlComponent();
protected  JComponent getUILiftContrlComponent(){
	if(m_leftRef == null){
		m_leftRef = createUILiftContrlComponent();
		m_leftRef.setBounds(60, 20, 150, 30);
//		m_leftRef.add
	}
//	360, 20, 150, 30
	return m_leftRef;
}
protected  JComponent getUIRithtContrlComponent(){

	if(m_rightRef == null){
		m_rightRef = createUIRithtContrlComponent();
		m_rightRef.setBounds(390, 20, 150, 30);
	}
		
//	return m_leftRef;

	return m_rightRef;
}
/**
 * ���� UILRightTitle ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UILabel getUILRightTitle() {
	if (ivjUILRightTitle == null) {
		try {
			ivjUILRightTitle = new nc.ui.pub.beans.UILabel();
			ivjUILRightTitle.setName("UILRightTitle");
			ivjUILRightTitle.setToolTipText("RightTitle");
			ivjUILLeftTitle.setSize(250,22);
			ivjUILRightTitle.setTranslate(true);
			ivjUILRightTitle.setBounds(360, 20, 50, 30);
			ivjUILRightTitle.setMinimumSize(new java.awt.Dimension(50, 22));
			ivjUILRightTitle.setText("RightTitle");
			ivjUILRightTitle.setBackground(new java.awt.Color(255,255,255));
			ivjUILRightTitle.setMaximumSize(new java.awt.Dimension(50, 22));
			ivjUILRightTitle.setForeground(new java.awt.Color(0,0,255));
			ivjUILRightTitle.setFont(new java.awt.Font("dialog", 1, 14));
			ivjUILRightTitle.setILabelType(0);
			// user code begin {1}
			if(getListController().getRightTitle() != null)
				ivjUILRightTitle.setText(getListController().getRightTitle());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUILRightTitle;
}
/**
 * ���� UIScrollPLeft ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UIScrollPane getUIScrollPLeft() {
	if (ivjUIScrollPLeft == null) {
		try {
			ivjUIScrollPLeft = new nc.ui.pub.beans.UIScrollPane();
			ivjUIScrollPLeft.setName("UIScrollPLeft");
			ivjUIScrollPLeft.setBounds(30, 50, 210, 240);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIScrollPLeft;
}
/**
 * ���� UIScrollPRight ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
protected nc.ui.pub.beans.UIScrollPane getUIScrollPRight() {
	if (ivjUIScrollPRight == null) {
		try {
			ivjUIScrollPRight = new nc.ui.pub.beans.UIScrollPane();
			ivjUIScrollPRight.setName("UIScrollPRight");
			ivjUIScrollPRight.setBounds(360, 50, 210, 240);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUIScrollPRight;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
protected void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// System.out.println("--------- δ��׽�����쳣 ---------");
	 exception.printStackTrace(System.out);
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("UIListToListPanel");
		setBounds(new java.awt.Rectangle(0, 0, 600, 310));
		setLayout(null);
		setSize(600, 310);
		setMinimumSize(new java.awt.Dimension(600, 310));
		add(getUIBuAddRight(), getUIBuAddRight().getName());
		add(getUIBuAddLeft(), getUIBuAddLeft().getName());
//		add(getUILLeftTitle(), getUILLeftTitle().getName());
//		add(getUILRightTitle(), getUILRightTitle().getName());
		add(getUILiftContrlComponent(),getUILiftContrlComponent().getName());
		add(getUIRithtContrlComponent(),getUIRithtContrlComponent().getName());//zhf
		add(getUIScrollPLeft(), getUIScrollPLeft().getName());
		add(getUIScrollPRight(), getUIScrollPRight().getName());
		//add(getUIBuResume(), getUIBuResume().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
///**
// * 1:�������ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
// * 2:����������ݵı���
// * 3:�������������ƶ�ʱ�Ƿ���ԭʼ������
// * @param leftDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
// * @param String leftTitle ����
// * @param boolean ifHoldDataLeft �ƶ�ʱ�Ƿ���ԭʼ����
// */
//protected void initLeftListDatas(){}
///**
// * 1:�����Ҳ�ԭʼ���ݣ�ʹ��nc.ui.trade.listtolist.IVOListData��Ϊ����
// * 2:�����Ҳ����ݵı���
// * 3:�����Ҳ�������ƶ�ʱ�Ƿ���ԭʼ������
// * @param rightDataInterface nc.ui.trade.listtolist.IVOListData ���ݽӿ�
// * @param String rightTitle ����
// * @param boolean ifHoldDataRight �ƶ�ʱ�Ƿ���ԭʼ����
// */
//protected void initRightListDatas(){
//}
/**
 * ���Ҳ��ƶ�ѡ������ݵ������¼�
 */
protected void onAddLeft(){}
/**
 * ������ƶ�ѡ������ݵ��Ҳ��е��¼�
 */
protected void onAddRight(){}
/**
 * ���ߣ������
 * ���ܣ�
 * ������
 * ���أ�
 * ���⣺
 * ���ڣ�(2004-2-21 10:16:34)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 *
 */
public void onResume() {}
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected void setLeftData(nc.ui.trade.pub.IVOTreeData data){
	m_leftdata = data;
}
/**
 *
 * �������ڣ�(2004-1-3 18:13:36)
 */
protected void setRightData(nc.ui.trade.pub.IVOTreeData data){
	m_rightdata = data;
}
}