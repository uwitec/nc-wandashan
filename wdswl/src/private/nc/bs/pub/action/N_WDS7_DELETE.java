package nc.bs.pub.action;
import java.util.Hashtable;
import nc.bs.ic.pub.IcInPubBO;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.trade.pub.IBDACTION;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 其他入库
 * 
 * @author mlr
 * 
 */
public class N_WDS7_DELETE extends AbstractCompiler2 {
	private java.util.Hashtable m_methodReturnHas = new java.util.Hashtable();
	private Hashtable m_keyHas = null;
	private BillStockBO1 stock = null;

	private BillStockBO1 getStock() {
		if (stock == null) {
			stock = new BillStockBO1();
		}
		return stock;
	}

	public N_WDS7_DELETE() {
		super();
	}

	/*
	 * 备注：平台编写规则类 接口执行类
	 */
	public Object runComClass(PfParameterVO vo) throws BusinessException {
		try {
			super.m_tmpVo = vo;
			// ####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
			Object retObj = null;

			AggregatedValueObject bill = getVo();
			// 进行数据基本校验
			check(bill);

			// 进行数据回写
			IcInPubBO bo = new IcInPubBO();
			bo.writeBackForInBill((OtherInBillVO) bill, IBDACTION.DELETE); // 参照情况，[回写本地其他出库]

			// 更新现存量
			getStock().updateStockByBill(bill,
					WdsWlPubConst.BILLTYPE_OTHER_IN_1);

			// 进行单据删除操作
			retObj = runClass("nc.bs.trade.comdelete.BillDelete", "deleteBill",
					"nc.vo.pub.AggregatedValueObject:01", vo, m_keyHas,
					m_methodReturnHas);// 方法说明:行业公共删除

			return retObj;
		} catch (Exception ex) {
			if (ex instanceof BusinessException)
				throw (BusinessException) ex;
			else
				throw new PFBusinessException(ex.getMessage(), ex);
		}

	}

	/**
	 * 单据基本校验
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 * @时间：2012-6-26上午11:22:26
	 * @param bill
	 * @throws BusinessException
	 */
	private void check(AggregatedValueObject bill) throws BusinessException {
		if (bill == null || bill.getParentVO() == null) {
			throw new BusinessException("传入数据为空");
		}

		TbGeneralBVO[] bodys = (TbGeneralBVO[]) bill.getChildrenVO();
		if (bodys == null || bodys.length == 0) {
			throw new BusinessException("传入数据为空");
		}

	}

	/*
	 * 备注：平台编写原始脚本
	 */
	public String getCodeRemark() {
		return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\nObject retObj  =null;\n//方法说明:行业公共删除\nretObj  =runClassCom@ \"nc.bs.trade.comdelete.BillDelete\", \"deleteBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n//##################################################\nreturn retObj;\n";
	}

	/*
	 * 备注：设置脚本变量的HAS
	 */
	private void setParameter(String key, Object val) {
		if (m_keyHas == null) {
			m_keyHas = new Hashtable();
		}
		if (val != null) {
			m_keyHas.put(key, val);
		}
	}
}
