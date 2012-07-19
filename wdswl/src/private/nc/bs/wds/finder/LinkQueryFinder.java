package nc.bs.wds.finder;
import nc.vo.scm.constant.ScmConst;
import nc.vo.wl.pub.IBillDataFinder2;
import nc.vo.wl.pub.Wds2WlPubConst;
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
		}else if(WdsWlPubConst.WDS5.equals(type)){//�����˵�
			return new String[]{WdsWlPubConst.BILLTYPE_SALE_OUT};
		}else if(WdsWlPubConst.BILLTYPE_SALE_OUT.equals(type)){//���۳���
			return new String[]{WdsWlPubConst.WDSO,WdsWlPubConst.WDSF};//���۳���ش���
		}else if(WdsWlPubConst.WDSC.equals(type)){//�ɹ�ȡ��
			return new String[] {WdsWlPubConst.BILLTYPE_OTHER_OUT};
		}else if(WdsWlPubConst.WDSF.equals(type)){//װж�Ѻ���
			return null;
		}else if(WdsWlPubConst.WDSO.equals(type)){//���۳���ش���
			return new String[]{"4C"};//���۳��ⵥ
		}else if(WdsWlPubConst.BILLTYPE_ALLO_IN.equals(type)){//wds�������
			return new String[] {WdsWlPubConst.WDSP,WdsWlPubConst.WDSF,Wds2WlPubConst.billtype_alloinsendorder};
		}else if(WdsWlPubConst.WDSP.equals(type)){//wds�������ش���
			return new String[] {WdsWlPubConst.GYL4E};
		}else if(WdsWlPubConst.BILLTYPE_OUT_IN.equals(type)){//�˻����
			return new String[]{WdsWlPubConst.WDSF,"4C"};//���۳��ⵥ
		}else if(WdsWlPubConst.BILLTYPE_OTHER_OUT.equals(type)){//��������
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4I,WdsWlPubConst.BILLTYPE_OTHER_IN};//ERP�������⣬�����������
		}else if(WdsWlPubConst.BILLTYPE_OTHER_IN.equals(type)){//�������
			return new String[]{WdsWlPubConst.WDSF,WdsWlPubConst.GYL4A};//���۳��ⵥ
		}else if(WdsWlPubConst.HWTZ.equals(type)){//��λ������
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};//��������
		}
		
//		zhf add
		else if(Wds2WlPubConst.billtype_statusupdate.equals(type)){//���״̬�����
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT};//��������
		}
		else if(WdsWlPubConst.WDSS.equals(type)){//����ҵ��
			return new String[]{WdsWlPubConst.BILLTYPE_OTHER_OUT,WdsWlPubConst.BILLTYPE_OTHER_IN};//��������
		}
		else if(WdsWlPubConst.WDSG.equals(type)){//�����˵�
			return new String[]{WdsWlPubConst.BILLTYPE_ALLO_OUT};//��������
		}
		else if(WdsWlPubConst.BILLTYPE_ALLO_OUT.equals(type)){//��������
			return new String[]{WdsWlPubConst.WDSX,WdsWlPubConst.WDSF};//��������
		}
		else if(WdsWlPubConst.WDSX.equals(type)){//��������ش�
			return new String[]{ScmConst.m_allocationOut};//erp��������
		}
//		zhf end
		
		
		return null;
	}

}
