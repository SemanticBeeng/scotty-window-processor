package de.tub.dima.scotty.core.windowType;

import de.tub.dima.scotty.core.*;
import de.tub.dima.scotty.core.*;

public class TumblingWindow implements ContextFreeWindow {

    private final WindowMeasure measure;
    /**
     * Size of the tumbling window
     */
    private final long size;

    public TumblingWindow(WindowMeasure measure, long size) {
        this.measure = measure;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    @Override
    public WindowMeasure getWindowMeasure() {
        return measure;
    }

    @Override
    public long assignNextWindowStart(long recordStamp) {
        return recordStamp + getSize() - (recordStamp) % getSize();
    }

    @Override
    public void triggerWindows(WindowCollector aggregateWindows, long lastWatermark, long currentWatermark) {
        long twSize = getSize();
        while (lastWatermark + twSize < currentWatermark) {
            long windowStart = lastWatermark - (lastWatermark % twSize);
            long windowEnd = windowStart + twSize;
            aggregateWindows.trigger(windowStart, windowEnd);
            lastWatermark = windowEnd;
        }
    }

    @Override
    public String toString() {
        return "TumblingWindow{" +
                "measure=" + measure +
                ", size=" + size +
                '}';
    }
}
