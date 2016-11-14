package data.framework.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 自动登录Cookie工具类
 * 
 * @author zhengguiyu
 * @date 2015-05-12
 *
 */
public class CookieUtil {
	// 保存cookie时的cookieName
	private final static String cookieDomainName = "zz_oa";
	// 加密cookie时的网站自定码
	//private final static String webKey = "zz_oa";
	// 设置cookie有效期是两个星期，根据需要自定义
	//private final static long cookieMaxAge = 60 * 60 * 24 * 7 * 2;

	/**
	 * 保存Cookie
	 * 
	 * @param user
	 * @param response
	 */
	public static void saveCookie(String loginAccount, String password, String autoLogin, HttpServletResponse response) {
		//String passwordMd5 = EncryptHelper.encryptEncode(password);
		// 将要被保存的完整的Cookie值
		String cookieValue = loginAccount + ":" + password + ":" + autoLogin;
		// 再一次对Cookie的值进行BASE64编码
		String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes()));
		Cookie cookie = new Cookie(cookieDomainName, cookieValueBase64);
		cookie.setMaxAge(60 * 60 * 24 * 365 * 2);
		cookie.setPath("/ZZ_OA/");
		response.addCookie(cookie);
	}

	/**
	 * 清除Cookie
	 * @param response
	 */
	public static void clearCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieDomainName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/ZZ_OA/");
		response.addCookie(cookie);
	}

}
