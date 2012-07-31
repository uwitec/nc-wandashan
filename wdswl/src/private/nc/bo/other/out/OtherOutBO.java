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
	 * �������ⵥ  ���۳��ⵥ ��д������Դ
	 * 
	 * @���ߣ�mlr
	 * @ʱ�䣺2011-4-8����06:52:43
	 * @param newBillVo
	 *            Ҫ�����ĵ�������
	 * @param iBdAction
	 *            ����״̬
	 * @param isNew
	 *            �Ƿ���������
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
		if (sourcetype == null)// ������������۳������Ƶ��������Ҫ��д
			return;

		// �������ݻ�д
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
