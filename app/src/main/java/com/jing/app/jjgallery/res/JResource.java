package com.jing.app.jjgallery.res;

import android.content.Context;

import com.jing.app.jjgallery.R;
import com.jing.app.jjgallery.config.Configuration;
import com.jing.app.jjgallery.controller.ThemeManager;
import com.jing.app.jjgallery.viewsystem.ProgressProvider;
import com.king.lib.colorpicker.ColorFormatter;
import com.king.lib.colorpicker.ResourceProvider;
import com.king.lib.resmanager.JResManager;
import com.king.lib.resmanager.action.JResManagerCallback;
import com.king.lib.resmanager.exception.JResNotFoundException;
import com.king.lib.resmanager.exception.JResParseException;

/**
 * @author JingYang
 * @version create time：2016-1-13 下午3:44:29
 *
 */
public class JResource {

	private static JResManager jResManager;

	public static void initializeColors() {
//		jResManager = JResManager.getInstance();
		jResManager = JResManager.createInstance();
		try {
			jResManager.parseColorFile(Configuration.EXTEND_RES_COLOR);
		} catch (JResParseException e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public static void registCallback(JResManagerCallback callback) {
		jResManager.registCallback(callback);
	}

	public static int getColor(Context context, String resName, int defaultResId) {

		// normal主题才采用自定义颜色
		if (ThemeManager.getInstance().isNormalTheme(context)) {
			try {
				int color = jResManager.getColor(context, resName);
				return color;
			} catch (JResNotFoundException e) {
				e.printStackTrace();
			} catch (JResParseException e) {
				e.printStackTrace();
			}
		}

		if (defaultResId == ResourceProvider.FLAG_DEFAULT) {
			return ResourceProvider.FLAG_DEFAULT;
		}
		return context.getResources().getColor(defaultResId);
	}

	public static void updateColor(String resName, int newColor) {
		jResManager.updateColor(resName, "#" + ColorFormatter.formatColor(newColor));
	}

	public static void removeColor(String resName) {
		jResManager.removeColorResource(resName);
	}

	public static void saveColorUpdate(Context context) {
		// normal主题才采用自定义颜色
		if (ThemeManager.getInstance().isNormalTheme(context)) {
			if (jResManager.saveColorUpdate()) {
				((ProgressProvider) context).showToastLong(context.getString(R.string.colorpicker_update_success), ProgressProvider.TOAST_SUCCESS);
			}
			else {
				((ProgressProvider) context).showToastLong(context.getString(R.string.colorpicker_update_failed), ProgressProvider.TOAST_ERROR);
			}
		}
	}

}
