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
 * 销售安排和销售安排2--->销售运单交换的类后续处理类
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
		head.setAttributeValue("itransstatus", 0);//默认在途
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
			head.setAttributeValue("itransstatus", 0);//默认在途
			setDefualtValue(nowVo);
		}
		return nowVos;
	}
	/**
	 * 
	 * @throws BusinessException 
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 * @时间：2013-3-7下午02:07:29
	 */
	private void setDefualtValue(AggregatedValueObject nowVo) throws BusinessException{
		//带出客商管理档案信息
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
		parent.setReserve2(basehead.getMobilephone3());//联系人电话
		parent.setReserve3(basehead.getLinkman1());//业务代表
		parent.setVyedbtel(basehead.getPhone1());//业务电话

		//---------设置客商收货地址,WMS最多保存4个收货地址
		CircularlyAccessibleValueObject[] custAddrVOs = basvo.getCustAddrVOs();
		if(custAddrVOs !=null){
			for(int i=0;i<custAddrVOs.length;i++){
				CustAddrVO addrVO= (CustAddrVO)custAddrVOs[i];
				UFBoolean  m_defaddrflag =addrVO.getDefaddrflag();
				if(m_defaddrflag !=null && m_defaddrflag==UFBoolean.TRUE){
					parent.setReserve1(addrVO.getLinkman());//联系人
					parent.setVtelphone(addrVO.getPhone());//联系电话
					parent.setVinaddress(addrVO.getAddrname());//收货地址
					parent.setCustareaid(addrVO.getPk_areacl());//到货物站

				}
			}
		}
		
		
	}

}
