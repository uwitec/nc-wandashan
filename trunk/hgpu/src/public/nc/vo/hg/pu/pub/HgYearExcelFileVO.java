package nc.vo.hg.pu.pub;

import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;


/**
 * liuys 
 * 2010-12-23
 * 
 * �������ƻ���Ϣ�ļ�VO
 * ���ܣ��������߹��ܵ�VO�ۺϣ���������Ҫ�ֶΡ�
		 ��VO��ӦExcel�ļ��У�����Excel��Ϣ�ļ�Ӧ���к��ֶ�һ�µ���

   Excel�ļ���ʽ
   ��������  ���뵥λ ���벿�� ������ ������֯ �Ƿ����� ������λ �������� ��� ��ע ������� ������֯ ����ֿ� ������֯ 
	1        2        3     4      5       6     7        8    9   10     11     12     13      14    
   �����ֿ� ����λ ë���� ���������� �ƻ����� �ƻ���� ��ע
     15     16    17    18       19     20     21
     
 * ���ڣ�(2004-8-25 10:26:26)
 */
public class HgYearExcelFileVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	/**
	 * ��ȼƻ���ͷ�ֶ� ��������
	 */
	
	private String dbilldate;
	/**
	 * ���뵥λ
	 */
	private String pk_corp;
	/**
	 * ���벿��
	 */
	private String capplydeptid;
	/**
	 * ������
	 */
	private String capplypsnid;
	/**
	 * ������֯
	 */
	private String hcreqcalbodyid;
	/**
	 * �Ƿ�����
	 */
	private String fisself;
	/**
	 * ������λ
	 */
	private String csupplycorpid;
	/**
	 * ��������
	 */
	private String csupplydeptid;
	/**
	 * ���
	 */
	private String cyear;
	/**
	 * ��ע
	 */
	private String hvmemo;
	
	//��ȼƻ������ֶ�
	/**
	 * �������
	 */
	private String invcode;
	/**
	 * ������֯
	 */
	private String bcreqcalbodyid;
	/**
	 * ����ֿ�
	 */
	private String creqwarehouseid;
	/**
	 * ������֯
	 */
	private String csupplycalbodyid;
	
	/**
	 * �����ֿ�
	 */
	private String csupplywarehouseid;
	/**
	 * ����λ
	 */
	private String pk_measdoc;
	/**
	 * ë����
	 */
	private String nnum;
	/**
	 * ����������
	 */
	private String nnetnum;
	/**
	 * �ƻ�����
	 */
	private String nprice;
	/**
	 * �ƻ����
	 */
	private String nmny;
	/**
	 * ��ע
	 */
	private String bvmemo;
	
	private String nmonnum1;
	private String nmonnum2;
	private String nmonnum3;
	private String nmonnum4;
	private String nmonnum5;
	private String nmonnum6;
	private String nmonnum7;
	private String nmonnum8;
	private String nmonnum9;
	private String nmonnum10;
	private String nmonnum11;	
	private String nmonnum12;
	
	@Override
	public String[] getAttributeNames() {
		// TODO Auto-generated method stub
		return new String[] {"dbilldate","pk_corp","capplydeptid","capplypsnid","hcreqcalbodyid","fisself","csupplycorpid",
				"csupplydeptid","cyear","hvmemo"
				,"invcode","bcreqcalbodyid","creqwarehouseid","csupplycalbodyid","csupplywarehouseid","pk_measdoc",
				"nnum","nnetnum","nprice","nmny","bvmemo","nmonnum1","nmonnum2","nmonnum3","nmonnum4","nmonnum5",
				"nmonnum6","nmonnum7","nmonnum8","nmonnum9","nmonnum10","nmonnum11","nmonnum12"
				};
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		// TODO Auto-generated method stub
		
		/*��ȼƻ���ͷ*/
		if (attributeName.equals("dbilldate")) {
			return dbilldate;
		}
		else if (attributeName.equals("pk_corp")) {
			return pk_corp;
		}
		else if (attributeName.equals("capplydeptid")) {
			return capplydeptid;
		}
		else if (attributeName.equals("capplypsnid")) {
			return capplypsnid;
		} 
		else if (attributeName.equals("hcreqcalbodyid")) {
			return hcreqcalbodyid;
		} else if (attributeName.equals("fisself")) {
			return fisself;
		} else if (attributeName.equals("csupplycorpid")) {
			return csupplycorpid;
		} else if (attributeName.equals("cyear")) {
			return cyear;
		} 
		else if (attributeName.equals("hvmemo")) {
			return hvmemo;
		} 

		//��ȼƻ�����
		else if (attributeName.equals("invcode")) {
			return invcode;
		} else if (attributeName.equals("bcreqcalbodyid")) {
			return bcreqcalbodyid;
		} else if (attributeName.equals("creqwarehouseid")) {
			return creqwarehouseid;
		} else if (attributeName.equals("csupplycalbodyid")) {
			return csupplycalbodyid;
		} else if (attributeName.equals("csupplywarehouseid")) {
			return csupplywarehouseid;
		} else if (attributeName.equals("pk_measdoc")) {
			return pk_measdoc;
		} else if (attributeName.equals("nnum")) {
			return nnum;
		} else if (attributeName.equals("nnetnum")) {
			return nnetnum;
		} else if (attributeName.equals("nprice")) {
			return nprice;
		} else if (attributeName.equals("nmny")) {
			return nmny;
		} else if (attributeName.equals("bvmemo")) {
			return bvmemo;
		} else if (attributeName.equals("nmonnum1")){
			return nmonnum1;
		}else if (attributeName.equals("nmonnum2")){
			return nmonnum2;
		}else if (attributeName.equals("nmonnum3")){
			return nmonnum3;
		}else if (attributeName.equals("nmonnum4")){
			return nmonnum4;
		}else if (attributeName.equals("nmonnum5")){
			return nmonnum5;
		}else if (attributeName.equals("nmonnum6")){
			return nmonnum6;
		}else if (attributeName.equals("nmonnum7")){
			return nmonnum7;
		}else if (attributeName.equals("nmonnum8")){
			return nmonnum8;
		}else if (attributeName.equals("nmonnum9")){
			return nmonnum9;
		}else if (attributeName.equals("nmonnum10")){
			return nmonnum10;
		}else if (attributeName.equals("nmonnum11")){
			return nmonnum11;
		}else if (attributeName.equals("nmonnum12")){
			return nmonnum12;
		}
		return null;
	}

	@Override
	public void setAttributeValue(String name, Object value) {
		// TODO Auto-generated method stub
		try {
			if (name.equals("dbilldate")) {
				dbilldate = (String) value;
			} else if (name.equals("pk_corp")) {
				pk_corp = (String) value;
			} else if (name.equals("capplydeptid")) {
				capplydeptid = (String) value;
			} else if (name.equals("capplypsnid")) {
				capplypsnid = (String) value;
			} else if (name.equals("hcreqcalbodyid")) {
				hcreqcalbodyid = (String) value;
			} else if (name.equals("fisself")) {
				fisself = (String) value;
			} else if (name.equals("csupplycorpid")) {
				csupplycorpid = (String) value;
			} else if (name.equals("csupplydeptid")) {
				csupplydeptid = (String) value;
			} else if (name.equals("cyear")) {
				cyear = (String) value;
			} else if (name.equals("hvmemo")) {
				hvmemo = (String) value;
			} 
			
			else if (name.equals("invcode")) {
				invcode = (String) value;
			} else if (name.equals("bcreqcalbodyid")) {
				bcreqcalbodyid = (String) value;
			} else if (name.equals("creqwarehouseid")) {
				creqwarehouseid = (String) value;
			} else if(name.equals("csupplycalbodyid")){
				csupplycalbodyid = (String) value;
			}else if (name.equals("csupplywarehouseid")) {
				csupplywarehouseid = (String) value;
			} else if (name.equals("pk_measdoc")) {
				pk_measdoc = (String) value;
			} else if (name.equals("nnum")) {
				nnum = (String) value;
			} else if (name.equals("nnetnum")) {
				nnetnum = (String) value;
			} else if (name.equals("nprice")) {
				nprice = (String) value;
			} else if (name.equals("nmny")) {
				nmny = (String) value;
			} else if (name.equals("bvmemo")) {
				bvmemo = (String) value;
			} else if(name.equals("nmonnum1")){
				nmonnum1 = (String)value;
			} else if(name.equals("nmonnum2")){
				nmonnum2 = (String)value;
			} else if(name.equals("nmonnum3")){
				nmonnum3 = (String)value;
			} else if(name.equals("nmonnum4")){
				nmonnum4 = (String)value;
			} else if(name.equals("nmonnum5")){
				nmonnum5 = (String)value;
			} else if(name.equals("nmonnum6")){
				nmonnum6 = (String)value;
			} else if(name.equals("nmonnum7")){
				nmonnum7 = (String)value;
			} else if(name.equals("nmonnum8")){
				nmonnum8 = (String)value;
			} else if(name.equals("nmonnum9")){
				nmonnum9 = (String)value;
			} else if(name.equals("nmonnum10")){
				nmonnum10 = (String)value;
			} else if(name.equals("nmonnum11")){
				nmonnum11 = (String)value;
			}else if(name.equals("nmonnum12")){
				nmonnum12 = (String)value;
			}
		} catch (ClassCastException e) {
			throw new ClassCastException("setAttributeValue������Ϊ " + name + " ��ֵʱ����ת�����󣡣�ֵ��" + value + "��");
		}
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

	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getCapplydeptid() {
		return capplydeptid;
	}

	public void setCapplydeptid(String capplydeptid) {
		this.capplydeptid = capplydeptid;
	}

	public String getCapplypsnid() {
		return capplypsnid;
	}

	public void setCapplypsnid(String capplypsnid) {
		this.capplypsnid = capplypsnid;
	}

	public String getHcreqcalbodyid() {
		return hcreqcalbodyid;
	}

	public void setHcreqcalbodyid(String hcreqcalbodyid) {
		this.hcreqcalbodyid = hcreqcalbodyid;
	}

	public String getFisself() {
		return fisself;
	}

	public void setFisself(String fisself) {
		this.fisself = fisself;
	}

	public String getCsupplycorpid() {
		return csupplycorpid;
	}

	public void setCsupplycorpid(String csupplycorpid) {
		this.csupplycorpid = csupplycorpid;
	}

	public String getCsupplydeptid() {
		return csupplydeptid;
	}

	public void setCsupplydeptid(String csupplydeptid) {
		this.csupplydeptid = csupplydeptid;
	}

	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}

	public String getHvmemo() {
		return hvmemo;
	}

	public void setHvmemo(String hvmemo) {
		this.hvmemo = hvmemo;
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public String getBcreqcalbodyid() {
		return bcreqcalbodyid;
	}

	public void setBcreqcalbodyid(String bcreqcalbodyid) {
		this.bcreqcalbodyid = bcreqcalbodyid;
	}

	public String getCreqwarehouseid() {
		return creqwarehouseid;
	}

	public void setCreqwarehouseid(String creqwarehouseid) {
		this.creqwarehouseid = creqwarehouseid;
	}

	public String getCsupplycalbodyid() {
		return csupplycalbodyid;
	}

	public void setCsupplycalbodyid(String csupplycalbodyid) {
		this.csupplycalbodyid = csupplycalbodyid;
	}

	public String getCsupplywarehouseid() {
		return csupplywarehouseid;
	}

	public void setCsupplywarehouseid(String csupplywarehouseid) {
		this.csupplywarehouseid = csupplywarehouseid;
	}

	public String getPk_measdoc() {
		return pk_measdoc;
	}

	public void setPk_measdoc(String pk_measdoc) {
		this.pk_measdoc = pk_measdoc;
	}

	public String getNnum() {
		return nnum;
	}

	public void setNnum(String nnum) {
		this.nnum = nnum;
	}

	public String getNnetnum() {
		return nnetnum;
	}

	public void setNnetnum(String nnetnum) {
		this.nnetnum = nnetnum;
	}

	public String getNprice() {
		return nprice;
	}

	public void setNprice(String nprice) {
		this.nprice = nprice;
	}

	public String getNmny() {
		return nmny;
	}

	public void setNmny(String nmny) {
		this.nmny = nmny;
	}

	public String getBvmemo() {
		return bvmemo;
	}

	public void setBvmemo(String bvmemo) {
		this.bvmemo = bvmemo;
	}

	public String getNmonnum1() {
		return nmonnum1;
	}

	public void setNmonnum1(String nmonnum1) {
		this.nmonnum1 = nmonnum1;
	}

	public String getNmonnum2() {
		return nmonnum2;
	}

	public void setNmonnum2(String nmonnum2) {
		this.nmonnum2 = nmonnum2;
	}

	public String getNmonnum3() {
		return nmonnum3;
	}

	public void setNmonnum3(String nmonnum3) {
		this.nmonnum3 = nmonnum3;
	}

	public String getNmonnum4() {
		return nmonnum4;
	}

	public void setNmonnum4(String nmonnum4) {
		this.nmonnum4 = nmonnum4;
	}

	public String getNmonnum5() {
		return nmonnum5;
	}

	public void setNmonnum5(String nmonnum5) {
		this.nmonnum5 = nmonnum5;
	}

	public String getNmonnum6() {
		return nmonnum6;
	}

	public void setNmonnum6(String nmonnum6) {
		this.nmonnum6 = nmonnum6;
	}

	public String getNmonnum7() {
		return nmonnum7;
	}

	public void setNmonnum7(String nmonnum7) {
		this.nmonnum7 = nmonnum7;
	}

	public String getNmonnum8() {
		return nmonnum8;
	}

	public void setNmonnum8(String nmonnum8) {
		this.nmonnum8 = nmonnum8;
	}

	public String getNmonnum9() {
		return nmonnum9;
	}

	public void setNmonnum9(String nmonnum9) {
		this.nmonnum9 = nmonnum9;
	}

	public String getNmonnum10() {
		return nmonnum10;
	}

	public void setNmonnum10(String nmonnum10) {
		this.nmonnum10 = nmonnum10;
	}

	public String getNmonnum11() {
		return nmonnum11;
	}

	public void setNmonnum11(String nmonnum11) {
		this.nmonnum11 = nmonnum11;
	}

	public String getNmonnum12() {
		return nmonnum12;
	}

	public void setNmonnum12(String nmonnum12) {
		this.nmonnum12 = nmonnum12;
	}

}
