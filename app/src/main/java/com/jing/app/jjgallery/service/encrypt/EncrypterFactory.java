package com.jing.app.jjgallery.service.encrypt;

import com.jing.app.jjgallery.service.encrypt.action.Encrypter;
import com.jing.app.jjgallery.service.encrypt.action.Generater;
import com.jing.app.jjgallery.service.encrypt.impl.SimpleEncrypter;
import com.jing.app.jjgallery.service.encrypt.impl.SimpleNameGenerater;

public class EncrypterFactory {

	public static Encrypter create() {
		
		return new SimpleEncrypter();
	}
	
	public static Generater generater() {
		return new SimpleNameGenerater();
	}
}
