package nc.ui.wds.report.zxfyhz;

import javax.swing.ListSelectionModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.zmpub.pub.report.buttonaction2.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ReportBaseUI;
/**
 * װж���»��ܱ�------------------����
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
        return WdsWlPubConst.REPORT13;
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
//        //������Ŀ��������
//        UITable cardTable = getReportBase().getBillTable();
//        GroupableTableHeader cardHeader = (GroupableTableHeader) cardTable.getTableHeader();
//        TableColumnModel cardTcm = cardTable.getColumnModel();
//        ColumnGroup[] card2 = new ColumnGroup[4];
//        //
//        card2[0]=new ColumnGroup("�ڳ����");
//        card2[0].add(cardTcm.getColumn(9));
//        card2[0].add(cardTcm.getColumn(10));
//        cardHeader.addColumnGroup(card2[0]);
//        //
//        card2[1]=new ColumnGroup("��������");
//        card2[1].add(cardTcm.getColumn(11));
//        card2[1].add(cardTcm.getColumn(12));
//        cardHeader.addColumnGroup(card2[1]);
//        //
//        card2[2]=new ColumnGroup("���ڷ���");
//        card2[2].add(cardTcm.getColumn(13));
//        card2[2].add(cardTcm.getColumn(14));
//        cardHeader.addColumnGroup(card2[2]);
//        //
//        card2[3]=new ColumnGroup("��ĩ���");
//        card2[3].add(cardTcm.getColumn(15));
//        card2[3].add(cardTcm.getColumn(16));
//        cardHeader.addColumnGroup(card2[3]);
//        getReportBase().getBillModel().updateValue();
    }
    @Override
    /*ȥ��С�ƺϼư�ť*/
    public int[] getReportButtonAry() {
    
        return new int[] { 
              IReportButton.QueryBtn, 
//              IReportButton.ColumnFilterBtn, 
              IReportButton.CrossBtn,
              IReportButton.FilterBtn, 
              IReportButton.SortBtn, 
              IReportButton.PrintBtn,
              IReportButton.PrintDirectBtn,
              IReportButton.RefreshBtn
//              IReportButton.SubTotalBtn
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
              	getReportBase().setHeadItem("ddatafrom", ddatefrom);
              	getReportBase().setHeadItem("ddatato", ddateto);
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
           
    	sql.append("select ");
    	sql.append( "  min(wds_loadprice_h.dapprovedate) approvedate");//��������
    	sql.append( " ,min(wds_loadprice_b2.pk_wds_teamdoc_h) pk_teamdoc");//����id   	
    	sql.append( " ,wds_teamdoc_h.teamcode classcode");//�������
    	sql.append( " ,min(wds_teamdoc_h.teamname) classname");//��������
    	sql.append( " ,min(wds_teamdoc_h.teamaddr) teamaddr");//�����ַ
    	sql.append( " ,sum(wds_loadprice_b2.nloadprice) accountload");//װж����
    	sql.append( " ,min(wds_teamdoc_b.psncode) psncode");//�ӳ�����               [���ֶ�Ŀǰû��ʹ�� ����]
    	sql.append( " ,min(wds_teamdoc_b.psnname) psnname");//�ӳ�����                [���ֶ�Ŀǰû��ʹ�� ����]
    	sql.append( " ,min(wds_teamdoc_b.contact1) cont1");//�ӳ���ϵ��ʽ          [���ֶ�Ŀǰû��ʹ�� ����]
    	sql.append( " ,min(wds_teamdoc_b.contact2) cont2");//�ӳ���ϵ��ʽ2   [���ֶ�Ŀǰû��ʹ�� ����]
    	sql.append( " ,min(wds_teamdoc_b.contact3) cont3");//�ӳ���ϵ��ʽ3    [���ֶ�Ŀǰû��ʹ�� ����]   	
    	sql.append( " from wds_loadprice_h join wds_loadprice_b2 on");
    	sql.append( " wds_loadprice_h.pk_loadprice=wds_loadprice_b2.pk_loadprice");
    	sql.append( " join wds_teamdoc_h on");
    	sql.append( " wds_loadprice_b2.pk_wds_teamdoc_h=wds_teamdoc_h.pk_wds_teamdoc_h");
    	sql.append( " join wds_teamdoc_b on");
    	sql.append( " wds_teamdoc_h.pk_wds_teamdoc_h= wds_teamdoc_b.pk_wds_teamdoc_h");
    	sql.append( " where upper(isnull(wds_teamdoc_b.isteam,'N'))='Y'");//���˳��ӳ�
    	sql.append( " and isnull(wds_loadprice_h.dr,0)=0");//��ѯû�б�ɾ����
    	sql.append( " and wds_loadprice_h.pk_corp='"+_getCorpID()+"'");
    	sql.append( " and wds_loadprice_h.vbillstatus=1");//����ͨ����
    	sql.append( " and wds_loadprice_h.dapprovedate ");
    	sql.append( " between '"+ddatefrom+"' and '"+ddateto+"'");
    	sql.append( " group by wds_teamdoc_h.teamcode");
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
//        SubtotalVO svo = new SubtotalVO();
//        svo.setGroupFldCanNUll(true);// �����е������Ƿ����Ϊ�ա�
//        svo.setAsLeafRs(new boolean[] { true });// �����кϲ����Ƿ���Ϊĩ���ڵ��¼��
//        svo.setValueFlds(new String[] { "nstartnum","nstartassnum","ninnum","ninassnum","noutnum","noutassnum","nendnum","nendassnum",});// ��ֵ��:
//        svo.setValueFldTypes(new int[] { IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD,IUFTypes.UFD });// ��ֵ�е�����:
//        svo.setTotalDescOnFld("invname");// ----�ϼ�---�ֶ� ---- ������
//        setSubtotalVO(svo);
//        doSubTotal();
 
    }
}
