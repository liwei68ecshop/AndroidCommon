package com.szy.common.View;

/**
 * Created by 宗仁 on 4/28/16.
 * All Rights Reserved By 秦皇岛商之翼网络科技有限公司.
 */
public class Line {
    public static final Line EMPTY_LINE = new Line();
    public int itemCount;
    public int totalWidth;
    public int maxHeight;
    public int maxHeightIndex;

    public Line() {
        itemCount = 0;
        totalWidth = 0;
        maxHeight = 0;
        maxHeightIndex = -1;

    }

    @Override
    public int hashCode() {
        int result = itemCount;
        result = 31 * result + totalWidth;
        result = 31 * result + maxHeight;
        result = 31 * result + maxHeightIndex;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Line line = (Line) o;

        if (itemCount != line.itemCount) {
            return false;
        }
        if (totalWidth != line.totalWidth) {
            return false;
        }
        if (maxHeight != line.maxHeight) {
            return false;
        }
        return maxHeightIndex == line.maxHeightIndex;

    }

    public Line clone() {
        Line clone = new Line();
        clone.itemCount = itemCount;
        clone.totalWidth = totalWidth;
        clone.maxHeight = maxHeight;
        clone.maxHeightIndex = maxHeightIndex;
        return clone;
    }

    @Override
    public String toString() {
        return "Line{" +
                "itemCount=" + itemCount +
                ", totalWidth=" + totalWidth +
                ", maxHeight=" + maxHeight +
                ", maxHeightIndex=" + maxHeightIndex +
                '}';
    }
}
