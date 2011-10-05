package nc.ui.wds.ic.allocation.in;
import java.util.Arrays;
import java.util.Hashtable;
import nc.vo.ic.pub.TbGeneralBBVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbgeneralB2VO;
import nc.vo.pub.SuperVO;
/**
  *
  *����ҵ��������ȱʡʵ��
  *@author author
  *@version tempProject version
  */
public class AlloDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {
		// ��������������ȡ���ӱ������
		TbGeneralBVO[] b1vos = (TbGeneralBVO[]) this.queryByCondition(TbGeneralBVO.class, "geh_pk='" + key + "' and isnull(dr,0)=0");	
		for(TbGeneralBVO body:b1vos){
			TbGeneralBBVO[] ctmps=(TbGeneralBBVO[]) this.queryByCondition(TbGeneralBBVO.class, " geb_pk = '"+body.getPrimaryKey()+"'");
			if(ctmps == null||ctmps.length==0)
				continue;
			body.setTrayInfor(Arrays.asList(ctmps));
		}		
		// ��������������ȡ���ӵ�����
		TbgeneralB2VO[] b2vos = (TbgeneralB2VO[]) this.queryByCondition(TbgeneralB2VO.class, "geh_pk='" + key + "' and isnull(dr,0)=0");
		// ��ѯ���ݷ�Hashtable������
		Hashtable<String, SuperVO[]> dataHT = new Hashtable<String, SuperVO[]>();
		if (b1vos != null && b1vos.length > 0) {
			dataHT.put(tableCodes[0], b1vos);
		}
		if (b2vos != null && b2vos.length > 0) {
			dataHT.put(tableCodes[1], b2vos);
		}
	    return dataHT;
	}	
}