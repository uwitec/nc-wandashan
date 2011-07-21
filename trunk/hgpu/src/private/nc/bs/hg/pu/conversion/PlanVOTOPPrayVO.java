package nc.bs.hg.pu.conversion;

import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pu.conversion.TranPrayTools;
import nc.bs.pu.pub.PubDMO;
import nc.bs.scm.pub.BillRowNoDMO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.po.pub.OrderPubVO;
import nc.vo.pr.pray.PraybillHeaderVO;
import nc.vo.pr.pray.PraybillItemVO;
import nc.vo.pr.pray.PraybillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.BillTypeConst;
import nc.vo.scm.pub.vosplit.SplitBillVOs;

public class PlanVOTOPPrayVO implements nc.vo.pf.change.IchangeVO {
	/**
	 * SOrderVOTOPPrayVO 构造子注解。
	 */
	public PlanVOTOPPrayVO() {
		super();
	}

	/**
	 * 作者：李金巧 功能：验证VO合法性 参数： 返回： 例外： 日期：(2002-6-13 10:50:43) 修改日期，修改人，修改原因，注释标志：
	 * 2002-06-13 wyf 修改判定表体空的错误
	 */
	private void checkVO(PraybillVO voPray) throws BusinessException {
		if (voPray.getBodyVO() == null) {
			PubDMO
					.throwBusinessException(new BusinessException(
							nc.bs.ml.NCLangResOnserver.getInstance()
									.getStrByID("4004pub", "UPP4004pub-000060")/*
																				 * @res
																				 * "请购单表体为空，无法生成！"
																				 */));
		}
		try{
			for (int i = 0; i < voPray.getChildNum(); i++) {
				PraybillItemVO item = (PraybillItemVO) voPray.getChildrenVO()[i];
				UFDouble nassistnum = item.getNassistnum();
				UFDouble npraynum = item.getNpraynum();
				if (nassistnum != null && npraynum != null) {
					if (nassistnum.doubleValue() > 0 && npraynum.doubleValue() < 0) {
						PubDMO
								.throwBusinessException(new BusinessException(
										nc.bs.ml.NCLangResOnserver.getInstance()
												.getStrByID("4004pub",
														"UPP4004pub-000061")/*
																			 * @res
																			 * "请购数量与辅数量正负不一致，无法生成请购单！"
																			 */));
					}
					if (nassistnum.doubleValue() < 0 && npraynum.doubleValue() > 0) {
						PubDMO
								.throwBusinessException(new BusinessException(
										nc.bs.ml.NCLangResOnserver.getInstance()
												.getStrByID("4004pub",
														"UPP4004pub-000061")/*
																			 * @res
																			 * "请购数量与辅数量正负不一致，无法生成请购单！"
																			 */));
					}
					//检查存货是否已经分配到此公司
					if (item.getCmangid() == null || item.getCmangid().trim().length() ==0){
						Object[][] retObj = new PubDMO().queryArrayValue(
									"bd_invbasdoc", "pk_invbasdoc",
									new String[] { "invname" }, new String[] { item
											.getCbaseid() });
						String invname = "";
						if(retObj != null && retObj[0]!=null && retObj[0][0] != null){
							invname =  retObj[0][0].toString();
						}
						retObj = new PubDMO().queryArrayValue(
								"bd_corp", "pk_corp",
								new String[] { "unitname" }, new String[] { voPray.getPk_corp()});
						String unitname = "";
						if(retObj != null && retObj[0]!=null && retObj[0][0] != null){
							unitname =  retObj[0][0].toString();
						}
						PubDMO.throwBusinessException(new BusinessException(
								NCLangResOnserver.getInstance().getStrByID(
										"common", "4004COMMON000000117",
										null,
										new String[]{invname,unitname}/* @res"存货管理档案为空，请检查存货：{0}，是否分配到公司{1} */)));
					}
				}
			}
		}catch(Exception e){
			PubDMO.throwBusinessException(e);
		}
	}
	/**
	 * 获得转换后的VO。 创建日期：(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject retChangeBusiVO(
			nc.vo.pub.AggregatedValueObject sorceVO,
			nc.vo.pub.AggregatedValueObject tagVO) throws BusinessException {
		try {
			PraybillVO voPray = (PraybillVO) tagVO;
			PraybillHeaderVO head = voPray.getHeadVO();
			PraybillItemVO[] items = voPray.getBodyVO();
			checkVO(voPray);
			//集采相关
			// 请购单的PUSHSAVE脚本中已经进行了设置
//			TranPrayTools.setCenterPurInfos(voPray,new Integer(8));
			//请购类型
//			TranPrayTools.setBusiAndPrayTypes(voPray,new Integer(8),false);
			head.setIpraytype(HgPubConst.IPRAYTYPE);
			for(PraybillItemVO item:items){
				item.setIpraytype(HgPubConst.IPRAYTYPE);
			}
			//行状态
			PubDMO.setLineStatus(tagVO, new Integer(nc.vo.pub.VOStatus.NEW));
			//建议单价
//			TranPrayTools.getInstance().setRulePrice(voPray);
			//建议订货日期
//			TranPrayTools.setSuggestDate(voPray);
		} catch (Exception e) {
			PubDMO.throwBusinessException(e);
		}
		return tagVO;
	}

	/**
	 * 获得转换后的VO。 创建日期：(2001-10-11 9:52:28)
	 * 
	 * @return java.lang.String[]
	 */
	public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(
			nc.vo.pub.AggregatedValueObject[] sorceVOs,
			nc.vo.pub.AggregatedValueObject[] tagVOs) throws BusinessException {
		PraybillVO[] voaRet = null;
		try {
			int iLen = sorceVOs.length;
			for (int i = 0; i < iLen; i++) {
				retChangeBusiVO(sorceVOs[i], tagVOs[i]);
			}
			//重新分单(上游按行生成多张请购单，此处按业务类型重新组合请购单)
			voaRet = (PraybillVO[]) SplitBillVOs.getSplitVOs(PraybillVO.class.getName(),
					PraybillHeaderVO.class.getName(),
					PraybillItemVO.class.getName(),
					tagVOs,
					null,
					new String[]{"cbiztype","ipraytype",HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME});
			
			//设置采购方式属性
			PraybillHeaderVO head = null;
			PraybillItemVO[] items = null;
			for (PraybillVO vo : voaRet) {//将采购方式从表体转移到表头自定义项
				head = vo.getHeadVO();
				items = vo.getBodyVO();
				head.setAttributeValue(
								HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME2,
								items[0].getAttributeValue(HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME));

				head.setAttributeValue(
								HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME,
								HgBsPubTool.getDefDoc_PuchType1()
										.get(items[0].getAttributeValue(HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME)));
				for (PraybillItemVO item : items) {
					item.setAttributeValue(
							HgPubConst.PRAY_BILL_PURTYPE_FIELDNAME, null);
				}
			}
			
			// 设置行号
			BillRowNoDMO.setVOsRowNoByRule(voaRet,BillTypeConst.PO_PRAY, "crowno");
			//辅计量计算
			TranPrayTools.setDefaultAssMeas(voaRet,true);
			//设置请购人、请购部门、计划员
			TranPrayTools.setPsnDpt(voaRet);
			// 设置业务类型及请购类型
			//TranPrayTools.setBusiAndPrayTypesToHead(voaRet);
      //跨公司项目ID转换
      OrderPubVO.chgDataForPrayCorp(voaRet);
		} catch (Exception e) {
			PubDMO.throwBusinessException(e);
		}
		return voaRet;
	}
	
}
