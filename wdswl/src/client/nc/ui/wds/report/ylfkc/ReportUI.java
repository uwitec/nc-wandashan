package nc.ui.wds.report.ylfkc;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.buttonaction.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.IUFTypes;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
/**
 * ԭ�Ϸ��շ�����ܱ�------------------����
 * @author Administrator
 */
public class ReportUI extends ReportBaseUI {
    
    private static final long serialVersionUID = 1L;
    private String ddatefrom =null;
    private String ddateto = null;
    
    /**
     * ���Ч��
     */
    public String _getModelCode() {
        return WdsWlPubConst.REPORT04;
    }
    @Override
    public void setUIAfterLoadTemplate() {
    	  setColumn();
          getReportBase().getBillTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    /**
     * �����кϲ�
     */
    private void setColumn() {
        //������Ŀ��������
        UITable cardTable = getReportBase().getBillTable();
        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
        TableColumnModel cardTcm = cardTable.getColumnModel();
        ColumnGroup[] card2 = new ColumnGroup[4];
        //
        card2[0]=new ColumnGroup("�ڳ����");
        card2[0].add(cardTcm.getColumn(9));
        card2[0].add(cardTcm.getColumn(10));
        cardHeader.addColumnGroup(card2[0]);
        //
        card2[1]=new ColumnGroup("��������");
        card2[1].add(cardTcm.getColumn(11));
        card2[1].add(cardTcm.getColumn(12));
        cardHeader.addColumnGroup(card2[1]);
        //
        card2[2]=new ColumnGroup("���ڷ���");
        card2[2].add(cardTcm.getColumn(13));
        card2[2].add(cardTcm.getColumn(14));
        cardHeader.addColumnGroup(card2[2]);
        //
        card2[3]=new ColumnGroup("��ĩ���");
        card2[3].add(cardTcm.getColumn(15));
        card2[3].add(cardTcm.getColumn(16));
        cardHeader.addColumnGroup(card2[3]);
        getReportBase().getBillModel().updateValue();
    }
    @Override
    /*ȥ��С�ƺϼư�ť*/
    public int[] getReportButtonAry() {
        return new int[] { 
              IReportButton.QueryBtn, 
              IReportButton.ColumnFilterBtn, 
              IReportButton.CrossBtn,
              IReportButton.FilterBtn, 
              IReportButton.SortBtn, 
              IReportButton.PrintBtn,
              IReportButton.PrintDirectBtn,
              IReportButton.RefreshBtn,
              IReportButton.SubTotalBtn
              };
    }
    @Override
    public void onQuery() {
        try { 	
        	//���ò�ѯģ��Ĭ�ϲ�ѯ����
            AccountCalendar  accCal = AccountCalendar.getInstance();     
            getQueryDlg().setDefaultValue("ddatafrom", accCal.getMonthVO().getBegindate().toString(), "");
            getQueryDlg().setDefaultValue("ddatato", accCal.getMonthVO().getEnddate().toString(), "");
            getQueryDlg().showModal();
            if (getQueryDlg().getResult() == UIDialog.ID_OK) {
            	//У�鿪ʼ���ڣ���ֹ����
            	UIRefPane obj1 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatafrom");
            	UIRefPane obj2 = (UIRefPane)getQueryDlg().getValueRefObjectByFieldCode("ddatato");
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
            	getReportBase().setHeadItem("ddatefrom", ddatefrom);
            	getReportBase().setHeadItem("ddateto", ddateto);
            	getReportBase().getHeadItem("qryconditons").setWidth(2);
            	getReportBase().setHeadItem("qryconditons", qryconditons);
            	//�õ��Զ����ѯ����
                //�õ���ѯ���
                ReportBaseVO[] vos = getReportVO(getQuerySQL());
                super.updateBodyDigits();
                setReportBaseVO(vos);
                setBodyVO(vos);
                //�ϼƴ���
                if ( vos != null && vos.length > 0 ) {
                    setTolal();//�ϼ�
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
    public String getQuerySQL(){
    	StringBuffer sql = new StringBuffer();
    	//--����ѯ,���ɱ���vo
    	sql.append("select ");
    	sql.append(" bd_stordoc.pk_stordoc pk_stordoc, ");//�ֿ�Id(ģ������)
    	sql.append(" bd_cargdoc.pk_cargdoc pk_cargdoc, ");//��λid(ģ������)
    	sql.append(" bd_invmandoc.pk_invmandoc pk_invmandoc,");//���id(ģ������)
        sql.append(" bd_invmandoc.pk_invbasdoc pk_invbasdoc,");//�������id(ģ������)
        sql.append(" bd_stordoc.storname storname,");//�ֿ�����
        sql.append(" bd_stordoc.storcode storcode,");//�ֿ����
        sql.append(" bd_cargdoc.csname cargname,");//��λ����
        sql.append(" bd_invbasdoc.invname invname,");//�������
        sql.append(" bd_invbasdoc.invcode invcode,");//�������
        sql.append(" bd_invbasdoc.invspec invspec,");//���
        sql.append(" bd_invbasdoc.invtype invtype,");//�ͺ�
        sql.append(" meas1.measname unitname,");//����λ
        sql.append(" meas2.measname assunitname,");//����λ
        sql.append(" sum(A.ninnum) ninnum,");//����������
        sql.append(" sum(A.ninassnum) ninassnum,");//�����븨����
        sql.append(" sum(A.noutnum) noutnum,");//���ڳ�����
        sql.append(" sum(A.noutassnum) noutassnum,");//���ڳ�������
        sql.append(" sum(A.nendnum) nendnum,");//���ڽ��
        sql.append(" sum(A.nendassnum) nendassnum");//���ڽ��
    	sql.append(" from (");
    	//----1��ѯ���ⵥ ���ڳ���������ʼ
    	sql.append("(");
    	sql.append("select ");
    	//sql.append(" tb_outgeneral_h.srl_pk pk_stordoc,");//�ֿ�id
    	sql.append(" tb_outgeneral_h.pk_cargdoc pk_cargdoc,");//��λid
    	sql.append(" tb_outgeneral_b.cinventoryid pk_invmandoc,");//�������id
        sql.append(" null ninnum,");
        sql.append(" null ninassnum,");
        sql.append(" tb_outgeneral_b.noutnum noutnum,");//���ڷ���
        sql.append(" tb_outgeneral_b.noutassistnum noutassnum,");//���ڷ�������
        sql.append(" null nendnum,");
        sql.append(" null nendassnum");
        sql.append("  from tb_outgeneral_h ");//--��������
        sql.append(" join  tb_outgeneral_b ");//--�����ӱ�
        sql.append(" on  tb_outgeneral_h.general_pk = tb_outgeneral_b.general_pk and isnull(tb_outgeneral_b.dr,0)=0");
        sql.append(" where isnull(tb_outgeneral_h.dr,0)=0 and tb_outgeneral_h.vbillstatus=1");//�������������ͨ��
        sql.append(" and dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );
        sql.append(" and pk_corp='"+_getCorpID()+"'");//����������˾
        sql.append(")");
        //----1��ѯ���ⵥ ���ڳ�����������
        sql.append(" union ");
       //----2��ѯ��ⵥ �������������ʼ
        sql.append("(");
        sql.append("select ");
      	//sql.append("  tb_general_h.geh_cwarehouseid pk_stordoc,");//�ֿ�id
    	sql.append("  tb_general_h.pk_cargdoc pk_cargdoc,");//��λid
    	sql.append(" tb_general_b.geb_cinventoryid pk_invmandoc,");//�������id
        sql.append(" tb_general_b.geb_anum ninnum,");//��������
        sql.append(" tb_general_b.geb_banum ninassnum,");//�������븨����
        sql.append(" null noutnum,");
        sql.append(" null noutassnum,");
        sql.append(" null nendnum,");
        sql.append(" null nendassnum");
        sql.append(" from tb_general_h ");//--�������
        sql.append(" join tb_general_b");//--����ӱ�
        sql.append(" on tb_general_h.geh_pk=tb_general_b.geh_pk and isnull(tb_general_b.dr,0)=0");
        sql.append(" where isnull(tb_general_h.dr,0)=0 and tb_general_h.pwb_fbillflag=1");
        sql.append(" and geh_dbilldate between '"+ddatefrom+"'and '"+ddateto+"'" );
        sql.append(" and pk_corp='"+_getCorpID()+"'");//��������:��˾
        sql.append(")");
        //----2��ѯ��ⵥ ���������������
        sql.append(" union ");
        //----3��ѯ���״̬�� ��ĩ���
        sql.append("(");
        sql.append("select ");
      	//sql.append("  tb_general_h.geh_cwarehouseid pk_stordoc,");//�ֿ�id
    	sql.append("   tb_warehousestock.pk_cargdoc pk_cargdoc,");//��λid
    	sql.append(" tb_warehousestock.pk_invmandoc pk_invmandoc,");//�������id
        sql.append(" null ninnum,");
        sql.append(" null ninassnum,");
        sql.append(" null noutnum,");
        sql.append(" null noutassnum,");
        sql.append(" tb_warehousestock.whs_stocktonnage nendnum,");//���ڽ��
        sql.append(" tb_warehousestock.whs_stockpieces nendassnum");//���ڽ��
        sql.append(" from tb_warehousestock ");//--��ѯ���״̬��
        sql.append(" where isnull(tb_warehousestock.dr,0)=0");
        sql.append(" and pk_corp='"+_getCorpID()+"'");//��������:��˾
        sql.append(")");
        //----3��ѯ���״̬�� ��ĩ���  
        sql.append(") A ");
        //--������ѯ,���ɱ���vo
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
        sql.append(" group by bd_stordoc.pk_stordoc,bd_cargdoc.pk_cargdoc,bd_invmandoc.pk_invmandoc,");
        sql.append(" bd_invmandoc.pk_invbasdoc ,");
        sql.append(" bd_stordoc.storname ,");
        sql.append(" bd_stordoc.storcode ,");
        sql.append(" bd_cargdoc.csname ,");
        sql.append(" bd_invbasdoc.invname ,");
        sql.append(" bd_invbasdoc.invcode ,");
        sql.append(" bd_invbasdoc.invspec ,");
        sql.append(" bd_invbasdoc.invtype ,");
        sql.append(" meas1.measname,");
        sql.append(" meas2.measname ");
        sql.append(" order by storname, cargname,invcode");
        return sql.toString();
    } 
    
    public ReportBaseVO[] getReportVO(String sql) throws BusinessException{
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
    
    
    //�������
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
