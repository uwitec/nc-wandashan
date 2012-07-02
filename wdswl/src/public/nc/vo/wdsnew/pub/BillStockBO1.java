package nc.vo.wdsnew.pub;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import nc.bs.zmpub.pub.tool.stock.BillStockBO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wl.pub.WdsWlPubConst;
/**
 * ���ɽ��Ŀ ҵ�񵥾��ȴ���������
 * �������� ������� �������� ������� ��ͨ������� �����ִ���
 * һ���ѯ�ִ��� Ҳͨ�������
 * @author mlr
 */
public class BillStockBO1 extends BillStockBO{
	/**
	 * �������� ->������ ��Ӧ��ϵ
	 */
	private Map<String ,String > typetoChangeclass=new HashMap<String, String>();
	/**
	 *  ��������->�ִ��� �����仯����
	 */
    private Map<String,UFBoolean[]> typetosetnum=new HashMap<String, UFBoolean[]>();
    /**
     * �ִ��� �����仯�ֶ�
     * ��������� ��渨����
     */
    private String[] changeNums =new String[]{"whs_stocktonnage","whs_stockpieces"};
    /**
     * �ִ���ʵ���� ·��
     */
    private String  className="nc.vo.ic.pub.StockInvOnHandVO";
    /**
     * �ȴ����������Сά��
     *ά��Ϊ�� ��˾ �ֿ� ��λ ��� ���� ���״̬ �������
     */
    private String[] def_fields=new String[]{
    		"pk_corp","pk_customize1","pk_cargdoc",
    		"pk_invmandoc","pk_invbasdoc","whs_batchcode",
    		"ss_pk","creadate"};
	@Override
	public Map<String, String> getTypetoChangeClass() throws Exception {
		if(typetoChangeclass.size()==0){
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN, "nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");//����������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, "nc.bs.wds.self.changedir.CHGWDS7TOACCOUNTNUM");//�����������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN, "nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");//���������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, "nc.bs.wds.self.changedir.CHGWDS9TOACCOUNTNUM");//����������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, "nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");//�����������Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1, "nc.bs.wds.self.changedir.CHGWDS6TOACCOUNTNUM");//������������ɾ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT, "nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");//�������۳��Ᵽ��
			typetoChangeclass.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1, "nc.bs.wds.self.changedir.CHGWDS8TOACCOUNTNUM");//�������۳���ɾ��			
		}	
		return typetoChangeclass;
	}

	@Override
	public Map<String, UFBoolean[]> getTypetosetnum() throws Exception {
		if(typetosetnum.size()==0){
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_IN_1, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_ALLO_IN_1, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_OTHER_OUT_1, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT, new UFBoolean[]{new UFBoolean(true),new UFBoolean(true)});
			typetosetnum.put(WdsWlPubConst.BILLTYPE_SALE_OUT_1, new UFBoolean[]{new UFBoolean(false),new UFBoolean(false)});

		}
		return typetosetnum;
	}
	/**
	 * ͨ��where������ѯ�ִ���
	 * @param whereSql
	 * @return
	 */
	public SuperVO[] queryStock(String whereSql)throws Exception{
		String clname=getClassName();
		if(clname==null || clname.length()==0)
			throw new Exception("û��ע���ִ���ʵ����ȫ·��");
		Class cl=Class.forName(clname);
		Collection list= getDao().retrieveByClause(cl, whereSql);
        if(list==null || list.size()==0)
        	return null;
        SuperVO[] vos=(SuperVO[]) list.toArray((SuperVO[])java.lang.reflect.Array.newInstance(cl, list.size()));		
		return vos;
		
	}
	@Override
	public String[] getChangeNums() {
		
		return changeNums;
	}

	@Override
	public String getClassName() {
		
		return className;
	}

	@Override
	public String[] getDef_Fields() {
		
		return def_fields;
	}

}
