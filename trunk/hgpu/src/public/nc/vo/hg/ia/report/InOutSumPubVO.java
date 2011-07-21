package nc.vo.hg.ia.report;

import java.util.HashMap;

import nc.vo.hg.pu.pub.HgPubTool;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.scm.pu.PuPubVO;

//��ѯ  �����ͽ��  �Ĺ��� ��������

public class InOutSumPubVO extends SuperVO {

	private String  pk_corp;
	private String  crdcenterid;// �����֯��ʶ    
	private String cwarehouseid;//�ֿ�
	private String cinvbasid;//���ID
	private String  cinventoryid;//  �����ʶ  
	private String  vbatch;//���κ�
	
	private String cinvclid;
	
	
	private UFDouble nnum;//����
	private UFDouble nmny;//���
	
	public static UFBoolean isqrybatch = UFBoolean.FALSE;
	public static UFBoolean isware = UFBoolean.FALSE;
	public static UFBoolean isinvcl = UFBoolean.FALSE;//�Ƿ���ϸ��ѯ
	
	public static void setIsInvcl(UFBoolean flag){
		isinvcl = flag;
	}
	
	public static String MCJGCK = "1002A11000000000D8AR";//�̶��ֿ�  ľ��ԭ�ϼӹ��ֿ�
	public static String[] sort_fields = new String[]{"crdcenterid","cinvclid","cinvbasid"};
	public static String materialOut_lldw = "vuserdef5";//���ϳ��ⵥ��ͷ���ϵ�λ �ֶ�
	private static java.util.Map<String, InOutSumPubVO> dataInfor = null;
	/**
	 * 
	 * @author zhf
	 * @˵�������׸ڿ�ҵ����������
	 * 2011-6-20����09:38:19
	 * @param vos
	 * @return
	 */
    public static java.util.Map<String, InOutSumPubVO> dealData(InOutSumPubVO[] vos){
    	if(vos == null || vos.length == 0)
    		return null;
    	if(dataInfor == null)
    		dataInfor = new HashMap<String, InOutSumPubVO>();
    	else
    		dataInfor.clear();
    	StringBuffer key = null;
    	for(InOutSumPubVO vo:vos){
    		key = new StringBuffer();
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getPk_corp()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCrdcenterid()));
    		//    		if(isware.booleanValue())
//    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCwarehouseid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvclid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinvbasid()));
    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getCinventoryid()));
    		//    		if(isqrybatch.booleanValue())
//    		key.append(HgPubTool.getString_NullAsTrimZeroLen(vo.getVbatch()));
    		dataInfor.put(key.toString(), vo);
    	}
    	return dataInfor;
    }
    
    public static String dealCons(ConditionVO[] cons){
    	if(cons == null || cons.length ==0)
			return null;
//		return new ConditionVO().getWhereSQL(cons);
		StringBuffer str = new StringBuffer();
		String value = null;
		for(ConditionVO con:cons){
			value = PuPubVO.getString_TrimZeroLenAsNull(con.getValue());
			if(value == null)
				continue;
			if(con.getFieldCode().equalsIgnoreCase("accountdate")){
				continue;
			}else if(con.getFieldCode().equalsIgnoreCase("invcl")){	
				if(str.length()>0)
					str.append("and");
				str.append(" substr(inv.invcode,0,2) = '"+value+"'");
			}else if(con.getFieldCode().equalsIgnoreCase("inv")){	
				if(str.length()>0)
					str.append("and");
				str.append("  inv.pk_invbasdoc = '"+value+"'");
			}else if(con.getFieldCode().equalsIgnoreCase("pk_calbody")){//--------�ò�ѯ�������� ������ ��Ʊ��֧��
				if(str.length()>0)
					str.append("and");
				str.append(" h.pk_calbody = '"+value+"'");
			}
		}
		
		return str.toString();
    }
    public String getCinvclid() {
		return cinvclid;
	}

	public void setCinvclid(String cinvclid) {
		this.cinvclid = cinvclid;
	}
	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCrdcenterid() {
		return crdcenterid;
	}

	public void setCrdcenterid(String crdcenterid) {
		this.crdcenterid = crdcenterid;
	}

	public String getCwarehouseid() {
		return cwarehouseid;
	}

	public void setCwarehouseid(String cwarehouseid) {
		this.cwarehouseid = cwarehouseid;
	}

	public String getCinvbasid() {
		return cinvbasid;
	}

	public void setCinvbasid(String cinvbasid) {
		this.cinvbasid = cinvbasid;
	}

	public String getCinventoryid() {
		return cinventoryid;
	}

	public void setCinventoryid(String cinventoryid) {
		this.cinventoryid = cinventoryid;
	}

	public String getVbatch() {
		return vbatch;
	}

	public void setVbatch(String vbatch) {
		this.vbatch = vbatch;
	}

	public UFDouble getNnum() {
		return nnum;
	}

	public void setNnum(UFDouble nnum) {
		this.nnum = nnum;
	}

	public UFDouble getNmny() {
		return nmny;
	}

	public void setNmny(UFDouble nmny) {
		this.nmny = nmny;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
