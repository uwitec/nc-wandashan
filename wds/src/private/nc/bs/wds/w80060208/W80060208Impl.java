package nc.bs.wds.w80060208;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uif.pub.IUifService;
import nc.itf.wds.w80060208.Iw80060208;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBDGetCheckClass2;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

public class W80060208Impl implements Iw80060208 {

	public AggregatedValueObject saveBD80060208(AggregatedValueObject billVO, Object userObj ) throws Exception {
		// TODO Auto-generated method stub
		//保存页面
		ArrayList params=(ArrayList) userObj;
//		AggregatedValueObject retVo = nc.ui.trade.business.HYPubBO_Client
//        .saveBD(billVO, params.get(0));
		 nc.itf.uif.pub.IUifService service = (IUifService) NCLocator.getInstance().lookup( 
					IUifService.class.getName());
		 AggregatedValueObject retVo=service.saveBD(billVO, params.get(0));
		//回写相关数据
		String csaleid="";
		if(null!=params&&params.size()>0&&null!=params.get(1)){
			csaleid = params.get(1).toString();
		}
		
		IUAPQueryBS query = (IUAPQueryBS) NCLocator.getInstance().lookup(
				IUAPQueryBS.class.getName());
		SoSaleVO ob = (SoSaleVO) query.retrieveByPK(SoSaleVO.class, csaleid);
		if(null!=ob.getVdef6()&&!"".equals(ob.getVdef6())){
			if(ob.getVdef6().equals("0")){
				ob.setVdef7((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()));
			}
		}
		
		ob.setVdef6("1");
		// 修改对象
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
				.getInstance().lookup(IVOPersistence.class.getName());
		iVOPersistence.updateVO(ob);
		// 拆分结束，剩下货物自动生成一张拆分单
		String fyd_splitend="";
		if(null!=params&&params.size()>0&&null!=params.get(2)){
			fyd_splitend = params.get(2).toString();
		}
		// 表头VO
		TbFydnewVO tbFydnewVO=new TbFydnewVO();
		if(null!=params&&params.size()>0&&null!=params.get(3)){
			tbFydnewVO = (TbFydnewVO) params.get(3);
		}
		// 表体VO
		TbFydmxnewVO[] tbFydmxnewVO=null;
		if(null!=params&&params.size()>0&&null!=params.get(4)){
			tbFydmxnewVO = (TbFydmxnewVO[]) params.get(4);
		}
		
		
		// 判断是否还剩有货物
		boolean isSurplus = false;
		if ("true".equals(fyd_splitend)) {

			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
				if (cfd_syfsl != 0) {
					isSurplus = true;
				}
			}
			if (!isSurplus) {
				tbFydnewVO.setFyd_splitstatus(2);
				iVOPersistence.updateVO(tbFydnewVO);
			}
			tbFydnewVO.setFyd_pk(null);
			tbFydnewVO.setFyd_splitstatus(2);
			//但据状态
			tbFydnewVO.setVbillstatus(1);
			//发运状态
			tbFydnewVO.setFyd_fyzt(0);
			tbFydnewVO.setDr(0);
			// 拆分单据号
			String spno = tbFydnewVO.getSplitvbillno();
			// 判断获得的订单号是否为空
			if (spno != null && !"".equals(spno)) {
				// 如果不为空进行字符串截取，截取到最后出现"-"到最后的值
				// 非数字部分
				String nonum = spno.substring(0, spno.lastIndexOf("-") + 2);
				// 整数部分
				String n = spno.substring(spno.lastIndexOf("-") + 2,
						spno.length()).toString();
				int numno = Integer.parseInt(n.trim());
				numno += 1;
				String splitvbillno = numno + "";
				while (splitvbillno.length() < 2) {
					splitvbillno = "0" + splitvbillno;
				}
				tbFydnewVO.setSplitvbillno(nonum + splitvbillno);
			}
			// 单据日期
			tbFydnewVO.setDmakedate(new UFDate(new Date()));

			// 添加主表
			String fyd_pk = "";
			if (isSurplus) {
				fyd_pk = iVOPersistence.insertVO(tbFydnewVO);
			}

			// 剩余VO
			ArrayList newTbFydmxnewVO = new ArrayList();
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
				if (cfd_syfsl != 0) {
					tbFydmxnewVO[i]
							.setCfd_ysyfsl(tbFydmxnewVO[i].getCfd_sysl());
					tbFydmxnewVO[i].setCfd_yfsl(tbFydmxnewVO[i].getCfd_sysl());
					tbFydmxnewVO[i].setCfd_ysxs(tbFydmxnewVO[i].getCfd_syfsl());
					tbFydmxnewVO[i].setCfd_xs(tbFydmxnewVO[i].getCfd_syfsl());
					tbFydmxnewVO[i].setCfd_sysl(new UFDouble(0));
					tbFydmxnewVO[i].setCfd_syfsl(new UFDouble(0));
					tbFydmxnewVO[i].setCfd_pk(null);
					tbFydmxnewVO[i].setFyd_pk(fyd_pk);
					tbFydmxnewVO[i].setDr(0);
					newTbFydmxnewVO.add(tbFydmxnewVO[i]);
					isSurplus = true;
				}
			}
			if (isSurplus) {
				iVOPersistence.insertVOList(newTbFydmxnewVO);
			}

			ob.setVdef6("2");
			iVOPersistence.updateVO(ob);
		} else {
			for (int i = 0; i < tbFydmxnewVO.length; i++) {
				double cfd_syfsl = tbFydmxnewVO[i].getCfd_syfsl().doubleValue();
				if (cfd_syfsl != 0) {
					isSurplus = true;
				}
			}
			if (!isSurplus) {
				ob.setVdef6("2");
				iVOPersistence.updateVO(ob);
				tbFydnewVO.setFyd_splitstatus(2);
				iVOPersistence.updateVO(tbFydnewVO);
			}
		}
		
		return retVo;
	}

	public int saveAndCommit80060208(AggregatedValueObject billVO, Object userObj) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public AggregatedValueObject deleteBD80060208(AggregatedValueObject billVO,
			Object userObj) throws Exception  {
		// TODO Auto-generated method stub
		// 删除页面
		ArrayList params=(ArrayList) userObj;
//		AggregatedValueObject vo = nc.ui.trade.business.HYPubBO_Client
//        .deleteBD(billVO, params.get(0));
		 nc.itf.uif.pub.IUifService service = (IUifService) NCLocator.getInstance().lookup( 
					IUifService.class.getName());
		 AggregatedValueObject retVo=service.deleteBD(billVO, params.get(0));
		//修改
//		int aaav=1/0;
		IVOPersistence iVOPersistence = (IVOPersistence) NCLocator
		.getInstance().lookup(IVOPersistence.class.getName());
		// 删除第一次拆分的单据
		ArrayList tbfydvos=new ArrayList();
		if(null!=params&&params.size()>0&&null!=params.get(1)){
			tbfydvos =  (ArrayList) params.get(1);
		}
		SoSaleVO ob=new SoSaleVO();
		if(null!=params&&params.size()>0&&null!=params.get(2)){
			ob =  (SoSaleVO) params.get(2);
		}
		if (null != tbfydvos && tbfydvos.size() > 0) {
			Object[] tbfydvo = (Object[]) tbfydvos.get(0);
			if (null != tbfydvo && tbfydvo.length > 0 && null != tbfydvo[0]) {
				if (Integer.parseInt(tbfydvo[1].toString()) == 0) {
					ob.setVdef6("0");
					ob.setVdef7(null);
					ob.setIprintcount(null);
					iVOPersistence.updateVO(ob);
				}
			}
		}
        return null;
	}

	public AggregatedValueObject prinDB80060208(TbFydnewVO tbFydnewVO,
			SoSaleVO ob) throws Exception {
		// TODO Auto-generated method stub
		IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
				IVOPersistence.class.getName());
		ivo.updateVO(tbFydnewVO);
		ivo.updateVO(ob);
		return null;
	}

	

}
