package com.zhuyun.protocols.otatcp;

import com.zhuyun.protocols.otatcp.messages.OtaFrame;

public interface OtaFramelistener {
	public void onOtaFrame(OtaFrame frame);
}
