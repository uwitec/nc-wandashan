package nc.bs.hg.pu.pub;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.pub.formulaparse.FormulaParse;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;

/**
 * 
 * @author zhf
 * �׸���Ŀ ��̨����������
 *
 */

public class HgBsPubTool {
	
//	/���ý��������������
	public static final ColumnProcessor COLUMNPROCESSOR = new ColumnProcessor();
	public static final ColumnListProcessor COLUMNLISTPROCESSOR = new ColumnListProcessor();
	public static final ArrayListProcessor ARRAYLISTPROCESSOR = new ArrayListProcessor();
	public static final ArrayProcessor ARRAYPROCESSOR = new ArrayProcessor();
	
	public static final String[] CHECK_FUND_NAME = new String[]{"nfund"};//�ʽ��ֶ�
//	public static final String[] CHECK_FUND_NAME2 = new String[]{"nlockfund","nactfund"};//�ʽ��ֶ�
	public static final String[] CHECK_LOCkFUND_NAME = new String[]{"nlockfund"};//�ʽ�  Ԥ���ʽ� �ֶ�
	public static final String[] CHECK_FUND_USENAMES = new String[]{"nlockfund","nactfund"};//�ʽ�  Ԥ���ʽ� �ֶ�
	
	private static FormulaParse fp = new FormulaParse();
	//��̨��ʽִ��
	public static final Object execFomular(String fomular,String[] names,String []values) throws BusinessException{
		fp.setExpress(fomular);
		if(names.length!=values.length){
			throw new BusinessException("��������쳣");
		}
		int index = 0;
		for(String name:names){
			fp.addVariable(name, values[index]);
			index ++;
		}		
		return fp.getValue();
	}
	
	
	private static java.util.Map<String, String> m_puchTypeInfor = null;
	
	public static java.util.Map<String,String> getDefDoc_PuchType() throws BusinessException{
		if(m_puchTypeInfor == null||m_puchTypeInfor.size()==0){			

			String sql ="select pk_defdoc,docname from bd_defdoc c join bd_defdoclist t on c.pk_defdoclist=t.pk_defdoclist where t.doclistname = '�ɹ���ʽ'";
			Object o=new BaseDAO().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);

			if(o == null){
				throw new BusinessException("����ɹ���ʽδ����");
			}
			ArrayList al = (ArrayList)o;
			if(al == null||al.size()==0){
				throw new BusinessException("����ɹ���ʽδ����");
			}

			m_puchTypeInfor = new HashMap<String, String>();
			int size= al.size();
			for(int i=0;i<size;i++){
				Object ob = al.get(i);
				if(ob != null){
					Object[] str = (Object[])ob;
					m_puchTypeInfor.put(PuPubVO.getString_TrimZeroLenAsNull(str[0]),PuPubVO.getString_TrimZeroLenAsNull(str[1]));
				}
			}
		}
		return m_puchTypeInfor;
	}
//	public static 
}
