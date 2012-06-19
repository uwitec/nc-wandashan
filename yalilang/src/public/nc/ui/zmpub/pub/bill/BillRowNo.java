package nc.ui.zmpub.pub.bill;

import java.util.HashMap;

import nc.ui.pub.bill.BillCardPanel;
import nc.vo.pub.lang.UFDouble;
/**
 * 单据表体自动生成行号的工具类
 * @author 
 */
public class BillRowNo {
	//行号起始值
	private static int m_rowNoStart = 10;
	//行号的最大长度
	public static final	int 		MAX_LENGTH = 20;
	//小数点后位数　
	public static final	int 		DIGIT_POWER = 8;
	//零
	public static final	UFDouble 	ZERO_UFDOUBLE = new	UFDouble(0.0);
	//可允许的最小值
	public static final	UFDouble 	MIN_VALUE = new	UFDouble(0.00000001);
	//暂时使用以下方式定义	步长
	public static final	int 		STEP_VALUE = 10;
	
	public static final	int 		START_VALUE = 10;

	public static String getCorrectString(UFDouble dOrgValue) {

		String	sOrgValue = dOrgValue.toString() ;
		if (sOrgValue.indexOf(".")<0) {
			return	sOrgValue ;
		}

		//取得新的四舍五入的UFDouble
		UFDouble	dNewValue = dOrgValue.setScale(-8,UFDouble.ROUND_HALF_UP) ;
		//返回正确的值
		dNewValue = dNewValue.compareTo(MIN_VALUE)<=0 ? MIN_VALUE : dNewValue ;

		//原值
		String	sNew = dNewValue.toString();
		int		iLen = sNew.length() ;

		//去掉最后的0
		int		iDotIndex = sNew.indexOf(".") ;
		if ( iDotIndex>0 ) {
			for (int i = iLen-1; i >=iDotIndex; i--){
				if ( '0'==sNew.charAt(i) ) {
					sNew = sNew.substring(0,i) ;
				}else	if ( '.'==sNew.charAt(i) ) {
					sNew = sNew.substring(0,i) ;
					break ;
				}else{
					break ;
				}
			}
		}

		return	sNew ;
	}


/**
* BillRowNo 构造子注解。
*/
private BillRowNo() {
	super();
}

public static void pasteLineRowNo(
		BillCardPanel 	pnlCard,
		String 			sBillType,
		String 			sRowNOKey,
		int 			iPasteCount) {

		//设置平均行号
		insertLineRowNos(
			pnlCard,
			sBillType,
			sRowNOKey,
			pnlCard.getBillTable().getSelectedRow(),
			iPasteCount
		) ;

	}
/**	 
* 增一行后，自动添加新增行的行号。
* <p><strong>调用模块：SCM所有模块。</strong>
* <p><strong>最后修改人：王印芬</strong>	 
* <p><strong>最后修改日前：2006-7-4</strong>	 
* <p><strong>用例描述：</strong>	 
* <p>	 
* @param	BillCardPanel pnlCard		单据模板
* @param	String 	sBillType			单据类型
* @param	String 	sRowNOKey			行号ITEMKEY
* @return	void	 
* @throws	无	 
* @since
* @see		
*/
public static void addLineRowNo(
	BillCardPanel pnlList,
	String sBillType,
	String sRowNOKey) {

	addLineRowNos(pnlList,sBillType,sRowNOKey,1) ;

}
/**	 
* 在最后增N行时，自动设置所有新增行的行号。
* <p><strong>调用模块：SCM所有模块。</strong>
* <p><strong>最后修改人：王印芬</strong>	 
* <p><strong>最后修改日前：2006-7-4</strong>	 
* <p><strong>用例描述：</strong>	 
* <p>	 
* @param	BillCardPanel pnlCard		单据模板
* @param	String 	sBillType			单据类型
* @param	String 	sRowNOKey			行号ITEMKEY
* @param	int				iSetCount		新增的行数，如由5->9，则本值为4
* @return	void	 
* @throws	无	 
* @since
* @see		
*/
public static void addLineRowNos(
	BillCardPanel pnlCard,
	String sBillType,
	String sRowNOKey,
	int 	iSetCount) {


	int iTotalCount = pnlCard.getBillTable().getRowCount();
	
	int[]	iaGivenRow = new	int[iSetCount] ;
	int		iPreRow = iTotalCount - iSetCount - 1  ;
	for (int i = 0; i < iSetCount; i++) {
		iaGivenRow[i] =  iPreRow + i + 1;
	}
	addLineRowNos(
			pnlCard,
			sBillType,
			sRowNOKey,
			iPreRow,
			iaGivenRow) ;
	

}
/**	 
* 增行后，自动添加新增行的行号。由使用者指定取得上一行号的行、在哪些行新增行号。
* <p><strong>调用模块：SCM所有模块。</strong>
* <p><strong>最后修改人：王印芬</strong>	 
* <p><strong>最后修改日前：2006-7-4</strong>	 
* <p><strong>用例描述：</strong>	 
* <p>	 
* @param	BillCardPanel pnlCard		单据模板
* @param	String 	sBillType			单据类型
* @param	String 	sRowNOKey			行号ITEMKEY
* @param	int		iGivenPreRow		使用者指定的取上一个行号的行
* @param	int[]	iaGivenRow			使用者指定的需设置行号的行
* @return	void	 
* @throws	无	 
* @since	V5.0
* @see		
*/
public static void addLineRowNos(
	BillCardPanel pnlList,
	String sBillType,
	String sRowNOKey,
	int		iGivenPreRow,
	int[] 	iaGivenSetRow) {

	//参数正确性判断
	if (pnlList==null || sBillType==null || sRowNOKey==null || pnlList.getBodyItem(sRowNOKey)==null ||
			iaGivenSetRow==null
			) {
		System.err.print("行号公共方法传入参数不正确，请检查！") ;
		return ;
	}

//	先对需设置的行行号进行清空，以免得到的最大行号是错误的
	int iSetLen = iaGivenSetRow.length ;
	for (int i = 0; i < iSetLen; i++) {
		pnlList.getBillModel().setValueAt(null,iaGivenSetRow[i],sRowNOKey);
	}
	
//	int iPreviousRow = iGivenPreRow ;
	UFDouble	dPreviousRowNO = getRowNoUFDoubleMax(pnlList,sRowNOKey) ;
	for (int i = 0; i < iSetLen; i++) {
		//新增行号＝最后一行行号＋步长
		UFDouble dCurRowNO = dPreviousRowNO.add(m_rowNoStart);
		//设置值
		pnlList.getBillModel().setValueAt(
			getCorrectString(dCurRowNO),
			iaGivenSetRow[i],
			sRowNOKey);

		dPreviousRowNO = dCurRowNO ;
	}
}
/**
* 功能描述:得到最大的行号
* @param:	BillCardPanel pnlCard		单据模板
* @param:	String		sRowNoKey		行号ITEMKEY
* @return:当前界面最大的行号
* @serialData: 2006-10-18
*/
private static UFDouble getRowNoUFDoubleMax(
		BillCardPanel pnlCard,
		String		sRowNOKey
		) {

//	界面只有此行
	int nRow = pnlCard.getBillTable().getRowCount();
	if (nRow==1) {
		return	ZERO_UFDOUBLE ;
	}

	UFDouble	dMaxValue = ZERO_UFDOUBLE;
	UFDouble	dEveryValue = null ;
	for (int i = 0; i < nRow; i++) {
		dEveryValue = getRowNoUFDoubleAt(pnlCard,sRowNOKey,i) ;
		if ( dMaxValue.compareTo(dEveryValue)<0 ) {
			dMaxValue = dEveryValue ;
		}
	}
	
	return	dMaxValue ;
}
/**
* 作者：王印芬
* 功能：得到某行的行号。不返回空
*			以下情况返回0.0：
*				1、如果当前行在第一行前;
* 参数：	BillCardPanel 	pnlCard		当前单据模板
*			int				iRow			行
*			String			sRowNoKey		行号ITEMKEY
* 返回：无
* 例外：无
* 日期：(2003-4-8 11:39:21)
* 修改日期，修改人，修改原因，注释标志：
*/
private static UFDouble getRowNoUFDoubleAt(
	BillCardPanel	pnlCard,
	String 			sRowNoKey,
	int				iRow
	) {

	if (iRow<0){
		return	ZERO_UFDOUBLE ;
	}

	Object	oValue = pnlCard.getBillModel().getValueAt(iRow,sRowNoKey) ;
	return	getUFDouble(oValue) ;
}
public static UFDouble getUFDouble(Object	oValue) {

	if (oValue==null || oValue.toString().trim().equals("")) {
		return	ZERO_UFDOUBLE ;
	}else	if (oValue instanceof	UFDouble) {
		return	(UFDouble)oValue ;
	}else{
		return	new	UFDouble(oValue.toString().trim()) ;
	}
}
private static UFDouble getRowNoUFDoubleLessThan(
		BillCardPanel pnlCard,
		String		sRowNOKey,
		int			iRow) {

//	界面只有此行
	int nRow = pnlCard.getBillTable().getRowCount();
	if (nRow==1) {
		return	ZERO_UFDOUBLE ;
	}
	
//	待比较行号
	UFDouble	dKnownRowNo = getRowNoUFDoubleAt(pnlCard,sRowNOKey,iRow) ;
//	记录正好比当前行号小的行号
	UFDouble	dLessFinal = ZERO_UFDOUBLE ;
	
	UFDouble	dTemp = null ;

	for (int i = 0; i < nRow; i++) {
		dTemp = getRowNoUFDoubleAt(pnlCard,sRowNOKey,i) ;
		if ( dTemp.compareTo(dKnownRowNo)<0 && dTemp.compareTo(dLessFinal)>0 ) {
			dLessFinal = dTemp;
		}
	}
	
	if (dLessFinal.compareTo(dKnownRowNo)==0) {
		dLessFinal = ZERO_UFDOUBLE ;
	}

	
//	返回最大行号
	return	dLessFinal;
}
public static void insertLineRowNos(
		BillCardPanel pnlCard,
		String sBillType,
		String sRowNOKey,
		int 	iEndRow,
		int 	iSetCount) {

		int		iPreviousRow = iEndRow - 1 - iSetCount ; 
		int[]	iaGivenSetRow = new	int[iSetCount] ;
		for (int i = 0; i < iSetCount; i++) {
			iaGivenSetRow[i] = iPreviousRow + 1 + i ;
		}

		insertLineRowNos(
				pnlCard,
				sBillType,
				sRowNOKey,
				iEndRow,
				iaGivenSetRow) ;

	}
public static void insertLineRowNos(
		BillCardPanel pnlCard,
		String sBillType,
		String sRowNOKey,
		int		iNextRow,
		int[] 	iaGivenSetRow) {
		
		//参数正确性判断
		if (pnlCard==null || sBillType==null || sRowNOKey==null || pnlCard.getBodyItem(sRowNOKey)==null ||
				iaGivenSetRow==null
				) {
			System.err.print("行号公共方法传入参数不正确，请检查！") ;
			return ;
		}
		
		//得到开始行、结束行行号
		//开始行号：比粘贴行行号略小的行，结束行号：粘贴行
		UFDouble dPreviousRowNO = getRowNoUFDoubleLessThan(pnlCard,sRowNOKey,iNextRow);
		UFDouble dNextRowNO = getRowNoUFDoubleAt(pnlCard,sRowNOKey, iNextRow);
		
		
		//存放最后计算出的行号的数组
		int			iSetLen = iaGivenSetRow.length ;
		UFDouble[]	uaRowNo = new	UFDouble[iSetLen] ;

		//避免首末行号相等的情况
		if (dPreviousRowNO.compareTo(dNextRowNO)==0) {
			for (int i = 0; i < iSetLen; i++) {
				uaRowNo[i] = dPreviousRowNO ;
			}
		}
		else{
			//取首末行之间较小的位数
			//int		iDgt = Math.min( getPower(dPreviousRowNO),getPower(dNextRowNO) );
			//步长,并取最小位数
			UFDouble dStep = (dNextRowNO.sub(dPreviousRowNO)).div(iSetLen + 1);
			for (int i = 0; i < iSetLen; i++) {
				uaRowNo[i] = dPreviousRowNO.add( dStep.multiply(i+1.) ) ;
			}

			//所有元素比较，如果不重复，则可以削位
			int		iStepDgt = DIGIT_POWER ;
			UFDouble	Zero =new	UFDouble(0.0) ;
			
			UFDouble[]	uaRowNoDgt = new	UFDouble[iSetLen] ;
			for (int i = 0; i < iSetLen; i++){
				uaRowNoDgt[i] = uaRowNo[i] ;
			}
			while (true) {
				//给临时数组赋值
				HashMap	hmapRowNo = new	HashMap() ;
				for (int i = 0; i < iSetLen; i++){
					//超出范围
					if ( (uaRowNoDgt[i].compareTo(dPreviousRowNO)<=0 &&  uaRowNoDgt[i].compareTo(dNextRowNO)<=0) ||
						 (uaRowNoDgt[i].compareTo(dPreviousRowNO)>=0 &&  uaRowNoDgt[i].compareTo(dNextRowNO)>=0) ||
						 uaRowNoDgt[i].compareTo(Zero)==0){
						break ;
					}
					hmapRowNo.put(getCorrectString(uaRowNoDgt[i]),null) ;
				}
				hmapRowNo.put( getCorrectString(dPreviousRowNO),null ) ;
				hmapRowNo.put( getCorrectString(dNextRowNO),null ) ;
				if (hmapRowNo.size()!=iSetLen+2) {
					break ;
				}else{
					for (int i = 0; i < iSetLen; i++){
						uaRowNo[i] = uaRowNoDgt[i] ;
					}
					if (iStepDgt<=0) {
						break ;
					}
				}

				//给新插入的元素削位
				--iStepDgt ;
				for (int i = 0; i < iSetLen; i++){
					uaRowNoDgt[i] = uaRowNoDgt[i].setScale(iStepDgt,UFDouble.ROUND_DOWN) ;
				}
			}
		}
		//向界面设置行号
		for (int i = 0; i < iSetLen; i++) {
			//设置值
			pnlCard.getBillModel().setValueAt(getCorrectString(uaRowNo[i]), iaGivenSetRow[i], sRowNOKey);
		}

	}
}