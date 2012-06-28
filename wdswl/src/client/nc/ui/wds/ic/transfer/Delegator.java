package nc.ui.wds.ic.transfer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import nc.vo.ic.other.out.TbOutgeneralB2VO;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.other.out.TbOutgeneralTVO;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.pub.SuperVO;
import nc.vo.wds.transfer.TransferBVO;
/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class Delegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	public Hashtable loadChildDataAry(String[] tableCodes,String key)throws Exception{	
		// ��������������ȡ���ӱ������
		TbOutgeneralBVO[] b1vos = (TbOutgeneralBVO[]) this.queryByCondition(TransferBVO.class, "general_pk='" + key + "' and isnull(dr,0)=0");	
		Hashtable<String, SuperVO[]> dataHT = new Hashtable<String, SuperVO[]>();
		if (b1vos != null && b1vos.length > 0) {
			dataHT.put(tableCodes[0], b1vos);
		}
	    return dataHT;
	}
}