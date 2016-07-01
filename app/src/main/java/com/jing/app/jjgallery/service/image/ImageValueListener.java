package com.jing.app.jjgallery.service.image;

public interface ImageValueListener {

	/**
	 * this operation need execute database I/O
	 * need consider source cost
	 * @param path
	 * @param width
	 * @param height
	 */
	public void onCreate(String path, int width, int height);
}
