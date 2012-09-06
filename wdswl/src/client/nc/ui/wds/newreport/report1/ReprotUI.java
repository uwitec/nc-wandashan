package nc.ui.wds.newreport.report1;
import java.util.List;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI2;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI3;
/**
 * 完达山库存现存量报表
 * @author mlr
 */
public class ReprotUI extends ZmReportBaseUI3{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8264102894793741723L;
	/**
	 * 接收查询的组合sql
	 * 
	 * @author mlr
	 * @说明：（鹤岗矿业） 2011-12-22上午10:41:05
	 * @return
	 */
	public String[] getSqls() throws Exception {
		return new String[]{getSql()};
	}
	/**
	 * 查询库存现存量sql
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-9-5上午10:06:38
	 * @return
	 * @throws Exception 
	 */
	private String getSql() throws Exception {
      StringBuffer sql=new StringBuffer();
      sql.append(" select pk_corp , ");
      sql.append(" pk_customize1 pk_stordoc, ");
      sql.append(" pk_cargdoc ,");
      sql.append(" pk_invmandoc ,");
      sql.append(" pk_invbasdoc ,");
      sql.append(" whs_batchcode , ");
      sql.append(" creadate dstartdate, ");
      sql.append(" whs_stocktonnage num ,");
      sql.append(" whs_stockpieces bnum ,");
      sql.append(" ss_pk  status ");
      sql.append(" from tb_warehousestock ");
      sql.append(" where ");
      sql.append(" isnull(tb_warehousestock.dr,0) = 0 and whs_stocktonnage > 0 ");
      sql.append(" and tb_warehousestock.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");
      
      if(getQuerySQL()!=null || getQuerySQL().length()!=0)
      sql.append(" and "+getQuerySQL());		
	  return sql.toString();
	}
	/**
	 * 设置到ui界面之前 处理分组查询后的数据
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{		
		return  super.dealBeforeSetUI(list);
	}
	
	
	private ClientQueryDlg m_qryDlg=null;
	public QueryDLG getQueryDlg() {
		if (m_qryDlg == null) {
			m_qryDlg = new ClientQueryDlg(					
					this, _getModelCode(), "pk_stordoc","pk_cargdoc");
			m_qryDlg.setTempletID(_getCorpID(), _getModelCode(), _getUserID(),
					null);
		}
		return m_qryDlg;
	}
	/**
	 * 查询完成 设置到ui界面之后 后续处理  
	 * @author mlr
	 * @说明：（鹤岗矿业）
	 * 2011-12-22上午10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
       super.dealQueryAfter();
	}		
	/**
	 * 设置报表功能节点号
	 */
	public String _getModelCode() {
		return "80100206";
	}

}
