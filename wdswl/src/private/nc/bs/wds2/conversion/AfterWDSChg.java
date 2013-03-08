package nc.bs.wds2.conversion;

import nc.bs.framework.common.NCLocator;
import nc.bs.trade.business.HYPubBO;
import nc.itf.uap.bd.cust.ICuBasDocQry;
import nc.vo.bd.b08.CubasdocVO;
import nc.vo.bd.b08.CustAddrVO;
import nc.vo.bd.b08.CustBasVO;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.dm.so.order.SoorderVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;

/**
 * ���۰��ź����۰���2--->�����˵������������������
 * @author Administrator
 *
 */
public class AfterWDSChg implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		if(nowVo == null)
			return nowVo;
		SuperVO head = (SuperVO)nowVo.getParentVO();
		head.setAttributeValue("vbillno",new HYPubBO().getBillNo(head.getAttributeValue("pk_billtype").toString(), head.getAttributeValue("pk_corp").toString(), null, null));
		head.setAttributeValue("itransstatus", 0);//Ĭ����;
		setDefualtValue(nowVo);
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVos, AggregatedValueObject[] nowVos)
			throws BusinessException {
		if(nowVos ==null || nowVos.length==0){
			return null;
		}
		HYPubBO bo = new HYPubBO();
		for(AggregatedValueObject nowVo:nowVos){
			SuperVO head = (SuperVO)nowVo.getParentVO();
			head.setAttributeValue("vbillno", bo.getBillNo(head.getAttributeValue("pk_billtype").toString(), head.getAttributeValue("pk_corp").toString(), null, null));
			head.setAttributeValue("itransstatus", 0);//Ĭ����;
			setDefualtValue(nowVo);
		}
		return nowVos;
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2013-3-7����02:07:29
	 */
	private void setDefualtValue(AggregatedValueObject nowVo) throws BusinessException{
		//�������̹�������Ϣ
		if(nowVo == null || nowVo.getParentVO() == null ){
			return;
		}
		SoorderVO parent = (SoorderVO)nowVo.getParentVO();
		String pk_cumandoc = PuPubVO.getString_TrimZeroLenAsNull(parent.getPk_cumandoc());		
		String pk_cubasdoc =PuPubVO.getString_TrimZeroLenAsNull(parent.getPk_cubasdoc());		
		if(pk_cubasdoc == null){
			HYPubBO bo = new HYPubBO();
			CumandocVO vo = (CumandocVO)bo.queryByPrimaryKey(CumandocVO.class, pk_cumandoc);
			pk_cubasdoc = vo.getPk_cubasdoc();
		}
		if(pk_cubasdoc == null){
			return ;
		}
		ICuBasDocQry qry = (ICuBasDocQry)NCLocator.getInstance().lookup(ICuBasDocQry.class.getName());
		CubasdocVO basvo = qry.findCubasdocVOByPK(pk_cubasdoc);
		CustBasVO basehead =(CustBasVO) basvo.getCustBasVO();
		parent.setReserve2(basehead.getMobilephone3());//��ϵ�˵绰
		parent.setReserve3(basehead.getLinkman1());//ҵ�����
		parent.setVyedbtel(basehead.getPhone1());//ҵ��绰

		//---------���ÿ����ջ���ַ,WMS��ౣ��4���ջ���ַ
		CircularlyAccessibleValueObject[] custAddrVOs = basvo.getCustAddrVOs();
		if(custAddrVOs !=null){
			for(int i=0;i<custAddrVOs.length;i++){
				CustAddrVO addrVO= (CustAddrVO)custAddrVOs[i];
				UFBoolean  m_defaddrflag =addrVO.getDefaddrflag();
				if(m_defaddrflag !=null && m_defaddrflag==UFBoolean.TRUE){
					parent.setReserve1(addrVO.getLinkman());//��ϵ��
					parent.setVtelphone(addrVO.getPhone());//��ϵ�绰
					parent.setVinaddress(addrVO.getAddrname());//�ջ���ַ
					parent.setCustareaid(addrVO.getPk_areacl());//������վ

				}
			}
		}
		
		
	}

}
