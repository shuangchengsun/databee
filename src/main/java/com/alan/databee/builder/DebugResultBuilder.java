package com.alan.databee.builder;

import com.alan.databee.model.DebugResult;
import com.alan.databee.model.ResultEnum;

public class DebugResultBuilder {

    public static DebugResult buildError(ResultEnum resultEnum){
        DebugResult result = new DebugResult();
        result.setStat(result.getStat());
        result.setMsg(resultEnum.getStatMsg());
        return result;
    }

    public static DebugResult buildSuccess(){
        DebugResult result = new DebugResult();
        result.setStat(0x00);
        result.setMsg("操作成功");
        return result;
    }

    public static DebugResult buildSuccess(String msg){
        DebugResult result = new DebugResult();
        result.setStat(0x00);
        result.setMsg(msg);
        return result;
    }
}
