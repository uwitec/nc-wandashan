package nc.ui.wds.pub.button.report2;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.report.ReportItem;
import nc.ui.scm.util.ObjectUtils;
import nc.ui.wds.pub.report2.CrossDLG;
import nc.ui.wds.pub.report2.ReportBaseUI;
import nc.ui.wds.pub.report2.ReportRowColCrossTool;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.rs.MemoryResultSetMetaData;
/**
 * ���洦��
 * ԭ�ȱ����Դ��Ľ��洦����ʹ
 * ���ڶ����ݽ���Ի������������ 
 * ����˲�ѯ����Ľ���Ի���
 * @author mlr
 */
public class CrossAction extends AbstractActionHasDataAvailable {
	private RotateCrossVO m_rc=new RotateCrossVO();
	private SelectFldVO[] fls=null;
	private ReportBaseUI ui=null;
	private MemoryResultSetMetaData mrsmd=null;
	private Vector vc=null;//�ڴ�����
	private ReportItem[] items=null;
	public CrossAction() {
		super();
	}
	public CrossAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}
	public void execute() throws Exception {
		CrossDLG dlg = new CrossDLG(getReportBaseUI());
		if(fls==null){
		  fls=getFls();
		}
		dlg.setRotateCross(m_rc, fls);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			m_rc = dlg.getRotateCross();
			// ִ�н���
			try {
				String[] rows=m_rc.getStrRows();
				String[] cols=m_rc.getStrCols();
				String[] vals=m_rc.getStrVals();
				getUI();
				ReportRowColCrossTool.onCross(getUI(),mrsmd,vc,items, rows, cols, vals);
			} catch (Exception e) {
				Logger.error(e);
			}
		}
	}
	private ReportBaseUI getUI() throws Exception {
		if(ui==null){
			ui=getReportBaseUI();
			vc=(Vector) ObjectUtils.serializableClone(ui.getReportBase().getBillModel().getDataVector());
			 mrsmd =(MemoryResultSetMetaData) ObjectUtils.serializableClone(ui.getReportBase().getReportGeneralUtil()
			.createMeteData());
			 items=ui.getReportBase().getBody_Items();
		}
		return ui;
	}
	private SelectFldVO[] getFls() {
		ReportItem[] its=getReportBaseUI().getReportBase().getBody_Items();
		List<SelectFldVO> list=new ArrayList<SelectFldVO>();
		for(int i=0;i<its.length;i++){
			if(its[i].isShow()==false)
				continue;
			SelectFldVO fs=new SelectFldVO();
			fs.setColtype(its[i].getDataType());
			fs.setDirty(false);
			fs.setFldalias(its[i].getKey());
			fs.setExpression(its[i].getKey());
			fs.setFldname(its[i].getName());
			//fs.setNote();
			list.add(fs);
		}	           
		return list.toArray(new SelectFldVO[0]);
	}
}
