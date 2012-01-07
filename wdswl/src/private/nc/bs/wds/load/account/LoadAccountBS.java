package nc.bs.wds.load.account;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.load.LoadpriceVO;
import nc.vo.wds.load.account.ExaggLoadPricVO;
import nc.vo.wds.load.account.LoadpriceB1VO;
import nc.vo.wds.load.account.LoadpriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * 
 * @author Administrator װж�Ѻ����̨��
 */
public class LoadAccountBS {
	// ��ǰ��¼��˾
	public static int LOADFEE=0;//����װ������
	public static int UNLOADFEE=1;//���ж������
	
	private String pk_corp = null;
//	��ǰ��¼�ֿ�
	private String cwarehouseid = null;
	// <�������id,���װж�۸���VO>
	Map<String, LoadpriceVO> invLoadPrice = null;

	ArrayList<LoadpriceB1VO> list = new ArrayList<LoadpriceB1VO>();

	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

//	/**
//	 * 
//	 * @���ߣ�lyf
//	 * @˵�������ɽ������Ŀ :װж�Ѻ���
//	 * @ʱ�䣺2011-5-16����03:12:30
//	 * @param vos
//	 *            ��������ѡ����������ݣ���Դ���������ⵥ(WDS6)�����۳��ⵥ(WDS8)��������ⵥ(WDS7)��������ⵥ(WDS9)��
//	 *            ��Դ��������ͬһ������ ������������۳�����ͬһ�����ͬһ��VO �������͵��������ͬһ�����ͬһ��VO
//	 * @return
//	 * @throws BusinessException
//	 */
//	public AggregatedValueObject accoutLoadPrice(AggregatedValueObject[] vos,
//			String corpid,String cwarehouseid) throws BusinessException {
//		this.pk_corp = corpid;
//		this.cwarehouseid = cwarehouseid;
//		// �õ�װж�Ѽ۸���
//		getInvLoadPrice();
//		// ������Դ���ݵĲ�ͬת����ʵ�ʵ���������
//		AggregatedValueObject vo = vos[0];
//		String billType = (String) vo.getParentVO().getAttributeValue(
//				"vbilltype");
//		if (billType == null || "".equalsIgnoreCase(billType)) {
//			billType = (String) vo.getParentVO().getAttributeValue(
//					"geh_cbilltypecode");
//		}
//		// �������⣬���۳���װж�Ѻ���
//		if (WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(billType)
//				|| WdsWlPubConst.BILLTYPE_SALE_OUT.equalsIgnoreCase(billType)) {
//			for (AggregatedValueObject value : vos) {
//				accountOutBill(value);
//			}
//		}
//		// ������⣬�������װж�Ѻ���
//		else if (WdsWlPubConst.BILLTYPE_OTHER_IN.equalsIgnoreCase(billType)
//				|| WdsWlPubConst.BILLTYPE_ALLO_IN.equalsIgnoreCase(billType)) {
//			for (AggregatedValueObject value : vos) {
//				accountInBill(value);
//			}
//		}
//		ExaggLoadPricVO agg = new ExaggLoadPricVO();
//		agg.setParentVO(null);
//		agg.setTableVO(agg.getTableCodes()[0], list
//				.toArray(new LoadpriceB1VO[0]));
//		agg.setTableVO(agg.getTableCodes()[1], null);
//		return agg;
//	}
	/**
	 * װж�Ѽ���
	 * @���ߣ�mlr
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-9-23����01:41:33
	 * @param vos
	 * @param corpid
	 * @param cwarehouseid
	 * @return
	 * @throws BusinessException
	 */
	public void accoutLoadPrice(AggregatedValueObject billvo,String corpid,String cwarehouseid,int lodytype) throws BusinessException {
		this.pk_corp = corpid;
		this.cwarehouseid = cwarehouseid;
		// �õ�װж�Ѽ۸���
		getInvLoadPrice();
		//����װж��
		accountBill(billvo,lodytype);
	}	


	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ ��ѯ �����װж�۸���
	 * @ʱ�䣺2011-5-16����03:56:46
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, LoadpriceVO> getInvLoadPrice() throws BusinessException {
		String sql = "select * from wds_loadprice where pk_corp='" + pk_corp
				+ "' and isnull(dr,0)=0 and cwarehouseid = '"+cwarehouseid+"'";
		Object o = getBaseDAO().executeQuery(sql,
				new BeanListProcessor(LoadpriceVO.class));
		if (o == null) {
			throw new BusinessException("����ά����װж�Ѽ۸�");
		} else {
			invLoadPrice = new HashMap<String, LoadpriceVO>();
			List<LoadpriceVO> list = (List<LoadpriceVO>) o;
			for (int i = 0; i < list.size(); i++) {
				invLoadPrice.put(list.get(i).getPk_invmandoc(), list.get(i));
			}
		}
		return invLoadPrice;
	}
	/**
	 * 
	 * ����װж��
	 * @���ߣ�mlr 
	 * @˵�������ɽ������Ŀ 
	 * @ʱ�䣺2011-5-16����07:26:09
	 */
	private void accountBill(AggregatedValueObject billVO,int lodytype)throws BusinessException {
		if(billVO==null || billVO.getParentVO()==null){
			return;
		}
		ExaggLoadPricVO mybill=(ExaggLoadPricVO)billVO;
		
		if(mybill.getTableVO(mybill.getTableCodes()[0])==null || mybill.getTableVO(mybill.getTableCodes()[0]).length==0){
			return;
		}
		LoadpriceB1VO[] vos1=(LoadpriceB1VO[]) mybill.getTableVO(mybill.getTableCodes()[0]);
		 UFDouble feess=new UFDouble();
		for (LoadpriceB1VO bvo : vos1) {
			// �жϴ���Ƿ���װж�Ѽ۸����û�ж��壬
			if (!invLoadPrice.containsKey(bvo.getPk_invmandoc())) {
				String invcode = getInvCodeByManid(bvo.getPk_invmandoc());
				throw new BusinessException("��ά�����װж�Ѽ۸�,�������:" + invcode);
			}
			LoadpriceVO loadPricvo = invLoadPrice.get(bvo.getPk_invmandoc());
			// �������
		    if(lodytype==LOADFEE){
			bvo.setNloadprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNloadprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// װ������
		    }else if(lodytype==UNLOADFEE){
				bvo.setNunloadprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNunloadprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// ж������	              
		    }
			bvo.setNtagprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNtagprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNtagnum())));// ��ǩ����
			if (loadPricvo.getFiscoded() != null&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {
				// �Ƿ����
				bvo.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(loadPricvo.getNcodeprice()).multiply(PuPubVO.getUFDouble_NullAsZero(bvo.getNassoutnum())));// �������
			}
			 UFDouble fees=new UFDouble();		    	
   		   fees=PuPubVO.getUFDouble_NullAsZero( bvo.getNloadprice())
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNunloadprice()))
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNcodeprice()))
   		        .add(PuPubVO.getUFDouble_NullAsZero( bvo.getNtagprice()));
   		   feess=feess.add(fees);			      	    
		}
		//���ñ�ͷ���ܷ���		
		LoadpriceHVO hvo=(LoadpriceHVO)(mybill.getParentVO());
		hvo.setVzfee(feess);
		hvo.setFloadtype(lodytype);
		
		if(mybill.getTableVO(mybill.getTableCodes()[1])==null|| mybill.getTableVO(mybill.getTableCodes()[1]).length==0){
			return;
		}
		//���ֻ��һ�����飬���ܷ��ø�ֵ�����������
		if(mybill.getTableVO(mybill.getTableCodes()[1]).length==1){
			mybill.getTableVO(mybill.getTableCodes()[1])[0].setAttributeValue("nloadprice",feess);
			//liuys add ���ӱ���2װж�ѽ��
			mybill.getTableVO(mybill.getTableCodes()[0])[0].setAttributeValue("nloadprice",feess);
		}
	}

//
//	/**
//	 * 
//	 * @throws BusinessException
//	 * @���ߣ�lyf
//	 * @˵�������ɽ������Ŀ ���ⵥ�������� װж�ѽ��㵥��Դ��ϸVO
//	 * @ʱ�䣺2011-5-16����07:26:09
//	 */
//	private void accountOutBill(AggregatedValueObject vo)
//			throws BusinessException {
//		TbOutgeneralHVO hvo = (TbOutgeneralHVO) vo.getParentVO();
//		TbOutgeneralBVO[] bvos = (TbOutgeneralBVO[]) vo.getChildrenVO();
//		for (TbOutgeneralBVO bvo : bvos) {
//			// �жϴ���Ƿ���װж�Ѽ۸����û�ж��壬
//			if (!invLoadPrice.containsKey(bvo.getCinventoryid())) {
//				String invcode = getInvCodeByManid(bvo.getCinventoryid());
//				throw new BusinessException("��ά�����װж�Ѽ۸�,�������:" + invcode);
//			}
//			LoadpriceVO loadPricvo = invLoadPrice.get(bvo.getCinventoryid());
//			LoadpriceB1VO b1 = new LoadpriceB1VO();
//			// �������
//			b1.setNloadprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNloadprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassistnum())));// װ������
//			b1.setNtagprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNtagprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getNtagnum())));// ��ǩ����
//			if (loadPricvo.getFiscoded() != null
//					&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {// �Ƿ����
//				b1
//						.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(
//								loadPricvo.getNcodeprice()).multiply(
//								PuPubVO.getUFDouble_NullAsZero(bvo
//										.getNoutassistnum())));// �������
//			}
//			// ���ô��������Ϣ
//			b1.setPk_invmandoc(bvo.getCinventoryid());
//			b1.setPk_invbasdoc(bvo.getCinvbasid());
//			b1.setVbatchecode(bvo.getVbatchcode());// ���κ�
//			b1.setNhsl(bvo.getHsl());// ������
//			b1.setCunitid(bvo.getUnitid());// ��������λ
//			b1.setCassunitid(bvo.getCastunitid());// ��������λ
//			b1.setNoutnum(bvo.getNoutnum());// ʵ������
//			b1.setNassoutnum(bvo.getNoutassistnum());// ʵ��������
//			b1.setNshouldoutnum(bvo.getNshouldoutnum());// Ӧ������
//			b1.setNassshouldoutnum(bvo.getNshouldoutassistnum());// Ӧ��������
//			b1.setNtagnum(bvo.getNtagnum());// ��ǩ����
//
//			// ��Դ������Ϣ
//			b1.setVsourcebillcode(hvo.getVbillcode());
//			b1.setCsourcetype(hvo.getVbilltype());
//			b1.setCsourcebillhid(bvo.getGeneral_pk());
//			b1.setCsourcebillbid(bvo.getGeneral_b_pk());
//			b1.setVfirstbillcode(bvo.getVfirstbillcode());
//			b1.setCfirsttype(bvo.getCfirsttyp());
//			b1.setCfirstbillhid(bvo.getCfirstbillhid());
//			b1.setCfirstbillbid(bvo.getCfirstbillbid());
//			list.add(b1);
//
//		}
//	}

//	/**
//	 * 
//	 * @throws BusinessException
//	 * @���ߣ�lyf
//	 * @˵�������ɽ������Ŀ ��ⵥ�������� װж�ѽ��㵥
//	 * @ʱ�䣺2011-5-16����07:26:09
//	 */
//	private void accountInBill(AggregatedValueObject vo)
//			throws BusinessException {
//
//		TbGeneralHVO hvo = (TbGeneralHVO) vo.getParentVO();
//		TbGeneralBVO[] bvos = (TbGeneralBVO[]) vo.getChildrenVO();
//		for (TbGeneralBVO bvo : bvos) {
//			// �жϴ���Ƿ���װж�Ѽ۸����û�ж��壬
//			if (!invLoadPrice.containsKey(bvo.getGeb_cinventoryid())) {
//				String invcode = getInvCodeByManid(bvo.getGeb_cinventoryid());
//				throw new BusinessException("��ά�����װж�Ѽ۸�,�������:" + invcode);
//			}
//			LoadpriceVO loadPricvo = invLoadPrice
//					.get(bvo.getGeb_cinventoryid());
//			LoadpriceB1VO b1 = new LoadpriceB1VO();
//			// �������
//			b1.setNunloadprice(PuPubVO.getUFDouble_NullAsZero(
//					loadPricvo.getNunloadprice()).multiply(
//					PuPubVO.getUFDouble_NullAsZero(bvo.getGeb_banum())));// ж������
//			
////			zhf   add  ��ⲻ��Ҫ��������     ע�͵�
////			if (loadPricvo.getFiscoded() != null
////					&& loadPricvo.getFiscoded() == UFBoolean.TRUE) {// �Ƿ����
////				b1.setNcodeprice(PuPubVO.getUFDouble_NullAsZero(
////						loadPricvo.getNcodeprice()).multiply(
////						PuPubVO.getUFDouble_NullAsZero(bvo.getGeb_banum())));// �������
////			}
//			// ���ô��������Ϣ
//			b1.setPk_invmandoc(bvo.getGeb_cinventoryid());
//			b1.setPk_invbasdoc(bvo.getGeb_cinvbasid());
//			b1.setVbatchecode(bvo.getGeb_vbatchcode());// ���κ�
//			b1.setNhsl(bvo.getGeb_hsl());// ������
//			b1.setCunitid(bvo.getPk_measdoc());// ��������λ
//			b1.setCassunitid(bvo.getCastunitid());// ��������λ
//			b1.setNoutnum(bvo.getGeb_anum());// ʵ������
//			b1.setNassoutnum(bvo.getGeb_banum());// ʵ��������
//			b1.setNshouldoutnum(bvo.getGeb_snum());// Ӧ������
//			b1.setNassshouldoutnum(bvo.getGeb_bsnum());// Ӧ��������
//
//			// ��Դ������Ϣ
//			b1.setVsourcebillcode(hvo.getGeh_billcode());
//			b1.setCsourcetype(hvo.getGeh_billtype());
//			b1.setCsourcebillhid(bvo.getGeh_pk());
//			b1.setCsourcebillbid(bvo.getGeb_pk());
//			b1.setVfirstbillcode(bvo.getVfirstbillcode());
//			b1.setCfirsttype(bvo.getCfirsttype());
//			b1.setCfirstbillhid(bvo.getCfirstbillhid());
//			b1.setCfirstbillbid(bvo.getCfirstbillbid());
//			list.add(b1);
//		}
//
//	}

	/**
	 * 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ " ���ݴ������id �õ� �������
	 * @ʱ�䣺2011-5-16����08:58:34
	 * @param pk_invmandoc
	 * @return
	 * @throws DAOException
	 */
	private String getInvCodeByManid(String pk_invmandoc)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select invcode from bd_invbasdoc where pk_invbasdoc in (");
		sql.append(" select pk_invbasdoc from bd_invmandoc ");
		sql.append(" where isnull(dr,0)=0 ");
		sql.append(" and pk_corp='" + pk_corp + "'");
		sql.append(" and pk_invmandoc='" + pk_invmandoc + "'");
		sql.append(" )");
		Object o = getBaseDAO().executeQuery(sql.toString(),
				WdsPubResulSetProcesser.COLUMNPROCESSOR);
		if (o == null) {
			throw new BusinessException("��ѯ����������쳣:�������id='" + pk_invmandoc
					+ "',������");
		}
		return o.toString();
	}

	/**
	 * 
	 * @throws BusinessException 
	 * @���ߣ�lyf
	 * @˵�������ɽ������Ŀ װж�Ѽ�����ɣ���д����ⵥװж�Ѽ�����ɱ�ʶ
	 * ֻ�в������������ϵ�ʱ����Ҫ��д
	 * @ʱ�䣺2011-5-17����08:02:04
	 */
	public void writeBack(AggregatedValueObject billvo) throws BusinessException {
		if (billvo == null)
			return;
		ExaggLoadPricVO axbillvo = (ExaggLoadPricVO)billvo;
		CircularlyAccessibleValueObject[] b1vos = axbillvo.getTableVO(axbillvo
				.getTableCodes()[0]);
		if(b1vos == null || b1vos.length ==0)
			return;
	   //������Դ���ݱ�
		ArrayList<String> csourcebillhidout = new ArrayList<String>();// ���ⵥ��Դ���ݱ�ͷid
		ArrayList<String> csourcebillhidin = new ArrayList<String>();// ��ⵥ��Դ���ݱ�ͷid	
		for (CircularlyAccessibleValueObject b1vo : b1vos) {
			String csourcetype = (String) b1vo.getAttributeValue("csourcetype");
			if (csourcetype == null || csourcetype.equals(""))
				continue;
			String cousbillhid = (String)b1vo.getAttributeValue("csourcebillhid");
			if (WdsWlPubConst.BILLTYPE_OTHER_OUT.equalsIgnoreCase(csourcetype)
					|| WdsWlPubConst.BILLTYPE_SALE_OUT
							.equalsIgnoreCase(csourcetype)) {
				
				if(csourcebillhidout.contains(cousbillhid))
					continue;
				csourcebillhidout.add(cousbillhid);
			} else if (WdsWlPubConst.BILLTYPE_OTHER_IN
					.equalsIgnoreCase(csourcetype)
					|| WdsWlPubConst.BILLTYPE_ALLO_IN
							.equalsIgnoreCase(csourcetype)) {
				if(csourcebillhidin.contains(cousbillhid))
					continue;
				csourcebillhidin.add(cousbillhid);
			}
		}
		String key = axbillvo.getParentVO().getPrimaryKey();
		String vaule = null;
		if( key == null || "".equalsIgnoreCase(key)){
			vaule ="Y";
		}else{
			vaule ="N";
		}
		String outSql =" update tb_outgeneral_h set fisload='"+vaule+"' where general_pk=?";
		String inSql =" update tb_general_h set fisload='"+vaule+"' where geh_pk=?";
		SQLParameter parameter = new SQLParameter();
		//����
		for(int i=0;i<csourcebillhidout.size();i++){
			parameter.addParam(csourcebillhidout.get(i));
			getBaseDAO().executeUpdate(outSql, parameter);
			parameter.clearParams();
		}
		//liuys modify  �޸ĵͼ��������
		for(int i=0;i<csourcebillhidin.size();i++){
			parameter.addParam(csourcebillhidin.get(i));
			getBaseDAO().executeUpdate(inSql, parameter);
			parameter.clearParams();
		}
	}

}
