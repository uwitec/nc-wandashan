package nc.ui.wl.pub;
import java.awt.Container;
import javax.swing.table.TableCellEditor;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
/**
 * ���ݲ�ѯ�Ի���֧�ֲֿ�ͻ�λ������
 * @author mlr
 */
public class HgQueryDIG extends HYQueryDLG{
	private static final long serialVersionUID = 930280775215555068L;
	protected String deptid = null;//�ֿ�
	protected String pk_strodoc = null;//��λ	
	protected String dep_fieldname;//��λ�ֶ�����
	protected String stor_fieldname;//�ֿ��ֶ�����	

	public HgQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType,String depfield,String storfield) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		dep_fieldname = depfield;
		stor_fieldname = storfield;	
	}
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		super.afterEdit(editor, row, col);
		String fieldcode = getFieldCodeByRow(row);
		if(PuPubVO.getString_TrimZeroLenAsNull(fieldcode)==null)
			return;
		if(fieldcode.equalsIgnoreCase(stor_fieldname)){
			ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
			if(cons == null || cons.length == 0){
				changeValueRef(dep_fieldname, getStoreCarRefModel(null));				
				return;
			}
			String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0].getRefResult().getRefPK());
			if(PuPubVO.getString_TrimZeroLenAsNull(value)==null)
				return;			   
			changeValueRef(dep_fieldname,getStoreCarRefModel(" and bd_cargdoc.pk_stordoc = '"+value+"'"));
			setValue(null, null, row+1, dep_fieldname);
		}	
		if(fieldcode.equalsIgnoreCase(dep_fieldname)){
			ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
			if(cons == null || cons.length == 0){
				return;
			}
			String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0].getRefResult().getRefPK());
			if(PuPubVO.getString_TrimZeroLenAsNull(value)==null)
				return;
    	}
	}
	public void setValue(Object value,String codevalue,int row,String fieldName){
		clearValues(new String[]{fieldName});
		getUITabInput().setValueAt(codevalue, row, 4);
		getTabModelInput().setValueAt(value, row, 4);		
	}
	protected UIRefPane getStoreRefModel(String whereSql){
		UIRefPane refPanel =  new UIRefPane();
		nc.ui.bd.ref.busi.StorDocDefaulteRefModel rf = new nc.ui.bd.ref.busi.StorDocDefaulteRefModel("�ֿ⵵��");		
		if(whereSql!=null && whereSql.length()!=0)
		rf.addWherePart(whereSql);
		refPanel.setRefModel(rf);
		return refPanel;
	}

	protected UIRefPane getStoreCarRefModel(String whereSql){
		UIRefPane refPanel =  new UIRefPane();
		nc.ui.bd.ref.busi.CargdocDefaultRefModel rf = new nc.ui.bd.ref.busi.CargdocDefaultRefModel("��λ����");		
		if(whereSql!=null && whereSql.length()!=0)
		rf.addWherePart(whereSql);
		refPanel.setRefModel(rf);
		return refPanel;
	}

}
