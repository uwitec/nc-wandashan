package nc.bs.zb.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.pf.changedir.CHGBiddingBodyTOSubPrice;
import nc.bs.pf.changedir.CHGSubBodyTOSubPrice;
import nc.bs.zb.pub.SingleVOChangeDataBsTool;
import nc.bs.zb.pub.ZbBsPubTool;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zb.bidding.BiddingBodyVO;
import nc.vo.zb.bidding.BiddingTimesVO;
import nc.vo.zb.price.SubmitPriceVO;
import nc.vo.zb.price.bill.SubmitPriceBillVO;
import nc.vo.zb.price.bill.SubmitPriceBodyVO;
import nc.vo.zb.price.bill.SubmitPriceHeaderVO;
import nc.vo.zb.pub.ResultSetProcessorTool;
import nc.vo.zb.pub.ZbPubConst;
import nc.vo.zb.pub.ZbPubTool;

public class SubmitPriceBO {

	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�������ֳ����ۻ�ȡ���ֱ����ִ�
	 * 2011-5-6����02:37:04
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getNewCircalNoIdForLocal(String cbiddingid,String cvendorid) throws BusinessException{

		if(cbiddingid == null)
			throw new BusinessException("������ϢΪ��");
		if(cvendorid == null){
			throw new BusinessException("���۹�Ӧ����ϢΪ��");
		}
		
		String sql = "select count(0) from zb_biddingtimes where cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0";
		//���鱨�۴���
		int num = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
//		��ǰ��Ӧ�̱��۴���
		sql = "select count(distinct ccircalnoid) from zb_submitprice where cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0" +
				" and cvendorid = '"+cvendorid+"'";
		int vendornum = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubTool.INTEGER_ZERO_VALUE);
		if(vendornum>num){
			throw new BusinessException("���۴���ά���쳣����Ӧ�̱��۴������ڱ���ı��۴���");
		}
		if(vendornum == num){//�����µı��۴���
			BiddingTimesVO time = new BiddingTimesVO();
			num++;
			time.setCbiddingid(cbiddingid);
			time.setVname("��"+ZbPubTool.tranIntNumToStringNum(num)+"��");			
			time.setCrowno(String.valueOf(num*10));
			time.setStatus(VOStatus.NEW);
			
			return getDao().insertVO(time);
		}else if(vendornum<num){
//			��ȡ�Ѵ��ڵı��۴�����id��Ϊ���α��۴���
			vendornum++;
			String crowno = String.valueOf(vendornum*10);
			sql = "select cbiddingtimesid from zb_biddingtimes where isnull(dr,0)=0 and crowno = '"+crowno+"' and cbiddingid = '"+cbiddingid+"'";
			String id = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
			if(id == null){
				throw new BusinessException("�������Ϊ"+crowno+"�ı����ִβ�����");
			}
			return id;
		}	
		return null;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����ⱨ�۴���  ��¼���ⱨ����ϸ  ��ֹ�ù�Ӧ�̼�������
	 * 2011-5-21����12:53:45
	 * @param prices
	 * @throws BusinessException
	 */
	public void dealBadSubmit(SubmitPriceVO[] prices) throws BusinessException{
		for(SubmitPriceVO price:prices){
			if(price.getIsubmittype() != ZbPubConst.BAD_SUBMIT_PRICE)
				throw new BusinessException("���ݴ���,���ڲ��Ƕ��ⱨ�۵�����");
			
			SubmitPriceVO newprice = getSubmitPrice(price);
//			price.setIsubmittype(isubtype);			
			if(newprice!=null)
				throw new BusinessException("���ڱ��ִ�����ɱ���,���ⱨ����Ϣ��Ч");
			
		}
		getDao().insertVOArray(prices);
//		��ֹ��Ӧ�̱���
		String sql = " update zb_biddingsuppliers set fisclose = 'Y',icloseno = isnull(icloseno,0)+1,cclosetime = '"+new UFDateTime(System.currentTimeMillis())+"'" +
				" where ccustmanid = '"+prices[0].getCvendorid()+"' and cbiddingid = '"+prices[0].getCbiddingid()+"'";
		getDao().executeUpdate(sql);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��
	 * ����ʱУ���ִε�ʱ����Ƿ����仯 �������仯 ��ʾ��Ӧ������ˢ�����ݺ� �ٱ���
	 * ����ʱУ�鹩Ӧ���Ƿ�����ֹ������ֹ���ñ���
	 * ����ʱУ���ִγ����Ƿ��ѷ����仯�������仯�Զ�����ǰ̨�ִλ���
	 * �����ύʱУ���ִ��Ƿ��ѳ������ִι涨��Χ
	 * 2011-5-21����02:02:44
	 * @param prices
	 * @param isubtype
	 * @param userObj
	 * @throws BusinessException
	 */
	private void checkSubmitPrices(SubmitPriceVO[] prices,int isubtype,Object userObj) throws BusinessException{
		String sql;
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && userObj != null){
//			У���ִ�ts
			if(userObj == null)
				throw new BusinessException("��������쳣");
			Object[] os = (Object[])userObj;
			String clientts = PuPubVO.getString_TrimZeroLenAsNull(os[0]);
			int num = PuPubVO.getInteger_NullAs(os[1], -1);
			if(clientts == null || num == -1)
				throw new BusinessException("��������쳣");
			sql = "select ts from zb_biddingtimes where cbiddingtimesid = '"+prices[0].getCcircalnoid()+"'";
			String ts = PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
			if(ts == null)
				throw new BusinessException("�����쳣");
			if(!ts.trim().equalsIgnoreCase(clientts.trim()))
				throw new BusinessException("���ִ�ʱ�䰲���Ѿ�����,��ˢ�����������ᱨ");
//			У���ִΰ����Ƿ����
			sql = "select count(0) num from zb_biddingtimes where isnull(dr,0)=0 and  cbiddingid = '"+prices[0].getCbiddingid()+"' and  coalesce(bisfollow, 'N') = 'Y'";
			int newnum = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0);
			if(num != newnum){
				throw new BusinessException("����ε��ִ�ʱ�䰲���Ѿ�����,��ˢ�����������ᱨ");
			}
			
//			�����ύʱУ���ִ��Ƿ��ѳ������ִι涨��Χ
			BiddingTimesVO time = (BiddingTimesVO)getDao().retrieveByPK(BiddingTimesVO.class, prices[0].getCcircalnoid());
			if(time == null)
				throw new BusinessException("����ε��ִ�ʱ�䰲���Ѿ�����,���ִβ�����,��ˢ�����������ᱨ");
//			У��ʱ�䷶Χ�Ƿ�����
			UFDateTime tbegin = time.getTbigintime();
			UFDateTime tend = time.getTendtime();

//			���������ӳ�
			sql = "select ndelaytime from zb_parameter_settings where isnull(dr,0) = 0 and pk_corp = '"+SQLHelper.getCorpPk()+"'";
			UFDouble ndelaytime = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
//			��ǰʱ��
			UFDateTime tcurrent = new UFDateTime(System.currentTimeMillis());
			if(tcurrent.compareTo(tbegin)<0)
				throw new BusinessException("��Ͷ���ִλ�û�п�ʼ,�ִο�ʼʱ��Ϊ��["+tbegin.toString()+"],��������ǰʱ��Ϊ" +
						"["+tcurrent.toString()+"]");
			if(tcurrent.getMillis()>tend.getMillis()+ndelaytime.doubleValue()){
				throw new BusinessException("��Ͷ���ִ��Ѿ�����,�ִν�ֹʱ��Ϊ��["+tend.toString()+"],��������ǰʱ��Ϊ" +
						"["+tcurrent.toString()+"]");
			}
		}
		checkBidibusstatus(prices[0].getCbiddingid());
		checkMinPrice(prices);
//		У�鱨�۹�Ӧ��  ��ǰ�Ƿ���иñ�εı�������
		checkVendorForBidding(prices[0].getCbiddingid(), prices[0].getCvendorid());		
	}

	/**
	 * У������ҵ��״̬
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-6-19����04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	private void checkBidibusstatus(String cbiddingid) throws BusinessException{
		String sql =" select count(0) from zb_bidding_h where cbiddingid ='"+cbiddingid+"' and ibusstatus >1 and isnull(dr,0)=0";
		if(PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), 0)>0)
			throw new BusinessException("�ñ��鲻�ܼ�������");
	}
	
	/**
	 * У�鱾�ֱ����Ƿ�С���ϴα���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-6-19����04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	private void checkMinPrice(SubmitPriceVO[] prices) throws BusinessException{
		if(prices==null || prices.length==0)
			return;
		String cbiddingid = prices[0].getCbiddingid();
		String vendorid = prices[0].getCvendorid();
		String sql = " select e.cinvmanid,min(e.nprice) from zb_submitprice e where e.cbiddingid ='"+cbiddingid+"' and e.cvendorid ='"+vendorid+"' and coalesce(e.isubmittype,0) <>3 and isnull(e.dr,0)=0 group by e.cinvmanid";
		ArrayList al = (ArrayList)getDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if(al ==null || al.size()==0)
			return;
		int size = al.size();
		Map map = new HashMap();
		for(SubmitPriceVO price:prices){
			if(!map.containsKey(price.getCinvmanid()))
				map.put(price.getCinvmanid(),price.getNprice());
		}
		for(int i=0;i<size;i++){
			Object o = al.get(i);
			if(o==null)
				continue;
			Object[] os = (Object[])o;
			if(os ==null || os.length==0)
				continue;
			if(map.containsKey(os[0]))
				if(PuPubVO.getUFDouble_NullAsZero(map.get(os[0])).compareTo(PuPubVO.getUFDouble_NullAsZero(os[1]))>0)
					throw new BusinessException("���ֱ��۳������ֱ���");
		}
		
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���ύ����
	 * 2011-5-6����01:19:51
	 * @param prices 
	 * @param isubtype ��������
	 * @throws BusinessException
	 */
	public void submitPrice(SubmitPriceVO[] prices,Integer isubtype,Object userObj) throws BusinessException{
		if(prices == null || prices.length == 0)
			return;
		
		checkSubmitPrices(prices, isubtype,userObj);
		
		if(isubtype == ZbPubConst.BAD_SUBMIT_PRICE){//���ⱨ����ϸ��¼
			dealBadSubmit(prices);
			return;
		}
		
		// ���� + ��Ӧ�� +Ʒ�� Ψһ   ������� update  ������ insert
		SubmitPriceVO newprice = null;
		List<SubmitPriceVO> lnew = new ArrayList<SubmitPriceVO>();
		List<SubmitPriceVO> lupdate = new ArrayList<SubmitPriceVO>();
		
		String newcircleid = null;
		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
//			�ֳ��б��Զ����������ִ�
			newcircleid = getNewCircalNoIdForLocal(prices[0].getCbiddingid(),prices[0].getCvendorid());
		}
		for(SubmitPriceVO price:prices){
			newprice = getSubmitPrice(price);
			price.setIsubmittype(isubtype);			
			if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && newprice!=null)
				throw new BusinessException("���ڱ��ִ�����ɱ���");
			if(newprice == null){
				if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
					price.setCcircalnoid(newcircleid);
				}
				lnew.add(price);
			}else{
				newprice.setNprice(price.getNprice());
				newprice.setCmodifyid(price.getCoprator());
				newprice.setTmodifytime(price.getTmodifytime());
				lupdate.add(newprice);
			}
		}

		if(lnew.size() > 0)
			getDao().insertVOArray(lnew.toArray(new SubmitPriceVO[0]));
		if(lupdate.size()>0)
			getDao().updateVOArray(lupdate.toArray(new SubmitPriceVO[0]), ZbPubConst.SUBMIT_PRICE_UPDATE_FIELD);
	}

	public void cancelSubmitPrice(String cbiddingid,String cvendorid,String ccircalnoid,Integer isubtype) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return;
		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE){
			ccircalnoid = getPreCircalnoForLocal(cbiddingid,cvendorid);
		}
		if(PuPubVO.getString_TrimZeroLenAsNull(ccircalnoid)==null)
			return;
		String sql = " update zb_submitprice set dr = 1 where cbiddingid = '"+cbiddingid+"' and ccircalnoid = '"+ccircalnoid+"' and cvendorid = '"+cvendorid+"' and isnull(dr,0)=0 ";
		getDao().executeUpdate(sql);
		
//		�Ƿ���  ���鱨���ִ�  
	}

	private SubmitPriceVO getSubmitPrice(SubmitPriceVO price)
			throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(price.getCcircalnoid()) == null)
			return null;
		StringBuffer str = new StringBuffer("select * from zb_submitprice "
				+ "where cbiddingid = '" + price.getCbiddingid() + "' "
				+ "and cvendorid = '" + price.getCvendorid() + "' " +
				// "and cinvclid = '"+price.getCinvclid()+"'" +
				" and isnull(dr,0)=0");
		// if(PuPubVO.getString_TrimZeroLenAsNull(price.getCinvmanid())!=null)
		str.append(" and cinvmanid = '" + price.getCinvmanid() + "'");
		str.append(" and ccircalnoid = '" + price.getCcircalnoid() + "'");
		SubmitPriceVO newPrice = (SubmitPriceVO) getDao().executeQuery(
				str.toString(), new BeanProcessor(SubmitPriceVO.class));
		return newPrice;
	}

	/**
	 * ��ѯ�ִ���ͱ���
	 * @param cbiddingid
	 * @param ccirclenoid
	 * @param cinvid
	 * @param isinv
	 * @throws Exception
	 */
	public UFDouble getLastLowerPrice(String cbiddingid,String ccirclenoid,String cinvid,boolean isinv) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null||PuPubVO.getString_TrimZeroLenAsNull(cinvid)==null)
			return null;

		StringBuffer str = new StringBuffer();
		str.append(" select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 ");
		str.append(" and cbiddingid = '"+cbiddingid+"' ");
		str.append(" and ccircalnoid = '"+ccirclenoid+"'");
		if(isinv){
			str.append(" and cinvmanid = '"+cinvid+"'");
		}else{
			str.append(" and cinvclid = '"+cinvid+"'");
		}
		
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(str.toString(), ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	public Map<String, UFDouble> getSubmitPriceInfor(String cbiddingid,String ccirclid,String cvendorid,boolean isinv) throws BusinessException{
		StringBuffer sql = new StringBuffer("select ");
		if(isinv)
			sql.append("cinvmanid");
		else 
			sql.append("cinvclid");
		sql.append(",nprice from zb_submitprice where");
		sql.append(" cbiddingid = '"+cbiddingid+"' and cvendorid = '"+cvendorid+"' and ccircalnoid = '"+ccirclid+"' and isnull(dr,0)=0");
		
		List ldata = (List)getDao().executeQuery(sql.toString(), ResultSetProcessorTool.ARRAYLISTPROCESSOR);
		if(ldata ==  null || ldata.size() == 0)
			return null;
		Map<String,UFDouble> priceInfor = new HashMap<String, UFDouble>();
		Object[] os = null;
		int len = ldata.size();
		for(int i=0;i<len;i++){
			os = (Object[])ldata.get(i);
			if(os == null || os.length == 0)
				return null;
			priceInfor.put(ZbPubTool.getString_NullAsTrimZeroLen(os[0]), PuPubVO.getUFDouble_NullAsZero(os[1]));
		}
		return priceInfor;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��У�����͹�Ӧ���Ƿ����
	 * 2011-5-4����01:20:11
	 * @param cbiddingid ����id
	 * @param cvendorid ��Ӧ�̹���id
	 * @throws BusinessException
	 */
	public void checkVendorForBidding(String cbiddingid, String cvendorid)
			throws BusinessException {
		if (PuPubVO.getString_TrimZeroLenAsNull(cbiddingid) == null
				|| PuPubVO.getString_TrimZeroLenAsNull(cvendorid) == null)
			throw new BusinessException("�����쳣");
		String sql = "select count(0) from zb_biddingsuppliers where cbiddingid = '"
				+ cbiddingid
				+ "' "
				+ "and ccustmanid = '"
				+ cvendorid
				+ "' and isnull(dr,0)=0  and coalesce(fisclose,'N') = 'N'";
		int index = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql,
				ResultSetProcessorTool.COLUMNPROCESSOR),
				ZbPubTool.INTEGER_ZERO_VALUE);
		if (index > 0)
			return;
		throw new BusinessException("�ù�Ӧ�̲��ܲ���ѡ�����ı���");
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ�ֳ����۲����ı������±��۴���
	 * 2011-5-7����01:48:13
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getPreCircalnoForLocal(String cbiddingid,String cvendorid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return null;
		StringBuffer sql = new StringBuffer("select ccircalnoid from (select ccircalnoid from zb_submitprice " +
				" where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null){
			sql.append(" and cvendorid = '"+cvendorid+"'");
		}
		sql.append(" order by ts desc) where  rownum < 2");
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql.toString(), ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ���ϱ��۵ĵ�һ�ִ�  �� ���������ϱ��۵��ִ�
	 * 2011-5-7����01:48:13
	 * @param cbiddingid
	 * @return
	 * @throws BusinessException
	 */
	private String getFirstCircalnoForWeb(String cbiddingid) throws BusinessException{
		if(PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			return null;
		String sql = "select cbiddingtimesid from (select cbiddingtimesid from zb_biddingtimes where isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"' " +
				" and  coalesce(bisfollow,'N')='N' order by tbigintime) where  rownum < 2";
		return PuPubVO.getString_TrimZeroLenAsNull(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ���鱨����Ϣ ����ִ�idΪ�� ����Ϊû�б�����
	 * 2011-5-4����10:46:22
	 * @param cbiddingid ����id
	 * @param ccirclenoid ���ִ�id  Ϊ�ձ�ʾ��һ�α���
	 * @param cvendorid ��Ӧ��id
	 * @param isinv �Ƿ���ϸ�б�
	 * @return
	 * @throws BusinessException
	 */
	public  SubmitPriceVO[] getPriceVos(String cbiddingid,String ccirclenoid,String cvendorid,UFBoolean isinv,Integer isubtype) throws Exception{
		//У������
		checkVendorForBidding(cbiddingid, cvendorid);
		//��ȡ����ı��� Ʒ��  ��Ϣ     ��ȡ����Ʒ���ڱ����ִ��ڵ���ͱ���  ����
		String whereSql = "cbiddingid = '"+cbiddingid+"' and isnull(dr,0)=0";
		List<BiddingBodyVO> ldata = (List<BiddingBodyVO>)getDao().retrieveByClause(BiddingBodyVO.class, whereSql);
		if(ldata == null || ldata.size() == 0)
			throw new BusinessException("������ϢΪ��");

		BiddingBodyVO[] biddVos = ldata.toArray(new BiddingBodyVO[0]);
		SubmitPriceVO[] prices = (SubmitPriceVO[])SingleVOChangeDataBsTool.
		runChangeVOAry(biddVos, SubmitPriceVO.class, CHGBiddingBodyTOSubPrice.class.getName());

		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE||PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null){
			//			��ȡ�ϴα����ִ�
			ccirclenoid = getPreCircalnoForLocal(cbiddingid,cvendorid);
		}

		if(isubtype != ZbPubConst.WEB_SUBMIT_PRICE)//�ֳ����ۻ�ȡ ��ͱ���
			for(SubmitPriceVO price:prices){
				price.setNllowerprice(getLowestPrice(cbiddingid, null,price.getInvID(isinv), isinv.booleanValue()));
			}
		
		if(PuPubVO.getString_TrimZeroLenAsNull(ccirclenoid)==null){//��һ�α���
			return prices;
		}


		//��ȡ�ù�Ӧ�����ִα���
		Map<String,UFDouble> priceInfor = getSubmitPriceInfor(cbiddingid, ccirclenoid, cvendorid, isinv.booleanValue());
		if(priceInfor != null && priceInfor.size() > 0){
			for(SubmitPriceVO price:prices){
				String key = price.getInvID(isinv);
				price.setNlastprice(priceInfor.get(key));
				if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE)
					price.setNllowerprice(getLastLowerPrice(cbiddingid, ccirclenoid,key , isinv.booleanValue()));

			}
		}

		

		return prices;
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��Ӧ����ͱ���  ���һ�ִεı���
	 * 2011-5-5����02:08:55
	 * @param cbiddingid ����id
	 * @param cvendorid ��Ӧ��id
	 * @param cinvid Ʒ��
	 * @param isinv �Ƿ���ϸ����
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getVendorLowestPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{
//		String sql = " select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"' and cvendorid = '"+cvendorid+"'";
//		if(isinv)
//			sql = sql+" and cinvmanid = '"+cinvid+"'";
//		else 
//			sql = sql + " and cinvclid = '"+cinvid+"'";
//		sql = sql + " order by nprice";
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)==null||PuPubVO.getString_TrimZeroLenAsNull(cbiddingid)==null)
			throw new BusinessException("�����쳣");
		UFDouble nprice = getLowestPrice(cbiddingid, cvendorid, cinvid, isinv);
//			PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
		return nprice;
	}
    
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ��	��ȡ��θ�Ʒ�ֵ���ͱ���   ������빩Ӧ���򷵻ظù�Ӧ�̵���ͱ���
	 * 2011-5-5����02:12:11
	 * @param cbiddingid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getLowestPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{
		String sql = " select min(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";// and cvendorid = '"+cvendorid+"'";
		if(isinv)
			sql = sql+" and cinvmanid = '"+cinvid+"'";
		else 
			sql = sql + " and cinvclid = '"+cinvid+"'";

		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null)
			sql = sql + " and cvendorid = '"+cvendorid+"'";
		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ�����е���߼�  
	 * 2011-5-23����11:15:32
	 * @param cbiddingid
	 * @param cinvid
	 * @param isinv
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getHignPrice(String cbiddingid,String cvendorid,String cinvid,boolean isinv) throws BusinessException{

		String sql = " select max(nprice)nprice from zb_submitprice where  isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'";// and cvendorid = '"+cvendorid+"'";
		if(isinv)
			sql = sql+" and cinvmanid = '"+cinvid+"'";
		else 
			sql = sql + " and cinvclid = '"+cinvid+"'";
		if(PuPubVO.getString_TrimZeroLenAsNull(cvendorid)!=null)
			sql = sql + " and cvendorid = '"+cvendorid+"'";

		return PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR));
	
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡƽ������   ��߼�+��ͼ�/2
	 * 2011-5-23����11:06:14
	 * @param cbiddingid
	 * @param cinvid
	 * @return
	 * @throws BusinessException
	 */
	public UFDouble getAveragePrice(String cbiddingid,String cvendorid , String cinvid) throws BusinessException{
		UFDouble nmin = getLowestPrice(cbiddingid, cvendorid,cinvid, true);
		UFDouble nmax = getHignPrice(cbiddingid, cvendorid,cinvid, true);
		return (nmin.add(nmax)).div(2);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ��Ӧ�̱�����Ϣ
	 * 2011-5-5����03:08:03
	 * @param cbiddingid
	 * @return MAP  //key invclid + invmanid + cvendorid   value:submitvo
	 * @throws BusinessException
	 */
	public Map<String,List<SubmitPriceVO>> getVendorSubmitPrices(String cbiddingid) throws BusinessException{
		List<SubmitPriceVO> lprice = (List<SubmitPriceVO>)getDao().retrieveByClause(SubmitPriceVO.class, " isnull(dr,0)=0 and cbiddingid = '"+cbiddingid+"'");
		if(lprice == null || lprice.size() == 0)
			throw new BusinessException("��ȡ�ñ���ı�����ϢΪ��");
		Map<String,List<SubmitPriceVO>> priceInfor = new HashMap<String, List<SubmitPriceVO>>();
		
		List<SubmitPriceVO> ltmp = null;
		String key = null;
		for(SubmitPriceVO price:lprice){
			key = ZbPubTool.getString_NullAsTrimZeroLen(price.getCinvclid())+ZbPubTool.getString_NullAsTrimZeroLen(price.getCinvmanid())
			+ZbPubTool.getString_NullAsTrimZeroLen(price.getCvendorid());
			if(priceInfor.containsKey(key)){
				ltmp = priceInfor.get(key);
			}else
				ltmp = new ArrayList<SubmitPriceVO>();
			ltmp.add(price);
			priceInfor.put(key, ltmp);
		}
		return priceInfor;
	}
	
	private int getSubType(String cbiddingid) throws BusinessException{
		String sql = "select izbtype from zb_bidding_h where cbiddingid = '"+cbiddingid+"'";
		return PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR), ZbPubConst.LOCAL_SUBMIT_PRICE);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ�����۵�����ͨ�����뱨����ϸ����
	 * 2011-5-25����08:01:06
	 * @param bill
	 * @throws Exception
	 */
	public void PushSaveSubmitPriceByBill(SubmitPriceBillVO bill) throws Exception{
		if(bill == null)
			throw new BusinessException("����Ϊ��");
		SubmitPriceBodyVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length ==0)
			throw new BusinessException("����Ϊ��");
		SubmitPriceHeaderVO head = bill.getHeader();
		int isubtype = getSubType(head.getCbiddingid());
		
		String circalid = null;
		/**
		 * ��ʱ�ڱ��۵�ǰ̨����δ֧���ִεĲ���¼��  ���б�Ҫ���� �����ϱ������͵� ���� ��֧��ѡ�� �������ϱ��۵��ִ�  �ڴ˴�¼��
		 */
		if(isubtype == ZbPubConst.WEB_SUBMIT_PRICE && PuPubVO.getString_TrimZeroLenAsNull(circalid)==null){
			circalid = getFirstCircalnoForWeb(head.getCbiddingid());
		}
		
		SubmitPriceVO[] prices = new SubmitPriceVO[bodys.length];
		SubmitPriceVO tmp = null;
		int index = 0;
		for(SubmitPriceBodyVO body:bodys){
			tmp = new SubmitPriceVO();
			tmp.setPk_corp(head.getPk_corp());
			tmp.setCbiddingid(head.getCbiddingid());
			tmp.setCcircalnoid(circalid);
			tmp.setCoprator(head.getVapproveid());
			tmp.setTmaketime(new UFDateTime(System.currentTimeMillis()).toString());
			tmp.setStatus(VOStatus.NEW);
			prices[index] = tmp;
			index++;
		}
		
		SingleVOChangeDataBsTool.runChangeVOAry(bodys, prices, CHGSubBodyTOSubPrice.class.getName());
		
		for(SubmitPriceVO price:prices){
			price.setCvendorid(head.getCvendorid());
			price.setIsubmittype(ZbPubConst.SELF_SUBMIT_PRICE);
			price.validationOnSubmit(true, isubtype);
		}		
//		getDao().insertVOArray(prices);		
		submitPrice(prices, ZbPubConst.SELF_SUBMIT_PRICE, null);
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ���˴���ʱ��������  ��������Ҫ����  �Ƿ�����������
	 * 2011-5-25����08:16:59
	 * @throws BusinessException
	 */
	public void deletePrice(SubmitPriceBillVO bill) throws BusinessException{
		if(bill == null)
			return;
		SubmitPriceHeaderVO head = bill.getHeader();
		String cbiddingid = head.getCbiddingid();
		String sql = " select ibusstatus from zb_bidding_h where cbiddingid = '"+cbiddingid+"'";
		int ista = PuPubVO.getInteger_NullAs(getDao().executeQuery(sql, ResultSetProcessorTool.COLUMNPROCESSOR),-1);
		if(ista != ZbPubConst.BIDDING_BUSINESS_STATUE_INIT)
			throw new BusinessException("�����ѽ���Ͷ��׶�,�������󱨼۵�");
		
		SubmitPriceBodyVO[] bodys = bill.getBodys();
		if(bodys == null || bodys.length == 0)
			return;
		String[] ids = new String[bodys.length];
		int index = 0;
		for(SubmitPriceBodyVO body:bodys){
			ids[index] = body.getPrimaryKey();
			index ++;
		}
		sql = "update zb_submitprice set dr = 1 where vdef1  in "+ZbPubTool.getSubSql(ids);
		getDao().executeUpdate(sql);
	}
	

	/**
	 * У�鱾�ֱ����Ƿ�С���ϴα���
	 * @author zhw
	 * @˵�������׸ڿ�ҵ��
	 * 2011-6-19����04:10:33
	 * @param prices
	 * @throws BusinessException 
	 */
	public void checkMinPriceZB09(SubmitPriceBillVO billvo) throws BusinessException{
		if(billvo==null)
			return;
		SubmitPriceHeaderVO head = billvo.getHeader();
		SubmitPriceBodyVO[] bodys = billvo.getBodys();
		if(head == null)
			return;
		String cbiddingid = head.getCbiddingid();
		String vendorid = head.getCvendorid();
		String sql = " select e.cinvmanid,min(e.nprice) from zb_submitprice e where e.cbiddingid ='"+cbiddingid+"' and e.cvendorid ='"+vendorid+"' and isnull(e.dr,0)=0 group by e.cinvmanid";
		ArrayList al = (ArrayList)getDao().executeQuery(sql,ZbBsPubTool.ARRAYLISTPROCESSOR);
		if(al ==null || al.size()==0)
			return;
		int size = al.size();
		if(bodys==null || bodys.length==0)
			return;
		Map map = new HashMap();
		for(SubmitPriceBodyVO price:bodys){
			if(!map.containsKey(price.getCinvmanid()))
				map.put(price.getCinvmanid(),price.getNprice());
		}
		for(int i=0;i<size;i++){
			Object o = al.get(i);
			if(o==null)
				continue;
			Object[] os = (Object[])o;
			if(os ==null || os.length==0)
				continue;
			if(map.containsKey(os[0]))
				if(PuPubVO.getUFDouble_NullAsZero(map.get(os[0])).compareTo(PuPubVO.getUFDouble_NullAsZero(os[1]))>0)
					throw new BusinessException("���ֱ��۳������ֱ���");
		}
		
	}
}
