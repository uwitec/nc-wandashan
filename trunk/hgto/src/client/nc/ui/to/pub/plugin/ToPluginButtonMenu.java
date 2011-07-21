package nc.ui.to.pub.plugin;

import java.util.ArrayList;
import java.util.HashMap;

import nc.ui.pub.ButtonObject;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.scm.plugin.ScmPluginXML;
import nc.vo.scm.plugin.UIPluginMenuInfo;
import nc.vo.scm.plugin.UIPluginsInfo;
import nc.vo.to.pub.ConstVO;

public class ToPluginButtonMenu {
   
  private static ToPluginButtonMenu instance;
  public static  ToPluginButtonMenu getInstance(){
    if(instance==null)
    {
      instance = new ToPluginButtonMenu();
    }
    return instance;
  }

  public ButtonTree addUIPluginMenu(ButtonTree m_boTree , String billtype,String billtypecode){
    try {
    UIPluginsInfo[] uiplugins = ScmPluginXML.getInstance().getExtUIPlugins("TO", billtype, null);
    if (uiplugins != null && uiplugins.length > 0) {
      UIPluginMenuInfo[] menus;
      String location;
      HashMap<String, ArrayList<UIPluginMenuInfo>> hm_menu = new HashMap();
      ArrayList<UIPluginMenuInfo> al_menu = new ArrayList();

      for (UIPluginsInfo uiplugin : uiplugins) {
        if (!uiplugin.getNodecode().equals(billtypecode))
          continue;

        menus = uiplugin.getMenuinfo();
         if(menus != null){
        	 for (UIPluginMenuInfo menu : menus) {
                 al_menu.add(menu);
               }
             }
         }
      UIPluginMenuInfo menu;
      if(al_menu==null||al_menu.size()==0)
        return m_boTree; 
      for (int i = 0, len = al_menu.size(); i < len; i++) {
        menu = al_menu.get(i);
        ButtonObject button = getPluginButn(menu);
        // parentcode为空 添加到根目录
        if (menu.getParentmenucode() == null) {
          m_boTree.addMenu(button);
        }
        // 根据parent加入到child下
        else {
          String parent = menu.getParentmenucode();
          ButtonObject parentbutton = m_boTree.getButton(parent);
          m_boTree.addChildMenu(parentbutton, button);
        }
      }
      
    }
    
    }
    catch (BusinessException be) {
      Debug.error(be.getMessage());
    }
    return m_boTree;
  }
  
  private ButtonObject getPluginButn(UIPluginMenuInfo menu) {
    return new ButtonObject(menu.getName(), menu.getName(), menu.getCode());
  }
}
