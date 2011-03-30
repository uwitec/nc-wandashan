package nc.ui.hg.to.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.busibean.ISysInitQry;
import nc.ui.pub.beans.UIListToList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.para.ISysInitPanel;
import nc.vo.hg.pu.pub.HgPubConst;
import nc.vo.pub.BusinessException;
import nc.vo.pub.para.SysInitVO;

public class TOPlanSplitPanel extends UIListToList implements ISysInitPanel {
	private String pk_corp = null;
	private HashMap<String, String> mapValue2Name = null;// key:int
	// value:name
	private HashMap<String, String> mapName2Value = null;// key:name
	// value:int
	private SysInitVO m_voOrderSource = null;

	public TOPlanSplitPanel(String pk_corp) throws BusinessException {
		super();
		if (pk_corp != null) {
			this.pk_corp = pk_corp;
		} else {
			this.pk_corp = nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
		}
		init();
	}

	private void init() throws BusinessException {
		// ��ʼ��value��name�Ķ�Ӧ��ϵ
		initValueMap();
		// ��ʼ����ߺ��ұߵ��б����Ѿ�ѡ���˵Ĳ��������ұ��б���
		initData();
	}

	private void initData() throws BusinessException {
		// ��ѯ�õ��û��Ѿ�ѡ������ݣ������ұ��б�����������
		ISysInitQry sysinit = (ISysInitQry) NCLocator.getInstance().lookup(
				ISysInitQry.class.getName());
		m_voOrderSource = sysinit.queryByParaCode(this.pk_corp,
				HgPubConst.HG_TO_PARA_01);

		String values = m_voOrderSource.getValue();

		if (values == null) {
			values = "";
		}

		String[] rightNames = new String[values.length()];
		for (int i = 0; i < values.length(); i++) {
			rightNames[i] = mapValue2Name.get(values.substring(i, i + 1));
		}
		this.setRightData(rightNames);
		// û��ѡ�еķ�����ߴ�ѡ
		Iterator<String> names = mapValue2Name.values().iterator();
		List<String> lstRight = Arrays.asList(rightNames);
		List<String> lstLeft = new ArrayList<String>();
		while (names.hasNext()) {
			String s = names.next();
			if (!lstRight.contains(s)) {
				lstLeft.add(s);
			}
		}
		this.setLeftData(lstLeft.toArray());
	}

	private void initValueMap() {
		final String[] sValues = new String[] {
		// �������뵥�ݺ�
				"1",
				// ������������
				"2",
				// ���벿��
				"3",
				// ��������
				"4", };
		final String[] sNames = new String[] {
				"����ƻ����ݺ�",
				"���",
				"���벿��",
				"��������"
		};

		mapValue2Name = new HashMap<String, String>();
		mapName2Value = new HashMap<String, String>();
		for (int i = 0; i < sNames.length; i++) {
			mapValue2Name.put(sValues[i], sNames[i]);
			mapName2Value.put(sNames[i], sValues[i]);
		}
		this.setLeftData(sNames);
	}

	public UIPanel getPanel() {
		// TODO Auto-generated method stub
		return this;
	}

	public SysInitVO[] getSysInitVOs() {
		// TODO Auto-generated method stub
		Object[] values = this.getReturnValues();

		if (values == null || values.length == 0) {
			//MessageDialog.showWarningDlg(this, "�������þ���", "��������ѡ��һ������");
			m_voOrderSource.setValue(null);
			return new SysInitVO[] { m_voOrderSource };
		}

		StringBuffer bResult = new StringBuffer();

		for (int i = 0; i < values.length; i++) {
			bResult.append(mapName2Value.get(values[i]));
		}
		m_voOrderSource.setValue(bResult.toString());

		SysInitVO[] vos = new SysInitVO[] { m_voOrderSource };
		return vos;
	}

	private Object[] getReturnValues() {
		Object[] datas = this.getRightData();

		if (datas == null || datas.length == 0) {
			return new String[0];
		}

		return datas;
	}

}
