package nc.bs.hg.ic.pub;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillHeaderVO;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pu.PuPubVO;

public class HgICPubBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��ͨ��ƥ���ͬ���� �ͺ�ͬ������������ⵥ�����ǰ���Խ���ȡ����ͬ
	 * 2011-4-23����01:13:36
	 * @param gbillvo
	 * @throws BusinessException
	 */
	public Object cancelPactMatch(GeneralBillVO gbillvo)
	throws BusinessException {
		if(gbillvo == null){
			throw new BusinessException("�쳣����������Ϊ��");
		}
		GeneralBillHeaderVO head = gbillvo.getHeaderVO();
		if(!HgPubTool.getString_NullAsTrimZeroLen(head.getAttributeValue(HgPubConst.F_IS_SELF)).equalsIgnoreCase(HgPubConst.SELF)){
			throw new BusinessException("��Դ�ں�ͬ��������յ����ܽ��д˲���");
		}
		GeneralBillItemVO[] gitems = gbillvo.getItemVOs();
		if (gitems == null || gitems.length == 0)
			throw new BusinessException("�쳣����������Ϊ��");
		// ��ⵥ����У��
		nc.bs.ic.pub.check.CheckDMO icdmo = null;
		try {
			icdmo = new CheckDMO();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

		icdmo.checkTimeStamp(gbillvo);	
		HgScmPubBO scmbo = new HgScmPubBO();
		UFBoolean isIcLock = UFBoolean.FALSE;
		try {
			isIcLock = scmbo.lockPkForVo(gbillvo);
			List<String> lid = new ArrayList<String>();
			for (GeneralBillItemVO gitem : gitems) {
				if (gitem == null)
					throw new BusinessException("�쳣���������ݴ��ڿ�");
				if (PuPubVO.getString_TrimZeroLenAsNull(gitem.getCsourcebillbid()) == null
						|| !HgPubTool.getString_NullAsTrimZeroLen(
								gitem.getCsourcetype()).equalsIgnoreCase(
										ScmConst.PO_Order)) {
					throw new BusinessException("δ������ͬ");
				}

				lid.add(gitem.getPrimaryKey());				
			}			
			Map<String,String> tsInfor = splitPactInforfromInBill(lid);
			rewriteBackArrNum(gitems, null, false);
			return tsInfor;
		} catch (Exception ee) {
			if (ee instanceof BusinessException)
				throw (BusinessException) ee;
			throw new BusinessException(ee);
		} finally {
			if (isIcLock.booleanValue()) {
				try {
					scmbo.freePkForVo(gbillvo);
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-23����12:17:27
	 * @param pactInfor
	 * @throws BusinessException
	 */
	private Map<String,String> splitPactInforfromInBill(List<String> lid)
			throws BusinessException {
		String sql = " update ic_general_b set csourcebillbid = ?,csourcebillhid = ?,csourcetype = ? , nshouldinnum = 0.0 where cgeneralbid in "
				+ HgPubTool.getSubSql(lid.toArray(new String[0]));

		SQLParameter para = new SQLParameter();
		para.addNullParam(Types.CHAR);
		para.addNullParam(Types.CHAR);
		para.addNullParam(Types.CHAR);
		getBaseDao().executeUpdate(sql, para);
		
		sql = " select cgeneralbid,ts from ic_general_b where cgeneralbid in "
			+ HgPubTool.getSubSql(lid.toArray(new String[0]));
		List ldata = (List)getBaseDao().executeQuery(sql, HgBsPubTool.ARRAYLISTPROCESSOR);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("��̨���ݲ����쳣");
		Object[] os = null;
		Map<String,String> tsInfor = new HashMap<String, String>();
		for(Object o:ldata){
			os = (Object[])o;
			tsInfor.put(HgPubTool.getString_NullAsTrimZeroLen(os[0]), HgPubTool.getString_NullAsTrimZeroLen(os[1]));
		}
		return tsInfor;
	}

	public Object splitGItemsOnPactMatch(GeneralBillVO gbillvo)
			throws BusinessException {
		if (gbillvo == null)
			return null;

		// �������� �������� ��ⵥ�������� ��ⵥ�����
		HgScmPubBO scmbo = new HgScmPubBO();
		// ��ͬ��������
		Map<String, UFDateTime> pactTsInfor = (Map<String, UFDateTime>) gbillvo
				.getHeaderVO().getAttributeValue("pacttsinfor");
		List<String> lid = new ArrayList<String>();
		GeneralBillItemVO[] bodys = gbillvo.getItemVOs();
		for (GeneralBillItemVO body : bodys) {
			if (lid.contains(body.getCsourcebillbid()))
				continue;
			lid.add(body.getCsourcebillbid());
		}
		if (lid.size() == 0)
			throw new BusinessException("���������޺�ͬ��Ϣ");

		if (pactTsInfor != null) {
			try {
				Map<String, UFDateTime> pactTsInfor2 = new HashMap<String, UFDateTime>();
				for (String key : pactTsInfor.keySet()) {// ȥ��δƥ�䵽��ͬts��Ϣ
					if (lid.contains(key))
						continue;
					pactTsInfor2.put(key, pactTsInfor.get(key));
				}

				scmbo.checkTime("po_order_b", "corder_bid", pactTsInfor2);
			} catch (SQLException se) {
				se.printStackTrace();
				throw new BusinessException(se);
			}

		}

		// ��ⵥ����У��
		nc.bs.ic.pub.check.CheckDMO icdmo = null;
		try {
			icdmo = new CheckDMO();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}

		icdmo.checkTimeStamp(gbillvo);

		// ��ͬ�����
		String user = HgPubTool.getString_NullAsTrimZeroLen(gbillvo
				.getHeaderVO().getAttributeValue("cuserid"));
		String[] pactbids = lid.toArray(new String[0]);
		UFBoolean isPactLock = UFBoolean.FALSE;
		UFBoolean isIcLock = UFBoolean.FALSE;
		Object o = null;
		try {
			isPactLock = scmbo.lockPkForKey(user, pactbids);
			isIcLock = scmbo.lockPkForVo(gbillvo);

			HgGeneralbillBO gbo = new HgGeneralbillBO();
			o = gbo.updateThisBill2(gbillvo);
			
		    GeneralBillItemVO[] items=null;
			if(o != null){
				Object[] os = (Object[])o;
			    items=(GeneralBillItemVO[])os[1];
			    int len =items.length;
			    GeneralBillItemVO[] olditems= new GeneralBillItemVO[items.length] ;
			    for(int i=0;i<len;i++){
			    	gbillvo.getChildrenVO()[i].setAttributeValue("ts", items[i].getTs());
			    	olditems[i]=(GeneralBillItemVO) gbillvo.getChildrenVO()[i];
			    }
			    o= new Object[]{os[0],olditems};
			}
			rewriteBackArrNum(gbillvo.getItemVOs(), null, true);
		} catch (Exception ee) {

			if (ee instanceof BusinessException)
				throw (BusinessException) ee;
			throw new BusinessException(ee);

		} finally {
			if (isPactLock.booleanValue()) {
				try {
					scmbo.freePkForKey(user, pactbids);
				} catch (Exception e) {
				}
			}
			if (isIcLock.booleanValue()) {
				try {
					scmbo.freePkForVo(gbillvo);
				} catch (Exception e) {
				}
			}
		}
		return o;
	}

	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ⵥ������ͬ��ѯ 2011-2-17����11:06:12
	 * @param cvendormanid
	 * @param cinvbasids
	 * @return
	 * @throws BusinessException
	 */
	public PactVO[] queryPactsByVendor(String cbiztypeid, String cvendormanid,
			String[] cinvbasids) throws BusinessException {
		// PactVO[] pvos = null;
		if (PuPubVO.getString_TrimZeroLenAsNull(cvendormanid) == null)
			return null;
	
		if (cinvbasids == null || cinvbasids.length == 0)
			return null;
	
		// ��ѯ���� ��ѯʲô���Ķ�����
		StringBuffer str = new StringBuffer();
		str.append(" select h.vordercode,h.cvendormangid,h.cvendorbaseid,b.corder_bid"
						+ " ,b.corderid,b.cmangid,b.cbaseid,b.nordernum,b.cassistunit,b.nassistnum,b.blargess,b.crowno,"
						+ "b.naccumarrvnum,b.naccumstorenum,b.ts,b.noriginalcurprice "
						+ " from po_order h inner join po_order_b b on h.corderid = b.corderid"
						+ " where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.cvendormangid = '"+ cvendormanid+"'"
						 //δ��ȫ���  modify by zhw  2011-04-15  �׸���Ŀ  ��ǰ�ݲ�
						// û�������ɵ�");// �ۼƵ�������Ϊ��
						+ " and coalesce(b.vdef20,'N')= 'N'");
						

		if (PuPubVO.getString_TrimZeroLenAsNull(cbiztypeid) != null)
			str.append(" and h.cbiztype = '" + cbiztypeid + "'");
		str.append(" and h.forderstatus = 3");// ������
		
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(PactVO.class));
		
		if (o == null)
			return null;
		
		java.util.List<PactVO> ldata = (java.util.List<PactVO>) o;
		java.util.List<PactVO> data = new ArrayList<PactVO>();
		
		//modify by zhw 2011-04-16  ��������=��������*�ݲ�-�ۼ��������
		int  size = ldata.size();
		for(int i=0;i<size;i++){
			
			PactVO pvo=	ldata.get(i);
			String basid = PuPubVO.getString_TrimZeroLenAsNull(pvo.getCbaseid());
			UFDouble allowance= PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(basid)).div(new UFDouble(100));
			UFDouble nordernum =PuPubVO.getUFDouble_NullAsZero(pvo.getNordernum());// ��ͬ����;
			
			if(!UFDouble.ZERO_DBL.equals(allowance))
				 nordernum=allowance.multiply(PuPubVO.getUFDouble_NullAsZero(pvo.getNordernum()));//�����ݲ��Ժ������
             UFDouble  nusenum= nordernum.sub(PuPubVO.getUFDouble_NullAsZero(pvo.getNaccumstorenum()));
			
             if(UFDouble.ZERO_DBL.compareTo(nusenum)<0){
				pvo.setNordernum(nordernum);
				 data.add(pvo);
			 }
             
		}
		return data.toArray(new PactVO[0]);
	}

	/**
	 * ���Ʋɹ���ⵥ   ��ͬƥ��ʱ  ��д�ɹ�����
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-23����05:05:07
	 * @param items
	 * @param olditems
	 * @param isSave
	 * @throws BusinessException
	 */
	public void rewriteBackArrNum(GeneralBillItemVO[] items,GeneralBillItemVO[] olditems, boolean isSave)
			throws BusinessException {

		if (items == null || items.length == 0)
			return;

		// ��д���� �ۼƵ������� = Ӧ������+   vdef20 �Ƿ�������
		String sql = "update po_order_b set vdef20 = ?,naccumstorenum  = coalesce(naccumstorenum ,0.0)+? where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;

		UFDouble ninnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;

		if (isSave) {
			int index = 0;
			for (GeneralBillItemVO item : items) {

				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());

				parameter.clearParams();

				
				// modify by zhw 2011-04-16 �ж��ۼ������Ƿ���ڶ������������ݲ�
				String sourceid = item.getCsourcebillbid();
				Object o = getNaccumstorenum(sourceid);

				UFDouble nordernum = UFDouble.ZERO_DBL;
				UFDouble accumstorenum = UFDouble.ZERO_DBL;
				UFDouble allowance = UFDouble.ZERO_DBL;
				
				if (o != null) {

					Object[] uds = (Object[]) o;
					nordernum = PuPubVO.getUFDouble_NullAsZero(uds[0]);
					accumstorenum = PuPubVO.getUFDouble_NullAsZero(uds[1]);
					allowance = PuPubVO.getUFDouble_NullAsZero(getAllowanceSet(item.getCinvbasid()).div(100));
				}
				
				if (UFDouble.ZERO_DBL.equals(ninnum)) {// ���û��������� ��Ҫ��дʵ���������������ۼ��������
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
					if (!UFDouble.ZERO_DBL.equals(nnum)
							&& nordernum.multiply(allowance).equals(accumstorenum.add(nnum))) {
						// ����б��κ�ͬ���� ���Һ�ͬ��������ʵ������+�ۼ�������� ��������

						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}

					if (olditems != null) {
						nnum = nnum.sub(PuPubVO.getUFDouble_NullAsZero(olditems[index].getNshouldinnum()));
					}
					
				} else {//������������ ϵͳ���Զ���д�����������Ҫ�ڴ˴����ƣ�����Ҫ��ȥ��д��ʵ��������
					
					if(VOStatus.UPDATED ==  item.getStatus())
						// �������޸�   �������û���޸Ĳ���Ҫ��ȥ��д��ʵ������
						nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
				        nnum =PuPubVO.getUFDouble_NullAsZero(nnum).multiply(-1);
					
					if (accumstorenum.add(ninnum).equals(nordernum.multiply(allowance))) {// ��ͬ��������ʵ������ ��������
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
				}

				parameter.addParam(nnum);
				parameter.addParam(sourceid);
				getBaseDao().executeUpdate(sql, parameter);
			}

			return;
		}

		for (GeneralBillItemVO item : items) {

			parameter = new SQLParameter();
			ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
			
			if (UFDouble.ZERO_DBL.equals(ninnum)) 
			    nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum()).multiply(-1);
			
			parameter.clearParams();

			parameter.addParam("N");
			parameter.addParam(nnum);
			parameter.addParam(item.getCsourcebillbid());
			getBaseDao().executeUpdate(sql, parameter);
		}
	}

	/**
	 * �ɹ���ⵥ���ղɹ�����ʱ �ɹ������Ļ�д
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-23����05:04:24
	 * @param items
	 * @param old
	 * @param isSave
	 * @throws BusinessException
	 */
	public void rewriteBackNinNum(GeneralBillItemVO[] items,GeneralBillItemVO[] old, boolean isSave) throws BusinessException {
		
		if (items == null || items.length == 0)
			return;

		// ��д���� �ۼ�������� = ʵ������   vdef20 �Ƿ�������
		String sql = "update po_order_b set vdef20 = ?,naccumstorenum = coalesce(naccumstorenum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		UFDouble ninnum = UFDouble.ZERO_DBL;
		UFDouble oldnum = UFDouble.ZERO_DBL;
		UFDouble nshouldinnum = UFDouble.ZERO_DBL;

		SQLParameter parameter = null;
		if (isSave) {
			for (GeneralBillItemVO item : items) {
				
				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
				nshouldinnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());// ��ͬ����
				
				if (ninnum.compareTo(UFDouble.ZERO_DBL) < 0)// ����������С����
					return;
				
				if (UFDouble.ZERO_DBL.equals(ninnum)) {// ���û���������   ��Ҫ��д ʵ���������������ۼ��������
					
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
					if(item.getStatus()==VOStatus.DELETED)
						nnum=UFDouble.ZERO_DBL;
					
					if (nnum.equals(nshouldinnum)) {// ��ͬ��������ʵ������ ��������
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
					
					if (old != null && old.length >0) {
						
						for (GeneralBillItemVO olditem : old) {
							
							if (item.getPrimaryKey().equalsIgnoreCase(olditem.getPrimaryKey())) {
								
								oldnum = PuPubVO.getUFDouble_NullAsZero(olditem.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = nnum.sub(oldnum);
							}
						}
					}
					
				} else {  //������������ ϵͳ���Զ���д�����������Ҫ�ڴ˴����ƣ�����Ҫ��ȥ��д��ʵ��������
					
					if(VOStatus.UPDATED ==  item.getStatus()) // �������޸�   �������û���޸Ĳ���Ҫ��ȥ��д��ʵ������
				        nnum =PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
					
					if (ninnum.equals(nshouldinnum)) {// ��ͬ��������ʵ������ ��������
						parameter.addParam("Y");
					} else {
						parameter.addParam("N");
					}
				}
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
			return;
		}

		for (GeneralBillItemVO item : items) {
			
			parameter = new SQLParameter();
			ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
			if (ninnum.compareTo(UFDouble.ZERO_DBL) < 0)// ����������С����
				return;
			
			if (UFDouble.ZERO_DBL.equals(ninnum)) 
				nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
				
				parameter.addParam("N");
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
		}
		
	}

	/**
	 * ����pk��ȡ�޸�ǰ������
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�� 2011-1-13����11:17:01
	 * @param pk
	 * @return
	 * @throws DAOException
	 */
	public UFDouble getOldNum(String pk) throws DAOException {
		
		String sql = "select " + HgPubConst.NUM_DEF_FAC
				+ " from  ic_general_b where cgeneralbid = '" + pk + "'";
		
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);
		
		return PuPubVO.getUFDouble_NullAsZero(o);
	}
	
	public void compareTONum(GeneralBillVO vo) throws BusinessException{
		
		if(vo == null)
			return;
	
		GeneralBillItemVO[] items = vo.getItemVOs();
		UFDouble outnum = UFDouble.ZERO_DBL;
		
		for(GeneralBillItemVO item:items){
		
			outnum = PuPubVO.getUFDouble_NullAsZero(item.getNoutnum());
			
			if(UFDouble.ZERO_DBL.compareTo(outnum)>=0)
				throw new BusinessException("ʵ����������С����");
		
		}
	}
	
	
	HgScmPubBO bo = null;
	private HgScmPubBO getScmPubBO(){
	
		if(bo == null){
			bo = new HgScmPubBO();
		}
		
		return bo;
	}
	
	/**
	 * ��ȡ����ĵ����ݲ�
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-15����01:46:49
	 * @param pk_invbasid
	 * @param fieldname
	 * @param where
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getAllowanceSet(String pk_invbasid) throws BusinessException{
		
		UFDouble allowance =PuPubVO.getUFDouble_NullAsZero(getScmPubBO().getAllowanceSet(pk_invbasid,"narrival",null));
		
		return allowance;
		
	}
	
	/**
	 *   ��ȡ�ɹ������ϵ�  ��������  �ۼ��������
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-4-23����05:08:16
	 * @param sourceid
	 * @return
	 * @throws BusinessException
	 */
	private Object getNaccumstorenum(String sourceid) throws BusinessException{
	
		String sql= "select nordernum,naccumstorenum from po_order_b where corder_bid = '"+sourceid+"' and isnull(dr,0)=0 ";
		
		Object o = getBaseDao().executeQuery(sql,HgBsPubTool.ARRAYPROCESSOR);
		
		return o;
	}
	/**
	 * �������κźʹ����ȡ��Ӧ��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-3-7����09:34:07
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws BusinessException
	 */
	public ArrayList getcvendor(ArrayList al)throws BusinessException{
		
		if(al ==null || al.size()==0)
			return null;
		
		int size= al.size();
		Object object = null;
		ArrayList als= new ArrayList();
		
		String infor=null;
		String[] strs = new String [2];
		
		for(int i=0;i<size;i++){
			
			infor = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
			
			if(infor != null){
				strs=infor.split("&");
				
				if(strs !=null && strs.length>0){
					
					String cinventoryid = strs[1];
					String vbatchcode=strs[0];
				    object = getcvendor(cinventoryid,vbatchcode);
				    als.add(object);
		    	}
			}
		}
		return als;
	}
	
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�����ݼƻ���Ŀ�ҳ��ⷽʽ 2012-2-21����02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public String getIOutWay(String pk_planproject) throws Exception {
		
		if (PuPubVO.getString_TrimZeroLenAsNull(pk_planproject) == null)
			return null;
		
		String sql = "select ioutway from hg_planproject where pk_planproject = '"
				+ pk_planproject + "' and isnull(dr,0)=0";
		Object o = getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNPROCESSOR);

		if (o == null)
			return null;
		
		String string = null;
		String str = PuPubVO.getString_TrimZeroLenAsNull(o);
		
		if ("0".equalsIgnoreCase(str))
			string = "�ƻ���";
		if ("1".equalsIgnoreCase(str))
			string = "��ͬ��";
		
		return string;
	}
	
	/**
	 * 
	 * @author zhw
	 * @˵�������׸ڿ�ҵ�����ϳ���ɹ���ͬ������ 2012-2-21����02:27:22
	 * @param cinvbasid
	 * @param cbatchid
	 * @throws BusinessException
	 */
	public UFDouble callPurchasePactPrice(String vbatch,String invbasid)
			throws Exception {
		
		if (PuPubVO.getString_TrimZeroLenAsNull(invbasid) == null
				||  PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		String sql = " select pi.nprice,pi.vuserdef11,man.pk_corp,pi.vbatchcode,bas.invcode from ic_general_b pi inner join bd_invmandoc man on pi.cinventoryid = man.pk_invmandoc "
                     + " inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc where isnull(pi.dr, 0) = 0 and isnull(bas.dr, 0) = 0 and isnull(man.dr, 0) = 0 and " 
                     + " man.pk_invbasdoc = '"+invbasid+"' and pi.vbatchcode='"+vbatch+"' and man.pk_corp ='"+1002+"' and  pi.vuserdef11 is not null";
	
		Object o = getBaseDao().executeQuery(sql,HgBsPubTool.ARRAYLISTPROCESSOR);
		
		if (o == null)
			return null;
	
		ArrayList ldata = (ArrayList) o;
	
		if (ldata.size() == 0)
			return null;
	
		Object ob= (Object)ldata.get(0);
	
		if(ob ==null)
			return null;
	
		Object[] os = (Object[]) ob;
		String str= PuPubVO.getString_TrimZeroLenAsNull(os[1]);
		UFDouble price = PuPubVO.getUFDouble_NullAsZero(os[0]);
		String  s =getIOutWay(str);
	
		if("��ͬ��".equalsIgnoreCase(s))
			return price;
	
		return UFDouble.ZERO_DBL;
	}
	
	/**
	 * �������κźʹ����ȡ��Ӧ��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-3-7����09:34:07
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws Exception 
	 */
	public ArrayList getInfoByBatchCode(ArrayList al)throws Exception{
		if(al ==null || al.size()==0)
			return null;
		int size= al.size();
		
		ArrayList als= new ArrayList();
		String infor=null;
		String[] strs = new String [4];
		
		for(int i=0;i<size;i++){
			
			infor = PuPubVO.getString_TrimZeroLenAsNull(al.get(i));
			
			if(infor != null){
			
				strs=infor.split("&");
			
				if(strs !=null && strs.length>0){
				
					String vbatchcode=strs[0];
					String cinventoryid = strs[1];
					String invbasid=strs[2];
					String corp =strs[3];
				
					if (HgPubConst.GYC_CORPID.equalsIgnoreCase(corp)) {
					
						Object object = getcvendor(cinventoryid,vbatchcode);
				        als.add(object);
					
					}else{
					
						UFDouble price =callPurchasePactPrice(vbatchcode,invbasid);
				
					}
		    	}
			}
		}
		return als;
	}
	
	/**
	 * �������κźʹ����ȡ��Ӧ��
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-3-7����09:34:07
	 * @param cinventoryid
	 * @param vbatchcode
	 * @return
	 * @throws BusinessException
	 */
	public String getcvendor(String cinventoryid, String vbatchcode)throws BusinessException{
		
		ArrayList<String> object = null;
		String cvendor=null;
		
		if(cinventoryid!=null&&cinventoryid.trim().length()>0&&vbatchcode!=null&&vbatchcode.trim().length()>0 ){
		
			String sql = "select distinct bb.cvendorid from ic_general_b  bb where  bb.dr=0 and cbodybilltypecode in('40', '41', '43','45', '46', '47', '48', '4A', '4B', '4E', '4X')"+
			"and cinventoryid = '"+cinventoryid.trim()+"' and vbatchcode = '"+vbatchcode.trim()+"' and isnull(bb.dr,0)=0 and  bb.cvendorid is not null";
    		
			object = (ArrayList<String>)getBaseDao().executeQuery(sql, HgBsPubTool.COLUMNLISTPROCESSOR);
			
			if (object != null && object.size() == 1 && object.get(0) != null){
				
				cvendor= PuPubVO.getString_TrimZeroLenAsNull(object.get(0));
			}
    	}
		return cvendor;
	}
	
	public void checkDatas(GeneralBillVO gbillvo) throws BusinessException{
		if(gbillvo==null)
			return;
		GeneralBillHeaderVO head= gbillvo.getHeaderVO();
		if(head ==null)
			return;
		GeneralBillItemVO[] items = gbillvo.getItemVOs();
		if(items==null || items.length==0)
			return;
		int len = items.length;
		for(int i=0;i<len;i++){
			UFDouble nkdnum = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_QUA));// ���պϸ�����
	        UFDouble arr = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_ARR));// ��������
	        UFDouble fac = PuPubVO.getUFDouble_NullAsZero(items[i].getAttributeValue(HgPubConst.NUM_DEF_FAC));// ʵ������
            UFDouble ninnum = PuPubVO .getUFDouble_NullAsZero(items[i].getAttributeValue( "ninnum"));// ʵ������
			if (!PuPubVO.getUFBoolean_NullAs(head.getFreplenishflag(), UFBoolean.FALSE).booleanValue()) {
				
				if (UFDouble.ZERO_DBL.compareTo(arr) >= 0) 
					throw new BusinessException("��" + (i + 1) + "�е����������������");
					
				if (UFDouble.ZERO_DBL.compareTo(fac) >= 0) 
					throw new BusinessException("��" + (i + 1) + "��ʵ���������������");
					
				if (arr.compareTo(fac) < 0) 
					throw new BusinessException("��" + (i + 1) + "��ʵ���������ܴ��ڵ�������");
				
				if (!UFDouble.ZERO_DBL.equals(nkdnum)) 
					if (UFDouble.ZERO_DBL.compareTo(ninnum) >= 0) 
						throw new BusinessException("��" + (i + 1) + "������������������");
						
					 else if (ninnum.compareTo(nkdnum) > 0) 
						throw new BusinessException("��" + (i + 1) + "������������ܴ�����������");
				}
			}
	}
	//У���д����
	public void check(AggregatedValueObject gbillvo) throws BusinessException{
		if(gbillvo==null)
			return;
		GeneralBillHeaderVO head= (GeneralBillHeaderVO)gbillvo.getParentVO();
		if(head ==null)
			return;
		GeneralBillItemVO[] items =(GeneralBillItemVO[]) gbillvo.getChildrenVO();
		if(items==null || items.length==0)
			return;
		for(GeneralBillItemVO item:items){
			String csql =" select ib.ninnum,ib.vuserdef19 from ic_general_b ib where  isnull(ib.dr, 0) = 0 and ib.csourcebillbid = '"+item.getCsourcebillbid()+"'";
	        String psql =" select pb.naccumstorenum from po_order_b pb where isnull(pb.dr, 0) = 0 and pb.corder_bid= '"+item.getCsourcebillbid()+"'";
			Object o = getBaseDao().executeQuery(csql,new ArrayListProcessor());
			Object o1 = getBaseDao().executeQuery(psql,new ColumnProcessor());
			UFDouble sum = UFDouble.ZERO_DBL;
			if(o ==null)
				return;
			ArrayList al =(ArrayList)o;
			if(al==null || al.size()==0)
				return;
			int size = al.size();
			for(int i=0;i<size;i++){
				Object alo = al.get(i);
				if(alo==null)
					return;
				Object[] os =(Object[])alo;
				if(PuPubVO.getUFDouble_NullAsZero(os[0]).compareTo(UFDouble.ZERO_DBL)==0)
					sum=sum.add(PuPubVO.getUFDouble_NullAsZero(os[1]));
				else
					sum = sum.add(PuPubVO.getUFDouble_NullAsZero(os[0]));
			}
	       if(sum.compareTo(PuPubVO.getUFDouble_NullAsZero(o1))!=0)
		      throw new BusinessException("��д�ۼ���������");
		}
		}
	
	//���ݴ���ֿ�  �ж��Ƿ�����˻�λ����
	public  boolean getCsflag(String cwarehouseid) throws BusinessException{
		String sql =" select csflag from bd_stordoc where isnull(dr,0)=0 and pk_stordoc = '"+cwarehouseid+"'";
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		Object o=query.executeQuery(sql,HgBsPubTool.COLUMNPROCESSOR );
		
		if(PuPubVO.getUFBoolean_NullAs(o,UFBoolean.FALSE).booleanValue()){
			return true;
		}
		return false;
	}
		
}
