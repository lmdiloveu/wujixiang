package com.infinitus.yearapp_a.base.auth;

public class TokenManager {

	public static TokenManager __instance = null;

	private TokenHandle tokenHandle;

	public static synchronized TokenManager instance() {
		if (__instance == null) {
			__instance = new TokenManager();
		}
		return __instance;
	}

	public TokenHandle getTokenHandle() {
		if(tokenHandle==null){
			tokenHandle = new TokenHandle();
		}
		return tokenHandle;
	}

	public void getAuthToken(String username, String password) {
		invalidateAuthToken();
		tokenHandle.auth(username, password);
	}

	public void invalidateAuthToken() {
		tokenHandle.unauth();
	}

	private TokenManager() {
		tokenHandle = new TokenHandle();
	}
	
	public void thirdpartyLogin(String cellphone, String token){
		invalidateAuthToken();
		tokenHandle.thirdpartyLoginAuth(cellphone, token);
	}
}
