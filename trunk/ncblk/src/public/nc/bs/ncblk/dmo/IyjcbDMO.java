package nc.bs.ncblk.dmo;

import java.util.List;

import nc.bs.ncblk.exception.MyDMOException;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;

//月结成本DMO
public interface IyjcbDMO {

	// 删除月结成本计算子表数据
	public boolean deleteNcReportYjcbBVO(String whereStr) throws MyDMOException;

	// 保存存成本计算子表
	public boolean saveAllNcReportYjcbBVOList(List yjcbVoList)
			throws MyDMOException;

	// 更新主表VO
	public boolean updateNcReportYjcbVO(NcReportYjcbVO pvo)
			throws MyDMOException;

	// 获取所有公司，当前年份、当前月份下，指定单据类型审核过的的合计数据。
	public List queryIaBillVoList(NcReportYjcbVO pvo, String billType)
			throws MyDMOException;

	// 根据表头查询条件，获取一个聚合VO
	public MyBillVO queryMyBillVO(String djnd, String djqj)
			throws MyDMOException;
	
	// 根据表头查询条件，获取一个聚合VO
	public MyBillVO queryMyBillVO_2(String djnd, String djqj)
			throws MyDMOException;
	
	// added by zjj 2010-05-19
	// 取得当前报表
	public List<NcReportYjcbBVO> getXsList(NcReportYjcbVO nj) throws MyDMOException;
	
	// added by zjj 2010-05-19
	// 组合保存blk_sfccb表
	public boolean saveSfccb(NcReportYjcbVO nj , List<NcReportYjcbBVO> list) throws MyDMOException;
	// added by zjj 2010-05-21
	// 获取所有公司，当前年份、当前月份下，指定单据类型审核过的的合计数据。
	public List<NcReportYjcbBVO> queryIaBillVoList2(NcReportYjcbVO pvo, String billType) throws MyDMOException;
   //更新调整金额后的数据库
	public boolean updateVolist(List<NcReportYjcbBVO> list) throws MyDMOException;
	//同步删除blk_sfccb表
	public boolean deleteBlk(String year,String month)throws MyDMOException;
	
}
