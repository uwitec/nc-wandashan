package nc.bs.wds.ic.write.back4c1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.wl.pub.WdsWlIcPubDealTool;
import nc.itf.ic.pub.IGeneralBill;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.uif.pub.exception.UifException;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.QryConditionVO;
import nc.vo.ic.pub.locator.LocatorVO;
import nc.vo.ic.pub.smallbill.SMGeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.scm.pub.vosplit.SplitBillVOs;
import nc.vo.to.pub.BillHeaderVO;
import nc.vo.to.pub.BillItemVO;
import nc.vo.to.pub.BillVO;
import nc.vo.wds.ic.write.back4c.MultiBillVO;
import nc.vo.wds.ic.write.back4c.Writeback4cB1VO;
import nc.vo.wds.ic.write.back4c.Writeback4cB2VO;
import nc.vo.wds.ic.write.back4c.Writeback4cHVO;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.report2.CombinVO;
/**
 * ��������ش���������
 * @author mlr
 */
public class SoBackBO{
	private  String beanName = IGeneralBill.class.getName(); 

	private Map<String,ArrayList<LocatorVO>> l_map =  new HashMap<String,ArrayList<LocatorVO>>();	
	BaseDAO dao = null;
	private String corp=null;
	private String coperator = null;
	private String date = null;
	
	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	/**
	 * ���� ��������ش� ����Ӧ��������������ݽ��� �� �������ɹ�Ӧ���ĵ������ⵥ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-12����10:56:07
	 * @param billvo
	 * @throws Exception 
	 */
	public void  changeTo4Y(PfParameterVO vo) throws Exception{
		MultiBillVO billvo =(MultiBillVO) vo.m_preValueVo;
		if(billvo==null)
			throw new Exception("����Ϊ��");
	    if(billvo.getHeadVO()==null)
	    	throw new Exception("��ͷΪ��");
		if(billvo.getTableVO(billvo.getTableCodes()[0])==null|| billvo.getTableVO(billvo.getTableCodes()[0]).length==0)
			throw new Exception("����Ϊ��");
		if(billvo.getTableVO(billvo.getTableCodes()[1])==null|| billvo.getTableVO(billvo.getTableCodes()[1]).length==0)
			throw new Exception("����Ϊ��");
		//��õ�������
		Writeback4cHVO hvo=billvo.getHeadVO();		
		String vcode =hvo.getCsaleid();//��õ�������id
	    BillVO bill=getSaleOrder(vcode);
	    //���зֵ� �������ֿ�  ����ֿ�ֵ� ��Ϊ�������ⵥ�ֿ���Ϣ�ڱ�ͷ �����������ֿ���Ϣ�ڱ���
	    BillVO[] bills= (BillVO[]) SplitBillVOs.getSplitVO(
	    		BillVO.class.getName(), BillHeaderVO.class.getName(), 
	    		BillItemVO.class.getName(), bill, null, new String[]{"coutwhid","cinwhid"});
	    
		//�������ݽ���  ��õ�������
		PfParameterVO paraVo = new PfParameterVO();
		paraVo.m_operator =vo.m_operator;
		paraVo.m_coId = vo.m_coId;		
		paraVo.m_currentDate = vo.m_currentDate;
		paraVo.m_preValueVos=bills;
		
	    corp=vo.m_operator;
		coperator = vo.m_coId;
		date = vo.m_currentDate;
		GeneralBillVO[] orderVos = (GeneralBillVO[]) PfUtilTools
				.runChangeDataAry(WdsWlPubConst.WDSX, "4Y",
						bills, paraVo);
	   //�������ݹ���   ����
		deal(orderVos,billvo);
		for (int i = 0; i < orderVos.length; i++) {
			if(orderVos[i].getChildrenVO()==null || orderVos[i].getChildrenVO().length==0)
				continue;
			// ִ�б������
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator
					.getInstance().lookup(IPFBusiAction.class.getName());
			ArrayList retList = (ArrayList) bsBusiAction.processAction("SAVE",
					"4Y", date, null, orderVos[i], null, null);
			//ִ��ǩ�ֲ���
			SMGeneralBillVO smbillvo = (SMGeneralBillVO) retList.get(2);
			orderVos[i].setSmallBillVO(smbillvo);
			//ǩ�ּ�� <->[ǩ�����ںͱ���ҵ������]
			//��ǰ������<->[ҵ�������������ǰ������Ա]
			//�ջ�λ��� bb1��
			bsBusiAction.processAction("SIGN", "4Y",date,null,orderVos[i], null,null); //ǩ�ֺ����ſ�
		}
	//	throw new Exception("css");


	}
	/**
	 * ���������� ->��Ӧ���ĵ������ⵥ ������ĵ���vo  ����Ӧ������ �� ʵ������ �����û�λ��Ϣ
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-12����12:12:28
	 * @param orderVos
	 * @param billvo
	 * @throws UifException 
	 */
	public void deal(GeneralBillVO[] orderVos, MultiBillVO billvo) throws UifException {
		if(orderVos==null  || orderVos.length==0){
			return;
		}
		String pk=billvo.getHeadVO().getPrimaryKey();
		for (int k = 0; k < orderVos.length; k++) {
			GeneralBillVO bill = orderVos[k];
			bill.getParentVO().setStatus(VOStatus.NEW);
			GeneralBillItemVO[] vos = bill.getItemVOs();
			if (vos == null || vos.length == 0) {
				return;
			}

			Writeback4cB1VO[] bvos = (Writeback4cB1VO[]) billvo
					.getTableVO(billvo.getTableCodes()[0]);


			for (int i = 0; i < vos.length; i++) {
				GeneralBillItemVO vo = vos[i];
				String orderbid = vo.getCsourcebillbid();// ȡ�õ�����������
				for (int j = 0; j < bvos.length; j++) {
					if (orderbid.equals(bvos[j].getCorder_bid())) {
						// ����Ӧ������
						vo.setNshouldoutnum(bvos[j].getNoutnum());
						vo.setNshouldoutassistnum(bvos[j].getNpacknumber());
						vo.setVbatchcode(getDefaultCode());
						// ����ʵ������
						vo.setNoutnum(bvos[j].getNoutnum());
						vo.setNoutassistnum(bvos[j].getNpacknumber());
						vo.setStatus(VOStatus.NEW);
						vo.setAttributeValue(WdsWlPubConst.csourcehid_wds, pk);
					}
				}
			}
			GeneralBillItemVO[] voss = flterNull(vos);
			bill.setChildrenVO(voss);		
		}
		setLocatorVO(orderVos, billvo);
	}
	/**
	 * ��������Ϊ�յ���
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-13����10:46:21
	 * @param vos
	 */
	private GeneralBillItemVO[] flterNull(GeneralBillItemVO[] vos) {
		if(vos==null || vos.length==0){
			return null;
		}
		ArrayList<GeneralBillItemVO> list=new ArrayList<GeneralBillItemVO>();
		for(int i=0;i<vos.length;i++){
			GeneralBillItemVO vo=vos[i];
			UFDouble uf=PuPubVO.getUFDouble_NullAsZero(vo.getNoutnum());
			if(uf.doubleValue()>0){
				list.add(vo);
			}
		}	
		return list.toArray(new GeneralBillItemVO[0]);
	}
	/**
	 * 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ ��ѯ �������ⵥ�� ��λ��Ϣ
	 * @ʱ�䣺2011-11-3����11:30:04
	 * @param value
	 * @throws UifException
	 */
	public void setLocatorVO(GeneralBillVO[] orderVos, MultiBillVO billvo) throws UifException {
		if(billvo==null || billvo.getParentVO()== null ){
			return;
		}
		if(orderVos==null || orderVos.length==0)
			return ;
		for (int i = 0; i < orderVos.length; i++) {
			GeneralBillVO gbillvo = orderVos[i];
			if (gbillvo == null || gbillvo.getHeaderVO() == null
					|| gbillvo.getChildrenVO() == null
					|| gbillvo.getChildrenVO().length == 0)
				return;

			Writeback4cB1VO[] bvos = (Writeback4cB1VO[]) billvo
					.getTableVO(billvo.getTableCodes()[0]);
			Writeback4cB2VO[] bvos1 = (Writeback4cB2VO[]) billvo
					.getTableVO(billvo.getTableCodes()[1]);
			// ������id �� ��λid �ϲ����� ��Ϊ��Ӧ�� ����ⵥ������ ����ͬһ����¼ �������ظ���λ
			Writeback4cB2VO[] zbvos = (Writeback4cB2VO[]) CombinVO.combinData(
					bvos1, new String[] { "cfirstbillbid", "vdef4" },
					new String[] { "noutnum", "noutassistnum" },
					Writeback4cB2VO.class);
			// �õ���λ��Ϣ
			for (Writeback4cB2VO bvo : zbvos) {
				String key = bvo.getCfirstbillbid();// ���۶������� id
				LocatorVO lvo = new LocatorVO();
				lvo.setPk_corp(corp);
				lvo.setNoutspacenum(bvo.getNoutnum());
				lvo.setNoutspaceassistnum(bvo.getNoutassistnum());
				lvo.setCspaceid(bvo.getVdef4());// ��λ
				lvo.setStatus(VOStatus.NEW);
				if (l_map.containsKey(key)) {
					l_map.get(key).add(lvo);
				} else {
					ArrayList<LocatorVO> zList = new ArrayList<LocatorVO>();
					zList.add(lvo);
					l_map.put(key, zList);
				}
			}
			// ���û�λ��Ϣ
			GeneralBillItemVO[] gbvos = gbillvo.getItemVOs();
			for (int j = 0; j < gbvos.length; j++) {
				GeneralBillItemVO vo = gbvos[j];
				ArrayList<LocatorVO> zList = l_map.get(vo.getCsourcebillbid());
				if (zList == null || zList.size() == 0)
					throw new UifException("��λ��ϢΪ��");
				vo.setLocator(zList.toArray(new LocatorVO[0]));
			}
		}
		
	}
	/** 
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-13����09:33:44
	 * @return
	 */
	public String getDefaultCode() {
		return WdsWlIcPubDealTool.getDefaultVbatchCode(corp);
	}
	/**
	 * ���ݵ����������� ��ѯ��������
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-12����11:05:27
	 * @param primarykey
	 * @return
	 * @throws BusinessException
	 */
	public BillVO getSaleOrder(String primarykey) throws BusinessException{
		if(primarykey==null){
			primarykey ="";
		}
		BillVO bill = new BillVO();
		String headSql = " select * from to_bill where isnull(dr,0)=0 and cbillid='"+primarykey+"'";
		ArrayList<BillHeaderVO> headList= (ArrayList<BillHeaderVO>)getBaseDAO().executeQuery(headSql, new BeanListProcessor(BillHeaderVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("��ѯ��������ʧ�� ");
		}
		String bodySql = " select * from to_bill_b where isnull(dr,0)=0 and cbillid='"+primarykey+"' and coutwhid is not null";
		ArrayList<BillItemVO> bodyList= (ArrayList<BillItemVO>)getBaseDAO().executeQuery(bodySql, new BeanListProcessor(BillItemVO.class));
		if(headList == null || headList.size() ==0){
			throw new BusinessException("��ѯ����������������ʧ�� ");
		}
		bill.setParentVO(headList.get(0));
		bill.setChildrenVO(bodyList.toArray(new BillItemVO[0]));
		return bill;
	}
	/**
	 * @���ܣ�ȡ��ǩ�ֶ���
	 */
	public GeneralBillVO[] canelSignQueryGenBillVO(AggregatedValueObject bill,String coperator,String date) throws Exception {
		if(bill==null){
			return null;
		}
		this.coperator = coperator;
		this.date = date;
		MultiBillVO billvo = (MultiBillVO)bill;
		Writeback4cHVO hvo = (Writeback4cHVO)billvo.getParentVO();
		String csaleid = hvo.getPrimaryKey()==null ?"":hvo.getPrimaryKey();
		String where  = "body."+WdsWlPubConst.csourcehid_wds+"='"+csaleid+"' ";
		QryConditionVO voCond = new QryConditionVO(where);
	    ArrayList alListData = (ArrayList)queryBills("4Y", voCond);
	    GeneralBillVO[] gbillvo = null;
		if(alListData!=null && alListData.size()>0){
			for(int i = 0;i<alListData.size();i++){
				GeneralBillVO gvo = (GeneralBillVO)alListData.get(i);
				gvo.getHeaderVO().setCoperatoridnow(coperator);
				gvo.getHeaderVO().setDaccountdate(new UFDate(date));
				gvo.getHeaderVO().setClogdatenow(date);
			}
			gbillvo = (GeneralBillVO[])alListData.toArray(new GeneralBillVO[0]);
		}
		return gbillvo;
	}
	public void canelPushSign4Y(String date, AggregatedValueObject[] billvo)
			throws Exception {
		// ȡ�����۳���ǩ��
		if (billvo != null && billvo[0] != null
				&& billvo[0] instanceof GeneralBillVO) {
			IPFBusiAction bsBusiAction = (IPFBusiAction) NCLocator
					.getInstance().lookup(IPFBusiAction.class.getName());
			for (int i = 0; i < billvo.length; i++) {
				//ȡ��ǩ��
				ArrayList retList = (ArrayList) bsBusiAction.processAction(
						"CANCELSIGN", "4Y", date, null, billvo[i], null,
						null);
				//����ɾ��
				if (retList.get(0) != null && (Boolean) retList.get(0)) {
					bsBusiAction.processAction("DELETE", "4Y", date,
							null, billvo[i], null, null);
				}
			}
		}
	}
	public  ArrayList queryBills(String arg0 ,QryConditionVO arg1 ) throws Exception{
		IGeneralBill bo = (IGeneralBill)NCLocator.getInstance().lookup(beanName);    
		ArrayList o =  bo.queryBills(arg0 ,arg1 );					
		return o;
	}
}
