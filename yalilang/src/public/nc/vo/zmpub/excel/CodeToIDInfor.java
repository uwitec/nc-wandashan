package nc.vo.zmpub.excel;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.scm.pu.PuPubVO;

public class CodeToIDInfor extends ValueObject {
	
//	不知管理档案类型   如存货管理档案的信息 该数据结构无法封装  用户自定义实现吧
	
	private String idname;//常量
	private String tablename;//常量
	private String codename;//常量
	private String codevalue;//------------运行期赋值
	
	private String corpname;//常量	
	private String corpvalue;//------------运行期赋值
	public UFBoolean isBasic = UFBoolean.FALSE;//是否产品基础档案  //常量
	public UFBoolean isCorp = UFBoolean.FALSE;//档案是否公司级  //常量
	public UFBoolean isCorpField = UFBoolean.FALSE;//公司字段  公司编码转换为公司ID  必须优先转换   //常量
	public UFBoolean isDefTran = UFBoolean.FALSE;//是否自定义  转换
	private String defTranClassName = null;//自定义编码转换类  继承 ideftran 接口
	
	

	public String getDefTranClassName() {
		return defTranClassName;
	}

	public void setDefTranClassName(String defTranClassName) {
		this.defTranClassName = defTranClassName;
	}

	public String getSelectSql(){
		if(PuPubVO.getString_TrimZeroLenAsNull(codevalue)==null)
			return null;
		StringBuffer str = new StringBuffer();
		str.append("select ");
		str.append(idname);
		str.append(" from ");
		str.append(tablename);
		str.append(" where ");
		str.append(codename);
		str.append(" = '"+codevalue+"' ");
		if(isCorp.booleanValue()){
			str.append(" and ");
			str.append(corpname);
			str.append(" = '"+corpvalue+"' ");
		}
		str.append(" and nvl(dr,0)=0 ");
		return str.toString();
	}
	
	public UFBoolean getIsCorp() {
		return isCorp;
	}

	public void setIsCorp(UFBoolean isCorp) {
		this.isCorp = isCorp;
	}

	public String getFomular(){
		StringBuffer str = new StringBuffer();
		str.append(idname);
		str.append("->getColValue(");
		str.append(tablename);
		str.append(","+idname);
		str.append(",");
		str.append(codename);
		str.append(",");
		str.append(codevalue);
		str.append(")");
		return str.toString();
	}

	public String getIdname() {
		return idname;
	}

	public void setIdname(String idname) {
		this.idname = idname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getCodevalue() {
		return codevalue;
	}

	public void setCodevalue(String codevalue) {
		this.codevalue = codevalue;
	}

	public String getCorpvalue() {
		return corpvalue;
	}

	public void setCorpvalue(String corpvalue) {
		this.corpvalue = corpvalue;
	}

	public UFBoolean getIsBasic() {
		return isBasic;
	}

	public void setIsBasic(UFBoolean isBasic) {
		this.isBasic = isBasic;
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

}
