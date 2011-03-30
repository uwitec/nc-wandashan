package nc.bs.hg.ic.pub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.bs.hg.scm.pub.HgScmPubBO;
import nc.bs.ic.pub.check.CheckDMO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.hg.ic.ic201.PactVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;

public class HgICPubBO {

	private BaseDAO dao = null;

	private BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
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
		str
				.append(" select h.vordercode,h.cvendormangid,h.cvendorbaseid,b.corder_bid"
						+ " ,b.corderid,b.cmangid,b.cbaseid,b.nordernum,b.cassistunit,b.nassistnum,b.blargess,b.crowno,"
						+ "b.naccumarrvnum,b.naccumstorenum,b.ts "
						+ " from po_order h inner join po_order_b b on h.corderid = b.corderid"
						+ " where isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and h.cvendormangid = '"
						+ cvendormanid
						+ "'"
						+ " and coalesce(nordernum,0.0) - coalesce(naccumarrvnum,0.0)>0.0 ");// �ۼƵ�������Ϊ��

		if (PuPubVO.getString_TrimZeroLenAsNull(cbiztypeid) != null)
			str.append(" and h.cbiztype = '" + cbiztypeid + "'");
		str.append(" and h.forderstatus = 3");// ������
		Object o = getBaseDao().executeQuery(str.toString(),
				new BeanListProcessor(PactVO.class));
		if (o == null)
			return null;
		java.util.List<PactVO> ldata = (java.util.List<PactVO>) o;
		return ldata.toArray(new PactVO[0]);
	}

	public void rewriteBackArrNum(GeneralBillItemVO[] items,
			GeneralBillItemVO[] olditems, boolean isSave)
			throws BusinessException {
		if (items == null || items.length == 0)
			return;
		// ��д���� �ۼƵ������� = Ӧ������
		String sql = "update po_order_b set naccumarrvnum = coalesce(naccumarrvnum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;
		if (isSave) {
			int index = 0;
			for (GeneralBillItemVO item : items) {
				parameter = new SQLParameter();

				nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum());
				if (olditems != null) {
					nnum = nnum.sub(PuPubVO
							.getUFDouble_NullAsZero(olditems[index]
									.getNshouldinnum()));
				}
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
			return;
		}

		for (GeneralBillItemVO item : items) {
			parameter = new SQLParameter();
			nnum = PuPubVO.getUFDouble_NullAsZero(item.getNshouldinnum())
					.multiply(-1);
			parameter.addParam(nnum);
			parameter.addParam(item.getCsourcebillbid());
			getBaseDao().executeUpdate(sql, parameter);
		}
	}

	public void rewriteBackNinNum(GeneralBillItemVO[] items,
			GeneralBillItemVO[] old, boolean isSave) throws BusinessException {
		if (items == null || items.length == 0)
			return;

		// ��д���� �ۼ�������� = ʵ������
		String sql = "update po_order_b set naccumstorenum = coalesce(naccumstorenum,0.0)+?  where corder_bid = ?";

		UFDouble nnum = UFDouble.ZERO_DBL;
		UFDouble ninnum = UFDouble.ZERO_DBL;
		UFDouble oldnum = UFDouble.ZERO_DBL;
		SQLParameter parameter = null;
		if (isSave) {
			for (GeneralBillItemVO item : items) {
				parameter = new SQLParameter();
				ninnum = PuPubVO.getUFDouble_NullAsZero(item.getNinnum());
				if(ninnum.compareTo(UFDouble.ZERO_DBL)<0)//����������С����
					return;
			   if (UFDouble.ZERO_DBL.equals(ninnum)) {// ������������ ϵͳ���Զ���д�����������Ҫ�ڴ˴����ƣ�����Ҫ��ȥ��д��ʵ��������
					if (old == null || old.length == 0) {
						nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
					} else {
						for (GeneralBillItemVO olditem : old) {
							if (item.getPrimaryKey().equalsIgnoreCase(
									olditem.getPrimaryKey())) {
								oldnum = PuPubVO.getUFDouble_NullAsZero(olditem.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC));
								nnum = nnum.sub(oldnum);
							}
						}
					}
				}else  {
					nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
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
			if(ninnum.compareTo(UFDouble.ZERO_DBL)<0)//����������С����
				return;
			if (UFDouble.ZERO_DBL.equals(ninnum)) {
				nnum = PuPubVO.getUFDouble_NullAsZero(item.getAttributeValue(HgPubConst.NUM_DEF_FAC)).multiply(-1);
				parameter.addParam(nnum);
				parameter.addParam(item.getCsourcebillbid());
				getBaseDao().executeUpdate(sql, parameter);
			}
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
	public String getcvendor(String cinventoryid ,String vbatchcode)throws BusinessException{
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
	public UFDouble callPurchasePactPrice(String invcode, String pk_corp,String vbatch)
			throws Exception {
		if (PuPubVO.getString_TrimZeroLenAsNull(invcode) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(pk_corp) == null || PuPubVO.getString_TrimZeroLenAsNull(vbatch)==null)
			return null;

		String sql = " select ord.noriginalcurprice,pi.vuserdef11 from po_order_b ord inner join ic_general_b pi on pi.csourcebillbid = ord.corder_bid "
				+ " inner join bd_invmandoc man on pi.cinventoryid = man.pk_invmandoc inner join bd_invbasdoc bas on bas.pk_invbasdoc = man.pk_invbasdoc "
				+ " where isnull(ord.dr, 0) = 0 and isnull(pi.dr, 0) = 0 and isnull(bas.dr,0)=0 and isnull(man.dr,0)=0" 
				+" and bas.invcode='"+invcode+"' and man.pk_corp ='"+pk_corp+"' and pi.vbatchcode = '"+vbatch+"'";
		Object o = getBaseDao().executeQuery(sql,
				HgBsPubTool.ARRAYLISTPROCESSOR);
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
	public Object[] getInfoByBatchCode(String cinventoryid ,String invcode, String pk_corp,String vbatch)throws Exception{
		String cvendor = getcvendor(cinventoryid, vbatch);
		UFDouble price = callPurchasePactPrice(invcode, pk_corp, vbatch);
		Object[] o = {cvendor,price};
		return o;
	}
}
