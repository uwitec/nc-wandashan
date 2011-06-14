package nc.ui.wds.report.yfmxb;

import javax.swing.ListSelectionModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.LongTimeTask;
import nc.ui.wl.pub.report.ReportBaseUI;
import nc.ui.wl.pub.report.buttonaction.IReportButton;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * �˷���ϸ��------------------����
 * @author mlr
 */
public class ReportUI extends ReportBaseUI {
    
    private static final long serialVersionUID = 1L;
   
    private String ddatefrom =null;
    private String ddateto = null;
    /**
     * ���Ч��
     */
    public String _getModelCode() {
        return WdsWlPubConst.REPORT12;
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
     * @���ߣ�mlr
     * @˵�������ɽ������Ŀ  
     * �����Զ��������õ���ѯ�������ݵ�SQL
     * @ʱ�䣺2011-5-10����09:41:31
     * @param wheresql
     * @return
     */
    public String getQuerySQL(){
    	
    	StringBuffer sql = new StringBuffer(); 
    	
    	sql.append("  select ");
    	sql.append("  min(WDS_TRANPRICEBILL_H.carriersid)  carriersid,");//������ID
    	sql.append("  WDS_TRANPRICEBILL_H.vbillno vbillno,");//�˷Ѻ��㵥�ݺ�    	
    	sql.append("  wds_tanscorp_h.ctranscorpcode ctranscorpcode,");//�����̱���
    	sql.append("  min(wds_tanscorp_h.vtranscorpname) vtranscorpname,");//����������
    	sql.append("  min(wds_tanscorp_h.vtranscorpaddr) vtranscorpaddr,");//�����̵�ַ
    	
//    	sql.append("  min(wds_tanscorp_h.vofficespace) vofficespace,");//�칫�ص�
//    	sql.append("  min(wds_tanscorp_h.vlawpsn) vlawpsn,");//����
//    	sql.append("  min(wds_tanscorp_h.vemail) vemail,");//����
//    	sql.append("  min(wds_tanscorp_h.vfax) vfax,");//����
//    	sql.append("  min(wds_tanscorp_h.denddate) denddate,");//��ͬ��������
//    	sql.append("  min(wds_tanscorp_h.dstartdate) dstartdate,");//��ͬ��ʼ����
//    	sql.append("  min(wds_tanscorp_h.pk_stordoc) pk_stordoc,");//�����̹����ֿ�   	  	
//    	sql.append( " WDS_TRANPRICE_B.pk_destore  pk_destore,");//�����ֿ�ID
//    	sql.append( " bd_stordocf.storcode storcodef,");//�����ֿ����
//    	sql.append( " bd_stordocf.storname stornamef,");//�����ֿ�����     
    	
    	sql.append(" min(wds_tranpricebill_b.pk_trader)  pk_trader,");//�����ֿ�󶨵ľ�����ID���������̵���ID�������۳���ſ��̣���������û�У�
     	sql.append(" bd_cubasdoc.custcode custcode,");//���̱���
    	sql.append(" min(bd_cubasdoc.custname) custname,");//��������
    	sql.append(" min(bd_cubasdoc.conaddr) conaddr,");//���̵�ַ   
    	
    	sql.append(" min(wds_tranpricebill_b.pk_restore) pk_restore,");//�ջ�ֿ�ID     	
    	sql.append(" bd_stordocs.storcode storcodes,");//�ջ�ֿ����
    	sql.append(" min(bd_stordocs.storname) stornames,");//�ջ�ֿ�����     	
    	
    	sql.append(" min(wds_tranpricebill_b.csendareaid) csendareaid,");//���巢������   	
    	sql.append(" bd_areaclf.areaclcode areaclcodef,");//������������
    	sql.append(" min(bd_areaclf.areaclname) areaclnamef,");//������������    
    	
    	sql.append(" min(wds_tranpricebill_b.creceiverealid) creceiverealid,");//�����ջ����   	    	
    	sql.append(" bd_areacls.areaclcode areaclcodes,");//�ջ��������
    	sql.append(" min(bd_areacls.areaclname) areaclnames,");//�ջ��������        	
    	
    	sql.append(" min(wds_tranpricebill_b.cinvbasdocid)  cinvbasdocid,");//�������ID   	
    	sql.append(" bd_invbasdoc.invcode invcode,");//�������
    	sql.append(" min(bd_invbasdoc.invname) invname,");//�������
    	sql.append(" min(bd_invbasdoc.invspec) invspec,");//���
    	sql.append(" min(bd_invbasdoc.invtype) invtype,");//�ͺ�        	
    	sql.append(" min(wds_tranpricebill_b.cinvmandocid) cinvmandocid,");//�������ID
    	
    	sql.append(" min(wds_tranpricebill_b.cpricehid) cpricehid,");//�˼۱�����ID	
    	sql.append(" min(wds_tranpricebill_b.cpriceid) cpriceid ,");//�˼۱��ӱ�ID 
    	sql.append(" min(wds_tranpricebill_b.nprice) nprice,");//����
    	
  //  	sql.append(" wds_transprice_h.ipriceunit ipriceunit,");//�˼۵�λ  	
    	sql.append(" min(wds_transprice_b.denddate) denddate,");//�������� 
    	
    	sql.append(" min(wds_tranpricebill_b.ngl) ngl,");//�����(�˷Ѻ��㵥����ӵ��ֶ�)    	
    	sql.append(" sum(wds_tranpricebill_b.nnum) nnum,");//��������
    	sql.append(" sum(wds_tranpricebill_b.nassnum) nassnum,");//���⸨���� 
    	sql.append(" sum(wds_tranpricebill_b.ncolmny) ncolmny,");//������
    	sql.append(" sum(wds_tranpricebill_b.nadjustmny) nadjustmny,");//�˷ѵ�����	  
    	sql.append(" sum(wds_tranpricebill_b.nmny) nmny,");//�˷�   
    	
    	sql.append(" min(wds_tranpricebill_b.pk_cardoc) pk_cardoc,");//������Ϣ����(�˷Ѻ��㵥����ӵ��Զ�)
    	sql.append(" wds_cardoc_h.ccarcode ccarcode,");//���ƺ�
    	sql.append(" min(wds_cardoc_h.vcarpsnname) vcarpsnname,");//��������
    	sql.append(" min(wds_cardoc_h.vphone) vphone");//��ϵ�绰    
    	
    	sql.append( " from WDS_TRANPRICEBILL_H join wds_tranpricebill_b on" );
    	sql.append( " WDS_TRANPRICEBILL_H.pk_tranpricebill_h=wds_tranpricebill_b.pk_tranpricebill_h");
    	sql.append( " left join wds_tanscorp_h on ");
    	sql.append( " WDS_TRANPRICEBILL_H.carriersid=wds_tanscorp_h.pk_wds_tanscorp_h");//���� ������    	
    	sql.append( " left join bd_areacl bd_areaclf on ");// ���� ��������
    	sql.append( " WDS_TRANPRICEBILL_b.csendareaid=bd_areaclf.pk_areacl");
    	sql.append( " left join bd_areacl bd_areacls on"); // ���� �ջ����
    	sql.append( " WDS_TRANPRICEBILL_b.creceiverealid=bd_areacls.pk_areacl");
    	sql.append( " left join bd_invbasdoc on"); //����  ������� ����
    	sql.append( " wds_tranpricebill_b.cinvbasdocid=bd_invbasdoc.pk_invbasdoc");
    	sql.append( " left join bd_invmandoc on");//���� ���������
    	sql.append( " wds_tranpricebill_b.cinvmandocid=bd_invmandoc.pk_invmandoc");
    	sql.append( " left join wds_transprice_h on");//���� �˼۱�����
    	sql.append( " wds_tranpricebill_b.cpricehid=wds_transprice_h.pk_wds_transprice_h");
    	sql.append( " left join wds_transprice_b on"); //����  �˼۱��ӱ�
    	sql.append( " wds_tranpricebill_b.cpriceid=wds_transprice_b.pk_wds_transprice_b");
    	sql.append( " left join bd_stordoc bd_stordocf on");//���� �����ֿ�
    	sql.append( " wds_tranpricebill_b.pk_destore=bd_stordocf.pk_stordoc");
    	sql.append( " left join bd_cubasdoc on");//����  ����
    	sql.append( " wds_tranpricebill_b.pk_trader=bd_cubasdoc.pk_cubasdoc");
    	sql.append( " left join bd_stordoc bd_stordocs on");//����  �ջ�ֿ�
    	sql.append( " wds_tranpricebill_b.pk_restore=bd_stordocs.pk_stordoc");
    	sql.append("  left join wds_cardoc_h on");//���� ������Ϣ
    	sql.append("  wds_tranpricebill_b.pk_cardoc=wds_cardoc_h.pk_wds_cardoc_h");
    	sql.append(" where ");
    	sql.append( " WDS_TRANPRICEBILL_H.pk_corp='"+_getCorpID()+"'");
    	sql.append( " and isnull(WDS_TRANPRICEBILL_H.dr,0)=0");
    	sql.append( " and WDS_TRANPRICEBILL_H.vbillstatus=1");//��������ͨ����	
    	sql.append( " and WDS_TRANPRICEBILL_H.dapprovedate ");//��������
    	sql.append( " between '"+ddatefrom+"' and '"+ddateto+"'");
    	sql.append( " group by ");
    	sql.append( " WDS_TRANPRICEBILL_H.vbillno,bd_invbasdoc.invcode,bd_areaclf.areaclcode,bd_areacls.areaclcode," +
    			"bd_stordocs.storcode,bd_cubasdoc.custcode,wds_tanscorp_h.ctranscorpcode,wds_cardoc_h.ccarcode");
    	sql.append(" order by WDS_TRANPRICEBILL_H.vbillno");
    	
    
    	
    	
  	
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
