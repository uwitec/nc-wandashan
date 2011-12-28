package nc.bs.ic.pub;

import java.util.HashMap;
import java.util.Map;
import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.wl.pub.WdsPubResulSetProcesser;
import nc.vo.pub.SuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.PuPubVO;
import nc.vo.wl.pub.WdsWlPubTool;
/**
 * ���ڻ�д�Ĺ�����
 * @author mlr
 * @date 2011-9-13
 */
public class WriteBackTool{
	private static BaseDAO dao=null;
	private static String vsourcebillrowid = "vsourcebillrowid";//Ĭ����Դ��id  �������Ĭ����Ҫ����
	private static String vsourcebilltype = "vsourcebilltype";
	private static String vsourcebillid = "vsourcebillid";
	private static BaseDAO getDao(){
	 if(dao==null){
		 dao=new BaseDAO();
	 }	
	 return dao;
	}
	private static Map<String,WriteBackVO> addmap= new HashMap<String,WriteBackVO>();//���������д������
	private static Map<String,WriteBackVO> editmap= new HashMap<String,WriteBackVO>();//����޸Ļ�д������
	private static Map<String,WriteBackVO> delemap= new HashMap<String,WriteBackVO>();//���ɾ����д������	
	
	public static void setVsourcebillrowid(String id){
		vsourcebillrowid = id;
	}
	public static void setVsourcebillid(String id){
		vsourcebillid = id;
	}
	public static void setVsourcebilltype(String type){
		vsourcebilltype = type;
	}
    public static Map<String, WriteBackVO> getAddmap() {
		return addmap;
	}

	public  void setAddmap(Map<String, WriteBackVO> addmap) {
		this.addmap = addmap;
	}

	public Map<String, WriteBackVO> getEditmap() {
		return editmap;
	}

	public void setEditmap(Map<String, WriteBackVO> editmap) {
		this.editmap = editmap;
	}

	public Map<String, WriteBackVO> getDelemap() {
		return delemap;
	}

	public void setDelemap(Map<String, WriteBackVO> delemap) {
		this.delemap = delemap;
	}
/**
   * ��д�������ڷ���	
   * 
   * @throws Exception
   * @param vos ��д�����vo����
   * @param soutablename  ��Դ����
   * @param soutableidname ��Դ����������
   * @param fieldnames     Ҫ��д���ֶ�
   * @param backfieldnames ��Դ���д��Ӧ�ֶ�
   */
  public static void writeBack(SuperVO[] vos,String soutablename,String soutableidname,String[] fieldnames,String[] backfieldnames)throws Exception{
	 //���ֻ�д��vo���� 
	 splitSetMap(vos,soutablename,soutableidname,fieldnames,backfieldnames);
	 //�������ݵĻ�д
	 writeBackSou();
	 clearMap();
  }
  
  private static void clearMap(){
	  addmap.clear();
	  editmap.clear();	
      delemap.clear();	
      setVsourcebillrowid("vsourcebillrowid");//����ΪĬ��ֵ
      setVsourcebillid("vsourcebillid");
      setVsourcebilltype("vsourcebilltype");
  }
  /**
   * �������ݵĻ�д
 * @throws Exception 
   */
private static void writeBackSou() throws Exception{
	writeBackAdd();//��д��������
	writeBackEdit();//��д�޸�����
	writeBackDete();//��дɾ������
}
/**
 * ��ɾ�����ݵĻ�д
 * @throws Exception 
 */
private static void writeBackDete() throws Exception {
	if(delemap == null || delemap.size() == 0)
		return;
	for(String key:delemap.keySet()){
		WriteBackVO vo=delemap.get(key);
		UFDouble[] oldnums=getOldNums(vo);//������    
		for(int i=0;i<oldnums.length;i++){
			oldnums[i]=WdsWlPubTool.DOUBLE_ZERO.sub(oldnums[i]);
		}
		vo.setNums(oldnums);      
	} 
	writeBackToSource(delemap);
}
/**
 * ������ݿ��оɵĲ�ѯ����
 * @param vo
 * @return
 * @throws Exception 
 */
private static UFDouble[] getOldNums(WriteBackVO vo) throws Exception {
	String sql=vo.getQueryOldSql();
	if(sql==null){
		return null;
	}
    Object o=getDao().executeQuery(sql, WdsPubResulSetProcesser.ARRAYROCESSOR);
    if(o==null){
  	  throw new Exception(" Ҫ��д�������Ѿ������� ,���ִ���");
    }
    Object[] obs=(Object[])o;
    if(obs==null || obs.length==0)
      throw new Exception(" Ҫ��д�������Ѿ������� ,���ִ���");
    
    UFDouble[] oldnums=new UFDouble[obs.length];
    for(int i=0;i<obs.length;i++){
    	oldnums[i]=PuPubVO.getUFDouble_NullAsZero(obs[i]);
    }  
	return oldnums;
}
/**
 * ���޸����ݵĻ�д
 * @throws DAOException 
 */
private static void writeBackEdit() throws Exception {
	 if(editmap == null || editmap.size()==0){
		   return;
	 }
	 for(String key:editmap.keySet()){
		  WriteBackVO vo=editmap.get(key);
	      UFDouble[] oldnums=getOldNums(vo);//������      
	      if(oldnums==null){
	    	  continue;
	      }
	      UFDouble[] newnums=vo.getNums();//������
	      if(newnums==null || newnums.length==0){
	    	  throw new Exception(" Ҫ��д�������Ѿ������� ,���ִ���");
	      }
	      if(oldnums==null || oldnums.length==0){
	    	  throw new Exception(" Ҫ��д�������Ѿ������� ,���ִ���");
	      }
	      UFDouble[] editnums=new UFDouble[newnums.length];//������ Ҫ��д������
	      for(int i=0;i<oldnums.length;i++){
	    	  editnums[i]=newnums[i].sub(oldnums[i]);
	      }
	      vo.setNums(editnums);//��������Ļ�д����  �����дvo	      
	 }	
     writeBackToSource(editmap);//�����ݻ�д����Դ,�������ݿ����	    	      

}
/**
 * ���л�д����,�������ݿ�
 * @param map
 * @throws Exception 
 */
private static void writeBackToSource(Map<String, WriteBackVO> map) throws Exception {
	  if(map == null || map.size()==0){
		  return;
	  }
	  for(String key:map.keySet()){
    	  WriteBackVO vo=map.get(key);
    	  String sql=vo.getWriteBackSql();
    	  if(sql==null){
    		  continue;
    	  }
    	  getDao().executeUpdate(sql);  
      }	
}

/**
 * ���������ݵĻ�д
 * @throws Exception
 */
private static void writeBackAdd() throws Exception {
	writeBackToSource(addmap);
}

/**
 * �÷������� ������ �޸�  ɾ�� �Ļ�д���ֳ���������װ�ɱ���������Ҫ����������
 * @param vos
 * @param soutablename
 * @param soutableid
 * @param backfieldnames
 * @param backnums
 * @throws Exception 
 */
private static void splitSetMap(SuperVO[] vos, String soutablename,String soutableidname,
		String[] fieldnames, String[] backfieldnames) throws Exception {
	if(vos==null || vos.length==0){
		return;
	}
	if(soutablename==null || soutablename.length()==0){
		return;
	}
	if(soutableidname==null || soutableidname.length()==0){
		return;
	}
	if(backfieldnames==null || backfieldnames.length==0){
		return;
	}
	if(backfieldnames.length!=fieldnames.length){
		throw new Exception("��д���ݲ��Ϸ�");
	}
	for(SuperVO vo:vos){		
		//�������Ϊ�� ��Ϊ��������
		if(vo.getStatus()==VOStatus.NEW ||vo.getPrimaryKey()==null || vo.getStatus()== VOStatus.UNCHANGED){				
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			addmap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillrowid)),backvo);
		}else if(vo.getStatus()==VOStatus.UPDATED ){
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			editmap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillrowid)),backvo);
		}else{
			setValue(vo, soutablename,soutableidname,fieldnames,backfieldnames);
			delemap.put(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillrowid)),backvo);			
		}	
	}
}
private static  WriteBackVO  backvo=null;
/**
 * ����д��vo��ֵ
 * @param vo  
 * @param soutablename ��Դ����
 * @param soutableidname ��Դ��id����
 * @param fieldnames     Ҫ��д�ı���ֶ�����
 * @param backfieldnames Ҫ��д����Դ���ֶ�������
 */
private static void setValue(SuperVO vo, String soutablename,
		String soutableidname, String[] fieldnames, String[] backfieldnames) {
	backvo=new WriteBackVO();
	backvo.setIdname(soutableidname);
	backvo.setIdvalue(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillrowid)));
    UFDouble[] backnums=new UFDouble[fieldnames.length];
	for(int i=0;i<fieldnames.length;i++){
		backnums[i]=PuPubVO.getUFDouble_NullAsZero(vo.getAttributeValue(fieldnames[i]));
	}
	backvo.setNums(backnums);
	backvo.setNumsnames1(fieldnames);
	backvo.setSourcetablename(soutablename);
	backvo.setVsourcebillid(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillid)));
	backvo.setVsourcebillrowid(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebillrowid)));
	backvo.setVsourcebilltype(PuPubVO.getString_TrimZeroLenAsNull(vo.getAttributeValue(vsourcebilltype)));
	backvo.setNumsnames(backfieldnames);
	backvo.setTablename(PuPubVO.getString_TrimZeroLenAsNull(vo.getTableName()));
	backvo.setId(PuPubVO.getString_TrimZeroLenAsNull(vo.getPrimaryKey()));
	backvo.setIdname1(PuPubVO.getString_TrimZeroLenAsNull(vo.getPKFieldName()));	
}
/**
 * ��д�������У��
 * @throws Exception
 */
 public static void check()throws Exception{} 
}
