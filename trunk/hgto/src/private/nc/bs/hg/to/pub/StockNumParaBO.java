package nc.bs.hg.to.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bd.accperiod.AccountCalendar;
import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.hg.pu.pub.HgBsPubTool;
import nc.itf.ic.pub.IInvOnHand;
import nc.jdbc.framework.util.SQLHelper;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.hg.pu.check.fund.FUNDSETVO;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.hg.to.pub.StockNumParaVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.to.pub.BillVO;

public class StockNumParaBO {
	
	private BaseDAO dao = null;
	private BaseDAO getDao(){
		if(dao == null){
			dao = new BaseDAO();
		}
		return dao;
	}
	
	private IInvOnHand  onHandBO = null;
	private IInvOnHand getOnHandBO(){
		if(onHandBO == null){
			onHandBO = (IInvOnHand)NCLocator.getInstance().lookup(IInvOnHand.class.getName());
		}
		return onHandBO;
	}
	
	public BillVO[]  getStockNumPara2(BillVO[] billvos) throws BusinessException{
		if(billvos == null || billvos.length == 0)
			return null;
		StockNumParaVO[] tmpParaVos = null;
		for(BillVO billvo:billvos){
			tmpParaVos = getStockNumPara(HgPubTool.transBillItemsToStockNumPara(billvo,null));
			billvo.setStockNumInfor(tmpParaVos);
		}
//		��ȡ���빫˾�Ŀ����ʽ�Ϳ����޶�
		String csourcetype = PuPubVO.getString_TrimZeroLenAsNull(billvos[0].getItemVOs()[0].getCsourcetypecode());
		if(csourcetype == null || (!csourcetype.equalsIgnoreCase(HgPubConst.PLAN_MNY_BILLTYPE)&&!csourcetype.equalsIgnoreCase(HgPubConst.PLAN_MONTH_BILLTYPE)&&!csourcetype.equalsIgnoreCase(HgPubConst.PLAN_TEMP_BILLTYPE))){
			return billvos;
		}
		
		setFundInfor(csourcetype, billvos,null);
		
		return billvos;
	}
	
	private void setFundInfor(String csourcetype,BillVO[] billvos,UFDate ulogdate) throws BusinessException{
		int ifundtype = HgPubTool.getIFundTypeByPlanType(csourcetype);
		FUNDSETVO vofund = null;
		FUNDSETVO vomny = null;
		BillVO billvo = billvos[0];
		UFDate uDate = ulogdate == null?new UFDate(billvo.getChildrenVO()[0].getAttributeValue("SDATE").toString()):ulogdate;
		String corp = billvo.getHeaderVO().getCincorpid();
		if(ifundtype == HgPubConst.FUND_CHECK_SPECIALFUND){
			vofund = getFundSet(corp, uDate, ifundtype);
		}else if(ifundtype == HgPubConst.FUND_CHECK_FUND_QUATO){
			vofund = getFundSet(corp, uDate, HgPubConst.FUND_CHECK_FUND);
			vomny = getFundSet(corp, uDate, HgPubConst.FUND_CHECK_QUATO);
		}
		
		for(BillVO bill:billvos){
			if(vofund!=null){
				bill.setNallfund(PuPubVO.getUFDouble_NullAsZero(vofund.getNfund()));
				bill.setNfund(PuPubVO.getUFDouble_NullAsZero(vofund.getNfund()).sub(PuPubVO.getUFDouble_NullAsZero(vofund.getNlockfund()).add(PuPubVO.getUFDouble_NullAsZero(vofund.getNactfund()))));
			}
			if(vomny!=null){
				bill.setNallmny(PuPubVO.getUFDouble_NullAsZero(vomny.getNfund()));
				bill.setNmny(PuPubVO.getUFDouble_NullAsZero(vomny.getNfund()).sub(PuPubVO.getUFDouble_NullAsZero(vomny.getNlockfund()).add(PuPubVO.getUFDouble_NullAsZero(vomny.getNactfund()))));
			
			}
		}
	}
	
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����ȡ�ʽ�����vo
	 * 2011-4-25����03:11:20
	 * @param scorpid
	 * @param uDate
	 * @param ifundtype
	 * @return
	 * @throws BusinessException
	 */
	private FUNDSETVO getFundSet(String scorpid, UFDate uDate, int ifundtype) throws BusinessException {
		String corp = PuPubVO.getString_TrimZeroLenAsNull(scorpid);
		if (corp == null)
			return null;
		
		StringBuffer strb = new StringBuffer();

		strb.append(" pk_corp = '"+corp+"' and isnull(dr,0)=0 ");
		// add by zhw 2010-12-28 ��ȡ��ǰ���ڶ�Ӧ�Ļ���� �����
		// �õ���׼�ڼ䷽��������ʵ��
		AccountCalendar calendar = AccountCalendar.getInstance();
		calendar.setDate(uDate);
		// ��������
		String iyear = calendar.getYearVO().getPeriodyear();
		String imonth = calendar.getMonthVO().getMonth();

		// zhw end
		strb.append(" and cyear = '" + String.valueOf(iyear)
				+ "' and imonth = '" + String.valueOf(imonth) + "'");
		strb.append(" and coalesce(fcontrol,'N')='Y' and ifundtype = "
				+ ifundtype);
		strb.append(" and vdef2 = '" + SQLHelper.getCorpPk() + "'");
		java.util.Collection c = getDao().retrieveByClause(FUNDSETVO.class,
				strb.toString());
		if (c == null || c.size() == 0 || c.size() > 1)
				return null;
		java.util.Iterator it = c.iterator();
		FUNDSETVO vo = (FUNDSETVO) it.next();
		return vo;
	}
 	
	private StockNumParaVO[] getStockNumPara(StockNumParaVO[] paraVos) throws BusinessException{
		if(paraVos == null || paraVos.length == 0)
			return null;
		//��ȡ�˴���
//		1����ȡ�ִ���
		getOnhandNum(paraVos);
		for(StockNumParaVO para:paraVos){
//			2����ȡ������˾��Ϣ �������������е�����������   �ѵ�������
			getOutNums(para);
//			3����ȡ���빫˾��Ϣ ���뷽�������е�����������    ����������	
			getInNums(para);
			
			getOutCloseNum(para);
			getInCloseNum(para);
		}
		return paraVos;
	}
	
	public BillVO getStockNumParaOnRefresh(BillVO billvo,UFDate uLogDate) throws BusinessException{
		if(billvo == null)
			return null;
		
		StockNumParaVO[] paraVos = HgPubTool.transBillItemsToStockNumPara(billvo,uLogDate);
		
		paraVos = getStockNumPara(paraVos);
//		//��ȡ�˴���
////		1����ȡ�ִ���
//		getOnhandNum(paraVos);
//		for(StockNumParaVO para:paraVos){
////			2����ȡ������˾��Ϣ �������������е�����������   �ѵ�������
//			getOutNums(para);
////			3����ȡ���빫˾��Ϣ ���뷽�������е�����������    ����������	
//			getInNums(para);
//			
//			getOutCloseNum(para);
//			getInCloseNum(para);
//		}
		billvo.setStockNumInfor(paraVos);
		setFundInfor(billvo.getItemVOs()[0].getCsourcetypecode(), new BillVO[]{billvo},uLogDate);
		return billvo;
	}
	
	private void getOnhandNum(StockNumParaVO[] paraVos) throws BusinessException{
		List<String> linvman = new ArrayList<String>();
		List<String> linvman2 = new ArrayList<String>();
		for(StockNumParaVO para:paraVos){
			if(!linvman.contains(para.getCoutinvid()))
				linvman.add(para.getCoutinvid());	
			if(!linvman2.contains(para.getCininvid()))
				linvman2.add(para.getCininvid());	
		}
		if(linvman.size()==0||linvman2.size()==0)
			throw new BusinessException("��ȡ�����쳣�������ϢΪ��");
		String[] invmanids = linvman.toArray(new String[0]);
		String[] invmanids2 = linvman2.toArray(new String[0]);
		UFDouble[] outOnHandNums = getOnHandBO().getOnhandNums(paraVos[0].getCoutcorpid(), paraVos[0].getCoutcalbodyid(), PuPubVO.getString_TrimZeroLenAsNull(paraVos[0].getCoutwarehouseid()), invmanids);
		UFDouble[] inOnHandNums = getOnHandBO().getOnhandNums(paraVos[0].getCincorpid(), paraVos[0].getCincalbodyid(), PuPubVO.getString_TrimZeroLenAsNull(paraVos[0].getCinwarehouseid()), invmanids2);
		
		if(outOnHandNums == null||outOnHandNums.length ==0 || outOnHandNums.length!=invmanids.length)
			throw new BusinessException("��ȡ������˾�������쳣");
		if(inOnHandNums == null||inOnHandNums.length ==0 || inOnHandNums.length!=invmanids.length)
			throw new BusinessException("��ȡ���빫˾�������쳣");
		int index = 0;
		Map<String, UFDouble> outNumInfor = new HashMap<String, UFDouble>();
		Map<String, UFDouble> inNumInfor = new HashMap<String, UFDouble>();
		for(String invmanid:invmanids){
			outNumInfor.put(invmanid, PuPubVO.getUFDouble_NullAsZero(outOnHandNums[index]));
//			inNumInfor.put(invmanid, PuPubVO.getUFDouble_NullAsZero(inOnHandNums[index]));
			index++;
		}
		index = 0;
		for(String invmanid:invmanids2){
//			outNumInfor.put(invmanid, PuPubVO.getUFDouble_NullAsZero(outOnHandNums[index]));
			inNumInfor.put(invmanid, PuPubVO.getUFDouble_NullAsZero(inOnHandNums[index]));
			index++;
		}
		
		for(StockNumParaVO para:paraVos){
			para.setNoutonhand(outNumInfor.get(para.getCoutinvid()));
			para.setNinonhand(inNumInfor.get(para.getCininvid()));
		}
	}
	
	private void getInNums(StockNumParaVO para) throws BusinessException{

//		/������ �����ܵ�����������   �ѷ�������		
		AccperiodmonthVO cuMonth = HgPubTool.getCurrentMonth();		
		StringBuffer sql = new StringBuffer(" select sum(b.nnum) ninallnum ,sum(b.norderinnum) ninnum from to_bill h inner join to_bill_b b on h.cbillid = b.cbillid where " +
				" h.cincorpid = '"+para.getCincorpid()+"' and h.cincbid = '"+para.getCincalbodyid()+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(para.getCoutwarehouseid())!=null)
			sql.append(" and h.cinwhid = '"+para.getCinwarehouseid()+"'");
		sql.append(" and b.cininvid = '"+para.getCininvid()+"'");
		sql.append(" and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and (h.fstatusflag = 4 or h.fstatusflag = 7) and coalesce(b.bretractflag,'N') = 'N'");
		sql.append(" and h.dbilldate >= '"+cuMonth.getBegindate().toString().trim()
				+"'and h.dbilldate <= '"+cuMonth.getEnddate().toString().trim()+"'");
		
		Object[] os = (Object[])getDao().executeQuery(sql.toString(), HgBsPubTool.ARRAYPROCESSOR);
		if(os == null)
			return;
		para.setNinallnum(PuPubVO.getUFDouble_NullAsZero(os[0]));
		para.setNinnum(PuPubVO.getUFDouble_NullAsZero(os[1]));
	
	}
	
	private void getOutNums(StockNumParaVO para) throws BusinessException{
//		/������ �����ܵ�����������   �ѷ�������		
		AccperiodmonthVO cuMonth = HgPubTool.getCurrentMonth();		
		StringBuffer sql = new StringBuffer(" select sum(b.nnum) noutallnum ,sum(b.norderoutnum) noutnum from to_bill h inner join to_bill_b b on h.cbillid = b.cbillid where " +
				" h.coutcorpid = '"+para.getCoutcorpid()+"' and h.coutcbid = '"+para.getCoutcalbodyid()+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(para.getCoutwarehouseid())!=null)
			sql.append(" and h.coutwhid = '"+para.getCoutwarehouseid()+"'");
		sql.append(" and b.coutinvid = '"+para.getCoutinvid()+"'");
		sql.append(" and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and (h.fstatusflag = 4 or h.fstatusflag = 7) and coalesce(b.bretractflag,'N') = 'N'");
		sql.append(" and h.dbilldate >= '"+cuMonth.getBegindate().toString().trim()+"'and h.dbilldate <= '"+cuMonth.getEnddate().toString().trim()+"'");
		
		Object[] os = (Object[])getDao().executeQuery(sql.toString(), HgBsPubTool.ARRAYPROCESSOR);
		if(os == null)
			return;
		para.setNoutallnum(PuPubVO.getUFDouble_NullAsZero(os[0]));
		para.setNoutnum(PuPubVO.getUFDouble_NullAsZero(os[1]));
	}
	
	private void getOutCloseNum(StockNumParaVO para) throws BusinessException{
//		/������ �����ܵ�����������   �ѷ�������		
		AccperiodmonthVO cuMonth = HgPubTool.getCurrentMonth();		
		StringBuffer sql = new StringBuffer(" select sum(coalesce(b.nnum,0.0)-coalesce(b.norderoutnum,0.0)) noutallnum  from to_bill h inner join to_bill_b b on h.cbillid = b.cbillid where " +
				" h.coutcorpid = '"+para.getCoutcorpid()+"' and h.coutcbid = '"+para.getCoutcalbodyid()+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(para.getCoutwarehouseid())!=null)
			sql.append(" and h.coutwhid = '"+para.getCoutwarehouseid()+"'");
		sql.append(" and b.coutinvid = '"+para.getCoutinvid()+"'");
		sql.append(" and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and (h.fstatusflag = 4 or h.fstatusflag = 7) and coalesce(b.bretractflag,'N') = 'N'");
		sql.append(" and h.dbilldate >= '"+cuMonth.getBegindate().toString().trim()+"' and h.dbilldate <= '"+cuMonth.getEnddate().toString().trim()+"'");
		sql.append(" and b.frowstatuflag = 7 ");
		
		UFDouble ndef1 = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql.toString(), HgBsPubTool.COLUMNPROCESSOR));
		
		para.setNdef1(ndef1);
//		para.setNoutnum(PuPubVO.getUFDouble_NullAsZero(os[1]));
	}
	
	private void getInCloseNum(StockNumParaVO para) throws BusinessException{

		//		/������ �����ܵ�����������   �ѷ�������		
		AccperiodmonthVO cuMonth = HgPubTool.getCurrentMonth();		
		StringBuffer sql = new StringBuffer(" select sum(coalesce(b.nnum,0.0)-coalesce(b.norderinnum,0.0)) ninallnum from to_bill h inner join to_bill_b b on h.cbillid = b.cbillid where " +
				" h.cincorpid = '"+para.getCincorpid()+"' and h.cincbid = '"+para.getCincalbodyid()+"'");
		if(PuPubVO.getString_TrimZeroLenAsNull(para.getCoutwarehouseid())!=null)
			sql.append(" and h.cinwhid = '"+para.getCinwarehouseid()+"'");
		sql.append(" and b.cininvid = '"+para.getCininvid()+"'");
		sql.append(" and isnull(h.dr,0)=0 and isnull(b.dr,0)=0 and (h.fstatusflag = 4 or h.fstatusflag = 7) and coalesce(b.bretractflag,'N') = 'N'");
		sql.append(" and h.dbilldate >= '"+cuMonth.getBegindate().toString().trim()
				+"' and h.dbilldate <= '"+cuMonth.getEnddate().toString().trim()+"'");
		sql.append(" and b.frowstatuflag = 7 ");
		UFDouble ndef2 = PuPubVO.getUFDouble_NullAsZero(getDao().executeQuery(sql.toString(), HgBsPubTool.COLUMNPROCESSOR));

		para.setNdef2(ndef2);

	}

}
