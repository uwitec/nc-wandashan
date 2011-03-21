package nc.bs.wds.w80060204;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060204.Iw80060204;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.wds.w80060406.TbFydmxnewVO;
import nc.vo.wds.w80060406.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;

public class W80060204Impl implements Iw80060204 {

	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());
	IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

	/*
	 * �������ֱ���(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w80060204.Iw80060204#saveFyd(nc.vo.pub.AggregatedValueObject)
	 */
	public void saveFyd(AggregatedValueObject billVO) throws Exception {
		// TODO Auto-generated method stub
		// �����жϳ����Ķ����Ƿ���ֵ
		if (null != billVO.getParentVO() && null != billVO.getChildrenVO()
				&& billVO.getChildrenVO().length > 0) {
			// ��ȡ�ۺ�VO�е�������Ϣ
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			String[] saleid = null; // ���۶�������
			String[] vercode = null; // ����������
			saleid = fydVO.getCsaleid().split(","); // ���н�ȡ���۶�������
			vercode = fydVO.getVbillno().split(","); // ��ȡ����������
			fydVO.setDr(0); // ����ɾ����־
			fydVO.setPk_mergelogo(fydVO.getCsaleid()); // �������������� ������ ���ϲ���ģ�
			fydVO.setCsaleid(""); // ��Ϊ���ݿ��е����������洢������20λ�������ڰ�������������������д洢
			fydVO.setIprintcount(1); // ���ô�ӡ����
			fydVO.setIprintdate(new UFDate(new Date())); // ���ô�ӡ����
			// ��ȡ�ۺ�VO���ӱ���Ϣ
			TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			// insert���˵����� ���������Ǻ͵�ǰҳ������ʾ������һ�£�û�в�ֿ���
			String pk = ivo.insertVO(fydVO);
			// ��ȡ���ص��������и��ӱ�ֵ
			for (int i = 0; i < fydmxVO.length; i++) {
				TbFydmxnewVO fydmx = fydmxVO[i];
				fydmx.setFyd_pk(pk); // �˵�����PK
				fydmx.setDr(0); // ����ɾ����־
				ivo.insertVO(fydmx); // �����ӱ�
			}

			// ���ñ�ʶ��
			fydVO.setMergelogo(fydVO.getVbillno());

			// ѭ�����۶�������������
			for (int i = 0; i < saleid.length; i++) {
				// �ѽ�ȡ������۶��������ٸ�ֵ����ǰ������VO�У�����������VO�е����������Ͷ����ž͸���ֿ���
				fydVO.setPk_mergelogo(null);
				fydVO.setCsaleid(saleid[i]);
				fydVO.setVbillno(vercode[i]); // ������ ͬ��
				// ���˵�������Ϣ ����ǲ�ֺ���˵���Ϣ ���зִ�
				pk = ivo.insertVO(fydVO);
				// ѭ���ӱ����飬��Ϊ�Ƕ���������ӱ���Ϣ��������һ������Ҫ��ֿ��������зֱ�洢
				for (int j = 0; j < fydmxVO.length; j++) {
					// �ж� ���ѭ��������������ĵ����������ӱ���Ҳ��������������������ж�������������Ƿ����
					if (saleid[i].equals(fydmxVO[j].getCsaleid())) {
						fydmxVO[j].setFyd_pk(pk); // �˵�PK
						fydmxVO[j].setDr(0);
						ivo.insertVO(fydmxVO[j]); // �����ӱ���Ϣ
					}
				}
				// ������������ID��ѯ�����Ӧ����Ϣ�����״̬���л�д
				SoSaleVO salevo = (SoSaleVO) iuap.retrieveByPK(SoSaleVO.class,
						fydVO.getCsaleid());

				if (null != salevo) {
					salevo.setVdef6("5"); // ����״̬
					if (null == salevo.getIprintcount()
							|| "".equals(salevo.getIprintcount()))
						salevo.setIprintcount(1); // ���ô�ӡ����
					else
						salevo.setIprintcount(salevo.getIprintcount() + 1);
					if (null == salevo.getVdef7()
							|| "".equals(salevo.getVdef7()))
						salevo.setVdef7(new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss").format(new Date()));// ���ô�ӡʱ��
				}
				ivo.updateVO(salevo); // ���и������ݿ�
			}

		}
	}

}
