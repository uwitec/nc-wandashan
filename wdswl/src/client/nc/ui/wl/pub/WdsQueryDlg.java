package nc.ui.wl.pub;

import java.awt.Component;
import java.awt.Container;
import javax.swing.table.TableCellEditor;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.query.HYQueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;

public class WdsQueryDlg extends HYQueryDLG {
	protected String cwhid = null;//²Ö¿â
	protected String ccargdoc = null;//»õÎ»
	
	protected String cwh_fieldname;//²Ö¿â×Ö¶ÎÃû³Æ
	protected String ccarg_fieldname;//»õÎ»×Ö¶ÎÃû³Æ
	public WdsQueryDlg(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType,String whfield,String cargfield) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType);
		cwh_fieldname = whfield;
		ccarg_fieldname = cargfield;
//		initData();
		LoginInforHelper login = new LoginInforHelper();
		
		try {
			cwhid = login.getCwhid(operator);
			ccargdoc = login.getLogInfor(operator).getSpaceid();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cwhid = null;
			ccargdoc = null;
		}
	}
	private void init(){
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)!=null){
			if(!WdsWlPubTool.isZc(cwhid)){
				getComponent(cwh_fieldname).setEnabled(false);
			}
		}
	}
	 public void initData() {
		if(PuPubVO.getString_TrimZeroLenAsNull(cwhid)!=null){
			setDefaultValue(cwh_fieldname,cwhid,cwhid);
			if(PuPubVO.getString_TrimZeroLenAsNull(ccargdoc)!=null){
				setDefaultValue(ccarg_fieldname,ccargdoc,ccargdoc);
			}
		}
		super.initData();
		init();
	}
	
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		super.afterEdit(editor, row, col);
		String fieldcode = getFieldCodeByRow(row);
		if(PuPubVO.getString_TrimZeroLenAsNull(fieldcode)==null)
			return;
		if(fieldcode.equalsIgnoreCase(cwh_fieldname)){
			ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
			if(cons == null || cons.length == 0)
				return;
			String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0].getRefResult().getRefPK());
			if(PuPubVO.getString_TrimZeroLenAsNull(value)==null)
				return;
			Object o = getValueRefObjectByFieldCode(ccarg_fieldname);
			if(o instanceof UIRefPane){
				UIRefPane ref = (UIRefPane)o;
				ref.getRefModel().addWherePart(" and bd_cargdoc.pk_stordoc = '"+value+"'");
			}
		}
	}
	
	protected Component getComponent(String filedcode){
		Object o = getValueRefObjectByFieldCode(filedcode);
		Component jb  = null;
		if(o instanceof UIRefCellEditor){
			jb = ((UIRefCellEditor)o).getComponent();//getUITabInput().getCellEditor(1, 4);
		}else{
			jb = (Component)o;
		}

		return jb;
	}


}
