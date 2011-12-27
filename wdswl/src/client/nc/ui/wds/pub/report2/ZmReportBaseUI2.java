package nc.ui.wds.pub.report2;
import java.util.List;
import java.util.Map;
import nc.bs.logging.Logger;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.wl.pub.LongTimeTask;
import nc.vo.pub.BusinessException;
import nc.vo.wl.pub.report.ReportBaseVO;
/**
 * ֧�ַ����ѯ�ı������
 * ֧�����ݽ���  �ּ��ε�С�� �ϼ�  ���� ����  ˢ�� �Լ���ӡ
 * ֧�ֲ�ѯ��̬�� �� ��ʼ����̬��
 * @author mlr
 *
 */
public class ZmReportBaseUI2 extends JxReportBaseUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2436203500270302801L;
	
	private String[] sqls=null;

	@Override
	public Map getNewItems() throws Exception {
		return null;
	}

	@Override
	public String getQuerySQL() throws Exception {
		return null;
	}

	@Override
	public void initReportUI() {

	}
	  @Override
		public void onQuery() {
			getQueryDlg().showModal();
			if (getQueryDlg().getResult() == UIDialog.ID_OK) {
				try {
					// ��ձ�������
					clearBody();
					setDynamicColumn1();
					// �õ���ѯ���
				  	   setColumn();

					List<ReportBaseVO[]> list = getReportVO(getSqls());
                    sqls=getSqls();
					ReportBaseVO[] vos = null;
					vos=dealBeforeSetUI(list);
					if (vos == null || vos.length == 0)
						return;
					if (vos != null) {
						super.updateBodyDigits();
						setBodyVO(vos);
						updateVOFromModel();//���¼��س�ʼ����ʽ  
						dealQueryAfter();//��ѯ��ĺ������� һ������ Ĭ�����ݽ���֮��Ĳ���
					}
				} catch (BusinessException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	/**
	 * ���ղ�ѯ�����sql
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:41:05
	 * @return
	 */
	public String[] getSqls()throws Exception{
		return null;
		
	}
	public void onboRefresh()throws Exception{
    	   //��ձ�������
  	   clearBody();	    	         	
    	  //���ö�̬��
  	  setDynamicColumn1();
        //�õ���ѯ���
    	  ReportBaseVO[] vos=null;
  	  //���û����кϲ�
  	   setColumn();
  	  //����vo
	  List<ReportBaseVO[]> list = getReportVO(getSqls());
      sqls=getSqls();
	  ReportBaseVO[] vos1 = null;
	  vos1=dealBeforeSetUI(list);
		if (vos1 == null || vos1.length == 0)
			return;
		if (vos1 != null) {
			super.updateBodyDigits();
			setBodyVO(vos1);
			updateVOFromModel();//���¼��س�ʼ����ʽ  
			dealQueryAfter();//��ѯ��ĺ������� һ������ Ĭ�����ݽ���֮��Ĳ���
		}
       this.showHintMessage("ˢ�²�������");
  }
	/**
	 * ���õ�ui����֮ǰ ��������ѯ�������
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 */
	public ReportBaseVO[] dealBeforeSetUI(List<ReportBaseVO[]> list)throws Exception{
		
		return null;
	}
	/**
	 * ��ѯ��� ���õ�ui����֮�� ��������  
	 * @author mlr
	 * @˵�������׸ڿ�ҵ��
	 * 2011-12-22����10:42:36
	 * @param list
	 * @return
	 */
	public void dealQueryAfter(){
		
		return ;
	}
	
	/**
	 * �����ѯ
	 */
	public List<ReportBaseVO[]> getReportVO(String[] sqls)
			throws BusinessException {
		List<ReportBaseVO[]> reportVOs = null;
		try {
			Class[] ParameterTypes = new Class[] { String[].class };
			Object[] ParameterValues = new Object[] { sqls };
			Object o = LongTimeTask.calllongTimeService(
					null, this, "���ڲ�ѯ...", 1,
					"nc.bs.ca.pub.report.ReportDMO", null, "queryVOBySql",
					ParameterTypes, ParameterValues);
			if (o != null) {
				reportVOs = (List<ReportBaseVO[]>) o;
			}
		} catch (Exception e) {
			Logger.error(e);
			MessageDialog.showErrorDlg(this, "����", e.getMessage());
		}
		return reportVOs;
	}

}
