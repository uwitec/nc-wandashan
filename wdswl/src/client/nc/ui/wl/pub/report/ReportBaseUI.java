package nc.ui.wl.pub.report;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import nc.bd.accperiod.AccountCalendar;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.print.IPrintTemplateQry;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.bill.BillScrollPane.BillTable;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.report.base.IButtonActionAndState;
import nc.ui.trade.report.controller.IReportCtl;
import nc.ui.trade.report.controller.ReportCtl;
import nc.ui.trade.report.group.GroupTableModel;
import nc.ui.trade.report.query.QueryDLG;
import nc.ui.wl.pub.report.buttonaction.ButtonAssets;
import nc.ui.wl.pub.report.buttonaction.IReportButton;
import nc.ui.wl.pub.report.buttonaction.LevelSubTotalAction;
import nc.ui.wl.pub.report.buttonaction.QueryAction;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.report.ReportModelVO;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;
import nc.vo.trade.report.IReportModelSelectType;
import nc.vo.trade.report.TableField;
import nc.vo.wl.pub.report.IPeneExtendInfo;
import nc.vo.wl.pub.report.ReportBaseVO;
import nc.vo.wl.pub.report.SubtotalVO;
import nc.vo.wl.pub.report.Toolkit;
import nc.vo.wl.pub.report.VOConvert;

/**
 * 
 * @author：刘建波
 */
abstract public class ReportBaseUI extends ToftPanel implements ISender,IAdjustCombinVOTool, IPene2NodeInfo {

    private int listenerNum;

    private QueryDLG m_qryDlg = null;

    private ReportBaseClass m_report = null;

    private SubtotalVO m_voSubtotal = null;

    private CircularlyAccessibleValueObject[] m_vosTotal = null;

    private CombinVOTool m_combinTool = null;

    private IMsgProxy m_msgProxy = null;

    private boolean m_bCrossTable = false;

    public ButtonObject[] m_buttons = null;

    public int[] m_buttonArray = null;

    public Boolean unlockSortListener = false;// 代表是否开启排序监听

    // -----------------------------------------------------------------------------------添加by
    // guanyj------------------------
    // 保存自添加按钮以及相应事件处理类
    private ButtonAssets button_action_map = new ButtonAssets(this);

    // 备份的ReportModelVO,因为报表模板可能会改变这个东西，我们自己把它备份下来
    private nc.vo.pub.report.ReportModelVO[] copyOfReportModelVOs = null;

    private LevelSubTotalAction subtotalAction = (LevelSubTotalAction) button_action_map
            .get(ButtonAssets.m_boLevelSubTotal);

    private boolean needGroup = false;

    private IReportCtl m_uiCtl;

    // 分组key列表
    private ArrayList<String> groupKeys = new ArrayList<String>();

    // 分组列表与表体分隔栏
    private UISplitPane veriSplitPane = null;

    // 分组显示Table
    private UITable groupTable = null;

    // 分组hash表
    private HashMap groupMap = new HashMap();

    private CircularlyAccessibleValueObject[] allBodyDataVO = null;

    private ArrayList reportModelColumnGroups = new ArrayList();

    // 分隔符号，
    protected static final char columnSystemDelimiter = '_';

    // 用于条件过滤
    private Vector dataVec = null;

    // 用于条件过滤计数用
    private int num = 0;

    private int n = 0;

    // -----------------------------------------------------------------------------------------------------------------------------

    public final int HEAD = 0;

    public final int BODY = 1;

    public final int TAIL = 2;

    // 本币精度
    private String m_digitBase = null;

    private Vector<String> vecNotMnyFlds = null;

    /**
     * ReportBaseUI 构造子注解。
     */
    public ReportBaseUI() {
        super();
        init();
    }

    public nc.ui.pub.ClientEnvironment _getCE() {
        return nc.ui.pub.ClientEnvironment.getInstance();
    }

    public String _getCorpID() {
        return _getCE().getCorporation().getPk_corp();
    }

    public UFDate _getCurrDate() {
        return _getCE().getDate();
    }

    public String _getModelCode() {
        // return _getCE().getModuleCode();
        return getFuncRegisterVO().getFunCode();
    }

    public String _getUserID() {
        return _getCE().getUser().getPrimaryKey();
    }

    /**
     * 动态增加表体列。
     */
    public void addNewItems(ReportItem[] items) {
        getReportBase().addBodyItem(items);
    }

    public void adjustCombinedVO(nc.vo.pub.CircularlyAccessibleValueObject vo,
            Object oMsg) {
        if (!(vo instanceof ReportBaseVO))
            return;
        ReportBaseVO voBase = (ReportBaseVO) vo;
        if (!(oMsg instanceof SubtotalVO))
            return;
        if (!(voBase.isCombined() || voBase.isSubtotal() || voBase.isTotal()))
            return;
        SubtotalVO voSubtotal = (SubtotalVO) oMsg;
        // 将除了求值列,分组列的其他字段设为空。
        String[] valueflds = voSubtotal.getValueFlds();
        String[] groupflds = voSubtotal.getGroupFlds();
        String[] names = voBase.getAttributeNames();
        for (int i = 0; i < names.length; i++) {
            if (Toolkit.binarySearch(valueflds, names[i]) < 0
                    && (groupflds == null || Toolkit.binarySearch(groupflds,
                            names[i]) < 0))
                voBase.setAttributeValue(names[i], null);
        }
        // 写小计合计。
        if (groupflds == null) {
            voBase.setAttributeValue(voSubtotal.getTotalDescOnFld(), "——合计——");
        } else {
            String[] onFlds = voSubtotal.getSubDescOnFlds();
            String onfld = onFlds[voSubtotal.getGroupFlds().length - 1];
            voBase.setAttributeValue(onfld, "——小计——");
        }
    }

    /* 对参加合并得VO进行处理。 */
    public void adjustDetailVO(nc.vo.pub.CircularlyAccessibleValueObject vo) {
    }

    /**
     * 由子类覆盖，提供编码规则，用于生成树空格 作者：薛恩平 创建日期：2005-1-12 9:26:22
     * 
     * @return
     */
    protected String createCodeRule() {
        return null;
    }

    public void doCross(String[] strRowKeys, String[] strColKeys,
            String[] strValueKeys) {
        if (getBuffedTotalVO() == null || getBuffedTotalVO().length == 0)
            return;
        try {
            getReportBase()
                    .drawCrossTable(strRowKeys, strColKeys, strValueKeys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateUI();
        setCrossTableFlag(true);
    }

    public void doSubTotal() throws Exception {
        if (m_voSubtotal == null)
            throw new Exception("小计合计首先要设置SubtotalVO,可以调用setSubtotalVO()方法。");
        m_voSubtotal.validate();
        CircularlyAccessibleValueObject[] vos = getVOFromUI();
        if (vos == null || vos.length == 0)
            m_vosTotal = null;
        else {
            // 按照分组列合并数据。
            String[] groupflds = m_voSubtotal.getGroupFlds();
            int iGroupDeep = m_voSubtotal.getGroupDepth();
            String[] curGroupFlds = null;
            SubtotalVO vobak = (SubtotalVO) m_voSubtotal.clone();
            if (groupflds != null) {
                for (int i = 0; i < groupflds.length
                        && (iGroupDeep == -1 || i < iGroupDeep); i++) {
                    curGroupFlds = new String[groupflds.length - i];
                    System.arraycopy(groupflds, 0, curGroupFlds, 0,
                            groupflds.length - i);
                    vobak.setGroupFlds(curGroupFlds);
                    vos = getCombinVOTool().combinVO(vos, vobak);
                }
            }
            vobak.setGroupFlds(null);
            CircularlyAccessibleValueObject voTotal = getCombinVOTool()
                    .combinVO(vos, vobak)[0];
            // 取值。
            m_vosTotal = (voTotal == null ? null : ((ReportBaseVO) voTotal)
                    .getAllVOs(vobak.getOrderbyFlds(), vobak.isDesc()));
        }
        // 设置UI.
        getReportBase().setBodyDataVO(m_vosTotal, false);
        updateUI();
    }

    // public void doSubTotal() throws Exception {
    // if (m_voSubtotal == null)
    // throw new Exception("小计合计首先要设置SubtotalVO,可以调用setSubtotalVO()方法。");
    // m_voSubtotal.validate();
    // CircularlyAccessibleValueObject[] vos = getVOFromUI();
    // if (vos == null || vos.length == 0)
    // m_vosTotal = null;
    // else {
    // String[] groupflds = m_voSubtotal.getGroupFlds();
    // int iGroupDeep = m_voSubtotal.getGroupDepth();
    // String[] curGroupFlds = null;
    // SubtotalVO vobak = (SubtotalVO) m_voSubtotal.clone();
    // if (groupflds != null) {
    // for (int i = 0; i < groupflds.length
    // && (iGroupDeep == -1 || i < iGroupDeep); i++) {
    // curGroupFlds = new String[groupflds.length - i];
    // System.arraycopy(groupflds, 0, curGroupFlds, 0,
    // groupflds.length - i);
    // vobak.setGroupFlds(curGroupFlds);
    // vos = getCombinVOTool().combinVO(vos, vobak);
    // }
    // }
    // vobak.setGroupFlds(null);
    // CircularlyAccessibleValueObject voTotal = getCombinVOTool()
    // .combinVO(vos, vobak)[0];
    // // 取值。
    // m_vosTotal = (voTotal == null ? null : vos);//修改－－－－－－－
    // }
    // // 设置UI.
    // getReportBase().setBodyDataVO(m_vosTotal, false);
    // //如果分组字段不为空 则进行小计合计
    // if(m_voSubtotal.getTotalDescOnFld() != null &&
    // m_voSubtotal.getTotalDescOnFld().length() != 0){
    // String[] gfs = new String[]{m_voSubtotal.getTotalDescOnFld()};
    // String[] tfs =
    // subtotalAction.totalTableFieldToString(subtotalAction.getSubTotalCandidateTotalFields());
    // subtotalAction.onSubtotal(gfs, tfs);
    // //设置小计合计的状态字段
    // subtotalAction.setSubTotalCurrentUsingGroupFields(subtotalAction.groupStringToTableField(gfs));
    // subtotalAction.setSubTotalCurrentUsingTotalFields(subtotalAction.totalStringToTableField(tfs));
    // }
    // m_vosTotal = getVOFromUI();//--------------
    // getReportBase().setBodyDataVO(getVOFromUI(), false);
    // updateUI();
    // }

    public CircularlyAccessibleValueObject[] getBuffedTotalVO() {
        return m_vosTotal;
    }

    // public nc.ui.pub.ButtonObject[] getButtons() {
    // if (m_buttons == null) {
    // if (getPene2NodeInfo() == null)
    // m_buttons = new ButtonObject[] { m_boQry, m_boPrint };
    // /**
    // * xkf 添加如果接口实现有内容 则添加该按钮
    // */
    // else
    // m_buttons = new ButtonObject[] { m_boQry, m_boPrint, m_boPenerate };
    // }
    //
    // return m_buttons;
    // }

    public CombinVOTool getCombinVOTool() {
        if (m_combinTool == null) {
            m_combinTool = new CombinVOTool(this);
        }
        return m_combinTool;
    }

    public ReportBaseVO getHeadVOByQryDlg(
            nc.ui.trade.report.query.QueryDLG qrydlg) {
        qrydlg = (qrydlg == null ? getQueryDlg() : qrydlg);
        nc.vo.pub.query.ConditionVO[] vos = qrydlg.getConditionVO();
        ReportBaseVO vo = new ReportBaseVO();
        for (int i = 0; vos != null && i < vos.length; i++) {
            if (vos[i].getValue() != null) {
                String fldcode = vos[i].getFieldCode();
                if (fldcode.indexOf(".") > -1) {
                    fldcode = "h_"
                            + fldcode.substring(fldcode.indexOf(".") + 1);
                }
                vo.setAttributeValue(fldcode, vos[i].getValue());
            }
        }
        return vo;
    }

    public ReportBaseVO getHeadVOFromUI() throws Exception {
        CircularlyAccessibleValueObject vo = getReportBase().getHeadDataVO();
        if (vo == null)
            return null;
        String[] attrnames = vo.getAttributeNames();
        if (attrnames == null || attrnames.length == 0)
            return null;
        ReportBaseVO voBase = new ReportBaseVO();
        for (int i = 0; i < attrnames.length; i++)
            voBase.setAttributeValue(attrnames[i], vo
                    .getAttributeValue(attrnames[i]));
        return voBase;
    }

    /**
     * 功能：根据选择条件返回需要外关联的表as a part of from caluse.
     */
    public String getJoinTableByQueryCon() {
        String s = getQueryDlg().getTableJoinClauseForQuery();
        return s == null ? "" : s;
    }

    /**
     * 功能：根据选择条件返回需要外关联的表as a part of from caluse.
     */
    public String getJoinTableByQueryCon(nc.ui.trade.report.query.QueryDLG dlg) {
        String s = dlg.getTableJoinClauseForQuery();
        return s == null ? "" : s;
    }

    public IMsgProxy getMsgProxy() {
        return m_msgProxy;
    }

    /**
     * 构造新列 创建日期：(01-7-19 17:59:47)
     */
    public ReportItem getNewItem(String key, String name, int dataType,
            int width) {
        ReportItem item = new ReportItem();
        item.setKey(key);
        item.setName(name);
        item.setDataType(dataType);
        item.setWidth(width);
        item.setEdit(true);
        return item;
    }

    public QueryDLG getQueryDlg() {
        if (m_qryDlg == null) {
            m_qryDlg = new QueryDLG(this);
            m_qryDlg.setTempletID(_getCorpID(), _getModelCode(), _getUserID(), null);
            m_qryDlg.setNormalShow(false);
        }
        return m_qryDlg;
    }

    public ReportBaseClass getReportBase() {
        if (m_report == null) {
            try {
                m_report = new ReportBaseClass();
                m_report.setName("ReportBase");
                m_report.setTempletID(_getCorpID(), _getModelCode(),
                        _getUserID(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return m_report;
    }

    public void setReportBase(ReportBaseClass m_report) {
        this.m_report = m_report;
    }

    public ReportBaseVO getSelectedRowVO() {
        int iSelRow = -1;
        iSelRow = getReportBase().getBillTable().getSelectedRow();
        if (iSelRow == -1) {
            MessageDialog.showHintDlg(this, "提示", "请选择一行后联查明细!");
            return null;
        }
        if (getBuffedTotalVO() == null)
            return null;
        ReportBaseVO voBase = (ReportBaseVO) getBuffedTotalVO()[iSelRow];
        if (voBase.isCombined() || voBase.isSubtotal() || voBase.isTotal()) {
            MessageDialog.showHintDlg(this, "提示", "小计合计行不能联查明细!");
            return null;
        }
        return voBase;
    }

    public ReportBaseVO getSelectedVO() {
        int iSelRow = -1;
        iSelRow = getReportBase().getBillTable().getSelectedRow();
        if (iSelRow == -1) {
            MessageDialog.showHintDlg(this, "提示", "请选择一行以进行穿透!");
            return null;
        }
        if (getBuffedTotalVO() == null)
            return null;
        ReportBaseVO voBase = new ReportBaseVO();
        if (getCurrentVO()[iSelRow] instanceof SuperVO) {
            voBase = (ReportBaseVO) VOConvert
                    .superVOTocircularlyVO((SuperVO) getCurrentVO()[iSelRow]);
        } else {
            voBase = (ReportBaseVO) getCurrentVO()[iSelRow];
        }
        if (voBase.isCombined() || voBase.isSubtotal() || voBase.isTotal()
                || checkIsTotalLine(voBase)) {
            // MessageDialog.showHintDlg(this, "提示", "小计合计行不支持穿透!");
            return null;
        }
        if (voBase.getStringValue("nrowclass") != null
                && Integer.parseInt(voBase.getStringValue("nrowclass")) == 0) {
            return null;
        }
        return voBase;
    }

    private Boolean checkIsTotalLine(ReportBaseVO vo) {
        for (int i = 0; i < vo.getAttributeNames().length; i++) {
            if (vo.getAttributeValue(vo.getAttributeNames()[i])
                    .equals("——小计——")
                    || vo.getAttributeValue(vo.getAttributeNames()[i]).equals(
                            "——合计——")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 子类实现该方法，返回业务界面的标题。
     * 
     * @version (00-6-6 13:33:25)
     * 
     * @return java.lang.String
     */
    public String getTitle() {
        return null;
    }

    /**
     * 这个方法仅在未动态的给UI增加列时才能正确工作
     */
    public CircularlyAccessibleValueObject[] getVOFromUI() throws Exception {
        ReportItem[] items = getReportBase().getBody_Items();
        int rows = getReportBase().getRowCount();
     ReportBaseVO[] result = new ReportBaseVO[rows];
        for (int row = 0; row < rows; row++) {
            result[row] = new ReportBaseVO();
            for (int i = 0; i < items.length; i++) {
                result[row].setAttributeValue(items[i].getKey(),
                        getReportBase().getBodyValueAt(row, items[i].getKey()));

            }
        }

        return result;
    }

    public void init() {
        add(getReportBase());
        reSetModel();
        setUIAfterLoadTemplate();

        getReportBase().setBodyMenu(new UIMenuItem[0]);

        // ////////////////////////////by guanyj
        initPrivateButton();
        setButtons(getAllBtnAry());
        updateAllButtons();
        backupReportModelVOs();

        if (getUIControl().getGroupKeys() != null)
            needGroup = true;

        initColumnGroups();

        // 显示行号
        getReportBase().setShowNO(true);
        // 显示千分位
        getReportBase().setShowThMark(true);
        // add by saf/08-4-16/设置数量单价金额精度：默认8位
        setDefaultPriceAndQuantityDecimalDigits();
        setDefaultDecimalDigits();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 备份报表模板数据，因为在某些操作中可能被改变
     */
    protected void backupReportModelVOs() {
        ArrayList<ReportModelVO> al = new ArrayList<ReportModelVO>();
        for (int i = 0; i < getReportBase().getAllBodyVOs().length; i++) {
            // 我们只备份 select_type!=未被选择的VO
            if (!getReportBase().getAllBodyVOs()[i].getSelectType().equals(
                    IReportModelSelectType.UNSELECTED))
                al.add((ReportModelVO) getReportBase().getAllBodyVOs()[i]
                        .clone());

        }
        copyOfReportModelVOs = (nc.vo.pub.report.ReportModelVO[]) al
                .toArray(new nc.vo.pub.report.ReportModelVO[0]);

    }

    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 添加方法
     * 
     */
    protected void initColumnGroups() {
        nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
        HashMap tmpHash = new HashMap();

        for (int i = 0; i < vos.length; i++) {
            int index = vos[i].getColumnSystem().indexOf(columnSystemDelimiter);
            String key;
            ArrayList al;
            if (index != -1) {
                key = vos[i].getColumnSystem().substring(0, index);
            } else {
                key = vos[i].getColumnSystem();
            }
            if (tmpHash.get(key) == null) {
                al = new ArrayList();
                tmpHash.put(key, al);
            } else {
                al = (ArrayList) tmpHash.get(key);
            }
            al.add(vos[i].getColumnCode());

        }
        reportModelColumnGroups.addAll(tmpHash.values());
    }

    public int[] getReportButtonAry() {
        m_buttonArray = new int[] { IReportButton.QueryBtn,
                IReportButton.ColumnFilterBtn, IReportButton.CrossBtn,
                IReportButton.FilterBtn, IReportButton.SortBtn,
                IReportButton.SubTotalBtn, IReportButton.PrintBtn,
                IReportButton.RefreshBtn, };
        return m_buttonArray;
    }

    /**
     * 
     * date:2007-8-22 下午01:17:30
     * 
     * @author:saf void
     */
    protected void initPrivateButton() {
    }

    protected final void addPrivateButton(String name, String hint,
            Integer btnno) {
        ButtonObject bo = new ButtonObject(name, hint, 1);
        bo.setTag(btnno.toString());
        button_action_map.getButtonMap().put(btnno, bo);
    }

    /**
     * 此方法返回预定按钮和自定义按钮
     * 
     */
    private ButtonObject[] getAllBtnAry() {
        if (button_action_map.getVisibleButtonsByOrder().size() == 0)
            return null;
        ButtonObject[] buttonAry = (ButtonObject[]) button_action_map
                .getVisibleButtonsByOrder().toArray(new ButtonObject[0]);

        return buttonAry;
    }

    public boolean isCrossTable() {
        return m_bCrossTable;
    }

    /**
     * 子类实现该方法，响应按钮事件。
     * 
     */
    public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
        // if (bo == m_boQry)
        // onQuery();
        // if (bo == m_boPrint) {
        // onPrint();
        // }
        // if (bo == m_boPenerate) {
        // onPenerate();
        // }
        // ----------------------------应用button_action_map-----------------------

        if (button_action_map.get(bo) != null) {
            IButtonActionAndState action = (IButtonActionAndState) button_action_map
                    .get(bo);
            // 判断是否是查询按钮被按下 若查询被按下则数据被更新
            if (action instanceof QueryAction) {
                num++;
            }

            try {
                action.execute();
                // modify by chenliang at 07/09/23
                updateBodyDigits();
            } catch (Exception e) {
                showErrorMessage("错误：" + e.getMessage());
            }
        } else {
            try {
                onBoElse(Integer.parseInt(bo.getTag()));
            } catch (Exception e) {
                showErrorMessage("错误：" + e.getMessage());
            }
        }

        updateAllButtons();
    }

    // --------------------------------------------------------down--------------------------by
    // guanyj///////////////////////

    /**
     * 
     */
    protected void onBoElse(Integer intBtn) throws Exception {
    }

    /**
     * 更新所有按钮状态-----------------------------------------------------------------------
     */
    protected void updateAllButtons() {
        boolean hasData = false;
        BillModel bm = m_report.getBillModel();
        if (bm != null
                && (bm.getDataVector() == null || bm.getDataVector().size() == 0)) {
            hasData = false;
        } else {
            hasData = true;
        }
        setAllButtonState(hasData);
        updateButtons();

    }

    /**
     * 设置所有button状态。依据是IButtonActionAndState接口的isButtonAvailable
     * 
     * @author guanyj
     * @param hasData
     */
    private void setAllButtonState(boolean hasData) {
        Iterator it = button_action_map.keySet().iterator();
        while (it.hasNext()) {
            ButtonObject obj = (ButtonObject) it.next();
            IButtonActionAndState state = (IButtonActionAndState) button_action_map
                    .get(obj);
            int result = state.isButtonAvailable();
            switch (result) {
            case 0:
                obj.setEnabled(false);
                break;
            case 1:
                obj.setEnabled(true);
                break;
            case IButtonActionAndState.ENABLE_ALWAYS:
                obj.setEnabled(true);
                break;
            case IButtonActionAndState.ENABLE_WHEN_HAS_DATA:
                if (hasData) {
                    obj.setEnabled(true);
                    break;
                } else
                    obj.setEnabled(false);
                break;
            }

        }
    }

    /**
     * “模板打印”按钮调用本方法。
     * 
     */
    public void onPrint()  throws Exception{
        Object template = null;
        try {
            // template =
            // nc.ui.pub.print.PrintBO_Client.getTempletByModule(_getModelCode());
            // xkf 升级 v5
            IPrintTemplateQry tempservice = (IPrintTemplateQry) NCLocator
                    .getInstance().lookup(IPrintTemplateQry.class.getName());
            template = tempservice.getTempletByModule(_getModelCode());
        } catch (Exception e) {
        }
        if (template == null) {
            nc.ui.pub.print.PrintDirectEntry print = PMPrintManager.getDirectPrinter(getReportBase(), getTitle());
            print.preview();
        } else {
            nc.ui.trade.pub.CardPanelPRTS ds = new nc.ui.trade.pub.CardPanelPRTS(getTitle(), getReportBase());
            nc.ui.pub.print.PrintEntry printer = new nc.ui.pub.print.PrintEntry(this);
            printer.setDataSource(ds);
            printer.setTemplateID(getCorpPrimaryKey(), _getModelCode(),_getUserID(), null, null);
            // @@@@ 解决报表打印无法选择打印模板 by xep 2006-06-08
            if (printer.selectTemplate() < 0)
                return;
            printer.preview();
        }

    }

    abstract public void onQuery();
    
    abstract public ReportBaseVO[] getReportVO(String wheresql) throws BusinessException;

    public void registerMsgProxy(IMsgProxy mp) {
        m_msgProxy = mp;
    }

    public void reloadReport() {
        this.removeAll();
        m_report = null;
        add(getReportBase());
        getReportBase().getBodyPanel().getTable().removeSortListener();
        setUIAfterLoadTemplate();
        m_bCrossTable = false;
    }

    /**
     * 设置显示列
     */
    protected void reSetModel() {
        getReportBase().setBillModel(getReportBase().getBillModel());
        if (getUnlockSortListener() != null
                && getUnlockSortListener().booleanValue() == false) {
            getReportBase().getBodyPanel().getTable().removeSortListener();
        }
        getReportBase().getBodyPanel().getTable().setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        getReportBase().getBodyPanel().getTable().setRowSelectionAllowed(true);
        getReportBase().getBodyPanel().getTable().setColumnSelectionAllowed(
                false);
        getReportBase().getBodyPanel().getTable().setCellSelectionEnabled(true);
        getReportBase().setShowThMark(true);
    }

    public void setBodyVO(CircularlyAccessibleValueObject[] vos) {
        if (isCrossTable())
            reloadReport();
        getReportBase().setBodyDataVO(vos, true);
        m_vosTotal = vos;
        // saf/07/08/21am/add/报表表体双击单元格执行联查相关单据
        if (listenerNum < 1) {
            initListener();
            listenerNum += 1;
        }
    }

    public void setBodyVO(CircularlyAccessibleValueObject[] vos,
            boolean isLoadFormula) {

        // 在赋值前先设置界面的精度 ,保证精度不丢失add by wenming 2008-3-25
        setDecimalDigits();
        if (isCrossTable())
            reloadReport();
        getReportBase().setBodyDataVO(vos, isLoadFormula);
        m_vosTotal = vos;
        // saf/07/08/21am/add/报表表体双击单元格执行联查相关单据
        if (listenerNum < 1) {
            initListener();
            listenerNum += 1;
        }
    }

    /**
     * 功能：报表表体双击单元格执行联查相关单据 date:2007-8-21 上午09:32:30
     * 
     * @author:saf void
     */
    // public void initListener() {
    // // 表体Table鼠标监听
    // Component com = getReportBase().getBillTable();
    // com.addMouseListener(new MouseAdapter() {
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
    // */
    // public void mouseClicked(MouseEvent e) {
    // if (e.getSource() instanceof BillTable && e.getClickCount() == 2) {
    // onPenerate();
    // }
    // }
    // });
    // }
    public void initListener() {
        // 表体Table鼠标监听
        Component com = getReportBase().getBillTable();
        MouseListener[] mosListener = (MouseListener[]) com
                .getListeners(MouseListener.class);
        com.addMouseListener(new CustMouseListener(mosListener));
    }

    /**
     * 内部类，表体table鼠标事件监听 作者：薛恩平 创建日期：2007-7-27 17:09:28
     */
    private class CustMouseListener extends MouseAdapter {
        MouseListener[] listeners = null;

        public CustMouseListener(MouseListener[] list) {
            super();
            this.listeners = list;
        }

        /*
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        public void mouseClicked(MouseEvent e) {
            for (int i = 0; listeners != null && i < listeners.length; i++) {
                listeners[i].mouseClicked(e);
            }
            if (e.getSource() instanceof BillTable && e.getClickCount() == 2) {
                onPenerate();
            }
        }
    }

    private void specailPenerate() {
        
    }

    /**
     * 设置列长度。
     */
    public void setColWidth(String colName, int iwidth) {
        if (colName == null)
            return;
        for (int i = 0; i < getReportBase().getBodyShowItems().length; i++) {
            if (getReportBase().getBodyShowItems()[i].getKey().equals(colName.trim()))
                getReportBase().getBodyShowItems()[i].setWidth(iwidth);
        }
    }

    public void setCrossTableFlag(boolean b) {
        m_bCrossTable = b;
    }

    public void setGroupColumn(String[] columnIdentifiers, String groupName) {
        UITable table = getReportBase().getBillTable();
        TableColumnModel cm = table.getColumnModel();
        ColumnGroup g = new ColumnGroup(groupName);
        for (int i = 0; i < cm.getColumnCount(); i++) {
            if (Toolkit.binarySearch(columnIdentifiers, cm.getColumn(i)
                    .getIdentifier()) > -1)
                g.add(cm.getColumn(i));
        }
        GroupableTableHeader header = (GroupableTableHeader) table
                .getTableHeader();
        header.addColumnGroup(g);
    }

    public void setHeadDataByQryDlg() {
        getReportBase().setHeadDataVO(getHeadVOByQryDlg(null));
        getReportBase().execHeadLoadFormulas();

    }

    public void setHeadDataBYQryDlg(nc.ui.trade.report.query.QueryDLG qryDlg) {
        getReportBase().setHeadDataVO(getHeadVOByQryDlg(qryDlg));
        getReportBase().execHeadLoadFormulas();

    }

    /*
     * 合计vo和明细 vo放在一个UI.
     */
    public void setReportVOsVsDetailVOs(String[] joinFlds, String[] detailFlds,
            String detailKeyFlds) {

        try {
            CircularlyAccessibleValueObject[] vos = getVOFromUI();
            if (vos == null || vos.length == 0)
                return;
            m_vosTotal = Toolkit.combinReportVOsVsDetailVOs(vos, joinFlds,
                    detailFlds, detailKeyFlds);
            getReportBase().setBodyDataVO(m_vosTotal, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 合计vo和明细 vo放在一个UI.
     */
    public void setReportVOsVsDetailVOs(
            CircularlyAccessibleValueObject[] totalvos,
            CircularlyAccessibleValueObject[] detailvos, String[] joinFlds,
            String[] detailFlds, String detailKeyFlds) {
        setBodyVO(Toolkit.addTwoVos(totalvos, detailvos));
        setReportVOsVsDetailVOs(joinFlds, detailFlds, detailKeyFlds);
    }

    /**
     * 设置显示列
     * 
     * @parameters:cols the cols that should be hide or showed,
     * @ipos,0-head 1-body,2-tail.
     * @bshow,true show
     */
    public void setShowCol(String[] cols, int ipos, boolean bShow) {
        nc.ui.pub.bill.BillItem[] items = null;
        switch (ipos) {
        case HEAD:
            items = getReportBase().getHead_Items();
            break;
        case BODY:
            items = getReportBase().getBodyItems();
            break;
        case TAIL:
            items = getReportBase().getTailItems();
            break;
        }
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < cols.length; j++) {
                if (items[i].getKey().equalsIgnoreCase(cols[j])) {
                    if (ipos != BODY && !bShow) {
                        // ReportITem的一个 bug.在此特殊处理。
                        getReportBase().getHeaderPanel().remove(
                                items[i].getComponent());
                        getReportBase().getHeaderPanel().remove(
                                items[i].getCaptionLabel());

                    }
                    items[i].setShow(bShow);
                }
            }
        }
        reSetModel();
    }

    protected void setSubtotalVO(SubtotalVO vo) {
        m_voSubtotal = vo;
    }

    protected SubtotalVO getSubtotalVO() {
        return m_voSubtotal;
    }

    /**
     * 加载报表摸板后设置对UI进行必要的调整。
     */
    abstract public void setUIAfterLoadTemplate();

    /**
     * 穿透节点信息接口实现，如需要穿透查询的，需要重载该方法。
     */
    public String getPene2NodeInfo() {
        return null;
    }

    /**
     * 联查时需要自定义信息 时overwrite此方法 默认返回空，表示不做处理，具体实现请参考IPeneExtendInfo接口
     * 
     * @return
     */
    public IPeneExtendInfo getExtendInfo() {
        return null;
    }

    
    public void onPenerate() {
        
    }

    private String[] getExtendInfoFromPeneInfo(IPeneExtendInfo extendInfo,
            String itemkey) {
        // TODO Auto-generated method stub
        if (extendInfo == null) {
            return null;
        }
        // if (exPeneInfoHash == null) {
        HashMap<String, String[]> exPeneInfoHash = new HashMap<String, String[]>();
        String[][] cloumkeys = extendInfo.getColumKeys();
        String[] exWhereStrs = extendInfo.getExtendsWhereStrings();
        String[] exNodecode = extendInfo.getNodeCodes();
        for (int i = 0; i < exWhereStrs.length; i++) {
            String[] arrayCols = cloumkeys[i];
            for (int j = 0; j < arrayCols.length; j++) {
                String columkey = arrayCols[j];
                if (!exPeneInfoHash.containsKey(columkey)) {
                    exPeneInfoHash.put(columkey, new String[] { exWhereStrs[i],exNodecode[i] });
                }
            }
        }
        // }
        return (String[]) exPeneInfoHash.get(itemkey);
    }

    /**
     * 对界面vo排序
     */
    public void onSort(String[] fields, int[] asc) {
        CircularlyAccessibleValueObject[] vos = null;
        if ((vos = getCurrentVO()) == null)
            return;
        getReportBase().getReportSortUtil().multiSort(vos, fields, asc);
        setBodyDataVO(vos, false);
    }

    /**
     * 返回当前VO，如果从分组表中找不到，则直接从界面上得到。从界面上得到VO比较耗时，但是，如果数据和
     * VO顺序等密切相关，则需直接getVOFromUI，因为一些操作，比如排序，没有在分组表中体现出来
     * @return
     */
    public CircularlyAccessibleValueObject[] getCurrentVO() {
        CircularlyAccessibleValueObject[] cvos = null;
        if ((cvos = getCurrentVOFromGroupMap()) == null) {
            try {
                cvos = getVOFromUI();
            } catch (Exception e) {
                showErrorMessage("错误：" + e.getMessage());
            }
        }
        return cvos;
    }

    private CircularlyAccessibleValueObject[] getCurrentVOFromGroupMap() {
        if (groupKeys.size() == 0)
            return null;
        int selectedRow = 0;
        if (getGroupTable().getSelectedRow() != -1)
            selectedRow = getGroupTable().getSelectedRow();
        String[] keys = getValuesFromGroupTable(selectedRow);
        String key = "";
        for (int i = 0; i < keys.length; i++) {
            key += keys[i];
            if (i != keys.length - 1)
                key += ":";
        }
        return (groupMap.get(key) == null ? null
                : (CircularlyAccessibleValueObject[]) ((ArrayList) groupMap.get(key)).toArray(new CircularlyAccessibleValueObject[0]));
    }

    /**
     * 
     * @return
     */
    protected UITable getGroupTable() {
        if (groupTable == null) {
            groupTable = new UITable(new DefaultTableModel());
            groupTable.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    int row = groupTable.getSelectedRow();
                    if (row == -1)
                        return;
                    int count = groupTable.getModel().getColumnCount();
                    StringBuffer key = new StringBuffer();
                    for (int i = 0; i < count; i++) {
                        key.append(groupTable.getModel().getValueAt(row, i) == null ? "": groupTable.getModel().getValueAt(row, i).toString());
                        if (i != count - 1)
                            key.append(":");
                    }
                    ArrayList tmpVO = (ArrayList) groupMap.get(key.toString());
                    if (tmpVO != null && tmpVO.size() > 0) {
                        setBodyDataVO((CircularlyAccessibleValueObject[]) tmpVO.toArray(new CircularlyAccessibleValueObject[0]), false);
                    }
                    setHeadItems(convertVOKeysToModelKeys((String[]) groupKeys.toArray(new String[0])),getValuesFromGroupTable(row));
                }
            });
        }
        return groupTable;
    }

    /**
     * 设置表头数据
     * 
     * @param key
     * @param value
     */
    private void setHeadItems(String[] keys, Object[] values) {
        if (keys == null || values == null || keys.length == 0
                || values.length == 0)
            return;
        if (keys.length != values.length) {
            System.out.println("键和值的数目不匹配");
            return;
        }
        for (int i = 0; i < keys.length; i++) {
            getReportBase().setHeadItem(keys[i], values[i]);
        }
    }

    /**
     * 从分组table中获取指定行数据
     * 
     * @param row
     * @return
     */
    private String[] getValuesFromGroupTable(int row) {
        UITable table = getGroupTable();
        int count = table.getColumnCount();
        String[] str = new String[count];
        for (int i = 0; i < str.length; i++) {
            str[i] = (String) table.getModel().getValueAt(row, i);
        }
        return str;
    }

    /**
     * @param bodyDataVO
     *            要设置的 bodyDataVO。
     * @param isLoadFormula
     *            是否执行加载公式
     */
    protected void setBodyDataVO(CircularlyAccessibleValueObject[] dataVO,
            boolean isLoadFormula) {
        getReportBase().setBodyDataVO(dataVO, isLoadFormula);
        if (needGroup) {
            needGroup = false;
            onGroup(getUIControl().getGroupKeys());
        }
        updateAllButtons();
        // saf/07/08/21am/add/报表表体双击单元格执行联查相关单据
        if (listenerNum < 1) {
            initListener();
            listenerNum += 1;
        }
    }

    /**
     * 分组
     * 
     * @param context
     * @throws Exception
     * @throws Exception
     */
    protected void onGroup(String[] keys) {
        String[] colNames = getColumnNamesByKeys(convertVOKeysToModelKeys(keys));
        try {
            onGroup(keys, colNames);
        } catch (Exception e) {
            showErrorMessage("错误：" + e.getMessage());
        }
    }

    /**
     * 通过keys获得col显示名
     * 
     * @param keys
     *            必须是ModelKey，即以'_'分隔的key
     * @return
     */
    protected String[] getColumnNamesByKeys(String[] keys) {
        if (keys == null || keys.length == 0)
            return null;
        ReportModelVO[] fields = getModelVOs();
        // 不使用固定数组，防止数组元素为NULL，妨碍后面的判断
        ArrayList list = new ArrayList();
        if (fields != null && fields.length != 0) {
            for (int i = 0; i < keys.length; i++) {
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].getColumnCode().equals(keys[i]))
                        list.add(fields[j].getColumnUser());
                }
            }
        }
        return (String[]) list.toArray(new String[0]);
    }

    /**
     * 分组
     * 
     * @param keys
     * @param names
     * @throws Exception
     */
    protected void onGroup(String[] keys, String[] names) throws Exception {
        String[] convertGroupKeys = convertVOKeysToModelKeys((String[]) groupKeys
                .toArray(new String[0]));
        // 显示上次隐藏的列
        getReportBase().showHiddenColumn(convertGroupKeys);
        // 清理上次添加的表头
        removeHeadItems(convertGroupKeys);
        // 清理分组记录
        groupKeys.clear();
        // 认为取消分组
        if (keys == null || names == null || keys.length == 0
                || names.length == 0 || keys.length != names.length) {
            setVeriSplitEnabled(false);
            // 设置回原数据
            setBodyDataVO(allBodyDataVO, false);
            return;
        }

        groupKeys.addAll(Arrays.asList(keys));
        // 启动分隔栏
        setVeriSplitEnabled(true);
        // 从界面获取VO
        if (allBodyDataVO == null || allBodyDataVO.length == 0)
            allBodyDataVO = getVOFromUI();
        // 清空分组hash
        groupMap.clear();
        // 安分组key hash，key的形式是 a:b:c::空值认为是" ",不影响使用
        for (int i = 0; i < allBodyDataVO.length; i++) {
            StringBuffer key = new StringBuffer();
            for (int j = 0; j < keys.length; j++) {
                key
                        .append(allBodyDataVO[i].getAttributeValue(keys[j]) == null ? " "
                                : allBodyDataVO[i].getAttributeValue(keys[j])
                                        .toString());
                if (j != keys.length - 1)
                    key.append(":");
            }
            addVoToHashmap(key.toString(), allBodyDataVO[i]);
        }
        String[] convertedKeys = convertVOKeysToModelKeys(keys);
        extractItemsToHead(convertedKeys, names);
        getReportBase().hideColumn(convertedKeys);
        // 构造表model
        GroupTableModel model = new GroupTableModel();
        model.addColumns(names);
        model.addRows(groupMap.keySet());
        getGroupTable().setModel(model);
        // 显示第一行数据
        ArrayList tmpVO = (ArrayList) groupMap.get(groupMap.keySet().iterator()
                .next());
        if (tmpVO != null && tmpVO.size() > 0) {
            setBodyDataVO((CircularlyAccessibleValueObject[]) tmpVO
                    .toArray(new CircularlyAccessibleValueObject[0]), false);
            setHeadItems(convertedKeys, getValuesFromGroupTable(0));
        }
    }

    /**
     * 将keys所对应的item提取到表头
     * 
     * @param keys
     *            主键
     * @param colNames
     *            列名称
     */
    private void extractItemsToHead(String[] keys, String[] colNames) {
        String[] convertKeys = convertVOKeysToModelKeys(keys);
        ReportItem[] items = new ReportItem[keys.length];
        for (int i = 0; i < convertKeys.length; i++) {
            items[i] = new ReportItem();
            items[i].setKey(convertKeys[i]);
            items[i].setShow(true);
            items[i].setName(colNames[i]);
        }
        getReportBase().addHeadItem(items);
    }

    /**
     * 将VO添加到HashMap中
     * 
     * @param key
     * @param vo
     */
    private void addVoToHashmap(String key, CircularlyAccessibleValueObject vo) {
        ArrayList list = null;
        if ((list = (ArrayList) groupMap.get(key)) == null) {
            list = new ArrayList();
            list.add(vo);
            groupMap.put(key, list);
        } else
            list.add(vo);
    }

    /**
     * @param veriSplitPane
     */
    private void setVeriSplitEnabled(boolean enabled) {
        if (enabled) {
            veriSplitPane.setDividerLocation(200);
            veriSplitPane.setEnabled(true);
            veriSplitPane.setDividerSize(8);
            veriSplitPane.setOneTouchExpandable(true);
            getReportBase().setShowNO(true);
        } else {
            veriSplitPane.setDividerLocation(0);
            // veriSplitPane.setEnabled(false);
            veriSplitPane.setDividerSize(0);
            veriSplitPane.setOneTouchExpandable(false);
            getReportBase().setShowNO(false);
        }
    }

    /**
     * @param strings
     */
    private void removeHeadItems(String[] strs) {
        ReportItem[] headItems = getReportBase().getHead_Items();
        ArrayList list = new ArrayList();
        if (headItems != null && headItems.length > 0) {
            for (int i = 0; i < headItems.length; i++) {
                if (contains(strs, headItems[i].getKey()))
                    continue;
                list.add(headItems[i]);
            }
        }
        getReportBase().setHead_Items(
                (ReportItem[]) list.toArray(new ReportItem[0]));
    }

    /**
     * 
     * @param source
     * @param element
     * @return
     */
    public boolean contains(String[] source, String element) {
        if (source != null) {
            for (int i = 0; i < source.length; i++) {
                if (source[i].equals(element))
                    return true;
            }
        }
        return false;
    }

    /**
     * 返回当前UI对应的控制类实例。
     * 
     * @return nc.ui.tm.pub.ControlBase
     */
    public IReportCtl getUIControl() {
        if (m_uiCtl == null)
            m_uiCtl = createIReportCtl();
        return m_uiCtl;
    }

    /**
     * 默认返回从数据库中查询出的报表控制信息，如果需自己控制，只需重载此方法即可。
     * 
     * @return nc.ui.trade.report.controller.IReportCtl
     */
    protected IReportCtl createIReportCtl() {
        return new ReportCtl();
    }

    /**
     * 打印预览
     * 
     * @throws Exception
     */
    public void onPrintPreview() throws Exception {
        getReportBase().previewData();
    }

    /**
     * 直接打印
     */
    public void onPrintTemplet() {
        getReportBase().printData();
    }

    public CircularlyAccessibleValueObject[] getBodyDataVO() {
        return getReportBase().getBodyDataVO();
    }

    /**
     * 条件过滤 能够完成恢复操作 过滤总是从查询产生的数据集中过滤
     * 
     * @author guanyj
     */
    public void onFilter(String strFomula) throws Exception {
        if (num > n) {
            n = num;
            this.dataVec = getReportBase().getBillModel().getDataVector();
        }
        if (strFomula == null || strFomula.trim().equals("")) {
            getReportBase().getBillModel().setDataVector(this.dataVec);
            MemoryResultSetMetaData mrsmd = getReportBase()
                    .getReportGeneralUtil().createMeteData();
            getReportBase().getReportInfoCtrl().setMrs(
                    getReportBase().getReportGeneralUtil().vector2Mrs(
                            this.dataVec, mrsmd));
            return;
        }

        MemoryResultSetMetaData mrsmd = getReportBase().getReportGeneralUtil()
                .createMeteData();
        // 构造内存结果集
        Vector dataVec = getReportBase().getBillModel().getDataVector();
        if (getReportBase().getReportInfoCtrl().getMrs() == null)
            getReportBase().getReportInfoCtrl().setMrs(
                    getReportBase().getReportGeneralUtil().vector2Mrs(dataVec,
                            mrsmd));
        MemoryResultSet Memrs = getReportBase().getReportGeneralUtil()
                .vector2Mrs(this.dataVec, mrsmd);

        // 转换内存结果集 从查询产生的结果集中过滤
        MemoryResultSet mrs = getReportBase()
                .changeMrs_filter(Memrs, strFomula);
        // 回转为向量
        dataVec = getReportBase().getReportGeneralUtil().mrs2Vector(mrs);
        // 回设表体向量
        getReportBase().getBillModel().setDataVector(dataVec);

        // 将dataVec的数据转换回成mrs保存起来
        getReportBase().getReportInfoCtrl().setMrs(
                getReportBase().getReportGeneralUtil().vector2Mrs(dataVec,
                        mrsmd));
        // else
        // getReportBase().filter(strFomula);
    }

    /**
     * 返回所有数据。
     * 
     * @return
     * @throws Exception
     */
    public CircularlyAccessibleValueObject[] getAllBodyDataVO() {
        if (allBodyDataVO == null) {
            try {
                allBodyDataVO = getVOFromUI();
            } catch (Exception e) {
                Logger.error(e.getMessage(), e);
            }
        }
        return allBodyDataVO;
    }

    /**
     * 更改显示界面内容，包括标题，表项顺序以及表项显示名称
     * 
     * @param title
     *            欲显示标题
     * @param fieldNames
     *            表项key值，将会按照它的顺序显示
     * @param showNames
     *            相应key的显示名称，可为空，空即认为不更改显示名称。如果与key的长度不一致，也认为为空
     * @throws Exception
     */
    public void onColumnFilter(String title, String[] fieldNames,
            String[] showNames, boolean isAdjustOrder) throws Exception {
        CircularlyAccessibleValueObject[] vos = getBodyDataVO();
        getReportBase().hideColumn(getAllColumnCodes());

        // 更新模板中标题
        getReportBase().setReportTitle(title);
        // 更新显示标题
        setTitleText(title);
        getReportBase().showHiddenColumn(fieldNames);
        if (isAdjustOrder)
            setColumnOrder(fieldNames);
        if (showNames != null && showNames.length == fieldNames.length)
            setColumnName(fieldNames, showNames);
        // setBodyDataVO(vos, false);//edit guanyj 由于栏目设置表体数据不更新， 不需要加载显示公式
        setBodyDataVO(vos, true);
    }

    /**
     * 得到标题所有列的编码
     * 
     * @return java.lang.String[]
     */
    protected String[] getAllColumnCodes() {
        nc.vo.pub.report.ReportModelVO[] vos = getReportBase().getAllBodyVOs();
        String[] names = new String[vos.length];
        for (int i = 0; i < vos.length; i++) {
            names[i] = vos[i].getColumnCode();
        }
        return names;
    }

    /**
     * 这个方法设置报表的列的顺序
     * 
     */
    protected void setColumnOrder(String[] column_codes) {
        ReportItem[] items = getReportBase().getBody_Items();

        ArrayList al = new ArrayList();
        HashMap tmpHas = new HashMap();
        for (int i = 0; i < items.length; i++) {
            tmpHas.put(items[i].getKey(), items[i]);
        }

        for (int i = 0; i < column_codes.length; i++) {
            if (tmpHas.containsKey(column_codes[i])) {
                al.add(tmpHas.get(column_codes[i]));
                tmpHas.remove(column_codes[i]);
            }

        }
        al.addAll(tmpHas.values());

        ReportItem[] newitems = (ReportItem[]) al.toArray(new ReportItem[0]);
        getReportBase().setBody_Items(newitems);

    }

    /**
     * 更改界面模板的显示名称
     * 
     * @param fieldNames
     * @param showNames
     */
    private void setColumnName(String[] fieldNames, String[] showNames) {
        ReportItem[] items = getReportBase().getBody_Items();
        HashMap tmpHas = new HashMap();
        for (int i = 0; i < items.length; i++) {
            tmpHas.put(items[i].getKey(), items[i]);
        }

        for (int i = 0; i < fieldNames.length; i++) {
            if (tmpHas.containsKey(fieldNames[i])) {
                ReportItem tmpItem = (ReportItem) tmpHas.get(fieldNames[i]);
                if (!tmpItem.getName().equals(showNames[i]))
                    tmpItem.setName(showNames[i]);
            }

        }
        getReportBase().setBody_Items(items);
    }

    /**
     * 从界面显示的字段中选择根据【type】相应类型的字段
     * 
     * @param type
     * @return TableField[]
     */
    public TableField[] getVisibleFieldsByDataType(Integer type) {
        TableField[] visibleFields = getVisibleFields();
        ArrayList<TableField> al = new ArrayList<TableField>();
        nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getDataType().equals(type)) {
                TableField f = createTableFieldFromReportModelVO(vos[i]);
                if (Arrays.asList(visibleFields).contains(f)) {
                    al.add(f);
                }
            }
        }
        return (TableField[]) al.toArray(new TableField[0]);
    }

    /**
     * 得到报表模板上隐藏的列(select_type == 1,但又没在界面上显示的列。 select_type==2
     * 的列被称为“终身隐藏列”排除在外)
     * 
     * @return nc.vo.tm.report.TableField[]
     */
    public TableField[] getInvisibleFields() {
        nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
        ArrayList invisible = new ArrayList();
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
                    .intValue()) {
                try {
                    getReportBase().getBillTable().getColumn(
                            vos[i].getColumnUser());

                }
                // 抛出异常，说明界面上已经隐藏
                catch (Exception e) {
                    invisible.add(createTableFieldFromReportModelVO(vos[i]));
                }

            }
        }

        TableField[] invisibleFields = (TableField[]) invisible
                .toArray(new TableField[0]);

        return invisibleFields;
    }

    /**
     * 获得界面上显示的所有字段
     * 
     * @return TableField[]
     */
    public TableField[] getVisibleFields() {
        nc.vo.pub.report.ReportModelVO[] vos = getModelVOs();// getReportBase().getAllBodyVOs();
        ArrayList<TableField> visible = new ArrayList<TableField>();
        for (int i = 0; i < vos.length; i++) {
            if (vos[i].getSelectType().intValue() == IReportModelSelectType.VISIBLE
                    .intValue()) {
                try {
                    getReportBase().getBillTable().getColumn(
                            vos[i].getColumnUser());
                    visible.add(createTableFieldFromReportModelVO(vos[i]));
                }
                // 该处异常不应该抛出
                catch (Exception e) {
                    // showErrorMessage("错误：" + e.getMessage());
                }
            }
        }

        TableField[] visibleFields = (TableField[]) visible
                .toArray(new TableField[0]);

        return visibleFields;
    }

    /**
     * 
     * @param context
     *            小计合计上下文
     * @throws Exception
     */
    public void onSubTotal(SubtotalContext context) throws Exception {
        getReportBase().setSubtotalContext(context);
        getReportBase().subtotal();
    }

    /**
     * 返回模板数据VO，目前返回的是备份下来的VO。在模板类提供了相应功能后，则直接调用模板类的相应方法
     * 
     * @return 模板modelVO
     */
    public ReportModelVO[] getModelVOs() {
        return copyOfReportModelVOs; // getReportBase().getAllBodyVOs();
    }

    protected TableField createTableFieldFromReportModelVO(
            nc.vo.pub.report.ReportModelVO vo) {
        return new TableField(convertReportModelFieldNameToVOFieldName(vo
                .getColumnCode()), vo.getColumnUser(), vo.getDataType()
                .intValue() == 1
                || vo.getDataType().intValue() == 2);
    }

    /**
     * 将以'_'分隔的ReportModel的fieldName转换为以'.'分隔的VOFieldName
     * 
     * @return java.lang.String
     * @param reportFieldName
     *            java.lang.String
     */
    protected String convertReportModelFieldNameToVOFieldName(
            String reportFieldName) {
        if (reportFieldName.indexOf('_') == -1)
            return reportFieldName;
        else
            return reportFieldName.substring(0, reportFieldName.indexOf('_'))
                    + "."
                    + reportFieldName.substring(
                            reportFieldName.indexOf('_') + 1, reportFieldName
                                    .length());
    }

    /**
     * 
     * @param keys
     * @return
     */
    public String[] convertVOKeysToModelKeys(String[] keys) {
        if (keys == null || keys.length == 0)
            return null;
        String[] convertedKeys = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            convertedKeys[i] = convertVOFieldNameToReportModelFieldName(keys[i]);
        }
        return convertedKeys;
    }

    /**
     * 将以'.'分隔的VOFieldName转换为以'_'分隔的ReportModel的fieldName
     * 
     * @return java.lang.String
     * @param voFieldName
     *            java.lang.String
     */
    public String convertVOFieldNameToReportModelFieldName(String voFieldName) {
        if (voFieldName.indexOf('.') == -1)
            return voFieldName;
        else
            return voFieldName.substring(0, voFieldName.indexOf('.'))
                    + "_"
                    + voFieldName.substring(voFieldName.indexOf('.') + 1,
                            voFieldName.length());
    }

    /**
     * 报表交叉
     */
    public void onCross(String[] rows, String[] cols, String[] values)
            throws Exception {
        getReportBase().drawCrossTable(rows, cols, values);
    }


    public void setBodyDataForSub() {
        int num = 0;
        boolean isAdd = false;

        CircularlyAccessibleValueObject[] tmpVOs = getReportBase()
                .getBodyDataVO();
        CircularlyAccessibleValueObject[] tmpBodyVOs = new CircularlyAccessibleValueObject[tmpVOs.length];

        for (int i = 0; i < tmpVOs.length; i++) {

            String[] names = tmpVOs[i].getAttributeNames();
            for (int j = 0; j < names.length; j++) {

                if (tmpVOs[i].getAttributeValue(names[j]) == "——合计——"
                        || tmpVOs[i].getAttributeValue(names[j]) == "——小计——") {
                    num++;
                    isAdd = true;
                    continue;
                }

            }
            if (isAdd) {
                isAdd = false;
                continue;
            }
            tmpBodyVOs[i - num] = tmpVOs[i];
        }
        CircularlyAccessibleValueObject[] bodyVOs = new CircularlyAccessibleValueObject[tmpVOs.length
                - num];

        for (int i = 0; i < bodyVOs.length; i++) {
            bodyVOs[i] = tmpBodyVOs[i];
        }
        getReportBase().setBodyDataVO(bodyVOs);
    }

    /**
     * 获取表体的金额字段KEY
     * 
     * created by chenliang at 2007-9-23 下午10:05:45
     * 
     * @return
     */
    private String[] getMnyFlds() {
        BillModel bm = getReportBase().getBillModel();
        BillItem[] items = bm.getBodyItems();
        if (items == null || items.length == 0)
            return null;
        List<String> mnyFldList = new ArrayList<String>();
        for (int i = 0; i < items.length; i++) {
            int idatatype = items[i].getDataType();
            if (idatatype == IBillItem.DECIMAL) {
                String mnyFld = items[i].getKey();
                mnyFldList.add(mnyFld);
            }
        }
        if (mnyFldList.size() == 0)
            return null;
        return (String[]) mnyFldList.toArray(new String[0]);
    }

    /**
     * 设置金额字段的显示精度
     */
    protected void setDefaultDecimalDigits() {
        BillModel bm = getReportBase().getBillModel();
        BillItem[] items = bm.getBodyItems();
        if (items == null || items.length == 0)
            return;
        for (int i = 0; i < items.length; i++) {
            int idatatype = items[i].getDataType();
            if (idatatype == IBillItem.DECIMAL) {
                items[i].setDecimalDigits(new Integer(8).intValue());
            }
        }

    }

    /**
     * 设置金额字段的显示精度
     */
    protected void setDecimalDigits() {
        BillModel bm = getReportBase().getBillModel();
        BillItem[] items = bm.getBodyItems();
        if (items == null || items.length == 0)
            return;
        for (int i = 0; i < items.length; i++) {
            int idatatype = items[i].getDataType();
            if (idatatype == IBillItem.DECIMAL && isMnyItem(items[i].getKey())) {
                
                /////////////////////------------------------------------------zpm注销，可以设置默认精度 为 8-------------??????????????????????????????????
                items[i].setDecimalDigits(4);
            }
        }

    }

    protected boolean isMnyItem(String key) {
        // TODO Auto-generated method stub
        if (vecNotMnyFlds == null) {
            vecNotMnyFlds = new Vector<String>();
            if (getPriceShowNum() != null && getPriceShowNum().length > 0)
                for (int i = 0; i < getPriceShowNum().length; i++)
                    vecNotMnyFlds.add(getPriceShowNum()[i]);
            if (getQuantityShowNum() != null && getQuantityShowNum().length > 0)
                for (int i = 0; i < getQuantityShowNum().length; i++)
                    vecNotMnyFlds.add(getQuantityShowNum()[i]);
            if (getOtherItemDigitShowNum() != null
                    && getOtherItemDigitShowNum().length == 2) {
                for (int i = 0; i < getOtherItemDigitShowNum()[0].length; i++)
                    vecNotMnyFlds.add(getOtherItemDigitShowNum()[0][i]);
            }
        }
        return !vecNotMnyFlds.contains(key);
    }

    /**
     * 刷新表体金额，使精度变化生效
     * 
     * created by chenliang at 2007-9-23 下午09:52:31
     */
    protected void updateBodyDigits() {
        setDecimalDigits();
        // 设置单价和数量的精度 ,add by wenming 2008-1-21
        setPriceAndQuantityDecimalDigits();
        setOtherDecimalDigtis();
        BillModel bm = getReportBase().getBillModel();
        int rowCount = bm.getRowCount();
        if (rowCount <= 0)
            return;

        String[] mnyFlds = getMnyFlds();
        if (mnyFlds == null || mnyFlds.length == 0)
            return;
        for (int row = 0; row < rowCount; row++) {
            for (int j = 0; j < mnyFlds.length; j++) {
                bm.setValueAt(bm.getValueAt(row, mnyFlds[j]), row, mnyFlds[j]);
            }
        }

    }

    /**
     * 2008-04-26设置其他字段精度
     */
    protected void setOtherDecimalDigtis() {
        // 需要设置精度的字段
        if (getOtherItemDigitShowNum() != null
                && getOtherItemDigitShowNum().length == 2) {
            String[] bodyotheritems = getOtherItemDigitShowNum()[0];
            // 需要设置的精度
            String[] bodyotheritemsdigi = getOtherItemDigitShowNum()[1];

            // 设置其他精度
            if (bodyotheritems != null && bodyotheritems.length > 0
                    && bodyotheritems.length == bodyotheritemsdigi.length) {

                for (int j = 0; j < bodyotheritems.length; j++) {
                    if (!isNull(bodyotheritems[j])) {
                        int digitint = isNull(bodyotheritemsdigi[j]) ? 2
                                : new Integer(bodyotheritemsdigi[j]);
                        String attrName = bodyotheritems[j];
                        BillItem item = getReportBase().getBillModel()
                                .getItemByKey(attrName);
                        if (item != null)
                            item.setDecimalDigits(digitint);
                    }

                }
            }
        }
    }

    // 返回需要設置的其他字段的精度對應數組 0維是字段，1維是精度
    protected String[][] getOtherItemDigitShowNum() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see nc.ui.trade.card.BillCardUI#getCardItemShowNum() 作者：wenming
     *      设置单价精度一维数组 如子类有单价,需要设置自己的精度,覆写此方法 创建日期：2008-1-21 11:16:58 @return
     */
    public String[] getPriceShowNum() {

        return null;
    }

    /*
     * @see nc.ui.trade.card.BillCardUI#getCardItemShowNum() 作者：wenming 设置数量精度
     *      如子类有数量,需要设置自己的精度,覆写此方法 作者：文明 创建日期：2008-1-21 上午09:40:47 @return
     */
    public String[] getQuantityShowNum() {

        return null;
    }

    /**
     * 
     * 作者：文明 创建日期：2008-1-21 下午03:03:04
     */
    protected void setDefaultPriceAndQuantityDecimalDigits() {
        // 数量单价
        String[] bodyunitpriceitems = getPriceShowNum();
        String[] bodyquantityitems = getQuantityShowNum();

        // 设置单价精度
        if (bodyunitpriceitems != null && bodyunitpriceitems.length > 0) {

            for (int j = 0; j < bodyunitpriceitems.length; j++) {
                if (!isNull(bodyunitpriceitems[j])) {
                    String attrName = bodyunitpriceitems[j];
                    getReportBase().getBillModel().getItemByKey(attrName)
                            .setDecimalDigits(new Integer(8).intValue());
                }

            }
        }
        // 设置数量精度
        if (bodyquantityitems != null && bodyquantityitems.length > 0) {
            for (int j = 0; j < bodyquantityitems.length; j++) {
                if (!isNull(bodyquantityitems[j])) {
                    String attrQuantityName = bodyquantityitems[j];
                    getReportBase().getBillModel().getItemByKey(
                            attrQuantityName).setDecimalDigits(
                            new Integer(8).intValue());
                }
            }
        }
    }

    /**
     * 
     * 设置单价、数量 精度
     */
    protected void setPriceAndQuantityDecimalDigits() {}

    /**
     * 空字符串视为空。
     */
    protected boolean isNull(Object o) {
        if (o == null || o.toString() == null
                || o.toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 说明：
     * @Version：
     * @DateTime 2008-2-22 上午11:05:37
     * @Authors：ssd
     * @return unlockSortListener
     */
    public Boolean getUnlockSortListener() {
        return unlockSortListener;
    }

    /**
     * 说明：
     * @Version：
     * @DateTime 2008-2-22 上午11:05:38
     * @Authors：ssd
     * @param unlockSortListener 
     */
    public void setUnlockSortListener(Boolean unlockSortListener) {
        this.unlockSortListener = unlockSortListener;
    }

    /**
     * 穿透节点信息接口实现，如需要穿透到不同目标时，需要重载该方法。
     * 作者：zhourj
     */
    public String getPenetrateObj() {
        return null;
    }

}