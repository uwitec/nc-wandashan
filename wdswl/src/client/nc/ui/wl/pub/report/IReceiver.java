package nc.ui.wl.pub.report;

/**
 * 此处插入类型说明。
 * 创建日期：(2004-8-19 9:14:23)
 * @author：刘建波
 */
public interface IReceiver {
/**
*收到sender的消息后的处理。
*@parameter:msgProxy,消息代理。
*@parameter:sender,消息来源。
*@parameter:msg,消息内容。
*@return:返回给sender的消息。
*/
public Object receiverMsg(IMsgProxy msgProxy, ISender sender, Object msg) throws Exception;
}
