package nc.bs.ic.tpyd;
import java.util.List;
import nc.bs.dao.BaseDAO;
import nc.bs.wds.ic.stock.StockInvOnHandBO;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.CombinVO;

public class TpydBO {

	private BaseDAO dao = null;
	private StockInvOnHandBO sbo = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	StockInvOnHandBO getStockBO() {
		if (sbo == null) {
			sbo = new StockInvOnHandBO(getBaseDAO());
		}
		return sbo;
	}

	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 审批后的回写 对移出的托盘进行状态的更改 对移入托盘形成新的现存量
	 * @时间：2011-7-20上午09:18:29
	 * @throws Exception
	 */
	public void writeBack(AggregatedValueObject vo) throws Exception {
		if(vo.getChildrenVO()==null ||vo.getChildrenVO().length==0){
			throw new Exception("表体为空");
		}
		SuperVO[] vos = (SuperVO[]) vo.getChildrenVO();
		valuteOutCdt(vos);
		valuteInCdt(vos);
		//防止并发的校验
		afterValute(vos);
	}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目
	 *       对移动数据作最后校验,防止移出托盘出现负结存
	 * @时间：2011-7-21上午09:59:03
	 * @param vos
	 * @throws Exception 
	 */
	private synchronized void afterValute(SuperVO[] vos) throws Exception{
	 if(vos==null ||vos.length==0)
		 throw new Exception("表体数据为空");
	 SuperVO[] newVos = CombinVO.combinVoByFields(vos,new String[] {"pk_trayout" }, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
	 int size1=newVos.length;
	 String pk_trayout=null;//移出托盘主键
	 UFDouble dnum=null;// 获取当前托盘的库存主数量
	 UFDouble dbnum =null;// 获取当前托盘的库存辅数量
	 StockInvOnHandVO svo=null;//现存量vo(用于形成存货状态)
	 for(int i=0;i<size1;i++){
		    pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos[i].getAttributeValue("pk_trayout"));		 
			Object o = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
			if(o==null){
				throw new Exception("编码为 ["+newVos[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	
			}				
			List list = (List) o;
			if(list==null&& list.size()==0)
				throw new Exception("编码为 ["+newVos[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	 
			svo = (StockInvOnHandVO) list.get(0);
			
			dnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stocktonnage());						
			dbnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stockpieces());
			if (dnum.doubleValue() < 0 || dbnum.doubleValue() < 0) {
				throw new Exception("编码为  ["+ newVos[i].getAttributeValue("outtraycode")+ "]的托盘移出数量超出当前托盘的存量");
			}
	 }		
}
	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *  
     * 对移入托盘的校验 同一个托盘，移入的总量 不能 超出当前托盘的容量
		      
	 * @时间：2011-7-20下午03:15:57
	 * @param vos
	 * @throws Exception
	 */
	private void valuteInCdt(SuperVO[] vos) throws Exception {	
		SuperVO[] newVos1 = CombinVO.combinVoByFields(vos,new String[] {"pk_trayin"}, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
		int size1 = newVos1.length;
		UFDouble rnum =null;//移入托盘总量(主数量)
		UFDouble rbnum =null;//移入托盘总量(辅数量)
		String pk_trayin =null;//移入托盘主键		
		String pk_trayout=null;//移出托盘主键	
		Integer cdt_traystatus =null;//托盘状态 0 表示未占用, 1表示已占用
		StockInvOnHandVO hvo = null;//现存量vo(用于形成存货状态)
		for (int i = 0; i < size1; i++) {			
			 rnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmovenum"));		
			 rbnum=PuPubVO.getUFDouble_NullAsZero(newVos1[i].getAttributeValue("nmoveassnum"));
			 pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_trayout"));			
			 pk_trayin=PuPubVO.getString_TrimZeroLenAsNull(newVos1[i].getAttributeValue("pk_trayin"));		
			// 审批前的校验
			// 查询货位托盘信息表查看移入托盘是否为空,如果为空,才允许移入			
			String sql = " select cdt_traystatus  from bd_cargdoc_tray w where isnull(w.dr,0)=0 and w.cdt_pk='"+ pk_trayin + "'";
			Object o = getBaseDAO().executeQuery(sql,WdsPubResulSetProcesser.COLUMNPROCESSOR);
			if(o==null){
				throw new Exception("编码为 ["+newVos1[i].getAttributeValue("intarycode")+"]的托盘不存在" );
			}
		    cdt_traystatus = PuPubVO.getInteger_NullAs(o, -1);
			if (cdt_traystatus != StockInvOnHandVO.stock_state_null) {
				throw new Exception("编码为  ["+newVos1[i].getAttributeValue("intarycode")+ "]的托盘已经被占用");	
			}
		    // 根据移入的托盘的量 形成现存量纪录		
		    // 首先查处移出的托盘的现存量vo
			Object o1 = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
			if(o1==null){
				throw new Exception("编码为 ["+newVos1[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");
			}           			    
			List list = (List) o1;
			if(list==null || list.size()==0){
				throw new Exception("编码为 ["+newVos1[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	
			}
			StockInvOnHandVO old = (StockInvOnHandVO) list.get(0);
			hvo = (StockInvOnHandVO) old.clone();
			if(hvo==null){
				throw new Exception("编码为 ["+newVos1[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	
			}
		 	hvo.setPrimaryKey(null);
			hvo.setPplpt_pk(pk_trayin);
			hvo.setWhs_omnum(rnum);
			hvo.setWhs_oanum(rbnum);
			hvo.setWhs_stocktonnage(rnum);
			hvo.setWhs_stockpieces(rbnum);
			hvo.setWhs_status(0);
			getBaseDAO().insertVO(hvo);
			//将货位托盘信息中  对应托盘状态改为占用
		    String sql1="update bd_cargdoc_tray set cdt_traystatus=1 where isnull(dr,0)=0 and cdt_pk='"+pk_trayin+"'";
			getBaseDAO().executeUpdate(sql1);		
			}			
		}
		
	


	/**
	 * 
	 * @作者：mlr
	 * @说明：完达山物流项目 对移出托盘的校验
	 * 
	 * @时间：2011-7-20上午11:11:25
	 * @param vos
	 *            表体vo数组
	 */
	private void valuteOutCdt(SuperVO[] vos) throws Exception{
		// 将移出托盘编码相同的合并到一起 将移入数量 求和
		SuperVO[] newVos = CombinVO.combinVoByFields(vos,new String[] {"pk_trayout" }, new int[] {nc.vo.wl.pub.IUFTypes.UFD, nc.vo.wl.pub.IUFTypes.UFD },new String[] { "nmovenum", "nmoveassnum" });
		
		UFDouble rnum =null;//移动数量(主数量)
		UFDouble rbnum=null;//移动数量(辅数量)
		String pk_trayout=null;//移出托盘主键
		UFDouble dnum=null;// 获取当前托盘的库存主数量
		UFDouble dbnum =null;// 获取当前托盘的库存辅数量
		StockInvOnHandVO svo=null;//现存量vo(用于形成存货状态)
		if (newVos != null && newVos.length != 0) {
			// 校验移出托盘
			int size = newVos.length;
			for (int i = 0; i < size; i++) {
				// 审批前的校验
				// 对移出托盘的校验,查询存货状态,查看托盘当前现存量是否够 移出数量
				// 按移出托盘的辅数量校验
				pk_trayout=PuPubVO.getString_TrimZeroLenAsNull(newVos[i].getAttributeValue("pk_trayout"));			
				rnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmovenum"));		
				rbnum=PuPubVO.getUFDouble_NullAsZero(newVos[i].getAttributeValue("nmoveassnum"));				
				Object o = getBaseDAO().retrieveByClause(StockInvOnHandVO.class," isnull(tb_warehousestock.dr,0)=0 and  tb_warehousestock.pplpt_pk='"+ pk_trayout + "'");
				if(o==null){
					throw new Exception("编码为 ["+newVos[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	
				}				
				List list = (List) o;
				if(list==null || list.size()==0)
					throw new Exception("编码为 ["+newVos[i].getAttributeValue("outtraycode")+"]的移出托盘存量为空或托盘已经不存在");	 
				svo = (StockInvOnHandVO) list.get(0);
				
				dnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stocktonnage());						
				dbnum = PuPubVO.getUFDouble_NullAsZero(svo.getWhs_stockpieces());
				if (dnum.doubleValue() < rnum.doubleValue()|| dbnum.doubleValue() < rbnum.doubleValue()) {
					throw new Exception("编码为  ["+ newVos[i].getAttributeValue("outtraycode")+ "]的托盘移出数量超出当前托盘的存量");
				} else {
					// 当前托盘存量够移出数量
					// 从存货状态中将移出数量减掉
					svo.setWhs_stockpieces(dnum.sub(rnum));
					svo.setWhs_stocktonnage(dbnum.sub(rbnum));
					if (svo.getWhs_stockpieces().doubleValue() == 0&& svo.getWhs_stocktonnage().doubleValue() == 0) {
						svo.setWhs_status(1);
						//如果托盘存量为空的话，将货位托盘信息中,对应托盘的状态设为未占用状态					
						String sql1="update bd_cargdoc_tray set cdt_traystatus="+StockInvOnHandVO.stock_state_null+" where isnull(dr,0)=0 and cdt_pk='"+pk_trayout+"'";
						getBaseDAO().executeUpdate(sql1);	
					}
					getStockBO().updateWarehousestock(svo);
				}
			}		
		}
	}
}

/**
 * 
 * @throws Exception
 * @作者：lyf
 * @说明：完达山物流项目 托盘移动 审批回写： 检查 移出托盘是否已经变化（绑定存货没有改变,库存数量没有变化,移动数量没有超量）; 检查
 *             目标托盘是可用（绑定存货没有改变，仍然为空,移动数量没有超量）;
 *             检查通过：减去移出托盘库存数量，如果库存数量完全移走，则改变状态为空着; 移入托盘增加库存数量，改变状态为占用;
 * @时间：2011-4-10下午06:59:34
 */
// public void writeBack(AggregatedValueObject obj ) throws Exception{
// if(obj == null || obj.getParentVO()==null
// || obj.getChildrenVO() == null
// || obj.getChildrenVO().length ==0){
// return ;
// }
// TpydHVO hvo = (TpydHVO )obj.getParentVO();
// TpydBVO[] bvos = (TpydBVO[])obj.getChildrenVO();
// StringBuffer sql = new StringBuffer();
// sql.append("select count(0) from tb_warehousestock where isnull(dr,0)=0 and pk_corp='"+hvo.getPk_corp()+"'");
// sql.append(" and pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? and whs_stocktonnage=? and whs_stockpieces=?");
// SQLParameter para = new SQLParameter() ;
// int i=1;
// for(TpydBVO bvo :bvos){
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// para.addParam(bvo.getPk_invmandoc());
// para.addParam(bvo.getNoutnum());
// para.addParam(bvo.getNoutassnum());
// Integer count
// =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(), para,
// WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
// if(count == 0){
// throw new BusinessException("在表体第"+i+"行：移出托盘信息绑定存货或者托盘库存数量已经改变");
// }
//			
// para.clearParams();
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// para.addParam(bvo.getPk_invmandoc());
// para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinnum()));
// para.addParam(PuPubVO.getUFDouble_NullAsZero(bvo.getNinassnum()));
// count =PuPubVO.getInteger_NullAs(getBaseDAO().executeQuery(sql.toString(),
// para, WdsPubResulSetProcesser.COLUMNPROCESSOR), 0);
// if(count == 0){
// throw new BusinessException("在表体第"+i+"行：移入托盘信息绑定存货改变或者托盘已占用");
// }
// i++;
// para.clearParams();
// }
//		
// String upStockInv =
// "update tb_warehousestock set whs_stocktonnage=coalesce(whs_stocktonnage,0)+?,whs_stockpieces=coalesce(whs_stockpieces,0)+?"
// +
// " where pk_cargdoc=? and pplpt_pk=? and pk_invmandoc=? ";
// String upCargeTary =" update bd_cargdoc_tray set cdt_traystatus=? " +
// " where pk_cargdoc=? and cdt_pk=?";
// for(TpydBVO bvo :bvos){
// UFDouble noutnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutnum());
// // UFDouble noutassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNoutassnum());
// UFDouble nmovenum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmovenum());
// UFDouble nmoveassnum = PuPubVO.getUFDouble_NullAsZero(bvo.getNmoveassnum());
// para.addParam(nmovenum.multiply(-1));
// para.addParam(nmoveassnum.multiply(-1));
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// para.addParam(bvo.getPk_invmandoc());
// getBaseDAO().executeUpdate(upStockInv,para);
// para.clearParams();
// if((nmovenum.sub(noutnum)).doubleValue()==0){//如果托盘移空，则改变托盘状态为空
// para.addParam(0);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayout());
// getBaseDAO().executeUpdate(upCargeTary,para);
// para.clearParams();
// }
// para.addParam(nmovenum);
// para.addParam(nmoveassnum);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// para.addParam(bvo.getPk_invmandoc());
// getBaseDAO().executeUpdate(upStockInv,para);
// para.clearParams();
// para.addParam(1);
// para.addParam(hvo.getPk_cargedoc());
// para.addParam(bvo.getPk_trayin());
// getBaseDAO().executeUpdate(upCargeTary,para);
// para.clearParams();
//			
// }
// }
// }
