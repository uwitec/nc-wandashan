package nc.bs.wds.invcl;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.invcl.WdsInvClVO;

public class InvClBO implements IBDBusiCheck {

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		// TODO Auto-generated method stub
//  zhf  ����ǰУ��  ���벻���ظ�   ���Ʋ����ظ�  ����Ϊ��    �������   ##--##--##  �������һ����У��
		if(vo == null || vo.getParentVO() == null)
			return;
		if(intBdAction == IBDACTION.SAVE){
			WdsInvClVO head = (WdsInvClVO)vo.getParentVO();
			head.validate();
//			У������Ƿ�͸��ౣ��һ����  ����λ��  ����Ϊ  ��λ  ��λ
			String code = head.getVinvclcode();
			String fathercode = getInvclCodeByKey(head.getPk_father(), head.getPk_corp());
			if(fathercode == null)
				throw new BusinessException("�����쳣����ȡ�������Ϊ��");
			checkCode(code, fathercode, head.getPrimaryKey(),head.getPk_corp());
		}else if(intBdAction == IBDACTION.DELETE){
//			У����������¼��ڵ� ����ɾ��
			WdsInvClVO head = (WdsInvClVO)vo.getParentVO();
			String sql = "select count(0) from wds_invcl where isnull(dr,0) = 0 and pk_corp = '"+head.getPk_corp()+"' and pk_father = '"+head.getPrimaryKey()+"'";
			if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0)>0){
				throw new BusinessException("�����¼��ڵ�");
			}
		}
	}
	
	private void checkCode(String code,String fathercode,String key,String corp) throws Exception{
//		����У����벻���ظ�
		StringBuffer str = new StringBuffer();
		str.append("select count(0) from wds_invcl where isnull(dr,0)= 0 ");
		str.append(" and pk_corp = '"+corp+"' and vinvclcode = '"+code+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(key)!=null){
			str.append(" pk_invcl <> '"+key+"'");
		}
		double a = code.length()/2;
		int b = code.length()/2;
		if(Math.abs(a-b)>0)
			throw new BusinessException("������벻���Ϲ���XX--XX--XX");
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(str.toString(), WdsPubResulSetProcesser.COLUMNPROCESSOR), 0)>0){
			throw new BusinessException("��������ظ�");
		}
		if(!code.startsWith(fathercode)){
			throw new BusinessException("�͸�����벻һ��");
		}
		if(code.length()-fathercode.length()!=2){
			throw new BusinessException("������벻���Ϲ���XX--XX--XX");
		}
	}
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	
	private String getInvclCodeByKey(String key,String logcorp) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(key)==null)
			return null;
		String sql = "select vinvclcode from wds_invcl where isnull(dr,0) = 0 and pk_corp = '"+logcorp+"' and pk_invcl = '"+key+"'";
		
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, WdsPubResulSetProcesser.COLUMNPROCESSOR));
	}

	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		// TODO Auto-generated method stub
// �����У��
	}

}
