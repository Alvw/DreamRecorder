package com.crostec.ads;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

abstract class FrameDecoder {

    public static final int START_FRAME_MARKER = 254;
    private int frameIndex;
    private int rawFrameSize;
    private int decodedFrameSize;
    private int[] rawFrame;
    private static final Log log = LogFactory.getLog(FrameDecoder.class);

    public FrameDecoder(AdsConfiguration configuration) {
        decodedFrameSize = AdsUtils.getDecodedFrameSize(configuration);
        rawFrameSize = ((decodedFrameSize - 2) * 3) + 3; //3 bytes for each ads channel value or accelerometer value + 1 byte marker + 1 byte counter + 1 byte loff status
        rawFrame = new int[rawFrameSize];
    }

    public void onByteReceived(int inByte) {
        if (frameIndex == 0 && inByte == START_FRAME_MARKER) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex > 0 && frameIndex < (rawFrameSize - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex++;
        } else if (frameIndex == (rawFrameSize - 1)) {
            rawFrame[frameIndex] = inByte;
            frameIndex = 0;
            onFrameReceived();
        } else {
            log.warn("Lost rawFrame. Frame index = " + frameIndex + " inByte = " + inByte);
            frameIndex = 0;
        }
    }

    private void onFrameReceived() {
        int[] decodedFrame = new int[decodedFrameSize];
        for (int i = 0; i < decodedFrameSize - 2; i++) {
            decodedFrame[i] = (((rawFrame[i * 3 + 3] << 24) + ((rawFrame[i * 3 + 2]) << 16) + (rawFrame[i * 3 + 1] << 8)) / 256);
        }
        decodedFrame[decodedFrame.length - 2] = rawFrame[rawFrame.length - 2];  //counter value
        decodedFrame[decodedFrame.length - 1] = rawFrame[rawFrame.length - 1];  //loff status
        notifyListeners(decodedFrame);
    }

    public abstract void notifyListeners(int[] decodedFrame);
}
