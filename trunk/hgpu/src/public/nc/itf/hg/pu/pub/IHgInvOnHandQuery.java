package nc.itf.hg.pu.pub;

import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;

public interface IHgInvOnHandQuery {

	//获取指定单位 指定日期的存量   维度：公司+库存组织
	public void getOnHandNum(String sLogCorp,String sCalbody,String sWareHouse,UFDate denddate,String[] invmanids) throws BusinessException;
	
	//获取剩余数量     没到货的采购合同量
	public void getRemainNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
////	获取上年1-N消耗量
//	public void getLastCurrNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
//	获取上年度全年消耗量
	public void getLastAllNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
//	获取当前年度1-N 消耗量
	public void getCurrNum(String sLogCorp,UFDate dDate,String[] invmanids) throws BusinessException;
	
}
