package nc.bs.wds.tranprice.bill;

import java.util.ArrayList;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.SQLParameter;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.wds.load.LoadpriceVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.tranprice.bill.SendBillVO;

/**
 * �˷Ѻ����̨��
 * @author Administrator
 *
 */
public class TranPriceAccount {
	// <�������id,���װж�۸���VO>
	Map<String, LoadpriceVO> invLoadPrice = null;

	ArrayList<LoadpriceB1VO> list = new ArrayList<LoadpriceB1VO>();

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ 
	 *  �����˷�
	 * @ʱ�䣺2011-5-19����08:34:49
	 * @param billvo
	 * @return
	 * @throws BusinessException
	 */
	public SendBillVO colTransCost(SendBillVO billvo) throws BusinessException{
		if(billvo == null || billvo.getBodyVos() == null || billvo.getBodyVos().length ==0 )
			throw new BusinessException("���������쳣");
		TranPriceColFactory colfac = new TranPriceColFactory(billvo);
		return colfac.col();
	}

	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ �˷Ѽ�����ɣ���д����ⵥװ�˷Ѽ�����ɱ�ʶ
	 * ֻ�в������������ϵ�ʱ����Ҫ��д
	 * @ʱ�䣺2011-5-17����08:02:04
	 */
	public void writeBack(AggregatedValueObject billvo) throws BusinessException {
		if (billvo == null)
			return;
		CircularlyAccessibleValueObject[] b1vos = billvo.getChildrenVO();
		if(b1vos == null || b1vos.length ==0)
			return;
	   //������Դ���ݱ�
		ArrayList<String> csourcebillhidout = new ArrayList<String>();// ���ⵥ��Դ���ݱ�ͷid
		for (CircularlyAccessibleValueObject b1vo : b1vos) {
			String csourcetype = (String) b1vo.getAttributeValue("csourcetype");
			if (csourcetype == null || csourcetype.equals(""))
				continue;
			String cousbillhid = (String)b1vo.getAttributeValue("csourcebillhid");
			if(csourcebillhidout.contains(cousbillhid))
				continue;
			csourcebillhidout.add(cousbillhid);
		}
		String key = billvo.getParentVO().getPrimaryKey();
		String vaule = null;
		if( key == null || "".equalsIgnoreCase(key)){
			vaule ="Y";
		}else{
			vaule ="N";
		}
		String outSql =" update tb_outgeneral_h set fistran='"+vaule+"' where general_pk=?";
		SQLParameter parameter = new SQLParameter();
		for(int i=0;i<csourcebillhidout.size();i++){
			parameter.addParam(csourcebillhidout.get(i));
			getBaseDAO().executeUpdate(outSql, parameter);
			parameter.clearParams();
		}
	}



}
