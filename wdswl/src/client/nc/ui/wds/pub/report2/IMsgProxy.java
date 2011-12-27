package nc.ui.wds.pub.report2;

/**
 * 消息代理接口。
 * 创建日期：(2004-8-19 9:23:13)
 * @author：刘建波
 */
public interface IMsgProxy {
	
public Object sendMsg(ISender sender,String receiverClassName,Object msg) throws Exception;
	
public Object sendMsg(ISender sender,IReceiver receiver,Object msg) throws Exception;
}
