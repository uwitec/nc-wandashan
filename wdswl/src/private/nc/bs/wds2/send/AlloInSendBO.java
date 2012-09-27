package nc.bs.wds2.send;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.pub.pf.PfUtilBO;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.trade.business.HYPubBO;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.dm.order.SendorderBVO;
import nc.vo.dm.order.SendorderVO;
import nc.vo.ic.other.in.OtherInBillVO;
import nc.vo.ic.pub.TbGeneralBVO;
import nc.vo.ic.pub.TbGeneralHVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;
import nc.vo.wds.trans.TransVO;
import nc.vo.wl.pub.Wds2WlPubConst;
import nc.vo.wl.pub.WdsWlPubConst;
import nc.vo.zmpub.pub.tool.ResultSetProcessorTool;
import nc.vo.zmpub.pub.tool.ZmPubTool;
public class AlloInSendBO {	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null)
			dao = new BaseDAO();
		return dao;
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ������ⵥ ����ʱ���ɵ����˵� 
	 * @ʱ�䣺2012-7-10����12:51:26
	 * @param bill
	 * @throws BusinessException
	 */
	public void createAlloInSendBill(String headid,PfParameterVO para,boolean isnew) throws Exception{
		if(headid == null)
			return;

		//		��ѯ������ⵥ
		OtherInBillVO bill = (OtherInBillVO)new HYPubBO().queryBillVOByPrimaryKey(
				new String[]{OtherInBillVO.class.getName(),
						TbGeneralHVO.class.getName(),
						TbGeneralBVO.class.getName()}, headid);

		if(bill == null){
			throw new BusinessException("�����쳣");
		}
		
		if(!isnew){
			updateAlloInSendBill(bill);
		}
		
		//		ת������
		HYBillVO tarBill = (HYBillVO)PfUtilTools.runChangeData(WdsWlPubConst.BILLTYPE_ALLO_IN, 
				Wds2WlPubConst.billtype_alloinsendorder, bill,para);

		if(tarBill == null)
			throw new BusinessException("�����쳣,δ���ɵ����˵�");

		check(tarBill);
		
		//�����˷ѵļ���
		calTrans(tarBill);
		

		new PfUtilBO().processAction("WRITE", Wds2WlPubConst.billtype_alloinsendorder, 
				para.m_currentDate, null, tarBill, null);
	}
	/**
	 * �����˷�
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-9-26����11:06:29
	 * @param tarBill
	 * @throws Exception 
	 */
	public void calTrans(HYBillVO tarBill) throws Exception {
		// �˷Ѽ��� ����
		// �� ������˾ ����ֿ� ��ѯ�˼۱�
		// ���м�������˷� ���ݴ������ ѡȡ�˼۱� �����˷�
		// �˷Ѻϼ�
		if (tarBill == null || tarBill.getParentVO() == null
				|| tarBill.getChildrenVO() == null
				|| tarBill.getChildrenVO().length == 0) {
			return;
		}
		SuperVO headvo = (SuperVO) tarBill.getParentVO();
		SuperVO[] bodyvos = (SuperVO[]) tarBill.getChildrenVO();
		String outcorp = PuPubVO.getString_TrimZeroLenAsNull(headvo.getAttributeValue("vdef1"));// ������˾
		String instore = PuPubVO.getString_TrimZeroLenAsNull(headvo.getAttributeValue("pk_inwhouse"));// ����ֿ�
		if (outcorp == null) {
			throw new Exception("������˾Ϊ��");
		}
		if (instore == null) {
			throw new Exception("����ֿ�");
		}
		UFDouble total=new UFDouble(0);//����˷�
		for (int i = 0; i < bodyvos.length; i++) {
			SuperVO vo = bodyvos[i];
			String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_invmandoc"));// �õ������������
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_invbasdoc"));// �õ����������������
			UFDouble num=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("ninacceptnum"));//�õ��������
			// �õ��������
			Integer type = PuPubVO.getInteger_NullAs(
						ZmPubTool.execFomular(
											"tray_volume_layers->getColValue(wds_invbasdoc,db_waring_dyas2,pk_invmandoc,pk_invmandoc)",
											new String[] { "pk_invmandoc" },
											new String[] { pk_invmandoc }), -3);

			if (type <= 0) {
				String invcode = PuPubVO.getString_TrimZeroLenAsNull(
						ZmPubTool.execFomular(
										" invcode ->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,pk_invbasdoc)",
										new String[] { "pk_invbasdoc" },
										new String[] { pk_invbasdoc }));
				throw new Exception(" �������Ϊ ��" + invcode + " �������û��ά���������");
			}

			// ���� ������˾ ����ֿ� ������� �����˼۱�
			List list = (List) getDao().retrieveByClause(
					TransVO.class,
					" isnull(dr,0)=0 and pk_corp='" + SQLHelper.getCorpPk()
							+ "' and ss_custom2='" + outcorp + "'"
							+ "  and  ss_custom6='" + instore
							+ "' and ss_isout= " + type);
           			
			if (list == null || list.size() == 0) {
				String corp = PuPubVO.getString_TrimZeroLenAsNull(
						ZmPubTool.execFomular(
										"corpname->getColValue(bd_corp,unitname,pk_corp,pk_corp)",
										new String[] { "pk_corp" },
										new String[] { outcorp }));
				String store = PuPubVO.getString_TrimZeroLenAsNull(
						ZmPubTool.execFomular(
										"storname->getColValue(bd_stordoc,storname,pk_stordoc,pk_stordoc)",
										new String[] { "pk_stordoc" },
										new String[] { instore }));
				String typeerror = null;
				if (type == 0) {
					typeerror=" ���������";
				}else if(type==1){
					typeerror=" ���(��)����";
				}else if(type==2){
					typeerror=" ���(��)����";
				}
				throw new Exception(" ������˾Ϊ��["+outcorp+ " ]����ֿ�Ϊ��["+ store+ "] �������Ϊ : ["+typeerror+"] û���ҵ��˼۱� ");
			}
			TransVO transvo=(TransVO) list.get(0);
			UFDouble price=PuPubVO.getUFDouble_NullAsZero(transvo.getPrice());
			UFDouble mail =PuPubVO.getUFDouble_NullAsZero(transvo.getMail());
			UFDouble fee=(price.multiply(mail)).multiply(num);//�˷�
			total=total.add(fee);		
		}
		headvo.setAttributeValue("ntransmny", total);
	}

	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ���ɵ����˵�����У�� 
	 * @ʱ�䣺2012-7-10����06:10:33
	 * @param bill
	 * @throws BusinessException
	 */
	public void check(HYBillVO  bill) throws BusinessException{
		if(bill == null)
			throw new  BusinessException("��������Ϊ��");
		SendorderVO head = (SendorderVO)bill.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVdef1())==null)
			throw new BusinessException("������˾Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_corp())==null)
			throw new BusinessException("���빫˾Ϊ��");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_billtype())==null)
			throw new BusinessException("��������Ϊ��");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
			head.setVbillno(
					new HYPubBO().getBillNo(Wds2WlPubConst.billtype_alloinsendorder, head.getPk_corp(), null, null));
		}
		
		SendorderBVO[] bodys = (SendorderBVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("�����쳣����������Ϊ��");
		 for(SendorderBVO body:bodys){
			 body.validate();
		 }
	}
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ ��ⵥ������ �˵�����Ӧ����  ��Ҫ��������������� �����˵��Ľ������� 
	 * @ʱ�䣺2012-7-10����06:09:45
	 * @param bill
	 * @throws BusinessException
	 */
	private void updateAlloInSendBill(OtherInBillVO bill) throws BusinessException{
		
	}
	
	/**
	 * 
	 * @���ߣ�zhf
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2012-7-23����01:16:47
	 * @param alloinheadid
	 * @throws BusinessException
	 */
	public void deleteAlloInSendBill(String alloinheadid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(alloinheadid)==null)
			return;
		String sql = " select h.vbillstatus,h.pk_sendorder from wds_sendorder h inner join wds_sendorder_b b" +
				" on h.pk_sendorder = b.pk_sendorder where b.csourcebillhid = '"+alloinheadid+"'";
		
		Map o = (Map)getDao().executeQuery(sql, ResultSetProcessorTool.MAPPROCESSOR);
		if(o == null || o.size() == 0)
			return;
		if(PuPubVO.getInteger_NullAs(o.get("vbillstatus"), -1).intValue() == IBillStatus.CHECKPASS)
			throw new BusinessException("�����˵��Ѿ�����ͨ��");
//		ɾ���˵�
		sql = "update wds_sendorder set dr = 1 where pk_sendorder = '"
			+PuPubVO.getString_TrimZeroLenAsNull(o.get("pk_sendorder"))+"'";
		getDao().executeUpdate(sql);
		sql = "update wds_sendorder_b set dr = 1 where pk_sendorder = '"
			+PuPubVO.getString_TrimZeroLenAsNull(o.get("pk_sendorder"))+"'";
		getDao().executeUpdate(sql);
	}
}
