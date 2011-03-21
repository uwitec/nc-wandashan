package nc.itf.ncblk.yjcb;

import nc.ui.ncblk.exception.MySrvException;
import nc.vo.bd.CorpVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;

//月结成本服务
public interface IYjcbSrv {
	
	// 每月成本计算服务
	public boolean yjcbjsSrv(UserVO userVo, CorpVO corpVo, UFDate ufdate,
			NcReportYjcbVO pvo, boolean isOne) throws MySrvException;
    
	// added by zjj 2010-05-18
	// 调整金额
	public boolean yjcbTz(double jine,NcReportYjcbVO pvo) throws MySrvException;
	 
	// added by zjj 2010-05-18
	// 确认后保存到新表
	
	public boolean yjcbQr(NcReportYjcbVO pvo) throws MySrvException;
	
	//同步blk_sfccb表
	public boolean yjcbSynDel(String year, String month)throws MySrvException;
	
}
