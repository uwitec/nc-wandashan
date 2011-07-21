// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2004-08-10 09:02:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) ansi 
// Source File Name:   N_F3_EDIT.java

package nc.bs.pub.action;

import java.util.Hashtable;

import nc.bs.ep.dj.HgCheckFreezeNmnyBo;
import nc.bs.pub.compiler.AbstractCompiler2;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.ep.dj.DJZBVO;
import nc.vo.pub.compiler.PfParameterVO;

public class N_F3_EDIT extends AbstractCompiler2
{

    public N_F3_EDIT()
    {
        m_methodReturnHas = new Hashtable<String, Object>();
        m_keyHas = null;
    }

    public String getCodeRemark()
    {
        return "\t//nc.vo.ep.dj.DJZBVO retObj=(nc.vo.ep.dj.DJZBVO)getVo();\n\t\t//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n\tObject retObj =null;\n\t//####重要说明：生成的业务组件方法尽量不要进行修改####\n\t//方法说明:null\n\tretObj =runClassCom@ \"nc.bs.ep.dj.DJZBBO\", \"editDj\", \"nc.vo.ep.dj.DJZBVO:F3\"@;\n\t//##################################################\n\treturn retObj;\n";
    }

    public Object runComClass(PfParameterVO pfparametervo)
        throws nc.vo.pub.BusinessException
    {
        try
        {
            super.m_tmpVo = pfparametervo;
            //add by  zhw 2011-06-27 做付款单保存时，系统自动校验，当前供应商的应付款余额-付款（预付款不受限制）是否小于零，
    		HgCheckFreezeNmnyBo bo = new HgCheckFreezeNmnyBo();
    		bo.checkNmny((DJZBVO)getVo());
    		//  zhw end
            Object obj = null;
            obj = runClass("nc.bs.ep.dj.DJZBBO", "editDj", "nc.vo.ep.dj.DJZBVO:F3", pfparametervo, m_keyHas, m_methodReturnHas);
            if(obj != null)
                m_methodReturnHas.put("editDj", obj);
         
            return obj;
        }
        catch(Exception exception)
        {
        	throw ExceptionHandler.handleException(this.getClass(), exception);

        }
    }

//    private void setParameter(String s, Object obj)
//    {
//        if(m_keyHas == null)
//            m_keyHas = new Hashtable<String, Object>();
//        if(obj != null)
//            m_keyHas.put(s, obj);
//    }

    private Hashtable<String, Object> m_methodReturnHas;
    private Hashtable<String, Object> m_keyHas;
}