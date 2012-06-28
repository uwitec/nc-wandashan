package nc.vo.wds.transfer;

import nc.vo.ic.other.out.TbOutgeneralHVO;

/**
 * 转货位
 * @author yf
 */
public class TransferVO extends TbOutgeneralHVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8299919978628715989L;
	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2011-3-24
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "wds_transfer";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2011-3-24
	 */
	public TransferVO() {

		super();
	}

}
