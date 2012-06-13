package nc.ui.wds.ic.out.in;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import nc.ui.wds.ic.pub.WdsIcPubHelper;
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
public class OutInDelegator extends nc.ui.trade.bsdelegate.BusinessDelegator{
	/**
	 * queryBodyVOs ����ע�⡣
	 */
	public nc.vo.pub.CircularlyAccessibleValueObject[] queryBodyAllData(
			Class voClass, String billType, String key, String strWhere)
			throws Exception {
		return WdsIcPubHelper.queryIcInBillBodyAllData(key, strWhere);
	}

	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key)
			throws Exception {
		// ��������������ȡ���ӱ������
		TbGeneralBVO[] b1vos = (TbGeneralBVO[]) this.queryByCondition(TbGeneralBVO.class, "geh_pk='" + key + "' and isnull(dr,0)=0");	
		for(TbGeneralBVO body:b1vos){
			TbGeneralBBVO[] ctmps=(TbGeneralBBVO[]) this.queryByCondition(TbGeneralBBVO.class, " geb_pk = '"+body.getPrimaryKey()+"'");
			if(ctmps == null||ctmps.length==0)
				continue;
			List<TbGeneralBBVO> list=new ArrayList<TbGeneralBBVO>();
			list.addAll(Arrays.asList(ctmps));
			body.setTrayInfor(list);
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