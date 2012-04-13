package nc.ui.wds.report.ckcrlsz;

import javax.swing.ListSelectionModel;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.IUFTypes;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report.SubtotalVO;
import nc.vo.zmpub.pub.report2.ReportBaseUI;

/**
 * �����ֿ�������ˮ��--����
 * @author Administrator
 */
public class ReportUI extends ReportBaseUI{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String ddatefrom =null;
    private String ddateto = null;

	@Override
	public String _getModelCode() {
		// TODO Auto-generated method stub
		return WdsWlPubConst.REPORT08;
	}
	@Override
	public void setUIAfterLoadTemplate() {
        getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
	}
	
	@Override
	public ReportBaseVO[] getReportVO(String sql) throws BusinessException {
		 ReportBaseVO[] reportVOs = null;
	        try{
	            Class[] ParameterTypes = new Class[]{String.class};
	            Object[] ParameterValues = new Object[]{sql};
	            Object o = LongTimeTask.calllongTimeService(WdsWlPubConst.WDS_WL_MODULENAME, this, 
	                    "���ڲ�ѯ...", 1, "nc.bs.wds.pub.report.ReportDMO", null, 
	                    "queryVOBySql", ParameterTypes, ParameterValues);
	            if(o != null){
	                reportVOs = (ReportBaseVO[])o;
	            }
	        }catch(Exception e){
	            Logger.error(e);
	            MessageDialog.showErrorDlg(this, "����", e.getMessage());
	        }
	        return reportVOs;
	}

	@Override
	public void onQuery() {
		try{
			getQueryDlg().showModal();
		     if (getQueryDlg().getResult() == UIDialog.ID_OK) {
            	//У�鿪ʼ���ڣ���ֹ����
            	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatefrom");
            	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddateto");
            	ddatefrom = obj1.getRefName();
            	if(ddatefrom == null ||"".equalsIgnoreCase(ddatefrom)){
            		showErrorMessage("�����뿪ʼ����");
            		return ;
            	}
            	//��ֹ�������Ϊ�գ���Ĭ��Ϊ��ǰ����
            	ddateto = obj2.getRefName();
            	if(ddateto == null || "".equalsIgnoreCase(ddateto)){
            		ddateto = _getCurrDate().toString();
            	}
            	//���ĵĲ�ѯ����
            	String qryconditons = getQueryDlg().getChText();
            	if(!qryconditons.contains("��ֹ����")){
            		qryconditons = qryconditons+"����(��ֹ���� С�ڵ��� '"+ddateto+"')";
            	}
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
            	getReportBase().getHeadItem("qryconditons").setWidth(2);
            	getReportBase().setHeadItem("qryconditons", qryconditons);
            	//�õ��Զ����ѯ����
                //�õ���ѯ���
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                if(vos != null){                	
                	//List<ReportBaseVO> list= Arrays.asList(vos);
                	//ReportBaseVO subTotal1 = new ReportBaseVO();
                	//subTotal1.setAttributeValue("", "");
					super.updateBodyDigits();
					setReportBaseVO(vos);
					setBodyVO(vos);
                }                
	          }
		} catch (Exception e) {
            e.printStackTrace();
            showWarningMessage(e.getMessage());
        }
	}
	
	/**
     * 
     * @���ߣ�lyf
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
	private String getQuerySQL(){
		StringBuffer sql = new StringBuffer();
		//--����ѯ,���ɱ���vo
		sql.append(" select ");
		sql.append(" A.cgeneralhid cgeneralhid,");
		sql.append(" A.cgeneralbid cgeneralbid,");
		sql.append(" A.dbilldate dbilldate,");//��������
		sql.append(" case when upper(A.dbilltype)='WDS6' then '��������'");
		sql.append(" when upper(A.dbilltype)='WDS7' then '�������'");
		sql.append(" when upper(A.dbilltype)='WDS8' then '���۳���'");
		sql.append(" when upper(A.dbilltype)='WDS9' then '�������' end "); 
		sql.append("  dbilltype,");//��������
		sql.append(" A.vbillno vbillno,");//���ݺ�
		sql.append(" bd_rdcl.rdname rdname,");//�շ����
		sql.append(" bd_stordoc.storcode storcode,");//�ֿ����
		sql.append(" bd_stordoc.storname storname,");//�ֿ�����
		sql.append(" bd_cargdoc.csname cargname,");//��λ����
		sql.append(" bd_deptdoc.deptname deptname,");//��������
		sql.append(" bd_psndoc.psnname psnname,");//���Ա����
		sql.append(" bd_invcl.invclasscode invclasscode,");//����������
		sql.append(" bd_invcl.invclassname invclassname,");//�����������
		sql.append(" bd_invbasdoc.invname invname,");//�������
        sql.append(" bd_invbasdoc.invcode invcode,");//�������
        sql.append(" bd_invbasdoc.invspec invspec,");//���
        sql.append(" bd_invbasdoc.invtype invtype,");//�ͺ�
        sql.append(" meas1.measname unitname,");//����λ
        sql.append(" meas2.measname assunitname,");//����λ
		sql.append(" A.nhsl nhsl,");//������
		sql.append(" A.isgift isgift,");//�Ƿ���Ʒ
		sql.append(" A.vbatchcode vbatchcode,");//���κ�
		sql.append(" A.ninnum ninnum,");//�˿�����
		sql.append(" A.nassinnum nassinnum,");//�˿⸨����
		sql.append(" A.noutnum  noutnum,");//��������
		sql.append(" A.nassoutnum nassoutnum,");//���⸨����
		sql.append(" A.vsourcebillcode vsourcebillcode,");
		sql.append(" A.nshouldnum nshouldnum,");//Ӧ������
		sql.append(" A.nassshouldnum nassshouldnum");//Ӧ��������
		sql.append(" from (");
		//--1��ѯ���ⵥ��ʼ
		sql.append("(");
		sql.append("select ");
		sql.append(" tb_outgeneral_b.general_pk cgeneralhid,");//����id
		sql.append(" tb_outgeneral_b.general_b_pk cgeneralbid,");//�����ӱ�id
		sql.append(" tb_outgeneral_h.dbilldate dbilldate,");//��������
		sql.append(" tb_outgeneral_h.vbilltype dbilltype,");//��������
		sql.append(" tb_outgeneral_h.vbillcode vbillno,");//���ݺ�
		sql.append(" tb_outgeneral_h.cdispatcherid pk_rdcl,");//�շ����id
		sql.append(" tb_outgeneral_h.srl_pk pk_stordoc,");//�ֿ�id����ֿ�
		sql.append(" tb_outgeneral_h.pk_cargdoc pk_cargdoc,");//��λid
		sql.append(" tb_outgeneral_h.cdptid pk_deptdoc,");//����id
		sql.append(" tb_outgeneral_h.cwhsmanagerid pk_psndoc,");//���Աid
		sql.append(" tb_outgeneral_b.cinventoryid pk_invmandoc,");//�������id
		sql.append("  null isgift,");//�Ƿ���Ʒ
		sql.append(" tb_outgeneral_b.hsl nhsl,");//������
		sql.append(" tb_outgeneral_b.vbatchcode vbatchcode,");//���κ�
		sql.append(" null ninnum,");
		sql.append(" null nassinnum,");
		sql.append(" tb_outgeneral_b.noutnum noutnum,");//��������
		sql.append(" tb_outgeneral_b.noutassistnum nassoutnum,");//���⸨����
		sql.append(" tb_outgeneral_b.vsourcebillcode vsourcebillcode,");//��Դ���ݺ�
		sql.append(" tb_outgeneral_b.nshouldoutnum nshouldnum,");//Ӧ������
		sql.append(" tb_outgeneral_b.nshouldoutassistnum nassshouldnum");//Ӧ��������
		sql.append(" from tb_outgeneral_h");//--���ⵥ����
		sql.append(" join  tb_outgeneral_b ");//--�����ӱ�
        sql.append(" on  tb_outgeneral_h.general_pk = tb_outgeneral_b.general_pk and isnull(tb_outgeneral_b.dr,0)=0");
        sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus=1");//�������������ͨ��
        sql.append(" and dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );//��������:�������� 
        sql.append(" and pk_corp='"+_getCorpID()+"'");//��������:��˾
        sql.append(")");
        //--1��ѯ���ⵥ����
        sql.append(" union ");
		//--2��ѯ��ⵥ��ʼ
		sql.append("(");
		sql.append("select ");
		sql.append(" tb_general_b.geh_pk cgeneralhid,");//����id
		sql.append(" tb_general_b.geb_pk cgeneralbid,");//�����ӱ�id
		sql.append(" tb_general_h.geh_dbilldate dbilldate,");//��������
		sql.append(" tb_general_h.geh_cbilltypecode dbilltype,");//��������
		sql.append(" tb_general_h.geh_billcode vbillno,");//���ݺ�
		sql.append(" tb_general_h.geh_cdispatcherid pk_rdcl,");//�շ����id
		sql.append(" tb_general_h.geh_cwarehouseid pk_stordoc,");//�ֿ�id���ֿ�
		sql.append(" tb_general_h.pk_cargdoc pk_cargdoc,");//��λid
		sql.append(" tb_general_h.geh_cdptid pk_deptdoc,");//����id
		sql.append(" tb_general_h.geh_cwhsmanagerid pk_psndoc,");//���Աid
		sql.append(" tb_general_b.geb_cinventoryid pk_invmandoc,");//�������id
		sql.append("  tb_general_b.geb_flargess isgift,");//�Ƿ���Ʒ
		sql.append(" tb_general_b.geb_hsl nhsl,");//������
		sql.append(" tb_general_b.geb_vbatchcode vbatchcode,");//���κ�
		sql.append(" tb_general_b.geb_anum ninnum,");
		sql.append(" tb_general_b.geb_banum nassinnum,");
		sql.append(" null noutnum,");//��������
		sql.append(" null nassoutnum,");//���⸨����
		sql.append(" tb_general_b.vsourcebillcode vsourcebillcode,");//��Դ���ݺ�
		sql.append(" tb_general_b.geb_snum nshouldnum,");//Ӧ������
		sql.append(" tb_general_b.geb_bsnum nassshouldnum");//Ӧ��������
		sql.append(" from tb_general_h ");//--�������
        sql.append(" join tb_general_b");//--����ӱ�
        sql.append(" on tb_general_h.geh_pk=tb_general_b.geh_pk and isnull(tb_general_b.dr,0)=0");
        sql.append(" where isnull(tb_general_h.dr,0)=0 and tb_general_h.pwb_fbillflag=1");//�������������ͨ��
        sql.append(" and geh_dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );//��������:�������� 
        sql.append(" and pk_corp='"+_getCorpID()+"'");//��������:��˾
        sql.append(")");
        //--2��ѯ��ⵥ����
        sql.append(")A");
        //--����ѯ,���ɱ���vo
        sql.append(" left join bd_rdcl");//--�շ�����
        sql.append(" on A.pk_rdcl = bd_rdcl.pk_rdcl ");
        sql.append(" left join bd_deptdoc ");//--���ŵ���
        sql.append(" on A.pk_deptdoc =bd_deptdoc.pk_deptdoc");
        sql.append(" left join bd_psndoc");//--��Ա����
        sql.append(" on A.pk_psndoc = bd_psndoc.pk_psndoc");
        sql.append(" left join bd_cargdoc ");//--��λ����
        sql.append(" on A.pk_cargdoc =bd_cargdoc.pk_cargdoc and isnull(bd_cargdoc.dr,0)=0");//
        sql.append(" left join bd_stordoc ");//--�ֿ⵵��
        sql.append(" on bd_cargdoc.pk_stordoc=bd_stordoc.pk_stordoc and isnull(bd_stordoc.dr,0)=0");
        sql.append(" left join  bd_invmandoc ");//--�������id
        sql.append(" on A.pk_invmandoc =bd_invmandoc.pk_invmandoc and isnull(bd_invmandoc.dr,0)=0");
        sql.append(" join bd_invbasdoc ");//--�������id
        sql.append(" on bd_invmandoc.pk_invbasdoc = bd_invbasdoc.pk_invbasdoc and isnull(bd_invbasdoc.dr,0)=0");
        sql.append(" join bd_measdoc meas1 ");//--��������������
        sql.append(" on bd_invbasdoc.pk_measdoc=meas1.pk_measdoc and isnull(meas1.dr,0)=0");
        sql.append(" join bd_measdoc meas2 ");//--��������������
        sql.append(" on bd_invbasdoc.pk_measdoc1=meas2.pk_measdoc and isnull(meas2.dr,0)=0");
        sql.append(" join bd_invcl ");//--�������
        sql.append(" on  bd_invbasdoc.pk_invcl=bd_invcl.pk_invcl");
		return sql.toString();
	}
	 /**
	  * 
	  * @���ߣ�lyf
	  * @˵�������ɽ������Ŀ:�������
	  * @ʱ�䣺2011-5-11����02:13:25
	  * @param vos
	  */
    public void setReportBaseVO(ReportBaseVO[] vos){
        if(vos!=null && vos.length>0){
            for(int i = 0 ;i < vos.length;i++){
                vos[i].setAttributeValue("lineno", (i+1));
            }
        }
    }
    /**
     * �ϼ�
     */
    public void setTolal() throws Exception {
        SubtotalVO svo = new SubtotalVO();
        svo.setGroupFldCanNUll(true);// �����е������Ƿ����Ϊ�ա�
        svo.setAsLeafRs(new boolean[] { true });// �����кϲ����Ƿ���Ϊĩ���ڵ��¼��
        svo.setValueFlds(new String[] { "nstartnum","nstartassnum","ninnum","ninassnum","noutnum","noutassnum","nendnum","nendassnum",});// ��ֵ��:
        svo.setValueFldTypes(new int[] { IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD });// ��ֵ�е�����:
        svo.setTotalDescOnFld("invname");// ----�ϼ�---�ֶ� ---- ������
        setSubtotalVO(svo);
        doSubTotal();
    }


}
