package com.colcoa.beans.payUBeans;

import java.util.HashMap;
import java.util.Map;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.TransactionResponse;

public class PayUCreditCard {
	
	String reference = "payment_test_00000001";
	String value= "20000";

	Map<String, String> parameters = new HashMap<String, String>();
	private void paymentMethd() {
		//Ingrese aquí el identificador de la cuenta.
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "512321");
		//Ingrese aquí el código de referencia.
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, ""+reference);
		//Ingrese aquí la descripción.
		parameters.put(PayU.PARAMETERS.DESCRIPTION, "payment test");
		//Ingrese aquí el idima de la orden.
		parameters.put(PayU.PARAMETERS.LANGUAGE, "Language.es");
	
		// -- Valores --
		//Ingrese aquí el valor de la transacción.
		parameters.put(PayU.PARAMETERS.VALUE, ""+value);
		//Ingrese aquí el valor del IVA (Impuesto al Valor Agregado solo valido para Colombia) de la transacción,
		//si se envía el IVA nulo el sistema aplicará el 19% automáticamente. Puede contener dos dígitos decimales.
		//Ej: 19000.00. En caso de no tener IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_VALUE, "3193");
		//Ingrese aquí el valor base sobre el cual se calcula el IVA (solo valido para Colombia).
		//En caso de que no tenga IVA debe enviarse en 0.
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE, "16806");
		//Ingrese aquí la moneda.
		parameters.put(PayU.PARAMETERS.CURRENCY, ""+Currency.COP.name());
	
		// -- Comprador --
		//Ingrese aquí el id del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_ID, "1");
		//Ingrese aquí el nombre del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_NAME, "First name and second buyer  name");
		//Ingrese aquí el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer_test@test.com");
		//Ingrese aquí el teléfono de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, "7563126");
		//Ingrese aquí el documento de contacto del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_DNI, "5415668464654");
		//Ingrese aquí la dirección del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_STREET, "calle 100");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_2, "5555487");
		parameters.put(PayU.PARAMETERS.BUYER_CITY, "Medellin");
		parameters.put(PayU.PARAMETERS.BUYER_STATE, "Antioquia");
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, "CO");
		parameters.put(PayU.PARAMETERS.BUYER_POSTAL_CODE, "000000");
		parameters.put(PayU.PARAMETERS.BUYER_PHONE, "7563126");
	
		// -- Pagador --
		//Ingrese aquí el id del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_ID, "1");
		//Ingrese aquí el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");
		//Ingrese aquí el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "payer_test@test.com");
		//Ingrese aquí el teléfono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "7563126");
		//Ingrese aquí el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "5415668464654");
		//Ingrese aquí la dirección del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "calle 93");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_2, "125544");
		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_STATE, "Bogota");
		parameters.put(PayU.PARAMETERS.PAYER_COUNTRY, "CO");
		parameters.put(PayU.PARAMETERS.PAYER_POSTAL_CODE, "000000");
		parameters.put(PayU.PARAMETERS.PAYER_PHONE, "7563126");
	
		// -- Datos de la tarjeta de crédito --
		//Ingrese aquí el número de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, "4097440000000004");
		//Ingrese aquí la fecha de vencimiento de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, "2014/12");
		//Ingrese aquí el código de seguridad de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, "321");
		//Ingrese aquí el nombre de la tarjeta de crédito
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "VISA");
	
		//Ingrese aquí el número de cuotas.
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "1");
		//Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
	
		//Session id del device.
		parameters.put(PayU.PARAMETERS.DEVICE_SESSION_ID, "vghs6tvkcle931686k1900o6e1");
		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");
		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");
	
		//Solicitud de autorización y captura
		TransactionResponse response;
		try {
			response = PayUPayments.doAuthorizationAndCapture(parameters);
			//Respuesta
			if(response != null){
				response.getOrderId();
				response.getTransactionId();
				response.getState();
				if(response.getState().toString().equalsIgnoreCase("PENDING")){
					response.getPendingReason();
				}
				response.getPaymentNetworkResponseCode();
				response.getPaymentNetworkResponseErrorMessage();
				response.getTrazabilityCode();
				response.getResponseCode();
				response.getResponseMessage();
			}
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
