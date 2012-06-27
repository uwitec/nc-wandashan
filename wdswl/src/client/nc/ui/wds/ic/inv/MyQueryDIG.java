package nc.ui.wds.ic.inv;

import java.awt.Container;

import javax.swing.table.TableCellEditor;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wl.pub.WdsQueryDlg;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;

public class MyQueryDIG extends WdsQueryDlg {
	private static final long serialVersionUID = 3308256416361572220L;
	public MyQueryDIG(Container parent, UIPanel normalPnl, String pk_corp,
			String moduleCode, String operator, String busiType) {
		super(parent, normalPnl, pk_corp, moduleCode, operator, busiType,"tb_warehousestock.pk_customize1","tb_warehousestock.pk_cargdoc");
	}
	
	 public void initData() {
		setDefaultValue("tb_warehousestock.dbilldate",null,ClientEnvironment.getInstance().getDate().toString());	
		super.initData();
	}
	 
	 protected void afterEdit(TableCellEditor editor, int row, int col) {
			super.afterEdit(editor, row, col);
			
//			---------编辑货位后 过滤托盘参照
			String fieldcode = getFieldCodeByRow(row);
			if(PuPubVO.getString_TrimZeroLenAsNull(fieldcode)==null)
				return;
			if(fieldcode.equalsIgnoreCase(ccarg_fieldname)){//货位
				ConditionVO[] cons = getConditionVOsByFieldCode(fieldcode);
				if(cons == null || cons.length == 0)
					return;
				String value = PuPubVO.getString_TrimZeroLenAsNull(cons[0].getRefResult().getRefPK());
				if(PuPubVO.getString_TrimZeroLenAsNull(value)==null)
					return;
				Object o = getValueRefObjectByFieldCode("tb_warehousestock.pplpt_pk");//托盘
				if(o instanceof UIRefPane){
					UIRefPane ref = (UIRefPane)o;
					ref.getRefModel().addWherePart(" and pk_cargdoc = '"+value+"'");
				}
			}
		}
}
