package nc.vo.zmpub.pub.report2;
import java.util.Enumeration;
import java.util.Hashtable;
/**
 * 创建日期：(2004-2-6 9:08:00)
 * @author：刘建波
 */
public class LevelByOrgContext {
	//对象编码->数据VO.
	private Hashtable m_hObjData = new Hashtable();
	//对象编码。
	private String[][] m_arsObjcode = null;
	//对象编码->在CirVO中，此对象ID对应的字段。
	private Hashtable m_hObjIDFld = new Hashtable();
	//对象编码->在CirVO中,此对象编码对应的字段.
	private Hashtable m_hObjCodeFld = new Hashtable();
	private Hashtable m_hObjTreeModel = new Hashtable();
	//对象编码－>对象名称。
	private Hashtable m_hObjcodeObjname = new Hashtable();
	//是否数据只是在末级。
	private boolean m_isLeaf = true;
	//是否根据编码匹配数据.
	private boolean isUseCodeMatchData = false;
	//为零是否显示。
	private boolean m_isShowZero = false;
	//是否只是显示小计合计。
	private boolean m_isShowSubtotal = false;
	//小计合计是否加编码。
	private boolean m_isSubtotalAddCode = true;
	//是否显示明细.
	private boolean m_isShowDetail = true;
	//显示的级次.
	private String[] m_ShowLevels = null;
	//合并的树模
	private javax.swing.tree.DefaultTreeModel m_tmCombined = null;
	//第一级数模
	private javax.swing.tree.DefaultTreeModel m_tmFirstLevel = null;
	private ITreeModelFactory m_tmf = null;
	private IAdjustNodeTool m_adjustNodeTool = null;
/**
 * LevelByOrgInfoVO 构造子注解。
 */
public LevelByOrgContext() {
	super();
}
	public IAdjustNodeTool getAdjustNodeTool() {
		return m_adjustNodeTool;
	}
public String getCodeFldByObjcode(String strObjcode)
{
	return m_hObjCodeFld.get(strObjcode).toString();
}
public javax.swing.tree.DefaultTreeModel getCombinedTM()
{
	if (getLevelNumber() == 1)
		return getModelByObjcode(getObjcodeByLevel(0)[0]);
	ExTreeNode root =
		(ExTreeNode) getModelByObjcode(getObjcodeByLevel(0)[0]).getRoot();
	for (int i = 1; i < getLevelNumber(); i++)
	{
		for (int j = 0; j < getObjcodeByLevel(i).length; j++)
		{
			Hashtable hnode = null;
			String strParentCode =
				((ExTreeNode) getModelByObjcode(getObjcodeByLevel(i)[j]).getRoot())
					.getParentCode();
			hnode = root.getAllNodeByObjtypecode(null, strParentCode);
			//得到下一个树的所有根节点。
			Enumeration en =
				((ExTreeNode) getModelByObjcode(getObjcodeByLevel(i)[j]).getRoot()).children();
			//叶子对下一个树的根进行合并。
			while (en.hasMoreElements())
			{
				ExTreeNode downtop = (ExTreeNode) en.nextElement();
				ExTreeNode supernode = (ExTreeNode) hnode.get(downtop.getSuperobjid());
				if (supernode != null && supernode.isSelected())
				{
					//解决jdk的一个问题。
					ExTreeNode tmpnode = (ExTreeNode) downtop.clone();
					supernode.add(tmpnode);
				}
			}
		}
	}
	return getModelByObjcode(getObjcodeByLevel(0)[0]);
}
	public nc.vo.pub.CircularlyAccessibleValueObject getDataByObjcode(
		String strObjcode) {
		return (nc.vo.pub.CircularlyAccessibleValueObject) m_hObjData.get(strObjcode);
	}
	public javax.swing.tree.DefaultTreeModel getFirstLevelModel() {
		return m_tmFirstLevel;
	}
	public String getIDFldByObjcode(String strObjcode) {
		return m_hObjIDFld.get(strObjcode).toString();
	}
	public int getLevelNumber() {
		return m_arsObjcode.length;
	}
	public javax.swing.tree.DefaultTreeModel getModelByObjcode(String strObjcode) {
		return (javax.swing.tree.DefaultTreeModel) m_hObjTreeModel.get(strObjcode);
	}
	public String[] getObjcodeByLevel(int iLevel) {
		return m_arsObjcode[iLevel];
	}
	public String getObjnameByCode(String strObjcode) {
		return m_hObjcodeObjname.get(strObjcode).toString();
	}
	//得到对象所在地级次。
	public int getObjPos(String strObjcode) {
		for (int i = 0; i < m_arsObjcode.length; i++) {
			for (int j = 0; j < m_arsObjcode[i].length; j++) {
				if (m_arsObjcode[i][j].equals(strObjcode))
					return i;
			}
		}
		return -1;
	}
	public String[] getShowLevels()
{
		return m_ShowLevels;
	}
	public ITreeModelFactory getTMFactory() {
		return m_tmf;
	}
private void initTMByData(
	String strObjcode,
	nc.vo.pub.CircularlyAccessibleValueObject[] vos)
{
	if (vos == null || vos.length == 0)
		return;
	Hashtable hnode = null;
	//认为各个节点都可能有数据.
	if (isUsecodeMatchData())
		hnode =
			(
				(ExTreeNode) getModelByObjcode(strObjcode)
					.getRoot())
					.getAllNodeByObjtypecodeObjCodeAsKey(
				null,
				strObjcode);
	else
		hnode =
			((ExTreeNode) getModelByObjcode(strObjcode).getRoot()).getAllNodeByObjtypecode(
				null,
				strObjcode);

	//非一级对象全选。
	if (getObjPos(strObjcode) > 0)
	{
		Enumeration en = hnode.elements();
		while (en.hasMoreElements())
		{
			ExTreeNode subnode = (ExTreeNode) en.nextElement();
			subnode.setSelected(true);
		}
	}
	for (int j = 0; j < vos.length; j++)
	{
		Object key = null;
		if (isUsecodeMatchData())
			key = vos[j].getAttributeValue(getCodeFldByObjcode(strObjcode));
		else
			key = vos[j].getAttributeValue(getIDFldByObjcode(strObjcode));
		if (key == null)
			continue;
		if (hnode.get(key.toString().trim()) == null)
			continue;
		ExTreeNode thisnode = (ExTreeNode) hnode.get(key.toString().trim());
		if (thisnode != null && thisnode.isSelected())
		{
			thisnode.setNodeVO(vos[j]);
		}
	}

}
public void initTMByObjcode(String[][] objcode) {
	if (m_tmFirstLevel != null)
		m_hObjTreeModel.put(objcode[0][0], m_tmFirstLevel);
	for (int i = 1; i < objcode.length; i++) {
		for (int j = 0; j < objcode[i].length; j++) {
			m_hObjTreeModel.put(
				objcode[i][j],
				getTMFactory().getModel(objcode[i][j]));
		}
	}
}
	public boolean isAddCodeToSubtotal() {
		return m_isSubtotalAddCode;
	}
	private boolean isOnlyOnLeaf(){
		return m_isLeaf;
	}
	public boolean isShowDetail()
{
		return m_isShowDetail;
	}
public boolean isShowSubtotal() {
	return m_isShowSubtotal ;
}
	public boolean isUsecodeMatchData()
{
		return isUseCodeMatchData;
	}
	public void setAddCodeToSubtotal(boolean b) {
		m_isSubtotalAddCode = b;
	}
	public void setAdjustNodeTool(IAdjustNodeTool adjustTool) {
		m_adjustNodeTool = adjustTool;
	}
	public void setCodeFldByObjCode(String strObjcode, String strFld) {
		m_hObjCodeFld.put(strObjcode, strFld);
	}
public void setDataByObjcode(
	String strObjcode,
	nc.vo.pub.CircularlyAccessibleValueObject[] vos)
{
	//初始树模。
	initTMByData(strObjcode, vos);
	//放入数据。
	if (vos != null && vos.length > 0)
		m_hObjData.put(strObjcode, vos);
}
	public void setFirstLevelTM(javax.swing.tree.DefaultTreeModel tm){
		m_tmFirstLevel=tm;
	}
	public void setIDFldByObjCode(String strObjcode, String strFld) {
		m_hObjIDFld.put(strObjcode, strFld);
	}
	public void setObjcode(String[][] objcode) {
		//得到树模型。	
		m_arsObjcode = objcode;	
		initTMByObjcode(objcode);
	}
	public void setObjnameByCode(String strObjcode, String strObjname) {
		m_hObjcodeObjname.put(strObjcode, strObjname);
	}
	public void setOnlyOnLeaf(boolean b){
		m_isLeaf=b;
	}
	public void setShowDetails(boolean b)
{
		m_isShowDetail = b;
	}
	public void setShowLevlels(String[] levels)
{
		m_ShowLevels = levels;
	}
public void setShowSubtotal(boolean b) {
	m_isShowSubtotal=b ;
}
	public void setTMFactory(ITreeModelFactory tmf) {
		m_tmf = tmf;
	}
	public void setUsecodeMatchData(boolean b)
{
		isUseCodeMatchData = b;
	}
}
