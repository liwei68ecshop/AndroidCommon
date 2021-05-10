package com.szy.common.ResponseModel.Region;

import java.util.List;

/**
 * Created by 宗仁 on 2016/11/11.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */

public class ResponseRegionModel {
    public int code;
    public String message;
    public List<List<ResponseRegionItemModel>> data;
    public List<String> level_names;
}
