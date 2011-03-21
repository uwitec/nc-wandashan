package nc.itf.ncblk.yjcb;

import nc.ui.ncblk.exception.MySrvException;
import nc.vo.bd.CorpVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;

//�½�ɱ�����
public interface IYjcbSrv {
	
	// ÿ�³ɱ��������
	public boolean yjcbjsSrv(UserVO userVo, CorpVO corpVo, UFDate ufdate,
			NcReportYjcbVO pvo, boolean isOne) throws MySrvException;
    
	// added by zjj 2010-05-18
	// �������
	public boolean yjcbTz(double jine,NcReportYjcbVO pvo) throws MySrvException;
	 
	// added by zjj 2010-05-18
	// ȷ�Ϻ󱣴浽�±�
	
	public boolean yjcbQr(NcReportYjcbVO pvo) throws MySrvException;
	
	//ͬ��blk_sfccb��
	public boolean yjcbSynDel(String year, String month)throws MySrvException;
	
}
