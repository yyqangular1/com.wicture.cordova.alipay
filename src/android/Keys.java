/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  ��ʾ����λ�ȡ��ȫУ����ͺ��������id
 *  1.������ǩԼ֧�����˺ŵ�¼֧������վ(www.alipay.com)
 *  2.������̼ҷ���(https://b.alipay.com/order/myorder.htm)
 *  3.�������ѯ���������(pid)��������ѯ��ȫУ����(key)��
 */

package com.wicture.cordova.alipay;

//
// ��ο� Androidƽ̨��ȫ֧������(msp)Ӧ�ÿ����ӿ�(4.2 RSA�㷨ǩ��)���֣���ʹ��ѹ�����е�openssl RSA��Կ���ɹ��ߣ�����һ��RSA��˽Կ��
// ����ǩ��ʱ��ֻ��Ҫʹ�����ɵ�RSA˽Կ��
// Note: Ϊ��ȫ�����ʹ��RSA˽Կ����ǩ���Ĳ������̣�Ӧ�þ����ŵ��̼ҷ�������ȥ���С�
public final class Keys {

	//���������id����2088��ͷ��16λ������
	public static final String DEFAULT_PARTNER = "20888XXXXXXXXXXXXX";

	//�տ�֧�����˺�
	public static final String DEFAULT_SELLER = "test@test.com";

	//�̻�˽Կ����������
	public static final String PRIVATE = "YgEPUlTGxtA9O27ow3PpeV3mxJRXXxf9RP8o5jnFymnPanKRA+WjmStZvZyeMzzx/i1Td3PWkWG+Uz17WgM9iEnTQR392t1j7MDY7AG81MnQJBAPNLIkFZL0PrH+tkTM5pZ0l0RJXhbg9g2vZpr5L/qbDlmh1CildsHx/U6+mTvdCxe87ZQi1je2zdt1SWq4pcGEMCQQDBY+OK2ASFUKkqrnWXlRniKzJmvkVXKue4o6wfljflPy3CaOwQp9tHzX48i2XTGTQri5+P6PuOSxVFyLx34AL/AkEArIpmWs3zFUnUs03IosJhu9g3I8xiD4ny/7poB+pE3+2VZJbiIanO";
   
   //֧����Ĭ�Ϲ�Կ �������޸�
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
