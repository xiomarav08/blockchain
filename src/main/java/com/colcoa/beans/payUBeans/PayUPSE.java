package com.colcoa.beans.payUBeans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.Bank;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.DocumentType;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.PersonType;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionState;

public class PayUPSE {
	
	String reference = "payment_test_00000001";
	String value= "20000";

	Map<String, String> parameters = new HashMap<String, String>();
	
	private void paymentMethod() {
		//Ingrese aquí el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");
	
		//Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());
	
		//Obtener el listado de bancos disponibles
		List banks;
		try {
			banks = PayUPayments.getPSEBanks(parameters);
			Iterator banks_iterator=banks.iterator();
			
			while(banks_iterator.hasNext()){
				Bank bank = (Bank) banks_iterator.next();
				bank.getPseCode();
				bank.getDescription();
			}
		} catch (PayUException | InvalidParametersException | ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void paymentMethod2() {
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

		//Ingrese aquí el email del comprador.
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer_test@test.com");

		// -- pagador --
		//Ingrese aquí el nombre del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_NAME, "First name and second payer name");
		//Ingrese aquí el email del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "payer_test@test.com");
		//Ingrese aquí el teléfono de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "7563126");

		// -- infarmación obligatoria para PSE --
		//Ingrese aquí el código pse del banco.
		parameters.put(PayU.PARAMETERS.PSE_FINANCIAL_INSTITUTION_CODE, "1007");
		//Ingrese aquí el tipo de persona (natural o jurídica)
		parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, PersonType.NATURAL.toString() );
		//o parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, PersonType.LEGAL.toString() );
		//Ingrese aquí el documento de contacto del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "123456789");
		//Ingrese aquí el tipo de documento del pagador.
		parameters.put(PayU.PARAMETERS.PAYER_DOCUMENT_TYPE, DocumentType.CC.toString());
		//IP del pagadador
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");

		//Ingrese aquí el nombre del medio de pago
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "PSE");

		//Ingrese aquí el nombre del pais.
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.COOKIE, "pt1t38347bs6jc9ruv2ecpv7o2");
		//Cookie de la sesión actual.
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:18.0) Gecko/20100101 Firefox/18.0");

		//Página de respuesta a la cual será redirigido el pagador.
		parameters.put(PayU.PARAMETERS.RESPONSE_URL, "http://www.test.com/response");

		//Solicitud de autorización y captura
		TransactionResponse response;
		try {
			response = PayUPayments.doAuthorizationAndCapture(parameters);
			//Respuesta
			if(response != null){
				response.getOrderId();
				response.getTransactionId();
				response.getState();
				if(response.getState().equals(TransactionState.PENDING)){
					response.getPendingReason();
					Map extraParameters = response.getExtraParameters();

					//obtener la url del banco
					String url=(String)extraParameters.get("BANK_URL");

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
