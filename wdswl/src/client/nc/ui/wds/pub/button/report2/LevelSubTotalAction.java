package nc.ui.wds.pub.button.report2;

/**
 * 分级小计  事件
 * @author guanyj1
 * @date:2007年8月9日
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.report.base.ISubTotalConf;
import nc.ui.wds.pub.report2.ReportBaseUI;
import nc.ui.wds.pub.report2.ZmSubTotalConfDLG;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.trade.report.IReportModelDataTypes;
import nc.vo.trade.report.TableField;

public class LevelSubTotalAction extends AbstractActionHasDataAvailable
		implements ISubTotalConf {

	// 小计合计分组字段
	private TableField[] subTotalCurrentUsingGroupFields = new TableField[0];

	// 小计合计汇总字段
	private TableField[] SubTotalCurrentUsingTotalFields = new TableField[0];
   // add  by  ycy  2009-09-07 可选择是否小计 是否合计 之前界面选择没有效果
	private boolean issub = false;

	private boolean issum = true;
	
	private boolean isshow=true;

	public LevelSubTotalAction(ReportBaseUI reportBaseUI) {
		super(reportBaseUI);
	}

	public LevelSubTotalAction() {
		super();
	}

	/**
	 * 产生小计合计窗口 点击[确定]调用executeSubTotal()
	 */
	public void execute() throws Exception {
	    subTotalCurrentUsingGroupFields = new TableField[0];
        SubTotalCurrentUsingTotalFields = new TableField[0];
        ZmSubTotalConfDLG dlg = new ZmSubTotalConfDLG(getReportBaseUI(), this);
		if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_OK) {
			issub = dlg.isSub();
			issum = dlg.isSum();
			isshow=dlg.isLevelCompute();
			setSubTotalCurrentUsingGroupFields(dlg.getGroupFields());
			setSubTotalCurrentUsingTotalFields(dlg.getTotalFields());
			getReportBaseUI().setBodyDataForSub();// 将表体数据中的合计 小计行去掉

			executeSubTotal();
            dealShow();
			getReportBaseUI()
					.showHintMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"uifactory_report",
									"UPPuifactory_report-000022")/*
																	 * @res
																	 * "小计合计成功"
																	 */);
		}
	}
	/**
	 * 自动进行数据合计
	 */
	public void atuoexecute2() throws Exception {
	    subTotalCurrentUsingGroupFields = new TableField[0];
        SubTotalCurrentUsingTotalFields = new TableField[0];
        ZmSubTotalConfDLG dlg = new ZmSubTotalConfDLG(getReportBaseUI(), this);
			issub = false;
			issum = true;
			isshow=dlg.isLevelCompute();
			setSubTotalCurrentUsingGroupFields(dlg.getGroupFields2());
			setSubTotalCurrentUsingTotalFields(dlg.getTotalFields2());
	//		getReportBaseUI().setBodyDataForSub();// 将表体数据中的合计 小计行去掉

			executeSubTotal();
            dealShow();
			getReportBaseUI()
					.showHintMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"uifactory_report",
									"UPPuifactory_report-000022")/*
																	 * @res
																	 * "小计合计成功"
																	 */);
	}
	/**
	 * 自动进行数据合计
	 */
	public void atuoexecute2(boolean issub,boolean issum,String[] totfields,String[] totfieldsNames) throws Exception {
	    subTotalCurrentUsingGroupFields = new TableField[0];
        SubTotalCurrentUsingTotalFields = new TableField[0];
        ZmSubTotalConfDLG dlg = new ZmSubTotalConfDLG(getReportBaseUI(), this);
			this.issub = issub;
			this.issum = issum;
			isshow=dlg.isLevelCompute();
			if(totfields==null || totfields.length==0){
			    setSubTotalCurrentUsingGroupFields(dlg.getGroupFields2());
			}
			else{
				setSubTotalCurrentUsingGroupFields(getTableFields(totfields,totfieldsNames));
			}
			setSubTotalCurrentUsingTotalFields(dlg.getTotalFields2());
	//		getReportBaseUI().setBodyDataForSub();// 将表体数据中的合计 小计行去掉

			executeSubTotal();
            dealShow();
			getReportBaseUI()
					.showHintMessage(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"uifactory_report",
									"UPPuifactory_report-000022")/*
																	 * @res
																	 * "小计合计成功"
																	 */);
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2011-12-29下午06:05:52
	 * @param totfields
	 * @param totfieldsNames
	 * @return
	 */
	public TableField[] getTableFields(String[]totfields,String[] totfieldsNames){
		if(totfields==null || totfields.length==0)
			return null;
		if(totfieldsNames==null || totfieldsNames.length==0)
			return null;
		if(totfields.length!=totfieldsNames.length){
			return null;
		}
		TableField[] tbs=new TableField[totfields.length];
		for(int i=0;i<tbs.length;i++){
			tbs[i]=new TableField(totfields[i].trim(),totfieldsNames[i].trim(),false);
		}
		return tbs;
	}
    /**
     * 处理是否只显示原始行
     * @author mlr
     * @说明：（鹤岗矿业）
     * 2011-12-22上午09:49:06
     */
	private void dealShow() {	
		Vector vecs=new Vector();
		String name=subTotalCurrentUsingGroupFields[0].getFieldName();
		
		BillModel mb=getReportBaseUI().getReportBase().getBillModel();
		int index=mb.getBodyColByKey(name);
		if(isshow==false){
			Vector vc=getReportBaseUI().getReportBase().getBillModel().getDataVector();
			for(int i=0;i<vc.size();i++){
				Vector v=(Vector) vc.get(i);
				String va=(String) v.get(index);
				if(va.equals("——小计——")){
					Vector n=(Vector) vc.get(i-1);
					String sf=create(subTotalCurrentUsingGroupFields,n);
					v.set(index, sf);
					vecs.add( v);
				}
					
			}
			getReportBaseUI().getReportBase().getBillModel().setDataVector(vecs);
			getReportBaseUI().getReportBase().getBillModel().updateValue();

		}
	}
	private String create(TableField[] subTotalCurrentUsingGroupFields2,
			Vector n) {
		String name="";
		BillModel mb=getReportBaseUI().getReportBase().getBillModel();
		for(int i=0;i<subTotalCurrentUsingGroupFields2.length;i++){
			String fname=subTotalCurrentUsingGroupFields2[i].getFieldName();
			if(fname==null || fname.length()==0)
				continue;
			if(".".equalsIgnoreCase(fname.substring(fname.length()-1))){
				fname=fname.substring(0,fname.length()-1)+"_";
			}
			int index=mb.getBodyColByKey(fname);  
			if(index<0 )
				continue;
			name=name+"%"+n.get(index);
		}
		return name;
	}

	public void getBodyValueVOs(CircularlyAccessibleValueObject[] bodyVOs) {
		BillModel mb=getReportBaseUI().getReportBase().getBillModel();
		for (int i = 0; i < bodyVOs.length; i++) {
			for (int j = 0; j < mb.getBodyItems().length; j++) {
				BillItem item = mb.getBodyItems()[j];
				Object aValue = mb.getValueAt(i, j);
				aValue = item.converType(aValue);
				bodyVOs[i].setAttributeValue(item.getKey(), aValue);
			}
		}
	}

	/**
	 * 判断条件 小计合计 2007-8-23 下午03:36:35 guanyj1
	 */
	public void executeSubTotal() throws Exception {
		// 表体为空的情况
		if (getReportBaseUI().getBodyDataVO() == null
				|| getReportBaseUI().getBodyDataVO().length == 0) {
			Logger.debug("报表中没有数据，小计合计操作取消");
			return;
		}
		// 未选择分组 合计 字段的情况
		if (getSubTotalCurrentUsingGroupFields() == null
				|| getSubTotalCurrentUsingGroupFields().length == 0
				|| getSubTotalCurrentUsingTotalFields() == null
				|| getSubTotalCurrentUsingTotalFields().length == 0)
			throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("uifactory_report",
							"UPPuifactory_report-000019")/*
															 * @res
															 * "分组字段和合计字段至少需要各选一项才能进行小计合计操作。"
															 */);

		// 正常情况 下 开始小计合计
		if (getSubTotalCurrentUsingGroupFields() != null
				&& getSubTotalCurrentUsingGroupFields().length != 0
				&& getSubTotalCurrentUsingTotalFields() != null
				&& getSubTotalCurrentUsingTotalFields().length != 0) {
			String[] gfs = new String[getSubTotalCurrentUsingGroupFields().length];
			for (int i = 0; i < gfs.length; i++) {
				gfs[i] = getSubTotalCurrentUsingGroupFields()[i].getFieldName();
				gfs[i] = gfs[i].replace('.', '_');
			}
			String[] tfs = new String[getSubTotalCurrentUsingTotalFields().length];
			for (int i = 0; i < tfs.length; i++) {
				tfs[i] = getSubTotalCurrentUsingTotalFields()[i].getFieldName();
				tfs[i] = tfs[i].replace('.', '_');
			}

			onSubtotal(gfs, tfs);

		}
	}
   
	public boolean isIssub() {
		return issub;
	}

	public void setIssub(boolean issub) {
		this.issub = issub;
	}

	public boolean isIssum() {
		return issum;
	}

	public void setIssum(boolean issum) {
		this.issum = issum;
	}

	/**
	 * 进行小计合计
	 * 
	 * @param gfs
	 * @param tfs
	 * @throws Exception
	 *             2007-8-23 下午04:15:33 guanyj1
	 */
	public void onSubtotal(String[] gfs, String[] tfs) throws Exception {
		SubtotalContext context = new SubtotalContext();
		context.setGrpKeys(gfs);
		context.setSubtotalCols(tfs);
		context.setTotalNameColKeys(gfs[0]);
		context.setIsSubtotal(issub);
		context.setIsSumtotal(issum);
		context.setSubtotalName("——小计——");
		context.setSumtotalName("——合计——");
		getReportBaseUI().onSubTotal(context);
	}

	/**
	 * 将TableField[]转化成String[]
	 * 
	 * @param table
	 * @return 2007-8-24 下午02:55:01 guanyj1
	 */
	public String[] totalTableFieldToString(TableField[] table) {
		String[] tfs = new String[table.length];
		for (int i = 0; i < tfs.length; i++) {
			tfs[i] = table[i].getFieldName();
			tfs[i] = tfs[i].replace('.', '_');
		}
		return tfs;
	}

	/**
	 * 将分组字段转化为TableField[]
	 * 
	 * @param gfs
	 * @return 2007-8-24 上午09:00:09 guanyj1
	 */
	public TableField[] groupStringToTableField(String[] gfs) {
		TableField[] all = getSubTotalCandidateGroupFields();
		TableField[] temp = new TableField[gfs.length];

		for (int i = 0; i < gfs.length; i++) {
			for (int j = 0; j < all.length; j++) {
				if (gfs[i].equals(all[j].getFieldName().replace('.', '_'))) {
					temp[i] = all[j];
					continue;
				}
			}
		}

		return temp;
	}

	/**
	 * 将汇总字段转化为TableField[]
	 * 
	 * @param tfs
	 * @return 2007-8-24 上午09:04:02 guanyj1
	 */
	public TableField[] totalStringToTableField(String[] tfs) {
		TableField[] all = getSubTotalCandidateTotalFields();
		TableField[] temp = new TableField[tfs.length];

		for (int i = 0; i < tfs.length; i++) {
			for (int j = 0; j < all.length; j++) {
				if (tfs[i].equals(all[j].getFieldName().replace('.', '_'))) {
					temp[i] = all[j];
					continue;
				}
			}
		}

		return temp;
	}

	/**
	 * @param newSubTotalCurrentUsingGroupFields
	 *            nc.vo.tm.report.TableField[]
	 */
	public void setSubTotalCurrentUsingGroupFields(
			TableField[] newSubTotalCurrentUsingGroupFields) {
		subTotalCurrentUsingGroupFields = newSubTotalCurrentUsingGroupFields;
	}

	/**
	 * 
	 * @param newSubTotalCurrentUsingTotalFields
	 *            nc.vo.tm.report.TableField[]
	 */
	public void setSubTotalCurrentUsingTotalFields(
			TableField[] newSubTotalCurrentUsingTotalFields) {
		SubTotalCurrentUsingTotalFields = newSubTotalCurrentUsingTotalFields;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCurrentUsingGroupFields() {
		return subTotalCurrentUsingGroupFields;
	}

	/**
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCurrentUsingTotalFields() {
		return SubTotalCurrentUsingTotalFields;
	}

	/**
	 * 返回可选的小计合计分组字段。 界面上所显示的所有字符型字段作为可选的小计合计分组字段
	 * 
	 * @return nc.vo.tm.report.TableField[]
	 */
	public TableField[] getSubTotalCandidateGroupFields() {
		return getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.STRING);
	}

	/**
	 * 返回可选的小计合计汇总字段。 界面上所显示的所有数值型(包括浮点和整型)字段作为可选的小计合计汇总字段。
	 * 
	 * @return nc.vo.tm.report.TotalField[]
	 */
	public TableField[] getSubTotalCandidateTotalFields() {
		TableField[] f = getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.FLOAT);
		TableField[] i = getReportBaseUI().getVisibleFieldsByDataType(
				IReportModelDataTypes.INTEGER);
		TableField[] j=getReportBaseUI().getVisibleFields();
		
		ArrayList<TableField> al = new ArrayList<TableField>();
		al.addAll(Arrays.asList(f));
		al.addAll(Arrays.asList(i));
		return (TableField[]) al.toArray(new TableField[0]);
	}
}
