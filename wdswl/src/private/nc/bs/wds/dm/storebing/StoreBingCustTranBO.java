package nc.bs.wds.dm.storebing;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.zmpub.excel.CodeToIDInfor;
import nc.vo.zmpub.excel.IDefTran;
import nc.vo.zmpub.pub.tool.ResultSetProcessorTool;

public class StoreBingCustTranBO implements IDefTran {
   private ArrayList<String> list =new ArrayList<String>();
	
	public String transCodeToID(BaseDAO dao,CircularlyAccessibleValueObject vo,CodeToIDInfor infor) throws BusinessException {
		// TODO Auto-generated method stub
//		
//		StringBuffer str = new StringBuffer();
//		str.append("select pk_cubasdoc,pk_cumandoc from bd_cumandoc where");
//		str.append(" pk_corp = '"+infor.getCorpvalue()+"'");
//		str.append(" and custflag = 2");
//		str.append(" and pk_cubasdoc = (select pk_cubasdoc from bd_cubasdoc where custcode = '"+infor.getCodevalue()+"')");
//		if(dao == null)
//			dao = new BaseDAO();
//		java.util.Map data = (java.util.Map)dao.executeQuery(str.toString(), ResultSetProcessorTool.MAPPROCESSOR);
//		if(data == null || data.size() == 0)
//			return null;
//		vo.setAttributeValue("pk_cumandoc", data.get("pk_cumandoc"));
//		vo.setAttributeValue("pk_cubasdoc", data.get("pk_cubasdoc"));
//		
//		// TODO Auto-generated method stub
		
		StringBuffer str = new StringBuffer();
		str.append("select pk_areacl from bd_areacl where");
		str.append(" pk_corp = '"+infor.getCorpvalue()+"'");
		str.append(" and areaclname like '"+infor.getCodevalue()+"%' and areaclcode like '00%' and isnull(dr,0)=0 ");
		if(dao == null)
			dao = new BaseDAO();
		java.util.Map data = (java.util.Map)dao.executeQuery(str.toString(), ResultSetProcessorTool.MAPPROCESSOR);
		
		if(data == null || data.size() == 0){
			list.add(infor.getCodevalue());
			return null;
		}	
		vo.setAttributeValue("pk_replace", data.get("pk_areacl"));		
//		需要返回编码对应值
		return PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue("pk_replace"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
