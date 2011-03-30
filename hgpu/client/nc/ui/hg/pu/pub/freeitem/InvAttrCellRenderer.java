package nc.ui.hg.pu.pub.freeitem;

import java.awt.*;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.AggregatedValueObject;

/**
 * 王乃军--变色龙
 * @author Administrator
 *
 */
public class InvAttrCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

    //原来的单元格字体颜色
    private final Color INIT_COLOR = Color.blue;
    //提示字体的颜色
    private final Color FOREGROUND_COLOR = Color.red;//new Color(128,128,255);
    //自由项列Itemkey
    private final String FREE_ITEM = "vfree0";

    javax.swing.table.DefaultTableCellRenderer m_defRender = null;    

    //包含存货控制属性的同步单据VO
    private AggregatedValueObject m_voBill = null;

    //保留的BILL CARD PANEL,用于VALUE IS NULL CHECK
    private nc.ui.pub.bill.BillCardPanel m_bcp = null;

    //自由项列值。
    private int m_iFreeItemCol = -1;    

    /**
     * RowNOCellRenderer 构造子注解。
     */
    public InvAttrCellRenderer() {
        super();
    }

    /*
     * Returns the table cell renderer.
     */
    public Component getTableCellRendererComponent(javax.swing.JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {

        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //属于变化的范围
        if (table.getModel() instanceof BillModel) {
            //set the foreground color.
            setForeground(FOREGROUND_COLOR);
            //setBackground(m_newColor);//new Color(128,128,255));
            this.setText("请录入");
        } else {
            setForeground(INIT_COLOR);
        }
        this.repaint();
        return comp;

    }

    /**
     * 创建人：王乃军
     * 
     * 功能：设置自由项着色器
     * 
     */
    public void setFreeItemRenderer(nc.ui.pub.bill.BillCardPanel bcp) {
        if (bcp == null || bcp.getBillTable() == null) {
            Logger.info("CELLRENDER:set renderer param null.");
            return;
        } else
            this.m_bcp = bcp;
        m_iFreeItemCol = bcp.getBodyColByKey(FREE_ITEM);
        nc.ui.pub.bill.BillItem bi = bcp.getBodyItem(FREE_ITEM);
        if (m_iFreeItemCol < 0 || bi == null || bi.getName() == null||!bi.isShow()) {
            Logger.info("CELLRENDER:cannot find free0.");
            return;
        }
        String sTitle = bi.getName();
        nc.ui.pub.beans.UITable table = bcp.getBillTable();
        //如果自由项目在模板中不存在，必须捕获异常并返回
        if (sTitle == null || sTitle.trim().length() <= 0)
            return;
        try {
            javax.swing.table.TableColumn tal = table.getColumn(sTitle);
            if (tal == null) {
                Logger.info("CELLRENDER:cannot find " + sTitle);
                return;
            }
            tal.setCellRenderer(this);
        } catch (Exception e) {
        	Logger.error(e);
            return;
        }
    }
}