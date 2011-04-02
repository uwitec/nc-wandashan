package nc.bs.wds.w80060604;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.wds.w80060604.Iw80060604;
import nc.vo.dm.confirm.TbFydmxnewVO;
import nc.vo.dm.confirm.TbFydnewVO;
import nc.vo.wds.w80060604.SoSaleVO;
import nc.vo.wl.pub.CommonUnit;

public class W80060604Impl implements Iw80060604 {

	IVOPersistence ivo = (IVOPersistence) NCLocator.getInstance().lookup(
			IVOPersistence.class.getName());

	/*
	 * 根据传过来的值进行操作数据库(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w80060604.Iw80060604#insertFyd(java.util.List,
	 *      java.util.List, java.util.List)
	 */
	public void insertFyd(List<TbFydnewVO> fydList,
			List<TbFydmxnewVO[]> fydmxList, List<SoSaleVO> saletempList)
			throws Exception {
		// TODO Auto-generated method stub

		if (null != fydList && fydList.size() > 0 && null != fydmxList
				&& fydmxList.size() > 0) {
			for (int i = 0; i < fydList.size(); i++) {
				fydList.get(i).setDr(0);
				String pk = ivo.insertVO(fydList.get(i));// 插入发运单主表
				TbFydmxnewVO[] fydmxVO = fydmxList.get(i);
				for (int j = 0; j < fydmxVO.length; j++) {
					TbFydmxnewVO fydmx = fydmxVO[j];
					if (null != fydmx) {
						fydmx.setFyd_pk(pk);
						fydmx.setDr(0);
						ivo.insertVO(fydmx); // 插入发运单子表
					}
				}

			}
		}
		if (null != saletempList && saletempList.size() > 0) {
			IUAPQueryBS iuap = (IUAPQueryBS) NCLocator.getInstance().lookup(
					IUAPQueryBS.class.getName());
			for (int i = 0; i < saletempList.size(); i++) {
				SoSaleVO sale = saletempList.get(i);
				if (null != sale) {
					if (null != sale.getCsaleid()
							&& !"".equals(sale.getCsaleid())) {
						int num = CommonUnit.getIprintCount(sale.getCsaleid());
						if (num == 0) {
							sale.setIprintcount(1);
						} else {
							sale.setIprintcount(num + 1); // 累加打印次数

						}
					}
					if (null == sale.getVdef7() || "".equals(sale.getVdef7())) {
						// 打印时间
						sale.setVdef7(new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss").format(new Date()));
					}
					// 更新销售主表的打印次数和打印时间
					ivo.updateVO(sale);
				}

			}

		}
	}

	// 更新操作
	public void updateSosale(List list) throws Exception {
		// TODO Auto-generated method stub
		if (null != list && list.size() > 0)
			ivo.updateVOList(list);
	}

}
