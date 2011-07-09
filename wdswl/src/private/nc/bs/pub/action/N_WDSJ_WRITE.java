package nc.bs.pub.action;

import java.util.Hashtable;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wl.pub.BsBeforeSaveValudate;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.ui.wl.pub.BeforeSaveValudate;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.uap.pf.PFBusinessException;
/**
 *  �����˼۱�
 * @author Administrator
 *
 */
public class N_WDSJ_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
public N_WDSJ_WRITE() {
	super();
}
/*
* ��ע��ƽ̨��д������
* �ӿ�ִ����
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	try {
			super.m_tmpVo = vo;
			Object retObj = null;
		   //����ǰ��ͳһУ��
			beforeSaveValute(vo);
			retObj = runClass("nc.bs.trade.comsave.BillSave", "saveBill","nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,	m_methodReturnHas);
			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/**
 * 
 * @���ߣ�mlr
 * @˵�������ɽ������Ŀ 
 *       ����ǰ��ͳһУ��
 * @ʱ�䣺2011-7-6����07:33:03
 * @param vo
 * @throws Exception
 */
private void beforeSaveValute(PfParameterVO vo)throws Exception {
	//������ ��    ���ݺ�  ��������  �����ֿ�  ������  ��С��  ����� 
	//  ���壺    �ջ�����  ��С���� ������   �˼�
    BsBeforeSaveValudate.FieldNotNull(new SuperVO[]{(SuperVO) vo.m_preValueVo.getParentVO()},new String[]{"vbillno","pk_billtype","reserve1","carriersid","nmincase","nmaxcase"},new String[]{"���ݺ�","��������","�����ֿ�","������","��С��","�����"}); 
    BsBeforeSaveValudate.FieldNotNull((SuperVO[])vo.m_preValueVo.getChildrenVO(),new String[]{"pk_replace","nmindistance","nmaxdistance"},new String[]{"�ջ����","��С����","������"});	
	//��������  ���빫˾��Ψһ  
	BsUniqueCheck.FieldUniqueCheck((SuperVO)vo.m_preValueVo.getParentVO(),new String[]{"vbillno","pk_billtype"},"���ݿ��д����ظ��ĵ��ݺ�");
	//�����ֿ� �������� ������   ���ά���� ���ܳ���    ��С�� ��  �����  ֮��Ľ���
	BsUniqueCheck.FieldUniqueCheckInment((SuperVO)vo.m_preValueVo.getParentVO(),new String[]{"reserve1", "pk_billtype","carriersid"},"nmincase","nmaxcase"," �Ѿ����� �˸� [�����ֿ�]  [������] �µ������������ε��˼۱� ");		
	//У�� �ջ������ͬ�������   ����С����������벻�ܳ��ֽ���
	BsBeforeSaveValudate.beforeSaveBodyUniqueInmet((SuperVO[])vo.m_preValueVo.getChildrenVO(),new String[]{"pk_replace"}, "nmindistance", "nmaxdistance",new String[]{"�ջ����"}, "��С����","������");
}
/*
* ��ע��ƽ̨��дԭʼ�ű�
*/
public String getCodeRemark(){
	return "	try {\n			super.m_tmpVo = vo;\n			Object retObj = null;\n			// ���漴�ύ\n			retObj = runClass(\"nc.bs.gt.pub.HYBillSave\", \"saveBill\",\"nc.vo.pub.AggregatedValueObject:01\", vo, m_keyHas,	m_methodReturnHas);\n			return retObj;\n		} catch (Exception ex) {\n			if (ex instanceof BusinessException)\n				throw (BusinessException) ex;\n			else\n				throw new PFBusinessException(ex.getMessage(), ex);\n		}\n";}
/*
* ��ע�����ýű�������HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
