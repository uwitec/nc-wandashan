package nc.ui.wds.ic.cargtray;

import nc.ui.bd.ref.AbstractRefModel;

public class InvmandocRefModel extends AbstractRefModel{

	// ����������ݻ��������仯��ͬʱ��setpkʱ����������
	private String envWherePart = null;
	
	 private String m_sRefTitle = "��ǰ��λ�´��";
	 
	 private String tablename="tb_spacegoods join " +
	 		"wds_invbasdoc on tb_spacegoods.pk_invmandoc=wds_invbasdoc.pk_invmandoc  join " +
	 		"bd_invmandoc on tb_spacegoods.pk_invmandoc=bd_invmandoc.pk_invmandoc  join  " +
	 		"bd_invbasdoc on tb_spacegoods.pk_invbasdoc= bd_invbasdoc.pk_invbasdoc ";
	
	 private String[] fieldcode={"invcode","invname","invspec","invtype",
			 "tb_spacegoods.pk_invbasdoc","tb_spacegoods.pk_invmandoc"};
	 
	 
	 private String[] fieldname={"�������","�������","���","�ͺ�"};
	
	 private String[] hidecode={"tb_spacegoods.pk_invbasdoc","tb_spacegoods.pk_invmandoc"};
	 
	 private int defaultFieldCount=4;
	    /**
	     * RouteRefModel ������ע�⡣
	     */
	    public InvmandocRefModel() {
	    	super();
	    }

	    /**
	     * getDefaultFieldCount ����ע�⡣
	     */
	    public int getDefaultFieldCount() {
		return defaultFieldCount;
	    }

	    /**
	     * ��ʾ�ֶ��б� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldCode() {
	    	return fieldcode ;
	    }

	    /**
	     * ��ʾ�ֶ������� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public java.lang.String[] getFieldName() {
	    	return fieldname;
	    }

	    /**
	     * �˴����뷽��˵���� �������ڣ�(2001-9-6 10:56:48)
	     */
	    public String[] getHiddenFieldCode() {
	    	return hidecode;
	    }

	    /**
	     * �����ֶ���
	     * 
	     * @return java.lang.String
	     */
	    public String getPkFieldCode() {
		return "tb_spacegoods.pk_invmandoc";
	    }

	    /**
	     * ���ձ��� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getRefTitle() {
		return m_sRefTitle;
	    }
	    @Override
	    public String getWherePart() {
	    	StringBuffer strWhere = new StringBuffer();
	    	strWhere.append(" isnull(tb_spacegoods.dr,0)=0 and isnull(wds_invbasdoc.dr,0) = 0 and isnull(bd_invbasdoc.dr,0) = 0" +
	    			" and isnull(bd_invmandoc.dr,0) = 0 ")   
	    	        .append(" and bd_invmandoc.pk_corp='"+getPk_corp()+"'");
	    	return strWhere.toString();
	    }
	    /**
	     * �������ݿ�������ͼ�� �������ڣ�(01-4-4 0:57:23)
	     * 
	     * @return java.lang.String
	     */
	    public String getTableName() {
	    
		return tablename;
	    }
	    @Override
	    public boolean isCacheEnabled() {
	    	
	    	return false;
	    }
		/**
		 * ������� SQL
		 */
		protected String buildBaseSql(String patch, String[] columns,
				String[] hiddenColumns, String tableName, String whereCondition) {
			StringBuffer whereClause = new StringBuffer();
			StringBuffer sql = new StringBuffer("select distinct").append(patch)
					.append(" ");
			int columnCount = columns == null ? 0 : columns.length;
			addQueryColumn(columnCount, sql, columns, hiddenColumns);
			// ����FROM�Ӿ�
			sql.append(" from ").append(tableName);
			// ����WHERE�Ӿ�
			if (whereCondition != null && whereCondition.trim().length() != 0) {
				whereClause.append(" where (").append(whereCondition).append(" )");
			} else
				whereClause.append(" where 11=11 ");

			appendAddWherePartCondition(whereClause);

			addDataPowerCondition(getTableName(), whereClause);
			addSealCondition(whereClause);
			addEnvWherePart(whereClause);
			sql.append(" ").append(whereClause.toString());

			return sql.toString();
		}
		private void appendAddWherePartCondition(StringBuffer whereClause) {

			if (getAddWherePart() == null) {
				return;
			}

			if (isPKMatch() && !isMatchPkWithWherePart()) {

				return;

			}
			whereClause.append(" ").append(getAddWherePart());

		}
		private void addEnvWherePart(StringBuffer whereClause) {
			// setpk ,������������
			if (isPKMatch()) {
				return;
			}
			String wherePart = getEnvWherePart();
			if (wherePart != null) {

				whereClause.append(" and (").append(wherePart).append(") ");

			}

		}
		/**
		 * @return ���� envWherePart��
		 */
		private String getEnvWherePart() {
			return envWherePart;
		}
		/**
		 * ��������ʱ��Ҫ���ò��յĻ��������ķ���ȥƴ�����������޷����ݻ��������ı仯���仯�� ���磺
		 * 
		 * @param envWherePart
		 *            Ҫ���õ� envWherePart��
		 */
		public void setEnvWherePart(String envWherePart) {
			this.envWherePart = envWherePart;
		}

}
