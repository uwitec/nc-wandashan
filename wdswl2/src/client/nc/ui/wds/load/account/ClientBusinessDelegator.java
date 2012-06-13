package nc.ui.wds.load.account;

import java.util.Hashtable;

import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.vo.pub.SuperVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceB2VO;

public class ClientBusinessDelegator extends BusinessDelegator {
	
	public ClientBusinessDelegator() {
		super();
	}
	
	@Override
	public Hashtable loadChildDataAry(String[] tableCodes, String key) throws Exception {
		// ��������������ȡ���ӱ������
		LoadpriceB1VO[] b1vos = (LoadpriceB1VO[]) this.queryByCondition(LoadpriceB1VO.class, "pk_loadprice='" + key + "' and isnull(dr,0)=0");
	
		// ��������������ȡ���ӵ�����
		LoadpriceB2VO[] b2vos = (LoadpriceB2VO[]) this.queryByCondition(LoadpriceB2VO.class, "pk_loadprice='" + key + "' and isnull(dr,0)=0");
	
		
	
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
