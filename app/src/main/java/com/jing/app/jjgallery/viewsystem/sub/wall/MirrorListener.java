package com.jing.app.jjgallery.viewsystem.sub.wall;

import android.view.View;

/**
 * @author JingYang
 * @version create time：2016-2-1 下午3:08:23
 *
 */
public interface MirrorListener {

	void startMirror(View view);
	void cancelMirror(View view);
	void endMirror(View view);
	void processMirror(View view, float scale);
}
