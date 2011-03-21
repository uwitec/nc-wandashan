package nc.bs.ncblk.dmo;

import java.util.List;

import nc.bs.ncblk.exception.MyDMOException;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;

//�½�ɱ�DMO
public interface IyjcbDMO {

	// ɾ���½�ɱ������ӱ�����
	public boolean deleteNcReportYjcbBVO(String whereStr) throws MyDMOException;

	// �����ɱ������ӱ�
	public boolean saveAllNcReportYjcbBVOList(List yjcbVoList)
			throws MyDMOException;

	// ��������VO
	public boolean updateNcReportYjcbVO(NcReportYjcbVO pvo)
			throws MyDMOException;

	// ��ȡ���й�˾����ǰ��ݡ���ǰ�·��£�ָ������������˹��ĵĺϼ����ݡ�
	public List queryIaBillVoList(NcReportYjcbVO pvo, String billType)
			throws MyDMOException;

	// ���ݱ�ͷ��ѯ��������ȡһ���ۺ�VO
	public MyBillVO queryMyBillVO(String djnd, String djqj)
			throws MyDMOException;
	
	// ���ݱ�ͷ��ѯ��������ȡһ���ۺ�VO
	public MyBillVO queryMyBillVO_2(String djnd, String djqj)
			throws MyDMOException;
	
	// added by zjj 2010-05-19
	// ȡ�õ�ǰ����
	public List<NcReportYjcbBVO> getXsList(NcReportYjcbVO nj) throws MyDMOException;
	
	// added by zjj 2010-05-19
	// ��ϱ���blk_sfccb��
	public boolean saveSfccb(NcReportYjcbVO nj , List<NcReportYjcbBVO> list) throws MyDMOException;
	// added by zjj 2010-05-21
	// ��ȡ���й�˾����ǰ��ݡ���ǰ�·��£�ָ������������˹��ĵĺϼ����ݡ�
	public List<NcReportYjcbBVO> queryIaBillVoList2(NcReportYjcbVO pvo, String billType) throws MyDMOException;
   //���µ�����������ݿ�
	public boolean updateVolist(List<NcReportYjcbBVO> list) throws MyDMOException;
	//ͬ��ɾ��blk_sfccb��
	public boolean deleteBlk(String year,String month)throws MyDMOException;
	
}
