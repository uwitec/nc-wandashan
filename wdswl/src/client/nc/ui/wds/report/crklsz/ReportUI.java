package nc.ui.wds.report.crklsz;
import java.util.List;
import java.util.Map;
import javax.swing.ListSelectionModel;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportItem;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wds.pub.button.report2.LevelSubTotalAction;
import nc.ui.wl.pub.CombinVO;
import nc.ui.wl.pub.report.ReportPubTool;
import nc.ui.wl.pub.report.ZmReportBaseUI2;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * 
 * @author yf �������ˮ��
 * 
 */
public class ReportUI extends ZmReportBaseUI2 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2193523266502400113L;
   //���̲�չ���ĺϲ�ά��
	private String[]  combinFields={"billtype","pk_bill","dbilldate",
			                        "coutwarehouseid","psnid","vbillcode"
			                        ,"coperatorid","cregister","pk_bill_b"
			                        ,"pk_cargdoc","vsourcebillcode","pk_invmandoc"
			                        ,"vbatchcode","isxnap","isgift"
			                       };
   //����չ���ĺϲ�ά��
	private String[]  combinFields1={"billtype","pk_bill","dbilldate",
            "coutwarehouseid","psnid","vbillcode"
            ,"coperatorid","cregister","pk_bill_b"
            ,"pk_cargdoc","vsourcebillcode","pk_invmandoc"
            ,"vbatchcode","isxnap","isgift"
            ,"cdt_pk"};
	// ��ͷ���Ƶ����ڡ������ֿ⡢���Ա����Դ���ݺš����ݺš��Ƶ��ˡ�ǩ����
	private String[] select_fields_out_h = new String[] { "vbilltype billtype",
			"general_pk pk_bill", "dbilldate dbilldate","vnote",//��ע
			"srl_pk coutwarehouseid", "cwhsmanagerid psnid",
			"vbillcode vbillcode", "coperatorid coperatorid",
			"cregister cregister" };// ���۳��⣬��������

	private String[] select_fields_in_h = new String[] {
			"geh_billtype billtype", "geh_pk pk_bill",
			"geh_dbilldate dbilldate", "geh_cwarehouseid cinwarehouseid","vnote",//��ע
			"geh_cwhsmanagerid psnid", "geh_billcode vbillcode",
			"coperatorid coperatorid", "geh_storname cregister" };// �������,�������

	// ���壺��λ�����̡���������Ρ����⡢���������
	private String[] select_fields_out_b = new String[] {
			"general_b_pk pk_bill_b",
			"cspaceid pk_cargdoc",
			"vfirstbillcode vsourcebillcode",
			// "",//û��������Ϣ
			"cinvbasid pk_invbasdoc", "vbatchcode vbatchcode",
			"isxnap isxnap", "flargess isgift"
			 };// ���۳���,��������

	private String[] select_fields_in_b = new String[] { "geb_pk pk_bill_b",
			"geb_space pk_cargdoc", "vfirstbillcode vsourcebillcode",
			// "",//û��������Ϣ
			"geb_cinvbasid pk_invbasdoc", "geb_vbatchcode vbatchcode",
			// "isxnap isxnap",//û����������
			"geb_flargess isgift"};// �������,�������

	// �������
	private String[] select_fields_out_bb = new String[] { "cdt_pk" };// ���۳���,��������
	private String[] select_fields_in_bb = new String[] { "cdt_pk" };// ���۳���,��������
	
	

	public ReportUI() {
		super();
		setLocation(2);
		getReportBase().getBillTable().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	/*
	 * ���۳���
	 */
	private String getQuerySQL1() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		for (int i = 0; i < select_fields_out_h.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("h." + select_fields_out_h[i]);
		}
		sql.append(" , ");
		for (int i = 0; i < select_fields_out_b.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("b." + select_fields_out_b[i]);
		}
		sql.append(" , ");
		sql.append(" bb.stockpieces  ninnum ,");
		sql.append(" bb.stocktonnage  nassinnum ");
		
		if (getGroupByOrSelectConditon() != null
				&& getGroupByOrSelectConditon().trim().length() > 0) {
			sql.append(" , bb.cdt_pk ");
		}
	

		sql.append(" from ");
		sql.append(" tb_outgeneral_h h,tb_outgeneral_b b,tb_outgeneral_t bb");

		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0) = 0 ");
		sql.append(" and h.general_pk = b.general_pk ");
		sql.append(" and b.general_b_pk = bb.general_b_pk ");

		if (getQueryConditon(0) != null && getQueryConditon(0).length() != 0) {
			sql.append(" and ");
			sql.append(getQueryConditon(0));
		}
		return sql.toString();
	}

	@Override
	public void setItemsAfter(ReportItem it, List<ReportItem> list) {
		it.setShow(false);
		if (it.getKey().equalsIgnoreCase("cdt_pk")) {
			it.setLoadFormula(new String[] { "cdtcode->getColValue(bd_cargdoc_tray,cdt_traycode,cdt_pk,cdt_pk)" });
			ReportItem it1 = ReportPubTool.getItem("cdtcode", "����",
					IBillItem.STRING, 2, 80);
			list.add(it1);
		}
	}

	protected String getQueryConditon(int type) throws Exception {
		QueryDLG querylg = getQueryDlg();// ��ȡ��ѯ�Ի���
		ConditionVO[] vos = querylg.getConditionVO();// ��ȡ�ѱ��û���д�Ĳ�ѯ����
		ConditionVO[] vos1 = filterQuery(vos);
		filterQuery2(vos1, type);
		String sql = querylg.getWhereSQL(vos1);
		return sql;
	}

	private void filterQuery2(ConditionVO[] vos, int type) {
		if (vos == null || vos.length == 0)
			return;
		if (type == 0) {
			for (int i = 0; i < vos.length; i++) {
				if ("dbilldate".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("dbilldate");
				}
				if ("srl_pk".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("srl_pk");
				}
			}
		} else if (type == 1) {
			for (int i = 0; i < vos.length; i++) {
				if ("dbilldate".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("geh_dbilldate");
				}
				if ("srl_pk".equals(vos[i].getFieldCode())) {
					vos[i].setFieldCode("geh_cwarehouseid");
				}
			}
		}
	}

	/*
	 * ��������
	 */
	private String getQuerySQL2() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		for (int i = 0; i < select_fields_in_h.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("h." + select_fields_in_h[i]);
		}
		sql.append(" , ");
		for (int i = 0; i < select_fields_in_b.length; i++) {
			if (i > 0) {
				sql.append(" , ");
			}
			sql.append("b." + select_fields_in_b[i]);
		}
		sql.append(" , ");
		sql.append(" bb.gebb_num  ninnum ,");
		sql.append(" bb.ninassistnum  nassinnum ");
		
		if (getGroupByOrSelectConditon() != null
				&& getGroupByOrSelectConditon().trim().length() > 0) {
			sql.append(" , bb.cdt_pk ");
		}
		sql.append(" from ");
		sql.append(" tb_general_h h,tb_general_b b,tb_general_b_b bb ");

		sql.append(" where ");
		sql.append(" isnull(h.dr,0) = 0 and isnull(b.dr,0) = 0 ");
		sql.append(" and h.geh_pk = b.geh_pk ");
		sql.append(" and b.geb_pk = bb.geb_pk ");
		if (getQueryConditon(1) != null && getQueryConditon(1).length() != 0) {
			sql.append(" and ");
			sql.append(getQueryConditon(1));
		}
		
		return sql.toString();
	}

	@Override
	public void onQuery() {
		getQueryDlg().showModal();
		if (getQueryDlg().getResult() == UIDialog.ID_OK) {
			try {
				// ��ձ�������
				clearBody();
				setDynamicColumn1();
				// �õ���ѯ���
				List<ReportBaseVO[]> list = getReportVO(new String[] {
						getQuerySQL1(), getQuerySQL2() });

				ReportBaseVO[] vos = null;
				vos = combinListVOs(list);
				if (vos == null || vos.length == 0)
					return;
				if (vos != null) {
					super.updateBodyDigits();
					setBodyVO(vos);
					setTolal1();
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setTolal1() throws Exception {
		   new LevelSubTotalAction(this).atuoexecute2();  			
		
	}

	private ReportBaseVO[] combinListVOs(List<ReportBaseVO[]> list)
			throws Exception {
		if (list == null || list.size() == 0) {
			return null;
		}
		ReportBaseVO[] vos1 = null;
		ReportBaseVO[] vos2 = null;
		// ReportBaseVO[] vos3 = null;
		if (list.get(0) != null || list.get(0).length > 0) {
			vos1 = list.get(0);
		}
		if (list.get(1) != null || list.get(1).length > 0) {
			vos2 = list.get(1);
		}
		// if (list.get(2) != null || list.get(2).length > 0) {
		// vos3 = list.get(2);
		// }
		ReportBaseVO[] rvos=null;
		ReportBaseVO[] combinvo = null;
		combinvo = CombinVO.comin(combinvo, vos1);
		combinvo = CombinVO.comin(combinvo, vos2);
		ConditionVO[] conds=getQueryDlg().getConditionVOsByFieldCode("iscdt_pk");
		boolean iscat=false;
		if(conds==null || conds.length==0)
			iscat=false;
		else{
			iscat=PuPubVO.getUFBoolean_NullAs(getQueryDlg().getConditionVOsByFieldCode("iscdt_pk")[0].getValue(), UFBoolean.FALSE).booleanValue(); //�Ƿ�չ��
		}
		if(combinvo==null || combinvo.length==0)
			return null;
		if(iscat){
			rvos=(ReportBaseVO[]) CombinVO.combinData(combinvo, combinFields1, new String[]{"ninnum","nassinnum"}, ReportBaseVO.class);
		}else{
			rvos=(ReportBaseVO[]) CombinVO.combinData(combinvo, combinFields, new String[]{"ninnum","nassinnum"}, ReportBaseVO.class);
		}	
 		return rvos;
		// return setVoByContion(combinvo, voCombinConds);
	}

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	@Override
	public void initReportUI() {

	}

	@Override
	public String _getModelCode() {
		return WdsWlPubConst.report_crklsz;
	}
}
