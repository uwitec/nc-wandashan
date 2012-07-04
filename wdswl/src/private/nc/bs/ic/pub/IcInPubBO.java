package nc.bs.ic.pub;

import nc.itf.scm.cenpur.service.TempTableUtil;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.tool.WriteBackTool;

public class IcInPubBO {

	private TempTableUtil ttutil = null;

	private TempTableUtil getTempTableUtil() {
		if (ttutil == null)
			ttutil = new TempTableUtil();
		return ttutil;
	}

	/**
	 * 
	 * @作者：mlr
	 * @时间：2011-4-8下午06:52:43
	 * @param newBillVo
	 *            要操作的单据数据
	 * @param iBdAction
	 *            操作状态
	 * @param isNew
	 *            是否新增保存
	 * @throws Exception
	 * @throws Exception
	 */
	public void writeBackForInBill(OtherInBillVO newBillVo, int iBdAction)
			throws Exception {
		if (newBillVo == null || newBillVo.getParentVO() == null
				|| newBillVo.getChildrenVO() == null
				|| newBillVo.getChildrenVO().length == 0)
			return;
		TbGeneralBVO[] bodys = (TbGeneralBVO[]) newBillVo.getChildrenVO();
		String billtype = PuPubVO.getString_TrimZeroLenAsNull(newBillVo
				.getHeaderVo().getGeh_billtype());
		if (billtype == null) {
			throw new BusinessException("单据类型为空");
		}
		if (billtype.equalsIgnoreCase(WdsWlPubConst.BILLTYPE_ALLO_IN)) {
			WriteBackTool.setVsourcebillid("gylbillhid");
			WriteBackTool.setVsourcebillrowid("gylbillbid");
			WriteBackTool.setVsourcebilltype("gylbilltype");
			String sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
					.getGylbilltype());
			// 自制单据不进行回写
			if (sourcetype == null) {
				return;
			}
			if (iBdAction == IBDACTION.SAVE) {
				if (sourcetype.equals("4Y")) {
					WriteBackTool
							.writeBack(
									bodys,
									"ic_general_b",
									"cgeneralbid",
									new String[] { "geb_anum" },
									new String[] { WdsWlPubConst.erp_allo_outnum_fieldname },
									new String[] { WdsWlPubConst.erp_allo_outnum_fieldname });
				}
			} else if (iBdAction == IBDACTION.DELETE) {
				for (int i = 0; i < bodys.length; i++) {
					bodys[i].setStatus(VOStatus.DELETED);
				}
				if (sourcetype.equals("4Y")) {
					WriteBackTool
							.writeBack(
									bodys,
									"ic_general_b",
									"cgeneralbid",
									new String[] { "geb_anum" },
									new String[] { WdsWlPubConst.erp_allo_outnum_fieldname },
									new String[] { WdsWlPubConst.erp_allo_outnum_fieldname });
				}
			}

		} else if (billtype.equalsIgnoreCase(WdsWlPubConst.BILLTYPE_OTHER_IN)) {
			WriteBackTool.setVsourcebillid("csourcebillhid");
			WriteBackTool.setVsourcebillrowid("csourcebillbid");
			WriteBackTool.setVsourcebilltype("csourcetype");
			String sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
					.getCsourcetype());
			// 自制单据不进行回写
			if (sourcetype == null) {
				return;
			}
			if (iBdAction == IBDACTION.SAVE) {
				if (sourcetype.equals(WdsWlPubConst.BILLTYPE_OTHER_OUT)) {
					WriteBackTool.writeBack(bodys, "tb_outgeneral_b",
							"general_b_pk", new String[] { "geb_anum",
									"geb_banum" }, new String[] { "nacceptnum",
									"nassacceptnum" }, new String[] {
									"nacceptnum", "nassacceptnum" });
				}
			} else if (iBdAction == IBDACTION.DELETE) {
				for (int i = 0; i < bodys.length; i++) {
					bodys[i].setStatus(VOStatus.DELETED);
				}
				if (sourcetype.equals(WdsWlPubConst.BILLTYPE_OTHER_OUT)) {
					WriteBackTool.writeBack(bodys, "tb_outgeneral_b",
							"general_b_pk", new String[] { "geb_anum",
									"geb_banum" }, new String[] { "nacceptnum",
									"nassacceptnum" }, new String[] {
									"nacceptnum", "nassacceptnum" });
				}
			}

		}else{
			WriteBackTool.setVsourcebillid("csourcebillhid");
			WriteBackTool.setVsourcebillrowid("csourcebillbid");
			WriteBackTool.setVsourcebilltype("csourcetype");
			String sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
					.getCsourcetype());
			// 自制单据不进行回写
			if (sourcetype == null) {
				return;
			}
			if (iBdAction == IBDACTION.SAVE) {
				if (sourcetype.equals(WdsWlPubConst.WDSS)) {
					WriteBackTool.writeBack(bodys, "wds_sendorder_b",
							"pk_sendorder_b", new String[] { "geb_anum",
									"geb_banum" }, new String[] { "noutnum",
									"nassoutnum" }, new String[] { "noutnum",
									"nassoutnum" });
				}
			} else if (iBdAction == IBDACTION.DELETE) {
				for (int i = 0; i < bodys.length; i++) {
					bodys[i].setStatus(VOStatus.DELETED);
				}
				if (sourcetype.equals(WdsWlPubConst.WDSS)) {
					WriteBackTool.writeBack(bodys, "wds_sendorder_b",
							"pk_sendorder_b", new String[] { "geb_anum",
									"geb_banum" }, new String[] { "noutnum",
									"nassoutnum" }, new String[] { "noutnum",
									"nassoutnum" });
				}
			}		
		}
	}

	public void checkNoutNumByOrderNum(String sourcetype, String[] ids)
			throws BusinessException {
		String sql = null;
		if (WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(sourcetype)) {
			sql = "select count(0) from tb_outgeneral_b where (coalesce(noutnum,0) - coalesce(nacceptnum,0)) < 0 "
					+ " and general_b_pk in "
					+ getTempTableUtil().getSubSql(ids);
		} else if (ScmConst.m_allocationOut.equalsIgnoreCase(sourcetype)) {
			sql = "select count(0) from ic_general_b where (coalesce(noutnum, 0) - coalesce("
					+ WdsWlPubConst.erp_allo_outnum_fieldname
					+ ", 0) - coalesce(naccumwastnum, 0)) < 0 "
					+ " and cgeneralbid in "
					+ getTempTableUtil().getSubSql(ids);
		}
		// if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
		// WdsPubResulSetProcesser.COLUMNPROCESSOR), -1)>0){
		// throw new BusinessException("实入数量超出来源单据发货数量");
		// }
	}

}
