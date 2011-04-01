package nc.bs.wds.ic.so.out;

import java.util.ArrayList;
import java.util.List;

import nc.bs.trade.business.HYPubBO;
import nc.bs.trade.business.IBDBusiCheck;
import nc.ui.pub.ClientEnvironment;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.wds.w8004040204.TbOutgeneralBVO;
import nc.vo.wds.w8004040204.TbOutgeneralHVO;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
/**
 * 
 * @author Administrator
 * 完达山项目--销售出库(WDS8)
 */
public class SoOutBO  implements IBDBusiCheck{

	public void check(int arg0, AggregatedValueObject arg1, Object arg2)
			throws Exception {
		
		
	}

	public void dealAfter(int arg0, AggregatedValueObject arg1, Object arg2)
			throws Exception {
		if(arg1 == null)
			return;
		insertFyd(arg1);
		
	}
	
	/**
	 * 生成发运单
	 * 
	 * @throws Exception
	 */
	private void insertFyd(AggregatedValueObject arg1) throws Exception {
		TbOutgeneralHVO generalh =(TbOutgeneralHVO) arg1.getParentVO();
		TbOutgeneralBVO[] generalb = (TbOutgeneralBVO[])arg1.getChildrenVO();
		Object[] o = new Object[3];
		o[0] = false;
		TbFydnewVO fydvo = new TbFydnewVO();
		List<TbFydmxnewVO[]> fydmxList = new ArrayList<TbFydmxnewVO[]>();

		// 进行VO转换/////////////////////////////////////////////

		// ------------转换表头对象-----------------//
		
		if (null != generalh && null != generalb && generalb.length > 0) {
			if (null != generalh.getVdiliveraddress()
					&& !"".equals(generalh.getVdiliveraddress())) {
				fydvo.setFyd_shdz(generalh.getVdiliveraddress()); // 收货地址
			}
			if (null != generalh.getVnote() && !"".equals(generalh.getVnote())) {
				fydvo.setFyd_bz(generalh.getVnote()); // 备注
			}
			if (null != generalh.getCdptid()
					&& !"".equals(generalh.getCdptid())) {
				fydvo.setCdeptid(generalh.getCdptid()); // 部门
			}
			// 设置运货方式
			fydvo.setFyd_yhfs("汽运");
			// 单据类型 0 发运制单 1 销售订单 2 分厂直流 3拆分订单4 合并订单 8 出库自制单据生成的运单
			fydvo.setBilltype(8);
			fydvo.setVbillstatus(1);
			// 单据号
			fydvo.setVbillno(generalh.getVbillcode());
			// 制单日期
			fydvo.setDmakedate(generalh.getDbilldate());
			fydvo.setVoperatorid(ClientEnvironment.getInstance().getUser()
					.getPrimaryKey()); // 设置制单人
			// 设置发货站
			fydvo.setSrl_pk(generalh.getSrl_pk());
			// 到货站
			fydvo.setSrl_pkr(generalh.getSrl_pkr());
			// --------------转换表头结束---------------//
			// --------------转换表体----------------//
			List<TbFydmxnewVO> tbfydmxList = new ArrayList<TbFydmxnewVO>();
			for (int j = 0; j < generalb.length; j++) {
				TbFydmxnewVO fydmxnewvo = new TbFydmxnewVO();
				TbOutgeneralBVO genb = generalb[j];
				if (null != genb.getCinventoryid()
						&& !"".equals(genb.getCinventoryid())) {
					fydmxnewvo.setPk_invbasdoc(genb.getCinventoryid()); // 单品主键
				}
				if (null != genb.getNshouldoutnum()
						&& !"".equals(genb.getNshouldoutnum())) {
					fydmxnewvo.setCfd_yfsl(genb.getNshouldoutnum()); // 应发数量
				}
				if (null != genb.getNshouldoutassistnum()
						&& !"".equals(genb.getNshouldoutassistnum())) {
					fydmxnewvo.setCfd_xs(genb.getNshouldoutassistnum()); // 箱数
				}
				if (null != genb.getNoutnum() && !"".equals(genb.getNoutnum())) {
					fydmxnewvo.setCfd_sfsl(genb.getNoutnum()); // 实发数量
				}
				if (null != genb.getNoutassistnum()
						&& !"".equals(genb.getNoutassistnum())) {
					fydmxnewvo.setCfd_sffsl(genb.getNoutassistnum()); // 实发辅数量
				}
				if (null != genb.getCrowno() && !"".equals(genb.getCrowno())) {
					fydmxnewvo.setCrowno(genb.getCrowno()); // 行号
				}
				if (null != genb.getUnitid() && !"".equals(genb.getUnitid())) {
					fydmxnewvo.setCfd_dw(genb.getUnitid()); // 单位
				}
				fydmxnewvo.setCfd_pc(genb.getVbatchcode()); // 批次				
				tbfydmxList.add(fydmxnewvo);
			}
			// ----------------转换表体结束---------------------//
				TbFydmxnewVO[] fydmxVO = new TbFydmxnewVO[tbfydmxList.size()];
				tbfydmxList.toArray(fydmxVO);
				fydmxList.add(fydmxVO);
				o[0] = true;
		HYBillVO billVo = new HYBillVO();
		billVo.setParentVO(fydvo);
		billVo.setChildrenVO(fydmxVO);
		new HYPubBO().saveBD(billVo, null);
	}
	}
}