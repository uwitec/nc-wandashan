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
	 * 进行三种保存(non-Javadoc)
	 * 
	 * @see nc.itf.wds.w80060204.Iw80060204#saveFyd(nc.vo.pub.AggregatedValueObject)
	 */
	public void saveFyd(AggregatedValueObject billVO) throws Exception {
		// TODO Auto-generated method stub
		// 首先判断出来的对象是否有值
		if (null != billVO.getParentVO() && null != billVO.getChildrenVO()
				&& billVO.getChildrenVO().length > 0) {
			// 获取聚合VO中的主表信息
			TbFydnewVO fydVO = (TbFydnewVO) billVO.getParentVO();
			String[] saleid = null; // 销售订单主键
			String[] vercode = null; // 订单号主键
			saleid = fydVO.getCsaleid().split(","); // 进行截取销售订单主键
			vercode = fydVO.getVbillno().split(","); // 截取订单号主键
			fydVO.setDr(0); // 设置删除标志
			fydVO.setPk_mergelogo(fydVO.getCsaleid()); // 设置销售主表中 的主键 （合并后的）
			fydVO.setCsaleid(""); // 因为数据库中的销售主键存储长度是20位所以现在把销售主键给清除不进行存储
			fydVO.setIprintcount(1); // 设置打印次数
			fydVO.setIprintdate(new UFDate(new Date())); // 设置打印日期
			// 获取聚合VO中子表信息
			TbFydmxnewVO[] fydmxVO = (TbFydmxnewVO[]) billVO.getChildrenVO();
			// insert发运单主表 这条数据是和当前页面中显示的数据一致，没有拆分开的
			String pk = ivo.insertVO(fydVO);
			// 获取返回的主键进行给子表赋值
			for (int i = 0; i < fydmxVO.length; i++) {
				TbFydmxnewVO fydmx = fydmxVO[i];
				fydmx.setFyd_pk(pk); // 运单主表PK
				fydmx.setDr(0); // 设置删除标志
				ivo.insertVO(fydmx); // 插入子表
			}

			// 设置标识列
			fydVO.setMergelogo(fydVO.getVbillno());

			// 循环销售订单主键的数组
			for (int i = 0; i < saleid.length; i++) {
				// 把截取后的销售订单主键再赋值给当前的主表VO中，这样子主表VO中的销售主键和订单号就给差分开了
				fydVO.setPk_mergelogo(null);
				fydVO.setCsaleid(saleid[i]);
				fydVO.setVbillno(vercode[i]); // 订单号 同上
				// 给运单插入信息 这次是拆分后的运单信息 进行分存
				pk = ivo.insertVO(fydVO);
				// 循环子表数组，因为是多个订单的子表信息都存在了一起，所以要拆分开来，进行分别存储
				for (int j = 0; j < fydmxVO.length; j++) {
					// 判断 外层循环的是销售主表的的主键，在子表当中也存有主表的主键，所以判断这个两个主键是否相等
					if (saleid[i].equals(fydmxVO[j].getCsaleid())) {
						fydmxVO[j].setFyd_pk(pk); // 运单PK
						fydmxVO[j].setDr(0);
						ivo.insertVO(fydmxVO[j]); // 插入子表信息
					}
				}
				// 根据销售主表ID查询出相对应的信息后更改状态进行回写
				SoSaleVO salevo = (SoSaleVO) iuap.retrieveByPK(SoSaleVO.class,
						fydVO.getCsaleid());

				if (null != salevo) {
					salevo.setVdef6("5"); // 更改状态
					if (null == salevo.getIprintcount()
							|| "".equals(salevo.getIprintcount()))
						salevo.setIprintcount(1); // 设置打印次数
					else
						salevo.setIprintcount(salevo.getIprintcount() + 1);
					if (null == salevo.getVdef7()
							|| "".equals(salevo.getVdef7()))
						salevo.setVdef7(new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss").format(new Date()));// 设置打印时间
				}
				ivo.updateVO(salevo); // 进行更新数据库
			}

		}
	}

}
