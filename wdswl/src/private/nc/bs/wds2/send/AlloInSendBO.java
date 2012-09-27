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
	 * @作者：zhf
	 * @说明：完达山物流项目 调拨入库单 保存时生成调入运单 
	 * @时间：2012-7-10下午12:51:26
	 * @param bill
	 * @throws BusinessException
	 */
	public void createAlloInSendBill(String headid,PfParameterVO para,boolean isnew) throws Exception{
		if(headid == null)
			return;

		//		查询调拨入库单
		OtherInBillVO bill = (OtherInBillVO)new HYPubBO().queryBillVOByPrimaryKey(
				new String[]{OtherInBillVO.class.getName(),
						TbGeneralHVO.class.getName(),
						TbGeneralBVO.class.getName()}, headid);

		if(bill == null){
			throw new BusinessException("数据异常");
		}
		
		if(!isnew){
			updateAlloInSendBill(bill);
		}
		
		//		转换生成
		HYBillVO tarBill = (HYBillVO)PfUtilTools.runChangeData(WdsWlPubConst.BILLTYPE_ALLO_IN, 
				Wds2WlPubConst.billtype_alloinsendorder, bill,para);

		if(tarBill == null)
			throw new BusinessException("数据异常,未生成调入运单");

		check(tarBill);
		
		//进行运费的计算
		calTrans(tarBill);
		

		new PfUtilBO().processAction("WRITE", Wds2WlPubConst.billtype_alloinsendorder, 
				para.m_currentDate, null, tarBill, null);
	}
	/**
	 * 计算运费
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-9-26上午11:06:29
	 * @param tarBill
	 * @throws Exception 
	 */
	public void calTrans(HYBillVO tarBill) throws Exception {
		// 运费计算 流程
		// 按 调出公司 调入仓库 查询运价表
		// 逐行计算表体运费 根据存货属性 选取运价表 计算运费
		// 运费合计
		if (tarBill == null || tarBill.getParentVO() == null
				|| tarBill.getChildrenVO() == null
				|| tarBill.getChildrenVO().length == 0) {
			return;
		}
		SuperVO headvo = (SuperVO) tarBill.getParentVO();
		SuperVO[] bodyvos = (SuperVO[]) tarBill.getChildrenVO();
		String outcorp = PuPubVO.getString_TrimZeroLenAsNull(headvo.getAttributeValue("vdef1"));// 调出公司
		String instore = PuPubVO.getString_TrimZeroLenAsNull(headvo.getAttributeValue("pk_inwhouse"));// 调入仓库
		if (outcorp == null) {
			throw new Exception("调出公司为空");
		}
		if (instore == null) {
			throw new Exception("调入仓库");
		}
		UFDouble total=new UFDouble(0);//存放运费
		for (int i = 0; i < bodyvos.length; i++) {
			SuperVO vo = bodyvos[i];
			String pk_invmandoc = PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_invmandoc"));// 得到存货管理主键
			String pk_invbasdoc = PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_invbasdoc"));// 得到存货基本档案主键
			UFDouble num=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue("ninacceptnum"));//得到存货吨数
			// 得到存货属性
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
				throw new Exception(" 存货编码为 ：" + invcode + " 存货档案没有维护存货属性");
			}

			// 根据 调出公司 调入仓库 存货属性 查找运价表
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
					typeerror=" 大包粉类型";
				}else if(type==1){
					typeerror=" 箱粉(袋)类型";
				}else if(type==2){
					typeerror=" 箱粉(听)类型";
				}
				throw new Exception(" 调出公司为：["+outcorp+ " ]调入仓库为：["+ store+ "] 存货类型为 : ["+typeerror+"] 没有找到运价表 ");
			}
			TransVO transvo=(TransVO) list.get(0);
			UFDouble price=PuPubVO.getUFDouble_NullAsZero(transvo.getPrice());
			UFDouble mail =PuPubVO.getUFDouble_NullAsZero(transvo.getMail());
			UFDouble fee=(price.multiply(mail)).multiply(num);//运费
			total=total.add(fee);		
		}
		headvo.setAttributeValue("ntransmny", total);
	}

	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 生成调入运单数据校验 
	 * @时间：2012-7-10下午06:10:33
	 * @param bill
	 * @throws BusinessException
	 */
	public void check(HYBillVO  bill) throws BusinessException{
		if(bill == null)
			throw new  BusinessException("传入数据为空");
		SendorderVO head = (SendorderVO)bill.getParentVO();
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVdef1())==null)
			throw new BusinessException("调出公司为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_corp())==null)
			throw new BusinessException("调入公司为空");
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getPk_billtype())==null)
			throw new BusinessException("单据类型为空");
		
		if(PuPubVO.getString_TrimZeroLenAsNull(head.getVbillno())==null){
			head.setVbillno(
					new HYPubBO().getBillNo(Wds2WlPubConst.billtype_alloinsendorder, head.getPk_corp(), null, null));
		}
		
		SendorderBVO[] bodys = (SendorderBVO[])bill.getChildrenVO();
		if(bodys == null || bodys.length == 0)
			throw new BusinessException("数据异常，表体数据为空");
		 for(SendorderBVO body:bodys){
			 body.validate();
		 }
	}
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 入库单调整后 运单做相应调整  主要是入库数量调整后 调整运单的接收数量 
	 * @时间：2012-7-10下午06:09:45
	 * @param bill
	 * @throws BusinessException
	 */
	private void updateAlloInSendBill(OtherInBillVO bill) throws BusinessException{
		
	}
	
	/**
	 * 
	 * @作者：zhf
	 * @说明：完达山物流项目 
	 * @时间：2012-7-23下午01:16:47
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
			throw new BusinessException("调入运单已经审批通过");
//		删除运单
		sql = "update wds_sendorder set dr = 1 where pk_sendorder = '"
			+PuPubVO.getString_TrimZeroLenAsNull(o.get("pk_sendorder"))+"'";
		getDao().executeUpdate(sql);
		sql = "update wds_sendorder_b set dr = 1 where pk_sendorder = '"
			+PuPubVO.getString_TrimZeroLenAsNull(o.get("pk_sendorder"))+"'";
		getDao().executeUpdate(sql);
	}
}
