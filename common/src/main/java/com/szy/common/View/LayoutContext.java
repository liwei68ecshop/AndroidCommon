package com.szy.common.View;

/**
 * Created by 宗仁 on 4/11/16.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class LayoutContext {
    public FlowLayoutOptions layoutOptions;
    public int currentLineItemCount;

    public static LayoutContext clone(LayoutContext layoutContext) {
        LayoutContext resultContext = new LayoutContext();
        resultContext.currentLineItemCount = layoutContext.currentLineItemCount;
        resultContext.layoutOptions = FlowLayoutOptions.clone(layoutContext.layoutOptions);
        return resultContext;
    }

    public static LayoutContext fromLayoutOptions(FlowLayoutOptions layoutOptions) {
        LayoutContext layoutContext = new LayoutContext();
        layoutContext.layoutOptions = layoutOptions;
        return layoutContext;
    }
}
