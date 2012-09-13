package nc.ui.wds.newreport.report1;
import java.util.List;
import nc.ui.pub.ClientEnvironment;
import nc.ui.trade.report.query.QueryDLG;
import nc.vo.pub.query.ConditionVO;
import nc.vo.zmpub.pub.report.ReportBaseVO;
import nc.vo.zmpub.pub.report2.ZmReportBaseUI3;
/**
 * ���ɽ����ִ�������
 * @author mlr
 */
public class ReprotUI extends ZmReportBaseUI3{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8264102894793741723L;
	/**
	 * ���ղ�ѯ�����sql
	 * 
	 * @author mlr
	 * @˵�������׸ڿ�ҵ�� 2011-12-22����10:41:05
	 * @return
	 */
	public String[] getSqls() throws Exception {
		return new String[]{getSql(),getSql1()};
	}
	/**
	 * ��ѯ����ִ���sql
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-5����10:06:38
	 * @return
	 * @throws Exception 
	 */
	private String getSql() throws Exception {
      StringBuffer sql=new StringBuffer();
      sql.append(" select tb_warehousestock.pk_corp , ");
      sql.append(" pk_customize1 pk_stordoc, ");
      sql.append(" pk_cargdoc ,");
      sql.append(" tb_warehousestock.pk_invmandoc ,");
      sql.append(" tb_warehousestock.pk_invbasdoc ,");
      sql.append(" whs_batchcode , ");
      sql.append(" creadate dstartdate, ");
      sql.append(" whs_stocktonnage num ,");
      sql.append(" whs_stockpieces bnum ,");
      sql.append(" ss_pk  status ");
      sql.append(" from tb_warehousestock ");
      sql.append(" join wds_invbasdoc on tb_warehousestock.pk_invmandoc=wds_invbasdoc.pk_invmandoc ");
      sql.append(" where ");
      sql.append(" isnull(tb_warehousestock.dr,0) = 0   and whs_stocktonnage > 0 ");
      sql.append(" and isnull(wds_invbasdoc.dr,0)=0  and coalesce(wds_invbasdoc.uisso,'N')='Y' ");
      sql.append(" and tb_warehousestock.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");     
      if(getQuerySQL()!=null && getQuerySQL().length()!=0)
      sql.append(" and "+getQuerySQL());		
	  return sql.toString();
	}
	
	/**
	 * ��ѯ����ִ���sql
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-5����10:06:38
	 * @return
	 * @throws Exception 
	 */
	private String getSql1() throws Exception {
      StringBuffer sql=new StringBuffer();
      sql.append(" select pk_corp , ");
      sql.append(" pk_invmandoc ,");
      sql.append(" pk_invbasdoc ");
      sql.append(" from wds_invbasdoc ");
      sql.append(" where ");
      sql.append(" isnull(wds_invbasdoc.dr,0) = 0 and coalesce(uisso,'N')='Y' ");
      sql.append(" and wds_invbasdoc.pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPrimaryKey()+"'");     
      String wsql=getwheresql();//ֻ��ȡ���ڴ���Ĳ�ѯ����     
      if(wsql!=null && wsql.length()!=0)
      sql.append(" and "+wsql);		
	  return sql.toString();
	}
	
	
	/**
	 * ֻ��ȡ���ڴ���Ĳ�ѯ����
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-10����10:25:17
	 * @return
	 */
	private String getwheresql() {
	  	QueryDLG querylg= getQueryDlg();//��ȡ��ѯ�Ի���		
		ConditionVO[] vos=querylg.getConditionVO();//��ȡ�ѱ��û���д�Ĳ�ѯ����		
		ConditionVO vo=null;  
		   if(vos==null || vos.length==0)
			   return null;
		   for(int i=0;i<vos.length;i++){		   
		      if(vos[i].getFieldCode().equals("pk_invmandoc")){
		    	  vo=vos[i];
		      }
		      
		   }
	    if(vo==null){
	    	return null;
	    }
		String wsql=querylg.getWhereSQL(new ConditionVO[]{vo});		
		return wsql;
	}
	/**
	 * ���õ�ui����֮ǰ ���������ѯ�������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
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
					this, _getModelCode(), "pk_customize1","pk_cargdoc");
			m_qryDlg.setTempletID(_getCorpID(), _getModelCode(), _getUserID(),
					null);
		}
		m_qryDlg.setNormalShow(false);
		return m_qryDlg;
	}
	/**
	 * ��ѯ��� ���õ�ui����֮�� ��������  
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public void dealQueryAfter() throws Exception{		
       super.dealQueryAfter();
	}		
	/**
	 * ���ñ������ܽڵ��
	 */
	public String _getModelCode() {
		return "80100206";
	}

}