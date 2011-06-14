package nc.bs.wds.tranprice.bill;

import java.util.List;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.tranprice.bill.SendBillBodyVO;
import nc.vo.wds.tranprice.bill.SendBillVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceBVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * ����˼ۼ���
 * @author Administrator
 *
 */
public class XsTranPriceCol extends AbstractTranPriceCol {

	@Override
	public void setTranspriceVO() throws BusinessException {
        //  ����*���� +����ֵ*����
		SendBillBodyVO body = getSendBody();
		if(body == null)
			throw new BusinessException("��������Ϊ��");
		priceBvo = getXsTranspriceVO();
//		UFDouble nprice = PuPubVO.getUFDouble_NullAsZero(getXsPrice());
//		UFDouble nnum = PuPubVO.getUFDouble_NullAsZero(getXS());
//		
//		
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ��ȡ �����˼۱�
	 *  ��ȡԭ�� ��һά�ȣ� �������� �ڶ�ά�ȣ�����
	 * @ʱ�䣺2011-5-19����08:56:16
	 * @return
	 */
	public TranspriceBVO getXsTranspriceVO()throws BusinessException{
		SendBillBodyVO  body = getSendBody();
		List<TranspriceBVO> lprice = getXsPriceInfor(WdsWlPubConst.WDSJ, body.getCsendareaid(),body.getCreceiverealid());
		if(lprice == null || lprice.size() == 0){	
			throw new BusinessException("δ�����˼ۣ��кţ�"+WdsWlPubTool.getString_NullAsTrimZeroLen(body.getCrowno()));
		}else		
		return lprice.get(0);

	}
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ����ȡ�����˼۱�
	 * @ʱ�䣺2011-5-20����10:11:31
	 * @param pricetype
	 * @param pk_deplace
	 * @param reareaid
	 * @return
	 * @throws BusinessException
	 */
	public List<TranspriceBVO> getXsPriceInfor(String pricetype,String pk_deplace,String reareaid) throws BusinessException{
		List<TranspriceBVO> lprice = null;
		StringBuffer sqlb  = new StringBuffer();
		sqlb.append("select ");
		String[] names = new TranspriceBVO().getAttributeNames();
		for(String name:names){
			sqlb.append(" b."+name+",");
		}
		sqlb.append(" 'aaa'");
		sqlb.append("from wds_transprice_h h inner join wds_transprice_b  b on b.pk_wds_transprice_h = h.pk_wds_transprice_h ");
		sqlb.append(" where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and vbillstatus = "+IBillStatus.CHECKPASS);
		sqlb.append(" and h.pk_billtype = '"+pricetype+"'");
		sqlb.append(" and h.carriersid='"+billvo.getHeaderVO().getCarriersid()+"'");
		sqlb.append(" and h.reserve1='"+m_sendbody.getPk_destore()+"'");//�����ֿ�
		sqlb.append(" and (isnull(b.ifw,0)= "+SendBillVO.YYFW_ALL +" or b.ifw = ");//Ӧ�˷�Χ����
		if(isSale()){//�Ƿ�����
			sqlb.append(SendBillVO.YYFW_SALE+")");
		}else{
			sqlb.append(SendBillVO.YYFW_ZFC+")");
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(getPricePeriodWhereSql())!=null)//�����������
			sqlb.append(" and "+getPricePeriodWhereSql());
		lprice = (List<TranspriceBVO>)getDao().executeQuery(sqlb.toString(), new BeanListProcessor(TranspriceBVO.class));
		return lprice;
	}
	
	@Override
	public String getPricePeriodWhereSql()  throws BusinessException{
		UFDouble nxs = getTotalXS();
		UFDouble gls =getGls();
		return " h.nmincase <= "+nxs.doubleValue()+" and h.nmaxcase >= "+nxs.doubleValue()+
		"  and b.nmindistance <= "+gls.doubleValue()+" and b.nmaxdistance >= "+gls.doubleValue();

	}
	
}
