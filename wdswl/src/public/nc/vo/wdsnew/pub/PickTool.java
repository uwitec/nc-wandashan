package nc.vo.wdsnew.pub;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.wdsnew.pub.BillStockBO1;
import nc.jdbc.framework.util.SQLHelper;
import nc.ui.scm.util.ObjectUtils;
import nc.vo.ic.other.out.TbOutgeneralBVO;
import nc.vo.ic.pub.StockInvOnHandVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
/**
 * 完达山物流出库单自动拣货
 * @author mlr
 */
public class PickTool implements Serializable{
	private static final long serialVersionUID = -6131447795689577612L;
	//拣货单
	private Map<String,List<StockInvOnHandVO>> mpick=new HashMap<String, List<StockInvOnHandVO>>();	
	private BillStockBO1 stock=null;
	private BillStockBO1 getStock(){
		if(stock==null){
			stock=new BillStockBO1();
		}
		return stock;
	}
	/**
	 * 该方法前台 必须远程调用 (查询数据库)
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * 	1 根据  仓库 货位  存货  查询现存量  根据先进先出原则  拣货

	    2捡完货后 形成拣货单  拣货单构成  是一个map  key=出库单表体id  ，value=现存量vo

	    3重新构造拣货后的  出库单表体  

	    4 反回数据 
	 * @时间：2012-6-20上午09:56:08
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param bvos
	 * @return
	 * @throws Exception 
	 */
	public TbOutgeneralBVO[] autoPick(String pk_stordoc,String pk_cargdoc ,TbOutgeneralBVO[] bvos) throws Exception{
		if(pk_stordoc==null || pk_stordoc.length()==0)
			throw new Exception("出库仓库为空");
		if(pk_cargdoc==null || pk_cargdoc.length()==0)
			throw new Exception("出库货位为空");		
		//清空拣货单
		mpick.clear();
		if(bvos==null || bvos.length==0)
			return null;
		for(int i=0;i<bvos.length;i++){
			pick(pk_stordoc,pk_cargdoc,bvos[i]);
		}	
		return createBill(bvos);
	}
	/**
	 * 根据拣货单 重新构建出库单表体信息
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-20上午10:43:38
	 * @return
	 * @throws Exception 
	 */
	private  TbOutgeneralBVO[] createBill(TbOutgeneralBVO[] bvos) throws Exception {
		List<TbOutgeneralBVO> list=new ArrayList<TbOutgeneralBVO>();//存货重新构建的表体数据
		for(int i=0;i<bvos.length;i++){
		   String pk=bvos[i].getPrimaryKey();	
		   //取出该行的拣货单
		   List<StockInvOnHandVO> li= mpick.get(pk);	
		   //如果没有现存量 该行保持不动
		   if(li==null|| li.size()==0){
			   list.add(bvos[i]);
		   }else{
			//否则  根据拣货单 重新构造  批次拆行后的表体
			 for(int j=0;j<li.size();j++){
				 TbOutgeneralBVO vo=(TbOutgeneralBVO) ObjectUtils.serializableClone(bvos[i]);
				 vo.setVbatchcode(li.get(j).getWhs_batchcode());//设置批次
				 vo.setAttributeValue("nshouldoutassistnum", li.get(j).getAttributeValue("whs_omnum"));//设置应发辅数量
				 vo.setAttributeValue("noutassistnum", li.get(j).getAttributeValue("whs_oanum"));//设置实发辅数量
				 list.add(vo);
			 }   
		   }	   
		}
		return list.toArray(new TbOutgeneralBVO[0]);		
	}
	/**
	 * 前台必须远程调用 (查询数据库)
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 *       按行拣货 根据 仓库  货位  存货  查询现存量拣货 
	 *       拣货后的数据  放入拣货单
	 * @时间：2012-6-20上午10:04:23
	 * @param pk_stordoc
	 * @param pk_cargdoc
	 * @param tbOutgeneralBVO
	 * @throws Exception 
	 */
	private void pick(String pk_stordoc, String pk_cargdoc,
			TbOutgeneralBVO vo) throws Exception {
		//构建查询条件
		String whereSql= " pk_customize1 = '"+pk_stordoc+"' " +
				        " and  pk_cargdoc = '"+pk_cargdoc+"' "+
						" and pk_invmandoc='"+vo.getCinventoryid()+"'"+
						" and isnull(dr,0)=0 "+
						" and pk_corp='"+SQLHelper.getCorpPk()+"'"+
		                " and whs_stockpieces >0 ";//库存主数量大于0
		//查询现存量
		StockInvOnHandVO[] stocks=(StockInvOnHandVO[]) getStock().queryStock(whereSql);
		//开始拣货
		   //拣货分量  构造拣货单
		   spiltNum(stocks,vo);				
	}
	/**
	 * 拣货分量  
	 * @作者：mlr
	 * @说明：完达山物流项目 
	 * @时间：2012-6-20上午10:25:33
	 * @param stocks 现存量 
	 * @param vo  出库单表体
	 */
	private void spiltNum(StockInvOnHandVO[] vos, TbOutgeneralBVO vo) {
		
	    UFDouble zbnum=PuPubVO.getUFDouble_NullAsZero(vo.getNshouldoutassistnum());//取得出库单应发辅数量
	    if(vos==null|| vos.length==0){
	    	mpick.put(vo.getPrimaryKey(), null);//如果现存量为空   则该行拣货单设置为空
	    	return;
	    }	
		if(zbnum.doubleValue()==0){
			mpick.put(vo.getPrimaryKey(), null);//如果出库单实发辅数量为0  则该行拣货单设置为空
			return;
		}	
		//进行分量  
		//按 批次号  由小到大 依次分量
		List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
		for(int i=0;i<vos.length;i++){
			UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(vos[i].getAttributeValue("whs_stockpieces"));	
			if(zbnum.doubleValue()>bnum.doubleValue()){
				if (i == vos.length - 1) {
					vos[i].setAttributeValue("whs_omnum", zbnum);// 设置应发数量(辅数量)		
					vos[i].setAttributeValue("whs_oanum", bnum);//设置实发数量
				} else {
					zbnum = zbnum.sub(bnum);
					vos[i].setAttributeValue("whs_omnum", bnum);// 设置应发数量(辅数量)																	
					vos[i].setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
				}
				list.add(vos[i]);
			}else if(zbnum.doubleValue()<bnum.doubleValue()){
				vos[i].setAttributeValue("whs_omnum", zbnum);//设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);//设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			}else{
				vos[i].setAttributeValue("whs_omnum", zbnum);//设置应发数量 (辅数量)
				vos[i].setAttributeValue("whs_oanum", zbnum);//设置实发数量(辅数量)
				list.add(vos[i]);
				break;
			}		
		}
		mpick.put(vo.getPrimaryKey(), list);
	}
	/**
	    * 拣货分量 
	    * @作者：mlr
	    * @说明：完达山物流项目 
	    * @时间：2012-6-20上午11:08:10
	    * @param vos
	    * @param row
	    * @param zbnum
	    */
		public  void spiltNum(List<StockInvOnHandVO> vos, int row, UFDouble zbnum) {
			List<StockInvOnHandVO> list=new ArrayList<StockInvOnHandVO>();
			for(int i=0;i<vos.size();i++){
				UFDouble bnum=PuPubVO.getUFDouble_NullAsZero(vos.get(i).getAttributeValue("whs_stockpieces"));	
				if(zbnum.doubleValue()>bnum.doubleValue()){
					if (i == vos.size() - 1) {
						vos.get(i).setAttributeValue("whs_omnum", zbnum);// 设置应发数量																		// (辅数量)
						vos.get(i).setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
					} else {
						zbnum = zbnum.sub(bnum);
						vos.get(i).setAttributeValue("whs_omnum", bnum);// 设置应发数量																	// (辅数量)
						vos.get(i).setAttributeValue("whs_oanum", bnum);// 设置实发数量(辅数量)
					}
					list.add(vos.get(i));
				}else if(zbnum.doubleValue()<bnum.doubleValue()){
					vos.get(i).setAttributeValue("whs_omnum", zbnum);//设置应发数量 (辅数量)
					vos.get(i).setAttributeValue("whs_oanum", zbnum);//设置实发数量(辅数量)
					list.add(vos.get(i));
					break;
				}else{
					vos.get(i).setAttributeValue("whs_omnum", zbnum);//设置应发数量 (辅数量)
					vos.get(i).setAttributeValue("whs_oanum", zbnum);//设置实发数量(辅数量)
					list.add(vos.get(i));
					break;
				}		
			}		
		}
}
