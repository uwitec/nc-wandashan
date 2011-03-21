package nc.bs.ncblk.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.ncblk.constants.Constants;
import nc.bs.ncblk.dmo.IyjcbDMO;
import nc.bs.ncblk.dmo.impl.YjcbDMOImpl;
import nc.bs.ncblk.exception.MyBsBeanException;
import nc.bs.ncblk.exception.MyDMOException;
import nc.ui.bd.b21.CurrParamQuery;
import nc.vo.bd.CorpVO;
import nc.vo.bd.b21.CurrinfoVO;
import nc.vo.ncblk.yjcb.yjcbjs.MyBillVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbBVO;
import nc.vo.ncblk.yjcb.yjcbjs.NcReportYjcbVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.sm.UserVO;

//月结成本后台业务逻辑处理
public class YjcbBean {

	private IyjcbDMO yjcbDMO = null;

	// 构造方法
	public YjcbBean() {
		try {
			yjcbDMO = new YjcbDMOImpl();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	// 删除子表数据
	public boolean deleteYjcbJs(NcReportYjcbVO pvo) throws MyBsBeanException {
		String whereSrt = " pk_yjcb='" + pvo.getPk_yjcb() + "' and dr=0";
		try {
			yjcbDMO.deleteNcReportYjcbBVO(whereSrt);
			return true;
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// 计算出最终结果
	public List jsAll(UserVO userVo, CorpVO corpVo, UFDate ufdate,
			NcReportYjcbVO pvo) throws MyBsBeanException {
		try {
			List rsList = new ArrayList();
			// 期初数据
			List qcsj = new ArrayList();

			// 获取期初数据 对期初数据进行处理(如果期初没有数据，则取上一个月的期末数据)
			qcsj = queryQcsj(pvo, qcsj);

			// 查询出所有生产入库
			List scrkList = yjcbDMO.queryIaBillVoList(pvo, "I3");

			// 查询所有采购入库
			List cgrkList = yjcbDMO.queryIaBillVoList(pvo, "I2");

			// 查询所有委外入库 ID
			List wwrkList = yjcbDMO.queryIaBillVoList(pvo, "ID");

			// 查询所有其他入库 I4
			List qrrkList = yjcbDMO.queryIaBillVoList(pvo, "I4");

			// 查询成本调整金 I9 IA
			List cbtzjList = yjcbDMO.queryIaBillVoList(pvo, "I9IA");

			// 查询所有材料出库 I6
			List clckList = yjcbDMO.queryIaBillVoList(pvo, "I6");

			// 查询所有委外出库 IC
			List wwckList = yjcbDMO.queryIaBillVoList(pvo, "IC");

			// 查询所有其他出库 I7
			List qtckList = yjcbDMO.queryIaBillVoList(pvo, "I7");

			// 销售出库量I5
			List xsckList = yjcbDMO.queryIaBillVoList2(pvo, "I5");

			// 销售收入
			List xssrList = yjcbDMO.queryIaBillVoList(pvo, "xssr");

			// 合并期初数据
			rsList = hbDate(rsList, qcsj, "I0");
			// 合并生产入库
			rsList = hbDate(rsList, scrkList, "I3");
			// 合并采购入库
			rsList = hbDate(rsList, cgrkList, "I2");
			// 合并委外入库
			rsList = hbDate(rsList, wwrkList, "ID");

			// 合并其他入库
			rsList = hbDate(rsList, qrrkList, "I4");
			// 合并成本调整金
			rsList = hbDate(rsList, cbtzjList, "I9IA");
			// 合并材料出库
			rsList = hbDate(rsList, clckList, "I6");
			// 合并委外出库
			rsList = hbDate(rsList, wwckList, "IC");
			// 合并其他出库
			rsList = hbDate(rsList, qtckList, "I7");

			// 合并销售出库
			rsList = hbDate(rsList, xsckList, "I5");

			// 合并销售收入
			rsList = hbDate(rsList, xssrList, "xssr");

			// 对要计算的字段以及要处理的字段进行处理
			rsList = hjProcess(rsList,pvo);
			
			//liuys 设置汇率
//			List<Number> hlinfo = getRowDigits_ExchangeRate(pvo);
//			List backList = hlJS(hlinfo,rsList);
			return rsList;
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	/**
	 * liuys add
	 * 根据汇率与汇率精度计算所有与金额相关的字段
	 * @param pvo
	 * @param qcsj
	 * @return
	 * @throws MyBsBeanException
	 */
	private List hlJS(List<Number> hlinfo,List reList){
		UFDouble hl = new UFDouble(hlinfo.get(0).toString());
		int jd = Integer.parseInt(hlinfo.get(1).toString());
		List rsList = new ArrayList();
		for(int i=0;i<reList.size();i++){
			NcReportYjcbBVO bvo = (NcReportYjcbBVO)reList.get(i);
//			qcjcje	期初结存金额	decimal(20,8)	是	
			bvo.setQcjcje(zerocl(bvo.getQcjcje().multiply(hl)));
//			qcjcdj	期初结存单价	decimal(20,8)	是	
			bvo.setQcjcdj(zerocl(bvo.getQcjcdj().multiply(hl)));
//			scrkje	生产入库金额	decimal(20,8)	是	
			bvo.setScrkje(zerocl(bvo.getScrkje().multiply(hl)));
//			cgrkje	采购入库金额	decimal(20,8)	是	
			bvo.setCgrkje(zerocl(bvo.getCgrkje().multiply(hl)));
//			wwrkje	委外入库金额	decimal(20,8)	是	
			bvo.setWwrkje(zerocl(bvo.getWwrkje().multiply(hl)));
//			qtrkje	其它入库金额	decimal(20,8)	是	1.2增加
			bvo.setQtrkje(zerocl(bvo.getQtrkje().multiply(hl)));
//			cbtzdje	成本调整单金额	decimal(20,8)	是	
			bvo.setCbtzdje(zerocl(bvo.getCbtzdje().multiply(hl)));
//			xgfyje	相关费用金额	decimal(20,8)	是	
			bvo.setXgfyje(zerocl(bvo.getXgfyje().multiply(hl)));
//			bqrkdj	本期入库单价	decimal(20,8)	是	
			bvo.setBqrkdj(zerocl(bvo.getBqrkdj().multiply(hl)));
//			bqrkje	本期入库金额	decimal(20,8)	是	
			bvo.setBqrkje(zerocl(bvo.getBqrkje().multiply(hl)));
//			xsckdj	销售出库单价	decimal(20,8)	是	
			bvo.setXsckdj(zerocl(bvo.getXsckdj().multiply(hl)));
//			xsckje	销售出库金额	decimal(20,8)	是	
			bvo.setXsckje(zerocl(bvo.getXsckje().multiply(hl)));
//			xssrje	销售收入金额	decimal(20,8)	是	1.2增加
			bvo.setXssrje(zerocl(bvo.getXssrje().multiply(hl)));
//			clckje	材料出库金额	decimal(20,8)	是	1.2增加
			bvo.setClckje(zerocl(bvo.getClckje().multiply(hl)));
//			wwckje	委外出库金额	decimal(20,8)	是	1.2增加
			bvo.setWwckje(zerocl(bvo.getWwckje().multiply(hl)));
//			qtckje	其它出库金额	decimal(20,8)	是	1.2增加
			bvo.setQtckje(zerocl(bvo.getQtckje().multiply(hl)));
//			ckhjje	出库合计金额	decimal(20,8)	是	1.2增加
			bvo.setCkhjje(zerocl(bvo.getCkhjje().multiply(hl)));
//			xscbdj	销售成本单价	decimal(20,8)	是	
			bvo.setXscbdj(zerocl(bvo.getXscbdj().multiply(hl)));
//			xscdje	销售成本金额	decimal(20,8)	是	
			bvo.setXscdje(zerocl(bvo.getXscdje().multiply(hl)));
//			qmjcdj	期末结存单价	decimal(20,8)	是	
			bvo.setQmjcdj(zerocl(bvo.getQmjcdj().multiply(hl)));
//			qmjcje	期末结存金额	decimal(20,8)	是	
			bvo.setQmjcje(zerocl(bvo.getQmjcje().multiply(hl)));
//			xscbjzje	销售成本调整金	decimal(20,8)	是	1.2增加
			bvo.setXscbjzje(zerocl(bvo.getXscbjzje().multiply(hl)));
//			xscbtzhje	销售成本调整后金	decimal(20,8)	是	1.2增加
			bvo.setXscbtzhje(zerocl(bvo.getXscbtzhje().multiply(hl)));
			//异常调整金额
			bvo.setYctz(zerocl(bvo.getYctz().multiply(hl)));
			//调整前期末结存金
			bvo.setTzqqmjcj(zerocl(bvo.getTzqqmjcj().multiply(hl)));
			rsList.add(bvo);
		}
		
		return rsList;
	}
	
	private UFDouble zerocl(UFDouble je){
		if(je.doubleValue() == 0)
			return new UFDouble(0);
		else
			return je;
	}
	// 获取期初数据 对期初数据进行处理(如果期初没有数据，则取上一个月的期末数据)
	private List queryQcsj(NcReportYjcbVO pvo, List qcsj)
			throws MyBsBeanException {
		// 如果是第一个月，则取期初数据
		if (qcsj != null && qcsj.size() > 0) {
			return qcsj;
		}
		// 取上一个月的数据
		else {
			List rsList = new ArrayList();
			// 单据年度
			String djnd = pvo.getDjnd();
			// 单据期间
			String djqj = pvo.getDjqj();
			UFDate currentDate = new UFDate(djnd + "-" + djqj + "-" + "01");
			int month = currentDate.getMonth();
			int year = currentDate.getYear();
			if (month != 1)
				month = month - 1;
			else {
				month = 12;
				year = year - 1;
			}
			if (month < 10)
				djqj = "0" + month;
			else
				djqj = String.valueOf(month);
			djnd = String.valueOf(year);
			// 获取上一期间的表头VO
			try {
				MyBillVO billvo = yjcbDMO.queryMyBillVO_2(djnd, djqj);
				NcReportYjcbVO beforepvo = (NcReportYjcbVO) billvo
						.getParentVO();
				if (beforepvo.getIsstatus().booleanValue() == false)
					throw new MyBsBeanException("当前年份:" + djnd + ",期间：" + djqj
							+ "，的月结成本计算还没有确认，请先进行确认！");
				else {
					NcReportYjcbBVO[] cvos = (NcReportYjcbBVO[]) billvo
							.getChildrenVO();
					for (int i = 0; i < cvos.length; i++) {
						cvos[i].setPk_yjcb_b(null);
						cvos[i].setPk_yjcb(pvo.getPk_yjcb());
						// 期初数据
						cvos[i].setQcjcsl(cvos[i].getQmjcsl());
						cvos[i].setQcjcdj(cvos[i].getQmjcdj());
						cvos[i].setQcjcje(cvos[i].getQmjcje());
						// 生产入库(产成品入库)
						cvos[i].setScrksl(new UFDouble(0));
						cvos[i].setScrkje(new UFDouble(0));
						// 采购入库
						cvos[i].setCgrksl(new UFDouble(0));
						cvos[i].setCgrkje(new UFDouble(0));
						// 委外入库
						cvos[i].setWwrksl(new UFDouble(0));
						cvos[i].setWwrkje(new UFDouble(0));
						// 其他入库 added by zjj 2010-05-17
						cvos[i].setQtrksl(new UFDouble(0));
						cvos[i].setQtrkje(new UFDouble(0));
						// 成本调整金
						cvos[i].setCbtzdje(new UFDouble(0));

						cvos[i].setXgfyje(new UFDouble(0));
						// 本期入库
						cvos[i].setBqrksl(new UFDouble(0));
						cvos[i].setBqrkdj(new UFDouble(0));
						cvos[i].setBqrkje(new UFDouble(0));
						// 销售出库
						cvos[i].setXscksl(new UFDouble(0));
						cvos[i].setXsckje(new UFDouble(0));
						cvos[i].setXsckdj(new UFDouble(0));
						// 销售收入 added by zjj 2010-05-17
						cvos[i].setXssrsl(new UFDouble(0));
						cvos[i].setXssrje(new UFDouble(0));
						// 材料出库 added by zjj 2010-05-17
						cvos[i].setClcksl(new UFDouble(0));
						cvos[i].setClckje(new UFDouble(0));
						// 委外出库 added by zjj 2010-05-17
						cvos[i].setWwcksl(new UFDouble(0));
						cvos[i].setWwckje(new UFDouble(0));
						// 其他出库 added by zjj 2010-05-17
						cvos[i].setQtcksl(new UFDouble(0));
						cvos[i].setQtckje(new UFDouble(0));
						// 出库合计 added by zjj 2010-05-17
						cvos[i].setCkhjsl(new UFDouble(0));
						cvos[i].setCkhjje(new UFDouble(0));

						cvos[i].setXscbdj(new UFDouble(0));
						cvos[i].setXscdje(new UFDouble(0));
						cvos[i].setChzzcs(new UFDouble(0));

						// 期末结存
						cvos[i].setQmjcsl(new UFDouble(0));
						cvos[i].setQmjcdj(new UFDouble(0));
						cvos[i].setQmjcje(new UFDouble(0));
						// 销售成本 added by zjj 2010-05-17
						cvos[i].setXscbjzje(new UFDouble(0));
						cvos[i].setXscbtzhje(new UFDouble(0));

						rsList.add(cvos[i]);
					}
					return rsList;
				}
			} catch (MyDMOException e) {
				e.printStackTrace();
				throw new MyBsBeanException(e.getMessage());
			}
		}
	}

	// 计算本期入库合计
	private List bqrkhj(List list) throws MyBsBeanException {
		List rsList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			NcReportYjcbBVO bvo = (NcReportYjcbBVO) list.get(i);
			// 本期入库数量合计
			UFDouble bqrksl = new UFDouble();
			// 本期入库单价
			UFDouble bqrkdj = new UFDouble();
			// 本期入库金额
			UFDouble bqrkje = new UFDouble();

			// 本期入库数量=生产入库+委外入库+采购入库
			bqrksl = bvo.getScrksl().add(bvo.getWwrksl()).add(bvo.getCgrksl());
			bqrksl = new UFDouble(bqrksl.doubleValue(), Constants.XS_SHUNLIANG);

			// 本期入库金额=生产入库金额+委外久库金额+采购入库金额+成本调价单+相关费用
			bqrkje = bvo.getScrkje().add(bvo.getWwrkje()).add(bvo.getCgrkje())
					.add(bvo.getCbtzdje()).add(bvo.getXgfyje());
			bqrkje = new UFDouble(bqrkje.doubleValue(), Constants.XS_JINE);

			bqrkdj = bqrkje.div(bqrksl, Constants.XS_DANJIA);// 单价

			bvo.setBqrksl(bqrksl);
			bvo.setBqrkje(bqrkje);
			bvo.setBqrkdj(bqrkdj);
			// 返回值
			rsList.add(bvo);
		}
		return rsList;
	}

	// 合并数据
	private List hbDate(List list_1, List list_2, String type)
			throws MyBsBeanException {
		Map rsMap = new HashMap();
		List rsList = new ArrayList();
		// 如果list 1为空，则返回 list 2
		if (list_1.size() == 0)
			return list_2;
		// 如果list 2为空，则返回 list 1
		if (list_2.size() == 0)
			return list_1;
		// 如果list 1为空，list 也为空，则返回 空的list
		if (list_2.size() == 0 && list_1.size() == 0)
			return rsList;
		for (int i = 0; i < list_1.size(); i++) {
			NcReportYjcbBVO bvo_1 = (NcReportYjcbBVO) list_1.get(i);
			String key_1 = bvo_1.getChbm();
			rsMap.put(key_1, bvo_1);

		}

		for (int j = 0; j < list_2.size(); j++) {
			NcReportYjcbBVO bvo_2 = (NcReportYjcbBVO) list_2.get(j);

			String key_2 = bvo_2.getChbm();
			// 如果是一个新的VO
			if (rsMap.get(key_2) == null)
				rsMap.put(key_2, bvo_2);
			else {
				// 如果已经存在
				NcReportYjcbBVO tempVo = (NcReportYjcbBVO) rsMap.get(key_2);
				// 移除原有VO
				rsMap.remove(key_2);
				// 合并 vo1与vo2
				// 如果是期初数据
				if (type.equals("I0")) {
					tempVo.setQcjcsl(bvo_2.getQcjcsl());
					tempVo.setQcjcdj(bvo_2.getQcjcdj());
					tempVo.setQcjcje(bvo_2.getQcjcje());

				}
				// 生产入库(产成品入库)
				if (type.equals("I3")) {
					tempVo.setScrksl(bvo_2.getScrksl());
					tempVo.setScrkje(bvo_2.getScrkje());
				}
				// 如果 采购入库 I2
				if (type.equals("I2")) {
					tempVo.setCgrksl(bvo_2.getCgrksl());
					tempVo.setCgrkje(bvo_2.getCgrkje());
				}
				// 委外加工 入库 ID
				if (type.equals("ID")) {
					tempVo.setWwrksl(bvo_2.getWwrksl());
					tempVo.setWwrkje(bvo_2.getWwrkje());
				}
				// 销售出库 I5
				if (type.equals("I5")) {
					tempVo.setXscksl(bvo_2.getXscksl());
					tempVo.setXsckdj(bvo_2.getXsckdj());
					tempVo.setXsckje(bvo_2.getXsckje());
					// tempVo.setXssrsl(bvo_2.getXssrsl());
				}

				/*
				 * 
				 * added by zjj 2010-05-18
				 */
				// 材料出库
				if (type.equals("I6")) {
					tempVo.setClckje(bvo_2.getClckje());
					tempVo.setClcksl(bvo_2.getClcksl());
				}
				// 其他出库
				if (type.equals("I7")) {
					tempVo.setQtckje(bvo_2.getQtckje());
					tempVo.setQtcksl(bvo_2.getQtcksl());
				}
				// 委外出库
				if (type.equals("IC")) {
					tempVo.setWwckje(bvo_2.getWwckje());
					tempVo.setWwcksl(bvo_2.getWwcksl());
				}
				// 其他入库
				if (type.equals("I4")) {
					tempVo.setQtrkje(bvo_2.getQtrkje());
					tempVo.setQtrksl(bvo_2.getQtrksl());
				}
				// 入库合计
				// if (type.equals("IIIJ")) {
				// tempVo.setBqrksl(bvo_2.getBqrksl());
				// tempVo.setBqrkje(bvo_2.getBqrkje());
				// tempVo.setBqrkdj(bvo_2.getBqrkdj());
				// }
				// 成本调整
				if (type.equals("I9IA")) {
					tempVo.setCbtzdje(bvo_2.getCbtzdje());//

				}
				if (type.equals("xssr")) {
					tempVo.setXssrsl(bvo_2.getXssrsl());
					tempVo.setXssrje(bvo_2.getXssrje());
				}

				// 

				/*
				 * added end
				 */

				rsMap.put(key_2, tempVo);
			}

		}

		// 合并完成
		Set setkey = rsMap.keySet();
		Iterator iterator = setkey.iterator();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			NcReportYjcbBVO obj = (NcReportYjcbBVO) rsMap.get(key);

			rsList.add(obj);
		}

		return rsList;
	}

	// 更新主表状态
	public void updateStatusNcReportYjcbVO(NcReportYjcbVO pvo)
			throws MyBsBeanException {
		pvo.setDef4(new UFBoolean(true));
		try {
			yjcbDMO.updateNcReportYjcbVO(pvo);
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// 保存最终计算结果
	public void saveVoList(List volist) throws MyBsBeanException {
		try {
			yjcbDMO.saveAllNcReportYjcbBVOList(volist);
		} catch (MyDMOException e) {
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	/*
	 * 
	 * added by zjj 2010-05-18
	 */
	// 对要计算的字段以及要处理的字段进行处理
	public List hjProcess(List volist,NcReportYjcbVO pvo) {
		boolean flag = false;
		List reList = new ArrayList();
		for (int i = 0; i < volist.size(); i++) {
			NcReportYjcbBVO bvo = (NcReportYjcbBVO) volist.get(i);
			//System.out.println("i="+i+" "+bvo.getChbm()+"  "+bvo.getScrksl());
			if(bvo.getScrksl()==null)
				bvo.setScrksl(new UFDouble(0));
			// 入库合计数 = 采购入库数 + 生产入库数　＋　委外入库数　＋　其他入库数
			UFDouble xs_cl11 = bvo.getCgrksl().add(bvo.getScrksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww11 = xs_cl11.add(bvo.getWwrksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww1_qt1 = xs_cl_ww11.add(bvo.getQtrksl(),
					Constants.XS_SHUNLIANG);
			bvo.setBqrksl(xs_cl_ww1_qt1);
			// 入库合计金 = 采购入库金 + 生产入库金　＋　委外入库金　＋　其他入库金
			UFDouble xs_cl111 = bvo.getCgrkje().add(bvo.getScrkje(),
					Constants.XS_JINE);
			UFDouble xs_cl_ww111 = xs_cl111.add(bvo.getWwrkje(),
					Constants.XS_JINE);
			UFDouble xs_cl_ww11_qt1 = xs_cl_ww111.add(bvo.getQtrkje(),
					Constants.XS_JINE);
			UFDouble xscl_sss11 = xs_cl_ww11_qt1.add(bvo.getCbtzdje(),
					Constants.XS_JINE);
			bvo.setBqrkje(xscl_sss11);

			// 入库单价 = 入库合计金 / 入库合计数
			bvo.setBqrkdj(xscl_sss11.div(xs_cl_ww1_qt1, Constants.XS_DANJIA));

			// 出库单价 = （期初金额+入库金额）/（期初数量+入库数量）
			bvo.setXsckdj(bvo.getQcjcje().add(bvo.getBqrkje()).div(
					bvo.getQcjcsl().add(bvo.getBqrksl()), Constants.XS_DANJIA));

			// 销售出库金 （取I5销售出库单数量-销售赠品数）*出库单价
			bvo.setXscdje(bvo.getXscksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));

			// 材料出库金 取I6材料出库单数*出库单价
			bvo.setClckje(bvo.getClcksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));
			// 销售收入金 取I5销售出库单数量*出库单价
			//bvo.setXssrje(bvo.getXssrsl().multiply(bvo.getXsckdj(),
			//		Constants.XS_JINE));
			// 其它出库金 （取I7且收发类别<>'转库出库'其它出库单数+销售赠品数）*出库单价
			bvo.setQtckje(bvo.getQtcksl().multiply(bvo.getXsckdj(),
					Constants.XS_JINE));
			// 出库合计数 = 销售出库数 + 材料出库数+ 委外出库数 +其它出库数
			UFDouble xs_cl1 = bvo.getXscksl().add(bvo.getClcksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww1 = xs_cl1.add(bvo.getWwcksl(),
					Constants.XS_SHUNLIANG);
			UFDouble xs_cl_ww_qt1 = xs_cl_ww1.add(bvo.getQtcksl(),
					Constants.XS_SHUNLIANG);
			bvo.setCkhjsl(xs_cl_ww_qt1);

			// 出库合计金=销售出库金 + 材料出库金+ 委外出库金 +其它出库金
			bvo.setCkhjje(bvo.getXscdje().add(bvo.getClckje()).add(bvo.getWwckje()).add(bvo.getQtckje()));
			
			//期末结存数=期初结存数+入库合计数-出库合计数
			bvo.setQmjcsl(bvo.getQcjcsl().add(bvo.getBqrksl()).sub(bvo.getCkhjsl()));
			
			
			// 销售成本调整后金 初始置0
			bvo.setXscbjzje(new UFDouble(0));
			// 销售成本调整金 = 销售成本金 + 成本调整金额
			bvo.setXscbtzhje(bvo.getXscdje().add(bvo.getXscbjzje(),
					Constants.XS_SHUNLIANG));
			
			/***************************liuys begin *****************************************/
			// add 2010-12-29 在得到计算结果后, 根据结果判断得出"调整前期末结存金","异常调整"
			//QCJCJ+NINMNY-(XSCKJ+CLJE+WWCKJ+QTCKJ)相当于原来的期末结存金额的公式
			//调整前期末结存金 = 原期末结存金额				
				bvo.setTzqqmjcj(bvo.getQcjcje().add(bvo.getBqrkje()).sub(bvo.getCkhjje()));
				//异常调整 取“数量为0时金额不为0和数量为正数金额为负数”的金额*（-1）
				//如果结存数量为0且金额不为0
			if(bvo.getQmjcsl().doubleValue()==0 && bvo.getTzqqmjcj().doubleValue()!=0){
				if(bvo.getTzqqmjcj().doubleValue()>0)
						bvo.setYctz(new UFDouble(-(bvo.getTzqqmjcj().doubleValue())));
				else
					bvo.setYctz(bvo.getTzqqmjcj().abs());
			}
			//如果数量为正数金额为负数”的金额
			if(bvo.getQmjcsl().doubleValue()>0 && bvo.getTzqqmjcj().doubleValue()<0)
				bvo.setYctz(bvo.getTzqqmjcj().abs());
			//调整后  ->期末结存金=期初结存金+入库合计金-出库合计金 +异常调整金额
			bvo.setQmjcje(bvo.getQcjcje().add(bvo.getBqrkje()).sub(bvo.getCkhjje()).add(bvo.getYctz()));
			/***************************liuys end *****************************************/
			
			//金额相关的字段
			reList.add(bvo);

		}
		return reList;
	}
	
	 /**
	   * 作者：liuys 
	   * 功能：设置折本汇率以及小数位 
	   * 参数：String pk_corp 
	   * 公司ID int iflag 
	   * 返回：无 例外：无
	   * 日期：(2010-12-30 11:39:21) 修改日期，修改人，修改原因，
	   */
//	  private List<Number> getRowDigits_ExchangeRate(NcReportYjcbVO pvo) throws MyBsBeanException{
//		  	// 取得币种
//		String sCurrId = pvo.getCcurrencytypeid();
//		String sPk_corp = pvo.getPk_corp();
//		Integer iDigit = new Integer(2);
//		List hlinfo = new ArrayList();
//		try {
//			BusinessCurrencyRateUtil curTool = new BusinessCurrencyRateUtil(
//					sPk_corp);
//			CurrinfoVO currinfoVO = curTool.getCurrinfoVO(sCurrId,
//					CurrParamQuery.getInstance().getLocalCurrPK(sPk_corp));
//			UFDouble hl = curTool.getAdjustRate(CurrParamQuery.getInstance()
//					.getLocalCurrPK(sPk_corp), sCurrId, null, pvo.getDjnd(),
//					pvo.getDjqj());
//			// 如果汇率为0,那么直接取1
//			if (hl == null || hl.isTrimZero())
//				hlinfo.add(1);
//			else
//				hlinfo.add(hl);
//			//取汇率精度
//			if (currinfoVO.getRatedigit() != null) {
//				iDigit = currinfoVO.getRatedigit();
//				hlinfo.add(iDigit);
//			}else
//				hlinfo.add(6);
//		} catch (Exception e) {
//			throw new MyBsBeanException(e.getMessage()+"取汇率异常！");
//		}
//		return hlinfo;
//	}
	
	// 调整金额
	public List<NcReportYjcbBVO> Tiaozheng(double jine, NcReportYjcbVO nb)
			throws MyBsBeanException {
		try {
			List<NcReportYjcbBVO> reList = yjcbDMO.getXsList(nb);
			int size = reList.size();
			double total = 0.0;
			double max = 0.0;
			int index = 0;
			for (int i = 0; i < size; i++) {
				double temp = reList.get(i).getXscdje().doubleValue();
				if (jine > 0 && temp > max) {
					max = temp;
					index = i;
				}

				total += temp;
			}
			List<NcReportYjcbBVO> rl = new ArrayList();
			double totalH = 0.0;
			if (total != 0.0) {
				for (int i = 0; i < size; i++) {
					NcReportYjcbBVO t = reList.get(i);

					double tt = jine * (t.getXscdje().doubleValue() / total);
					double temp = tt + t.getXscdje().doubleValue();
					t.setXscbjzje(new UFDouble(tt, Constants.XS_JINE));
					t.setXscbtzhje(new UFDouble(temp, Constants.XS_JINE));
					totalH += t.getXscbjzje().doubleValue();
					rl.add(t);
				}
			} else {
				throw new MyBsBeanException("销售成本金总额为零！");
			}
			if (totalH > jine) {
				double chae = totalH - jine;
				NcReportYjcbBVO t = rl.get(index);
				double cur = t.getXscbjzje().doubleValue();
				double curr = t.getXscbtzhje().doubleValue();
				double now = cur - chae;
				double now1 = curr - chae;
				rl.remove(index);

				t.setXscbjzje(new UFDouble(now, Constants.XS_JINE));
				t.setXscbtzhje(new UFDouble(now1, Constants.XS_JINE));
				rl.add(t);

			} else if (totalH < jine) {
				double chae = jine - totalH;
				NcReportYjcbBVO t = rl.get(index);
				double cur = t.getXscbjzje().doubleValue();
				double curr = t.getXscbtzhje().doubleValue();
				double now = cur + chae;
				double now1 = curr + chae;
				rl.remove(index);
				t.setXscbjzje(new UFDouble(now, Constants.XS_JINE));
				t.setXscbtzhje(new UFDouble(now1, Constants.XS_JINE));
				rl.add(t);
			}
			this.UpdateAfterTz(rl);
			return rl;
		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// 确认后保存到 blk_sfccb表
	public void SaveAfterQr(NcReportYjcbVO nb) throws MyBsBeanException {
		try {
			List<NcReportYjcbBVO> reList = this.yjcbDMO.getXsList(nb);
			yjcbDMO.saveSfccb(nb, reList);

		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// 更新调整金额后的报表
	public void UpdateAfterTz(List<NcReportYjcbBVO> reList)
			throws MyBsBeanException {
		try {
			yjcbDMO.updateVolist(reList);

		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}

	// 同步blk_sfccb表
	public void deleteBlk_sfccb(String year, String month)
			throws MyBsBeanException {
		try {
			yjcbDMO.deleteBlk(year, month);
		} catch (MyDMOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MyBsBeanException(e.getMessage());
		}
	}
}
