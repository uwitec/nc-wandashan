package nc.ui.wl.pub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;

import nc.ui.pub.beans.UITable;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.IUniqueRule;

/**
 * 
 * ����Ϊ����ǰ�� ǰ̨У���� 
 * 
 * */
public  class BeforeSaveValudate{
	
	//����ļ����ֶι���Ψһ�ԣ�ÿһ�еļ�¼�ļ����ֶι�����Ψһ�Լ�¼���������бȽ� ������Ψһ��У��
	public static void  beforeSaveBodyUnique(UITable table,BillModel model,String[] fields,String[] displays) throws Exception{
		int num =table.getRowCount();
		if(fields == null || fields.length == 0){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				String key = "";
				for(String str : fields){
					Object o1 =model.getValueAt(i, str);
					key = key + ","+String.valueOf(o1);
				}
				if(list.contains(key)){
					String dis="";
					for(int j=0;j<displays.length;j++){
					   dis=dis+"[ "+displays[j]+" ]";
					}			
					throw new BusinessException("��["+(i+1)+"]�б����ֶ� "+dis+" �����ظ�!");
				}else{
					list.add(key);
				}
			}
		}
	}
	public static void BodyNotNULL(UITable table) throws Exception{
		if(table.getRowCount()<=0){
			throw new Exception("���岻����Ϊ��");
		}
	}
	
	//����ĳ���ֶε�Ψһ��У��
	public static void  FieldBodyUnique(UITable table,BillModel model,String checkField,String displayName) throws Exception{
		int num =table.getRowCount();
		
		if(checkField == null || "".equalsIgnoreCase(checkField)){
			return;
		}
		if(num>0){
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0 ;i<num; i++){
				    String key = "";			
					Object o1 = model.getValueAt(i,checkField);
					key = String.valueOf(o1);	
					
				if(list.contains(key)){
					throw new BusinessException("�����["+(i+1)+"]���ֶ�"+"[ "+displayName+" ]"+"�����ظ�!");
				}else{
					list.add(key);
				}
			}
		}
	}
	//����ı����Ǵӽ����ȡ�������ı����vos(Ч�ʵͣ���ò���)
	public static  boolean checkUniqueRule(CircularlyAccessibleValueObject[] vos, IUniqueRule rule) throws Exception
	{
        StringBuffer errormsg=new StringBuffer();
		 
		if (isEmpty(vos) || isEmpty(rule))
			return true;

		for (int i = 0; i < vos.length - 1; i++)
		{
			for (int j = i + 1; j < vos.length; j++)
			{
				boolean same = true;
				for (int k = 0; k < rule.getFields().length; k++)
				{
					Object o1 = vos[i].getAttributeValue(rule.getFields()[k]);
					Object o2 = vos[j].getAttributeValue(rule.getFields()[k]);
					same = same
							&& voassert(o1, ICompareRule.OPERATOR_EQUAL, o2);
				}
				if (same)
				{
					errormsg.append(rule.getHint());
					errormsg.append("\n");
					return false;
				}
			}
		}
		return true;
	}
	//����ı����Ǵӽ����ȡ�������ı����vos(Ч�ʵͣ���ò���)
	public static void bodyFieldNotNull(CircularlyAccessibleValueObject[] bodys,String checkField,String displayName)throws Exception{
		
		  for(int i=0;i<bodys.length;i++){
              if(isEmpty(bodys[i].getAttributeValue(checkField))){            	  
            	  throw new Exception("�����"+(i+1)+"��"+displayName+"����Ϊ��");
              }
          }    	
	}
	public static boolean isEmpty(Object value)
	{
		if (value == null)
			return true;
		if ((value instanceof String)
				&& (((String) value).trim().length() <= 0))
			return true;
		if ((value instanceof Object[]) && (((Object[]) value).length <= 0))
			return true;
		if ((value instanceof Collection) && ((Collection) value).size() <= 0)
			return true;
		if ((value instanceof Dictionary) && ((Dictionary) value).size() <= 0)
			return true;
		return false;
	}

	public static boolean isNULL(Object o) {
		if (o == null || o.toString().trim().equals(""))
			return true;
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static boolean voassert(Object op1, int op, Object op2)
	{

		//ͬʱΪ��
		if (isEmpty(op1) && isEmpty(op2))
		{
			switch (op)
			{
			case ICompareRule.OPERATOR_NOTEQUAL:
			case ICompareRule.OPERATOR_LESS:
			case ICompareRule.OPERATOR_MORE:
				return false;
			default:
				return true;
			}
		}
		//һ��Ϊ�գ�һ������
		if (isEmpty(op1) || isEmpty(op2))
		{
			switch (op)
			{
			case ICompareRule.OPERATOR_EQUAL:
			case ICompareRule.OPERATOR_NOTLESS:
			case ICompareRule.OPERATOR_NOTMORE:
			case ICompareRule.OPERATOR_COEXIST:
				return false;
			default:
				return true;
			}
		}
		//����������

		//���棬����true��
		if (op == ICompareRule.OPERATOR_COEXIST)
			return true;
		//���ܹ��棬����false��
		if (op == ICompareRule.OPERATOR_NOTCOEXIST)
			return false;

		Comparable value1 = null;
		Comparable value2 = null;

		//UFDouble��UFDate��Ҫ���⴦���������ͱ���ʵ��Comparable��
		//��Ϊ���op1ΪUFDouble����ôop2һ��ΪUFDouble��
		if (op1 instanceof Comparable)
		{
			value1 = (Comparable) op1;
			value2 = (Comparable) op2;
		}
		else if (op1 instanceof UFDouble)
		{
			value1 = new Double(((UFDouble) op1).doubleValue());
			value2 = new Double(((UFDouble) op2).doubleValue());
		}
		else if (op1 instanceof UFDate)
		{
			value1 = op1.toString();
			value2 = op2.toString();
		}
		else
		{
//			getErrorMsg().append(
//					nc.bs.ml.NCLangResOnserver.getInstance().getStrByID(
//							"uffactory_hyeaa",
//							"UPPuffactory_hyeaa-000530",
//							null,
//							new String[] { op1.getClass().getName(), op1 + "",
//								op2 + "" }));
//			/* Error����֧�ֵ���������({0})���޷��Ƚ�{1}��{2}. */
//			return false;
		}

		switch (op)
		{
		case ICompareRule.OPERATOR_EQUAL:
			return (value1.compareTo(value2) == 0);
		case ICompareRule.OPERATOR_LESS:
			return (value1.compareTo(value2) < 0);
		case ICompareRule.OPERATOR_MORE:
			return (value1.compareTo(value2) > 0);
		case ICompareRule.OPERATOR_NOTEQUAL:
			return (value1.compareTo(value2) != 0);
		case ICompareRule.OPERATOR_NOTLESS:
			return (value1.compareTo(value2) >= 0);
		case ICompareRule.OPERATOR_NOTMORE:
			return (value1.compareTo(value2) <= 0);
		default:
//		getErrorMsg().append(
//					nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
//							"uffactory_hyeaa", "UPPuffactory_hyeaa-000097")/*
//																			    * @res
//																		    * "Error����֧�ֵĲ�������"
//																		    */
//							+ op);
			return false;
		}
	}
}
