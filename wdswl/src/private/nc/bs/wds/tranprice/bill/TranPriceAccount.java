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
 * 运费核算后台类
 * @author Administrator
 *
 */
public class TranPriceAccount {
	// <存货管理id,存货装卸价格定义VO>
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
	 * @作者：lyf
	 * @说明：完达山物流项目 
	 *  计算运费
	 * @时间：2011-5-19下午08:34:49
	 * @param billvo
	 * @return
	 * @throws BusinessException
	 */
	public SendBillVO colTransCost(SendBillVO billvo) throws BusinessException{
		if(billvo == null || billvo.getBodyVos() == null || billvo.getBodyVos().length ==0 )
			throw new BusinessException("传入数据异常");
		TranPriceColFactory colfac = new TranPriceColFactory(billvo);
		return colfac.col();
	}

	/**
	 * 
	 * @throws BusinessException 
	 * @作者：lyf
	 * @说明：完达山物流项目 运费计算完成，回写出入库单装运费计算完成标识
	 * 只有参照新增和作废的时候需要回写
	 * @时间：2011-5-17下午08:02:04
	 */
	public void writeBack(AggregatedValueObject billvo) throws BusinessException {
		if (billvo == null)
			return;
		CircularlyAccessibleValueObject[] b1vos = billvo.getChildrenVO();
		if(b1vos == null || b1vos.length ==0)
			return;
	   //过滤来源单据表
		ArrayList<String> csourcebillhidout = new ArrayList<String>();// 出库单来源单据表头id
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
