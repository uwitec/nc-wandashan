package nc.bs.wds.tranprice.freight;

import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wds.dm.corpseal.CorpsealVO;
import nc.vo.wds.tranprice.freight.ZhbzBVO;
import nc.vo.wds.tranprice.freight.ZhbzHVO;

public class ZhbzBSCheck implements IBDBusiCheck {
	private BaseDAO dao;

	private BaseDAO getDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	public void check(int intBdAction, AggregatedValueObject vo, Object userObj)
			throws Exception {
		if (intBdAction != IBDACTION.SAVE) {
			return;
		}
		if (vo == null || vo.getParentVO() == null) {
			return;
		}
		if(vo.getChildrenVO()==null || vo.getChildrenVO().length==0){
			return;
		}

		// �ж����޸ĺ�ı��滹��������ı���
		ZhbzHVO ivo = (ZhbzHVO) vo.getParentVO();   //�õ�����VO
		
		ZhbzBVO[] bvo=(ZhbzBVO[])vo.getChildrenVO();  //�õ��ӱ�
		
		String prmint=PuPubVO.getString_TrimZeroLenAsNull(ivo.getPrimaryKey());
		//�ж��Ƿ���������
		if(prmint==null){
			//��ͷ�� ΨһУ��
			String carriersid=PuPubVO.getString_TrimZeroLenAsNull(ivo.getCarriersid());
			String standardtune=PuPubVO.getString_TrimZeroLenAsNull(ivo.getStandardtune());
			String tuneunits=PuPubVO.getString_TrimZeroLenAsNull(ivo.getTuneunits());
			if(carriersid==null||standardtune==null||tuneunits==null){
				return;
			}
			StringBuffer sql=new StringBuffer();
			sql.append( " carriersid='" + carriersid+ "' and  isnull(dr,0)=0");
			sql.append("  and standardtune='"+standardtune+"' and tuneunits ='"+tuneunits+"'");
	        List list1 = (List) getDao().retrieveByClause(ZhbzHVO.class,sql.toString());
	        if(list1 != null &&  list1.size() >0){
	        	throw new BusinessException("��ͷ���ֶδ����ظ�!");
	        }

			
			//BsUniqueCheck.FieldUniqueCheck(ivo, new String[]{"carriersid","standardtune","tuneunits"}, " ��ͷ���ֶδ����ظ��� ");
			//�Ա�������Ψһ��Ч��
			if(bvo==null||bvo.length==0){
				throw new BusinessException("���岻��Ϊ�գ�");
			}
			String sql1="select pk_invmandoc from wds_zhbz_b where isnull(dr,0)=0";
			ArrayListProcessor processor = new ArrayListProcessor();
			List list=(List) getDao().executeQuery(sql1,processor);
			for(int i=0;i<bvo.length;i++){
			  String invmandoc=PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getPk_invmandoc());
			  if(invmandoc==null){
					throw new BusinessException("�����Ϣ����Ϊ�գ�");
			  }
			  if(list!=null&&list.size()>0){
					for(int j=0;j<list.size();j++){
						Object[] lis= (Object[]) list.get(j);
						String old=lis[0].toString();
						if(invmandoc.equals(old)){
							throw new BusinessException("�����Ϣ�ڿ����Ѿ����ڣ������ظ���");

						}
					}
				}
			}
		}else{
			//�޸�У�� 
			//��ͷ�� ΨһУ��
			String carriersid=PuPubVO.getString_TrimZeroLenAsNull(ivo.getCarriersid());
			String standardtune=PuPubVO.getString_TrimZeroLenAsNull(ivo.getStandardtune());
			String tuneunits=PuPubVO.getString_TrimZeroLenAsNull(ivo.getTuneunits());
			if(carriersid==null||standardtune==null||tuneunits==null){
				return;
			}
			StringBuffer buf=new StringBuffer();
			buf.append( " carriersid='" + carriersid+ "' and  isnull(dr,0)=0");
			buf.append("  and standardtune='"+standardtune+"' and tuneunits ='"+tuneunits+"'");
	        List list1 = (List) getDao().retrieveByClause(ZhbzHVO.class,buf.toString());
	        if(list1 != null &&  list1.size() >1){
	        	throw new BusinessException("��ͷ���ֶδ����ظ�!");
	        }
	        if(list1 != null &&  list1.size() == 1){
	        	ZhbzHVO zhbz = (ZhbzHVO)list1.get(0);
	        	if(!prmint.equalsIgnoreCase(zhbz.getPrimaryKey())){
		        	throw new BusinessException("��ͷ���ֶδ����ظ�");

	        	}
	        }
	        
	    	//�Ա�������Ψһ��Ч��
			if(bvo==null||bvo.length==0){
				throw new BusinessException("���岻��Ϊ�գ�");
			}
			String sql="select pk_invmandoc from wds_zhbz_b where isnull(dr,0)=0";
			ArrayListProcessor processor = new ArrayListProcessor();
			List list=(List) getDao().executeQuery(sql,processor);
			for(int i=0;i<bvo.length;i++){
			  String invmandoc=PuPubVO.getString_TrimZeroLenAsNull(bvo[i].getPk_invmandoc());
			  if(invmandoc==null){
					throw new BusinessException("�����Ϣ����Ϊ�գ�");
			  }
			  if(list!=null&&list.size()>0){
					for(int j=0;j<list.size();j++){
						Object[] lis= (Object[]) list.get(j);
						String old=lis[0].toString();
						if(invmandoc.equals(old)){
							throw new BusinessException("�����Ϣ�ڿ����Ѿ����ڣ������ظ���");

						}
					}
				}
			}
	        
			
		
		}
	
	}
	
	public void dealAfter(int intBdAction, AggregatedValueObject billVo,
			Object userObj) throws Exception {
		
	}

}
