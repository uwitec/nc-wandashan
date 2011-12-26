package nc.bs.wds.ic.other.out;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nc.bs.dao.BaseDAO;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralHVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.ic.zgjz.ZgjzBVO;
import nc.vo.wds.ic.zgjz.ZgjzHVO;

/**
 * lyf:
 * ����Ƿ�����������ⵥ����
 * @author Administrator
 *
 */
public class OtherOutBO_XN {
	HYPubBO pubbo = null;
	HYPubBO getHypubBO() {
		if (pubbo == null) {
			pubbo = new HYPubBO();
		}
		return pubbo;
	}
	BaseDAO dao = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:����ת�ֲ��ݹ����˵��ۼư�������
	 * @ʱ�䣺2011-12-22����09:50:16
	 */
	public void updateZgjzNum(AggregatedValueObject billvo ,boolean isAudit) throws BusinessException{
		if(billvo == null){
			return;
		}
		TbOutgeneralHVO head =(TbOutgeneralHVO) billvo.getParentVO();
		TbOutgeneralBVO[] xnvo =(TbOutgeneralBVO[]) billvo.getChildrenVO();;
		if(xnvo == null || xnvo.length ==0){
			return ;
		}
		String pk_outwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pk());
		if(pk_outwhouse == null){
			throw new BusinessException("�����ݹ��������ݣ�����ֿⲻ��Ϊ��");
		}
		String pk_inwhouse = PuPubVO.getString_TrimZeroLenAsNull(head.getSrl_pkr());
		if(pk_inwhouse == null){
			throw new BusinessException("�����ݹ��������ݣ����ֿⲻ��Ϊ��");
		}
		//���մ�����ܳ�������
		Map<String, UFDouble[]> map = new HashMap<String, UFDouble[]>();
		for(TbOutgeneralBVO bvo:xnvo){
			String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(bvo.getCinventoryid());
			if(pk_invmandoc == null){
				continue;
			}
			UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
			UFDouble noutassistnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassistnum());
			if(map.containsKey(pk_invmandoc)){
				UFDouble noutnum_old = map.get(pk_invmandoc)[0].add(noutnum);
				UFDouble noutassistnum_old = map.get(pk_invmandoc)[1].add(noutassistnum);
				map.get(pk_invmandoc)[0] = noutnum_old;
				map.get(pk_invmandoc)[1] = noutassistnum_old;
			}else{
				UFDouble[]  nums= new UFDouble[2];
				nums[0]=noutnum;
				nums[1]=noutassistnum;
				map.put(pk_invmandoc,nums );
			}
		}
		if(map.size()==0){
			return;
		}
		//��ѯ���ڵ��ݹ����� 
			String strWhere = " pk_outwhouse='"+pk_outwhouse+"' and pk_intwhouse='"+pk_inwhouse+"' and  isnull(dr,0)=0  and isnull(ilacktype,0)=1 and vbillstatus='"+IBillStatus.FREE+"'";
			SuperVO[] heads = getHypubBO().queryByCondition(ZgjzHVO.class, strWhere);
			if(heads == null || heads.length==0){
				throw new BusinessException("δ��ѯ��ƥ���ת�ֲ�����Ƿ������");
			}else if(heads.length >1){
				throw new BusinessException("��ѯ���ó���ֿ��������̬ת�ֲ�����Ƿ������");
			}
			ZgjzHVO hvo = (ZgjzHVO)heads[0]; 
			ZgjzBVO[] bodys = (ZgjzBVO[])getHypubBO().queryByCondition(ZgjzBVO.class, " isnull(dr,0)=0 and pk_wds_zgjz_h='"+hvo.getPrimaryKey()+"'");
			Set<String> keys = map.keySet();
			for(String key:keys){
				boolean falge = false;
				for(ZgjzBVO body:bodys){
					String pk_invmandoc =PuPubVO.getString_TrimZeroLenAsNull(body.getPk_invmandoc()) ;
					if(pk_invmandoc == null){
						continue;
					}
					if(key.equalsIgnoreCase(pk_invmandoc)){
						//���³�������
						UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());
						UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(body.getNoutassnum());
						UFDouble noutnum_add= map.get(key)[0];
						UFDouble noutassnum_add= map.get(key)[1];
						if(!isAudit){
							noutnum_add = noutnum_add.multiply(-1);
							noutassnum_add = noutassnum_add.multiply(-1);
						}
						body.setNoutnum(noutnum.add(noutnum_add));
						body.setNoutassnum(noutassnum.add(noutassnum_add));
						//У�� ���ܳ���
						UFDouble nlastnum= PuPubVO.getUFDouble_NullAsZero(body.getNlastnum());//�ݹ�Ƿ����
						UFDouble nreducnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNreducnum());//�����
						UFDouble noutnum_new= PuPubVO.getUFDouble_NullAsZero(body.getNoutnum());//������
						if(nlastnum.sub(nreducnum_new).sub(noutnum_new).doubleValue()<0){
							throw new BusinessException("���:"+getInvCode(key)+"�������ݹ�δ��������");
						}
						falge = true;
					}
				}
				if(!falge){
					throw new BusinessException("���:"+getInvCode(key)+"�����ݹ�");
				}
			}
			getHypubBO().updateAry(bodys);
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ:��ѯ�������
	 * @ʱ�䣺2011-12-15����07:20:55
	 * @param pk_ivnmandoc
	 * @return
	 */
	public String getInvCode(String pk_ivnmandoc) throws BusinessException{
		String sql =" select invcode from bd_invbasdoc where pk_invbasdoc = " +
				"( select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_ivnmandoc+"' and isnull(dr,0)=0 ) " +
						" and isnull(dr,0)=0 ";
		String value = (String)getBaseDAO().executeQuery(sql, new ColumnProcessor());
		if(value == null){
			value ="";
		}
		return value;
	}

}
