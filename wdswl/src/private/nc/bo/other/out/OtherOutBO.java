package nc.bo.other.out;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.MyBillVO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.pub.VOStatus;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.tool.WriteBackTool;
/**
 * @author mlr
 */
public class OtherOutBO {
	/**
	 * 其他出库单  销售出库单 回写数据来源
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
	public void writeBack(MyBillVO newBillVo, int iBdAction) throws Exception {
		if (newBillVo == null || newBillVo.getParentVO() == null
				|| newBillVo.getChildrenVO() == null
				|| newBillVo.getChildrenVO().length == 0)
			return;
		TbOutgeneralBVO[] bodys = (TbOutgeneralBVO[]) ObjectUtils
				.serializableClone(newBillVo.getChildrenVO());
		if (bodys == null || bodys.length == 0)
			return;
		String sourcetype = null;
		sourcetype = PuPubVO.getString_TrimZeroLenAsNull(bodys[0]
				.getCsourcetype());
		if (sourcetype == null)// 其他出库和销售出库自制的情况不需要回写
			return;

		// 进行数据会写
		WriteBackTool.setVsourcebillid("csourcebillhid");
		WriteBackTool.setVsourcebillrowid("csourcebillbid");
		WriteBackTool.setVsourcebilltype("csourcetype");
		if (iBdAction == IBDACTION.SAVE) {
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if(sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)){
				WriteBackTool.writeBack(bodys, "wds_soorder_b",
						"pk_soorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });			
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSS)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if(sourcetype.equalsIgnoreCase(WdsWlPubConst.HWTZ)){
				WriteBackTool.writeBack(bodys, "wds_transfer_b",
						"general_b_pk", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "nacceptnum",
								"nassacceptnum" }, new String[] { "noutnum",
								"noutassistnum" });			
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSG)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
				WriteBackTool.writeBack(bodys, "wds_cgqy_b",
						"pk_cgqy_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
		} else if (iBdAction == IBDACTION.DELETE) {
			for (int i = 0; i < bodys.length; i++) {
				bodys[i].setStatus(VOStatus.DELETED);
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS3)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if(sourcetype.equalsIgnoreCase(WdsWlPubConst.WDS5)){
				//noutnumnoutassistnum
				WriteBackTool.writeBack(bodys, "wds_soorder_b",
						"pk_soorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });			
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSS)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if(sourcetype.equalsIgnoreCase(WdsWlPubConst.HWTZ)){
				WriteBackTool.writeBack(bodys, "wds_transfer_b",
						"general_b_pk", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "nacceptnum",
								"nassacceptnum" }, new String[] { "noutnum",
								"noutassistnum" });			
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSG)) {
				WriteBackTool.writeBack(bodys, "wds_sendorder_b",
						"pk_sendorder_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
			if (sourcetype.equalsIgnoreCase(WdsWlPubConst.WDSC)) {
				WriteBackTool.writeBack(bodys, "wds_cgqy_b",
						"pk_cgqy_b", new String[] { "noutnum",
								"noutassistnum" }, new String[] { "noutnum",
								"nassoutnum" }, new String[] { "noutnum",
								"nassoutnum" });
			}
		}
	}
}
