package nc.bs.wds.tranprice.bill;

import java.util.List;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * �ֹ����˼ۼ���
 * @author Administrator
 *
 */
public class DsTranPriceCol extends AbstractTranPriceCol {


	@Override
	public void setTranspriceVO() throws BusinessException {
	//  ��� = ����*����*������       +  ���۵���ֵ*����
		SendBillBodyVO body = getSendBody();
		if(body == null)
			throw new BusinessException("��������Ϊ��");
		priceBvo = getDsTranspriceVO();	
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *  ���� ��������  �ջ����� ��ǰ���� ��ȡ�ֹ����˼۱�
	 * @ʱ�䣺2011-5-18����07:56:51
	 *  ��ȡ���䵥��
	 * �����ջ��������˼۱��еĶ��壺���������༶�� �ݹ�ɨ���ȡ  �˼۶���  �粻����  ���� ����ʡ�� ����ʡ�ⶨ����˼�
	 * �����ջ������Ƿ���ʡ�� ����  
     * ����Ƕ���ʡ�ڵĿ���  ��̷�Χ����
	 * @return
	 * @throws BusinessException
	 */
	public  TranspriceBVO getDsTranspriceVO()  throws BusinessException{
		SendBillBodyVO  body = getSendBody();
		List<TranspriceBVO> lprice = getDsPriceInfor(WdsWlPubConst.WDSI, body.getCsendareaid(),body.getCreceiverealid(),true);
		if(lprice == null || lprice.size() == 0){	
			throw new BusinessException("δ�����˼ۣ��кţ�"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCrowno()));
		}else
		return lprice.get(0);
	
	}
	/**
	 * ��ȡ�˼۱�    ���������� ����  �ɵͼ����ϼ� �ݹ�ɨ��ֱ�� �ҵ� �˼۶��� ���������������
	 * @param pricetype  �˼۱������� :�ֹ����˼۱�������˼۱�����ͬһ��������ǵ������Ͳ�ͬ
	 * @param whereSql  
	 * @param pk_deplace ��������
	 * @param reareaid  �ջ�����Id
	 * @param isDG �Ƿ�ݹ�
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getDsPriceInfor(String pricetype,String pk_deplace,String reareaid,boolean isDG) throws BusinessException{
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb  = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for(String name:names){
			sqlb.append(" b."+name+",");
		}
		sqlb.append(" 'aaa'");
		sqlb.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.vbillstatus = "+IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '"+pricetype+"'");//�˼۱������� :�ֹ����˼۱�������˼۱�����ͬһ��������ǵ������Ͳ�ͬ
		sqlb.append(" and h.carriersid='"+billvo.getHeaderVO().getCarriersid()+"'");//������
		sqlb.append(" and h.reserve1='"+m_sendbody.getPk_destore()+"'");//�����ֿ�
		sqlb.append(" and (isnull(b.ifw,0) = "+SendBillVO.YYFW_ALL +" or b.ifw = ");//Ӧ�˷�Χ����
		if(isSale()){//�Ƿ�����
			sqlb.append(SendBillVO.YYFW_SALE+")");
		}else{
			sqlb.append(SendBillVO.YYFW_ZFC+")");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql())!=null)//�����������
			sqlb.append(" and "+getPricePeriodWhereSql());
		sqlb.append(" and b.pk_deplace='"+pk_deplace+"' and b.pk_replace='"+reareaid+"'");
		lprice = (List<TranspriceBVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		//���û�л�ȡ��Ӧ�ջ������Ķ��壬��ѯ��û���ϼ��Ķ���
		if((lprice == null || lprice.size() == 0)&& isDG){
			String  pk_fatherarea= getFatherId(reareaid);
			if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
				//���û���ϼ����࣬ȡ����ʡ�ڻ��߶���ʡ�ⶨ��
				String code = getAreaCode(reareaid);
				// ����������� ������ʡ='01' ����ʡ='02' ����ʡ='03'
				if(code.startsWith("01") || code.startsWith("02")||code.startsWith("03")){
					pk_fatherarea = getAreaIdByCode("0001");
					if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
						throw new BusinessException("���� 0001 ��Ϊ����������� ���嶫��ʡ�ڵ�������");
					}
				}else{
					pk_fatherarea = getAreaIdByCode("0002");
					if(pk_fatherarea == null || "".equalsIgnoreCase(pk_fatherarea)){
						throw new BusinessException("���� 0002 ��Ϊ����������� ���嶫��ʡ���������");
					}
				}
				lprice = getDsPriceInfor(pricetype,pk_deplace,pk_fatherarea,false);
			}else{
				//������Ͻ��������࣬�ݹ��ѯ
				lprice = getDsPriceInfor(pricetype,pk_deplace,pk_fatherarea,true);
			}
		}
		return lprice;
	}

	@Override
	public String getPricePeriodWhereSql() throws BusinessException{
		// TODO Auto-generated method stub
		UFDate logdate = getM_logDate();
		if(logdate==null)
			logdate = new UFDate(System.currentTimeMillis());
		return " h.dstartdate <= '"+logdate.toString()+"' and h.denddate >= '"+logdate.toString()+"'";
	}


}
