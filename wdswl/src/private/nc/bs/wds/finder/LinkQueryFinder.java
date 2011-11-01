package nc.bs.wds.finder;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.WdsWlPubConst;
public class LinkQueryFinder extends AbstractBillFinder2{
	public LinkQueryFinder() {
		super();
	}	
	public IBillDataFinder2 createBillDataFinder(String billType) throws Exception {
		return new WdsDataFinder2();
	}
	//ע�����ε�������
	public String[] getAllBillType() {
		String type = getCurrentvo().getType();
		if(WdsWlPubConst.WDS1.equals(type)){//���˼ƻ�¼��
			return new String[]{WdsWlPubConst.WDS3};
		}else if(WdsWlPubConst.WDS3.equals(type)){//���˶���
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(type)){//��������
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_IN};
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//�������
			return null;
		}else if(WdsWlPubConst.WDS5.equals(type)){//�����˵�
			return new String[]{WdsWlPubConst.BILLTYPE_SALE_OUT};
		}else if(WdsWlPubConst.BILLTYPE_SALE_OUT.equals(type)){//���۳���
			return new String[]{"4C"};//��Ӧ�����۳���
		}else if(WdsWlPubConst.WDSC.equals(type)){//�ɹ�ȡ��
			return new String[] {WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.WDSF.equals(type)){//װж�Ѻ���
			return null;
		}
		return null;
	}

}