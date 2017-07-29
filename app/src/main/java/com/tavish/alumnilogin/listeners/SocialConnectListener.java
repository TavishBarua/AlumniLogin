package com.tavish.alumnilogin.listeners;

import com.tavish.alumnilogin.model.UserLoginDetails;

public interface SocialConnectListener {

	public void onUserConnected(int requestIdentifier,UserLoginDetails userData);
	public void onConnectionError(int requestIdentifier,String message);
	public void onCancelled(int requestIdentifier,String message);
}
