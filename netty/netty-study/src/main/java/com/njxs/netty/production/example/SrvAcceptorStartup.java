package com.njxs.netty.production.example;

import com.njxs.netty.production.srv.acceptor.DefaultCommonSrvAcceptor;

public class SrvAcceptorStartup {
	
	public static void main(String[] args) throws InterruptedException {
		
		DefaultCommonSrvAcceptor defaultCommonSrvAcceptor = new DefaultCommonSrvAcceptor(20011,null);
		defaultCommonSrvAcceptor.start();
		
	}

}
