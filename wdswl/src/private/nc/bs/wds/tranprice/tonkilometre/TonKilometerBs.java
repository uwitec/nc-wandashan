package nc.bs.wds.tranprice.tonkilometre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.BsNotNullCheck;
import nc.bs.wl.pub.BsUniqueCheck;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wds.tranprice.tonkilometre.TranspriceHVO;
import nc.vo.wl.pub.WdsWlPubConst;

/**
 * 
 * @author Administrator 吨公里运价表后台校验类
 */
public class TonKilometerBs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseDAO dao = null;

	BaseDAO getBaseDAO() {
		if (dao == null) {
			dao = new BaseDAO();
		}
		return dao;
	}

	/**
	 * 
	 * @throws DAOException
	 * @作者：lyf
	 * @说明：完达山物流项目 审批前校验 已经审批过的单据是否存在 日期范围交叉
	 * @时间：2011-5-24下午01:05:16
	 */
	public void beforApprove(AggregatedValueObject vo) throws BusinessException {
		if (vo == null)
			return;
		TranspriceHVO hvo = (TranspriceHVO) vo.getParentVO();
		UFBoolean fisbigflour= PuPubVO.getUFBoolean_NullAs(hvo.getFisbigflour(),UFBoolean.FALSE);//是否大包粉
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wds_transprice_h where isnull(dr,0)=0  ");
		sql.append(" and vbillstatus='1'");// 审批通过的
		sql.append(" and reserve1='"+hvo.getReserve1()+"'");//仓库
		sql.append(" and carriersid='"+hvo.getCarriersid()+"'");//承运商 	
		sql.append(" and fisbigflour='"+fisbigflour+"'");//是否大包粉 	
		sql.append(" and pk_billtype='" + WdsWlPubConst.WDSI + "'");
		sql.append(" and( (dstartdate<='" + hvo.getDstartdate()
				+ "' and denddate>='" + hvo.getDstartdate() + "')");
		sql.append(" or (dstartdate<='" + hvo.getDenddate()
				+ "' and denddate>='" + hvo.getDenddate() + "')");
		sql.append(" or (dstartdate>='" + hvo.getDstartdate()
				+ "' and denddate<='" + hvo.getDenddate() + "'))");
		List<TranspriceHVO> list = (ArrayList<TranspriceHVO>) getBaseDAO()
				.executeQuery(sql.toString(),
						new BeanListProcessor(TranspriceHVO.class));
		if (list.size() > 0) {
			TranspriceHVO oldHvo = list.get(0);
			throw new BusinessException("和已经审批过相同仓库的相同承运商相同的大包粉 吨公里运价表存在日期交叉:\n单据编号="
					+ oldHvo.getVbillno() +"\n运价编码="+oldHvo.getVpricecode()+"\n运价名称="+oldHvo.getVpricename()+"\n开始日期=" + oldHvo.getDstartdate()
					+ "\n截止日期=" + oldHvo.getDenddate());
		}

	}
//	public void beforeSaveCheck(AggregatedValueObject vo) throws Exception{
//		//必须项校验
//		BsNotNullCheck.FieldNotNull(new SuperVO[]{(SuperVO)vo.getParentVO()},new String[]{"vbillno","pk_billtype","reserve1","carriersid","dstartdate","denddate"},new String[]{"单据号","单据类型","发货仓库","承运商","开始日期","结束日期"});
//		BsNotNullCheck.FieldNotNull((SuperVO[])vo.getChildrenVO(),new String[]{"ntransprice","pk_replace"},new String[]{"运价","收获地区"});
//		// 唯一性的校验 
//	    //单据号公司级唯一
//		BsUniqueCheck.FieldUniqueCheck((SuperVO)vo.getParentVO(), new String[]{"vbillno","pk_billtype","pk_corp"}, "[  单据号  ] 在数据库中已经存在");			
//		//开始日期大于截止日期的校验
//		if(vo.getParentVO()!=null){
//			TranspriceHVO hvo=(TranspriceHVO) vo.getParentVO();
//			if(hvo.getDstartdate().after(hvo.getDenddate())){
//				throw new BusinessException("开始日期不能大于截止日期");
//			}
//		}
//		//发货仓库 承运商 单据类型 是否原料粉 组合唯一  
//		//  外加 开始日期 到 截止日期不能交叉
//		BsUniqueCheck.FieldUniqueCheckInment((SuperVO)vo.getParentVO(),new String[]{"reserve1","fisbigflour","carriersid","pk_billtype"}, "dstartdate","denddate"," 已经定义 了该 [发货仓库]  [承运商] [是否大包粉] 下的这个期间段的运价表 ");		
//		//收获地区应用范围表体唯一性校验
//		validateBodyRePlace(vo.getChildrenVO(),new String[]{"pk_replace","ifw"},new String[]{"收获地区","应用范围"});	
//	}
	/*
	 * 收获地区 应用范围表体唯一性校验
	 */
	private void validateBodyRePlace(CircularlyAccessibleValueObject[] chs,String[] fields,String[] displays) throws Exception{
		if(chs==null || chs.length==0){
			return;
		}
		int num =chs.length;
		if(fields == null || fields.length == 0){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				String key = "";
				for(String str : fields){
					Object o1 =chs[i].getAttributeValue(str);
					key = key + ","+String.valueOf(o1);
				}
				String dis="";
				for(int j=0;j<displays.length;j++){
					   dis=dis+"[ "+displays[j]+" ]";
					}
					
				if(list.contains(key)){							
					throw new BusinessException("第["+(i+1)+"]行表体字段 "+dis+" 存在重复!");
				}else{
					list.add(key);
				}
				//如果应用 范围  为 全部 查看发货站和收获站相同情况下 应用范围是否存在包含
				// 应用范围 全部 是 0 ,经销商 1,分仓  2  
				if("0".equals(chs[i].getAttributeValue(fields[1]).toString())){
					String[] strs=key.split(",");
					
					if(list.contains(","+strs[1]+","+"1") || list.contains(","+strs[1]+","+"2")){
						throw new Exception("第["+(i+1)+"]行表体字段 "+dis+" 存在[ 应用范围 ] 的包含!");
					}
				}
				//如果应用 范围  为 经销商 或 分仓 查看发货站和收获站相同情况下 应用范围是否存在包含
				if("1".equals(chs[i].getAttributeValue(fields[1]).toString()) || "2".equals(chs[i].getAttributeValue(fields[1]).toString()) ){
                    
					String[] strs=key.split(",");
					
					if(list.contains( ","+strs[1]+","+"0") ){
						throw new Exception("第["+(i+1)+"]行表体字段 "+dis+" 存在[ 应用范围 ] 的包含!");
					}
				}
				
			}
		}	
	}

}
